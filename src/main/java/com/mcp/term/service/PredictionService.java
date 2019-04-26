package com.mcp.term.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mcp.term.mapper.PredictionMapper;
import com.mcp.term.model.Prediction;
import com.mcp.term.util.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PredictionService {

    @Autowired
    private PredictionMapper predictionMapper;


    public void saveOrUpdate(Prediction prediction) {
        if (prediction.getId() == null) {
            predictionMapper.insertSelective(prediction);
        } else {
            predictionMapper.updateByPrimaryKeySelective(prediction);
        }
    }

    public Prediction get(Prediction prediction) {
        return predictionMapper.selectOne(prediction);
    }

    public PageInfo<Prediction> getList(Pager pager, Map param){
        PageInfo pageInfo = PageHelper.startPage(pager.getPage(), pager.getLimit()).doSelectPageInfo(() -> predictionMapper.getAll(param));
        return pageInfo;

    }



}
