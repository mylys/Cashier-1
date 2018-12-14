package com.easygo.cashier.bean;

public class LoginResponse {


    /**
     * real_name : xxx
     * session_id : 7d0ab661affdca203d692dcb0f2d2ad2358
     */

    private String real_name;
    private String session_id;
    private int handover_id;

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public int getHandover_id() {
        return handover_id;
    }

    public void setHandover_id(int handover_id) {
        this.handover_id = handover_id;
    }
}
