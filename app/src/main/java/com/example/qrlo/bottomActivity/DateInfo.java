package com.example.qrlo.bottomActivity;

import java.util.ArrayList;

public class DateInfo {

    long when;
    String where;
    String wherelogo;

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
