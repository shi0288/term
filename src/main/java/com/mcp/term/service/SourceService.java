package com.mcp.term.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mcp.term.model.Term;
import com.mcp.term.util.Cons;
import com.mcp.term.util.HttpClientWrapper;
import com.mcp.term.util.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
public class SourceService {

    @Autowired
    private TermService termService;


    public void updateCQSSCPrize(int recent) {
        String url = "https://www.66c07.com/lottery/trendChart/lotteryOpenNum.do?lotCode=CQSSC&recentDay=" + recent + "&rows=100&timestamp=" + new Date().getTime();
        HttpResult httpResult = HttpClientWrapper.sendGet(url, null, null);
        JSONArray jsonArray = JSONArray.parseArray(httpResult.getResult());
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String term = jsonObject.getString("qiHao");
            Term query = new Term();
            query.setGame(Cons.Game.CQSSC);
            query.setTermCode(term);
            query = termService.get(query);
            if (query == null) {
                continue;
            }
            if (StringUtils.isEmpty(query.getWinNumber())) {
                Term updateDb = new Term();
                updateDb.setId(query.getId());
                updateDb.setWinNumber(jsonObject.getString("haoMa"));
                termService.update(updateDb);
            }
        }


    }


}
