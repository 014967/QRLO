package com.example.qrlo.bottomActivity;

import java.util.ArrayList;

public class DateInfo {

    long when;
    ArrayList<VisitInfo> arrayList;


    public DateInfo(long when, ArrayList<VisitInfo> arrayList)
    {
        this.when = when;
        this.arrayList = arrayList;
    }

    public ArrayList<VisitInfo> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<VisitInfo> arrayList) {
        this.arrayList = arrayList;
    }

    public long getWhen() {
        return when;
    }

    public void setWhen(long when) {
        this.when = when;
    }
}
