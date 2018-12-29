package com.easygo.cashier.bean;

/**
 * @Describe：
 * @author：hgeson
 * @date：2018-12-28
 */
public class FunctionListBean {
    private int function_name;
    private int res;

    public FunctionListBean(int function_name, int res) {
        this.function_name = function_name;
        this.res = res;
    }

    public int getFunction_name() {
        return function_name;
    }

    public void setFunction_name(int function_name) {
        this.function_name = function_name;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }
}
