package com.example.qrlo.bottomActivity;

import java.sql.Timestamp;

public class DateInfo {

    long when;
    String where;

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

    String wherelogo;

    public DateInfo()
    {

    }

    public long getWhen() {
        return when;
    }

    public void setWhen(long when) {
        this.when = when;
    }
}
