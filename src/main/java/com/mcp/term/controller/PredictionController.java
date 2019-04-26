package com.mcp.term.controller;


import com.mcp.term.service.PredictionService;
import com.mcp.term.util.Pager;
import com.mcp.term.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("prediction")
public class PredictionController {

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private PredictionService predictionService;

    @Autowired
    private Result result;



    @RequestMapping("list")
    @ResponseBody
    Result list(Pager pager){
        Map param =  this.getParamMap();
        return result.format(predictionService.getList(pager,param));
    }

    private Map getParamMap() {
        Map<String, Object> map = new HashMap();
        Enumeration<String> paramMap = httpServletRequest.getParameterNames();
        while (paramMap.hasMoreElements()) {
            String paramName = paramMap.nextElement();
            String paramValue = httpServletRequest.getParameter(paramName);
            //形成键值对应的map
            if (!StringUtils.isEmpty(paramValue)) {
                map.put(paramName, paramValue);
            }
        }
        map.remove("page");
        map.remove("limit");
        return map;
    }





}
