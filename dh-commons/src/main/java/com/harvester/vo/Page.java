package com.harvester.vo;

import java.util.List;

/**
 * 分页文件
 *
 * @param <T>
 * @time 2016年4月22日18:04:50
 */
public class Page<T> {

    private int curPage = 1; // 当前是第几页
    protected int limit = 0;// 每页行数
    protected int offset = 0;// 开始行数
    protected List<T> rows;// 页面json名称 传数据
    protected int total;// 总条数，供页面显示和分页

    public int getCurPage() {
        return curPage;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public List<T> getRows() {
        return rows;
    }

    public int getTotal() {
        return total;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
