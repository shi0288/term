package com.mcp.term.util;


import org.springframework.stereotype.Component;

@Component
public class Result {

    private int code;

    private String message;

    private Object data;

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public Result format() {
        this.code = ResultCode.SUCCESS;
        this.message = ResultCode.MESSAGE.get(code);
        this.data = null;
        return this;
    }

    public Result format(int code) {
        this.code = code;
        this.message = ResultCode.MESSAGE.get(code);
        this.data = null;
        return this;
    }

    public Result format(int code, String message) {
        this.code = code;
        this.message = message;
        this.data = null;
        return this;
    }

    public Result format(Object data) {
        this.code = ResultCode.SUCCESS;
        this.message = ResultCode.MESSAGE.get(code);
        this.data = data;
        return this;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Result append(Object data) {
        this.data = data;
        return this;
    }


}
