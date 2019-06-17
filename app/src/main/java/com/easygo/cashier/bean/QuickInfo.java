package com.easygo.cashier.bean;

import java.util.List;

/**
 * @Describe：
 * @Date：2019-01-30
 */
public class QuickInfo {

    public List<GoodsResponse> header;
    public List<ClassifyInfo> list;

    public List<GoodsResponse> getHeader() {
        return header;
    }

    public void setHeader(List<GoodsResponse> header) {
        this.header = header;
    }

    public List<ClassifyInfo> getList() {
        return list;
    }

    public void setList(List<ClassifyInfo> list) {
        this.list = list;
    }
}
