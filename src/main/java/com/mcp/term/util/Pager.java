package com.mcp.term.util;

/**
 * Created by daNuo.
 */
public class Pager {

    private int page = 1;

    private int limit = 1;


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
