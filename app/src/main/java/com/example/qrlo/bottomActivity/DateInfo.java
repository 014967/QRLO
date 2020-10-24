package com.example.qrlo.bottomActivity;

import java.util.ArrayList;

public class DateInfo {

    long when;
    String where;
    String wherelogo;
    private String currentDegree;
    private String Pulmonarysym;

    public String getCurrentDegree() {
        return currentDegree;
    }

    public void setCurrentDegree(String currentDegree) {
        this.currentDegree = currentDegree;
    }

    public String getPulmonarysym() {
        return Pulmonarysym;
    }

    public void setPulmonarysym(String pulmonarysym) {
        Pulmonarysym = pulmonarysym;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStvisithistory() {
        return stvisithistory;
    }

    public void setStvisithistory(String stvisithistory) {
        this.stvisithistory = stvisithistory;
    }

    public String getVisitHistoryfor2week() {
        return visitHistoryfor2week;
    }

    public void setVisitHistoryfor2week(String visitHistoryfor2week) {
        this.visitHistoryfor2week = visitHistoryfor2week;
    }

    public String getVisitlocationfor2week() {
        return visitlocationfor2week;
    }

    public void setVisitlocationfor2week(String visitlocationfor2week) {
        this.visitlocationfor2week = visitlocationfor2week;
    }

    private String key;
    private String stvisithistory;
    private String visitHistoryfor2week;
    private String visitlocationfor2week;

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getWherelogo() {
        return wherelogo;
    }

    public void setWherelogo(String wherelogo) {
        this.wherelogo = wherelogo;
    }

    public DateInfo(){}
    public DateInfo(long when)
    {
        this.when = when;

    }



    public long getWhen() {
        return when;
    }

    public void setWhen(long when) {
        this.when = when;
    }
}
