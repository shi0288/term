package com.mcp.term.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.mcp.term.mapper.OnlineMapper;
import com.mcp.term.model.Online;
import com.mcp.term.model.Term;
import com.mcp.term.util.Cons;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OnlineService {

    @Autowired
    private TermService termService;

    @Autowired
    private OnlineMapper onlineMapper;

    public void updatePrizeNumber() {
        PageHelper.startPage(1, 30).setOrderBy("id desc");
        List<Online> list = onlineMapper.selectAll();
        for (int i = 0; i < list.size(); i++) {
            Online online = list.get(i);
            if (StringUtils.isEmpty(online.getPrize())) {
                continue;
            }
            Term term = new Term();
            term.setGame(Cons.Game.TXFFC);
            term.setTermCode(online.getTerm());
            term = termService.get(term);
            if (term == null || !StringUtils.isEmpty(term.getWinNumber())) {
                continue;
            }
            term.setWinNumber(online.getPrize());
            termService.update(term);
        }
    }

}
