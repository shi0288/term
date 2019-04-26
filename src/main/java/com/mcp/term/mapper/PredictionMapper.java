package com.mcp.term.mapper;

import com.mcp.term.model.Prediction;
import com.mcp.term.util.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PredictionMapper extends BaseMapper<Prediction> {

    List<Prediction> getAll(Map param);

}
