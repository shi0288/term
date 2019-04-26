package com.mcp.term.util;


public enum DateStyle {


    YYYY_MM("yyyy-MM", false),
    YYYY_MM_DD("yyyy-MM-dd", false),
    YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm", false),
    YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss", false),

    YYYYMMDD("yyyyMMdd", false),
    YYYYMMDDHHMMSS("yyyyMMddHHmmss", false),

    YYYY_MM_EN("yyyy/MM", false),
    YYYY_MM_DD_EN("yyyy/MM/dd", false),
    YYYY_MM_DD_HH_MM_EN("yyyy/MM/dd HH:mm", false),
    YYYY_MM_DD_HH_MM_SS_EN("yyyy/MM/dd HH:mm:ss", false),

    YYYY_MM_CN("yyyy年MM月", false),
    YYYY_MM_DD_CN("yyyy年MM月dd日", false),
    YYYY_MM_DD_HH_MM_CN("yyyy年MM月dd日 HH:mm", false),
    YYYY_MM_DD_HH_MM_SS_CN("yyyy年MM月dd日 HH:mm:ss", false),

    HH_MM("HH:mm", true),
    HH_MM_SS("HH:mm:ss", true),

    MM_DD("MM-dd", true),
    MM_DD_HH_MM("MM-dd HH:mm", true),
    MM_DD_HH_MM_SS("MM-dd HH:mm:ss", true),

    MM_DD_EN("MM/dd", true),
    MM_DD_HH_MM_EN("MM/dd HH:mm", true),
    MM_DD_HH_MM_SS_EN("MM/dd HH:mm:ss", true),

    MM_DD_CN("MM月dd日", true),
    MM_DD_HH_MM_CN("MM月dd日 HH:mm", true),
    MM_DD_HH_MM_SS_CN("MM月dd日 HH:mm:ss", true);

    private String value;

    private boolean isShowOnly;

    DateStyle(String value, boolean isShowOnly) {
        this.value = value;
        this.isShowOnly = isShowOnly;
    }

    public String getValue() {
        return value;
    }

    public boolean isShowOnly() {
        return isShowOnly;
    }

    /**
     * field
     */
    public static final class FIELD_TYPE {
        /**
         * 年
         */
        public static final int YEAR = 1;
        /**
         * 月
         */
        public static final int MONTH = 2;
        /**
         * 星期
         */
        public static final int WEEK = 3;
        /**
         * 日
         */
        public static final int DAY = 5;
        /**
         * 小时
         */
        public static final int HOUR = 11;
        /**
         * 分钟
         */
        public static final int MINUTE = 12;
        /**
         * 秒
         */
        public static final int SECOND = 13;


    }


}
