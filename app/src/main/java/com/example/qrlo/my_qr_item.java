package com.example.qrlo;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class my_qr_item {
    private Bitmap iconBitmap ;
    private String titleStr ;
    private String addressStr;
    private String detailaddressStr ;
    private Drawable tempDrawble;
    private String phoneStr;

    public void setIcon(Bitmap icon) {
        iconBitmap = icon ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setAddress(String address){addressStr = address; }
    public void setDetailAddress(String desc) {
        detailaddressStr = desc ;
    }
    public void setTemp(Drawable temp){tempDrawble = temp;}
    public void setPhone(String phone){phoneStr = phone; }

    public Bitmap getIcon() { return this.iconBitmap ; }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getAddress() { return this.addressStr ; }
    public String getDetailAddress(){ return this.detailaddressStr ; }
    public Drawable getTemp() {return this.tempDrawble;}
    public String getPhone() {
        return this.phoneStr ;
    }
}
