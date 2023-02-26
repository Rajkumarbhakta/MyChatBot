package com.rkbapps.mychatbot.models;

public class RecyclerViewModelClass {

    public static final int SEND_BY_ME = 1;
    public static final int SEND_BY_BOT = 0;

    private String massage;
    private int viewType;

    public RecyclerViewModelClass() {
    }

    public RecyclerViewModelClass(String massage, int viewType) {
        this.massage = massage;
        this.viewType = viewType;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}

