package com.mcp.term.service;


import com.mcp.term.mapper.TermMapper;
import com.mcp.term.model.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TermService {

    @Autowired
    private TermMapper termMapper;


    public Term getOpenTerm(String game) {
        Term query = new Term();
        query.setStatus(1);
        query.setGame(game);
        List<Term> list = termMapper.select(query);
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    public Term get(Term term){
        return termMapper.selectOne(term);
    }

    public boolean update(Term term){
        if(termMapper.updateByPrimaryKeySelective(term)==1){
            return true;
        }
        return false;
    }





}
