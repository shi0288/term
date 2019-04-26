package com.mcp.term.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResult {

    private String result;

    private int code;

    private List<String> cookies = new ArrayList<>();

    private Map param = new HashMap();


    @Override
    public String toString() {
        return "HttpResult{" +
                "result='" + result + '\'' +
                ", code=" + code +
                ", cookies=" + cookies +
                '}';
    }

    public void addCookies(String cookie) {
        cookies.add(cookie);
    }

    public List<String> getCookies() {
        return cookies;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Map getParam() {
        return param;
    }

    public void setParam(String key, Object value) {
        this.param.put(key, value);
    }
}
