package com.mcp.term.util;


import java.util.HashMap;

public class ResultCode {
    public static final int SUCCESS = 10000;
    public static final int ERROR = 9999;

    public static HashMap<Integer, String> MESSAGE = new HashMap();

    static {
        MESSAGE.put(10000, "操作成功");
        MESSAGE.put(9999, "失败");
    }
}
