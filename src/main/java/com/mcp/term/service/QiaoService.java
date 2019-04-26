package com.mcp.term.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.mcp.term.mapper.QiaoqiaoyingMapper;
import com.mcp.term.model.Prediction;
import com.mcp.term.model.Qiaoqiaoying;
import com.mcp.term.model.Term;
import com.mcp.term.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class QiaoService {

    @Autowired
    private QiaoqiaoyingMapper qiaoqiaoyingMapper;

    @Autowired
    private TermService termService;

    @Autowired
    private PredictionService predictionService;

    private String getPredictionUrl = "http://123.56.180.12:10004/prediction/list";


    public JSONObject getPredictionBy(String game) {
        String url = getPredictionUrl + "?game=" + game;
        HttpResult httpResult = HttpClientWrapper.sendGet(url, null, null);
        Result result = JSONObject.parseObject(httpResult.getResult(), Result.class);
        if (result.getCode() == 10000) {
            JSONObject obj = JSONObject.parseObject(result.getData().toString());
            JSONArray list = obj.getJSONArray("list");
            if (list.size() == 1) {
                return list.getJSONObject(0);
            }
        }
        return null;
    }

    public void updatePredictionBy(String game) {
        //获取当前期次
        Term term = termService.getOpenTerm(game);
        if (term == null) {
            //当前没有可售期次则不再执行
            return;
        }
        //获取当前期次预测结果
        Prediction query = new Prediction();
        query.setGame(game);
        query.setTerm(term.getTermCode());
        query = predictionService.get(query);
        if (query != null) {
            JSONObject dataObj = JSONObject.parseObject(query.getData());
            if (dataObj.containsKey("items")) {
                JSONArray items = dataObj.getJSONArray("items");
                //如果当前有预测结果则不再执行
                if (items.size() > 0) {
                    return;
                } else {
                    //如果当前秒数大于30 不再获取
                    Calendar c = Calendar.getInstance();
                    int second = c.get(Calendar.SECOND);
                    if (second > 30) {
                        return;
                    }
                }
            }
        }
        JSONObject result = this.getPredictionBy(game);
        if (result == null) {
            //获取预测为空则返回
            return;
        }
        String issue = result.getString("term");
        //期次是否相同
        if (!term.getTermCode().equals(issue)) {
            return;
        }
        Prediction prediction = new Prediction();
        prediction.setGame(game);
        prediction.setTerm(term.getTermCode());
        Prediction predictionDB = predictionService.get(prediction);
        if (predictionDB == null) {
            predictionDB = prediction;
        }
        predictionDB.setData(result.getString("data"));
        predictionService.saveOrUpdate(predictionDB);
    }


    /**
     * 获取悄悄赢预测
     */
    public String getPrediction(String game) {
        switch (game) {
            case Cons.Game.CQSSC: {
                game = "ZhongQingShiShiCai";
                break;
            }
            case Cons.Game.TXFFC: {
                game = "TengXunFenFenCai";
                break;
            }
        }
        PageHelper.startPage(1, 1);
        PageHelper.orderBy("id asc");
        Qiaoqiaoying query = new Qiaoqiaoying();
        List<Qiaoqiaoying> list = qiaoqiaoyingMapper.select(query);
        if (list.size() == 1) {
            Qiaoqiaoying qiaoqiaoying = list.get(0);
            if (qiaoqiaoying.getExpiresIn() == null || qiaoqiaoying.getExpiresIn().getTime() - new Date().getTime() <= 500) {
                if (!updateToken(qiaoqiaoying)) {
                    return null;
                }
            }
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + qiaoqiaoying.getToken());
            headers.put("adp.tenantid", "1");
            JSONObject params = new JSONObject();
            params.put("name", game);
            params.put("baseCode", "1");
            params.put("ctlgAnlsSysId", "8c8357d847f8223");
            //区分游戏种类
            String url = "http://www.qiaoqiaoying.cn/api/lottery/forecastToAutoBet";
            HttpResult httpResult = HttpClientWrapper.sendPost(url, headers, params);
            if (httpResult.getResult().indexOf("token") > -1) {
                updateToken(qiaoqiaoying);
            }
            if (httpResult.getResult().indexOf("data") > -1) {
                return httpResult.getResult().replaceAll("anlsBetResult", "betResult").replace("data", "items");
            }
        }
        return null;
    }


    /**
     * 更新悄悄赢预测
     */
    public void updatePrediction(String game) {
        //获取当前期次
        Term term = termService.getOpenTerm(game);
        if (term == null) {
            //当前没有可售期次则不再执行
            return;
        }
        //获取当前期次预测结果
        Prediction query = new Prediction();
        query.setGame(game);
        query.setTerm(term.getTermCode());
        query = predictionService.get(query);
        if (query != null) {
            JSONObject dataObj = JSONObject.parseObject(query.getData());
            if (dataObj.containsKey("items")) {
                JSONArray items = dataObj.getJSONArray("items");
                //如果当前有预测结果则不再执行
                if (items.size() > 0) {
                    return;
                } else {
                    //如果当前秒数大于30 不再获取
                    Calendar c = Calendar.getInstance();
                    int second = c.get(Calendar.SECOND);
                    if (second > 30) {
                        return;
                    }
                }
            }
        }
        String res = this.getPrediction(game);
        if (StringUtils.isEmpty(res)) {
            //获取预测为空则返回
            return;
        }
        JSONObject jsonObject = JSONObject.parseObject(res);
        JSONObject result = jsonObject.getJSONObject("result");
        String issue = result.getString("nextDrawIssue");
        //期次是否相同
        if (!term.getTermCode().equals(issue)) {
            return;
        }
        Prediction prediction = new Prediction();
        prediction.setGame(game);
        prediction.setTerm(term.getTermCode());
        Prediction predictionDB = predictionService.get(prediction);
        if (predictionDB == null) {
            predictionDB = prediction;
            predictionDB.setData(result.toString());
            predictionService.saveOrUpdate(predictionDB);
        } else {
            JSONArray items = result.getJSONArray("items");
            if (items.size() > 0) {
                predictionDB.setData(result.toString());
                predictionService.saveOrUpdate(predictionDB);
            }
        }
    }


    /**
     * 更新悄悄赢预测
     */
    public boolean updateToken(Qiaoqiaoying qiaoqiaoying) {
        Map<String, String> headers = new HashMap<>();
        headers.put("abp.tenantid", "1");
        Map<String, String> params = new HashMap<>();
        params.put("client_id", "Admin.Client");
        params.put("client_secret", "admin");
        params.put("grant_type", "password");
        params.put("username", qiaoqiaoying.getUsername());
        params.put("password", qiaoqiaoying.getPassword());
        HttpResult httpResult = HttpClientWrapper.sendPost(qiaoqiaoying.getLoginUrl(), headers, params);
        JSONObject jsonObject = JSON.parseObject(httpResult.getResult());
        if (jsonObject.containsKey("access_token")) {
            qiaoqiaoying.setToken(jsonObject.getString("access_token"));
            qiaoqiaoying.setRefreshToken(jsonObject.getString("refresh_token"));
            qiaoqiaoying.setExpiresIn(DateUtil.addSecond(new Date(), jsonObject.getIntValue("expires_in")));
            qiaoqiaoyingMapper.updateByPrimaryKeySelective(qiaoqiaoying);
            return true;
        }
        return false;
    }


    /**
     * 更新悄悄赢历史预测
     */
    public void historyPrediction(String game, Date date) {
        String res = this.getResult(game, date);
        if (StringUtils.isEmpty(res)) {
            //获取预测为空则返回
            return;
        }
        JSONObject result = JSONObject.parseObject(res).getJSONObject("result");
        JSONObject history = result.getJSONObject("history");
        JSONArray items = history.getJSONArray("items");
        String termDay = DateUtil.DateToString(date, "yyyyMMdd");
        for (int i = 0; i < items.size(); i++) {
            try {
                JSONObject temp = items.getJSONObject(i);
                String str = temp.getString("drawIssue");
                if (str.equals("0000")) {
                    str = "1440";
                }
                String term = termDay + str;
                Prediction prediction = new Prediction();
                prediction.setGame(game);
                prediction.setTerm(term);
                Prediction target = predictionService.get(prediction);
                if (target != null) {
                    continue;
                }
                JSONObject data = new JSONObject();
                data.put("subtotal", temp.get("betAmounts"));
                JSONArray dataArr = new JSONArray();
                data.put("items", dataArr);
                JSONArray anlsItems = temp.getJSONArray("anlsItems");
                for (int m = 0; m < anlsItems.size(); m++) {
                    JSONObject item = anlsItems.getJSONObject(m);
                    item.remove("resultState");
                    dataArr.add(item);
                }
                prediction.setData(data.toString());
                predictionService.saveOrUpdate(prediction);
            } catch (Exception e) {
            }
        }
    }


    /**
     * 获取历史数据
     */
    public String getResult(String game, Date date) {
        PageHelper.startPage(1, 1);
        PageHelper.orderBy("id asc");
        Qiaoqiaoying query = new Qiaoqiaoying();
        List<Qiaoqiaoying> list = qiaoqiaoyingMapper.select(query);
        if (list.size() == 1) {
            Qiaoqiaoying qiaoqiaoying = list.get(0);
            Map<String, String> headers = new HashMap<>();
            Map<String, String> params = new HashMap<>();
            //区分游戏种类
            String url = null;
            if (game.equals(Cons.Game.CQSSC)) {
                url = qiaoqiaoying.getDataUrl().replace("$data$", DateUtil.DateToString(date, "yyyyMMdd"));
            } else if (game.equals(Cons.Game.TXFFC)) {
                url = qiaoqiaoying.getDataUrlFfc().replace("$data$", DateUtil.DateToString(date, "yyyyMMdd"));
            }
            url = url + "?isall=1";
            HttpResult httpResult = HttpClientWrapper.sendGet(url, headers, params);
            if (httpResult.getResult().indexOf("history") > -1) {
                return httpResult.getResult();
            }
        }
        return null;
    }

}
