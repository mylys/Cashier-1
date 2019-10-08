package com.easygo.cashier.bean;


public class PrinterStatusResponse {

    /**
     * online : 1
     */

    private int online;
    private String printer_sn;

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public String getPrinter_sn() {
        return printer_sn;
    }

    public void setPrinter_sn(String printer_sn) {
        this.printer_sn = printer_sn;
    }
}


