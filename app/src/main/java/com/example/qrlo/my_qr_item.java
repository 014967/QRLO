package com.example.qrlo;

import android.graphics.Bitmap;

public class my_qr_item {
    private Bitmap iconBitmap;
    private String iconURI;
    private String titleStr;
    private String addressStr;
    private String detailAddressStr;
    private boolean tempDrawable;
    private String phoneStr;
    private String strQR;
    private String logoURL;

    public void setIcon(Bitmap icon) {
        iconBitmap = icon;
    }

    public void setIconURI(String iconUri){iconURI = iconUri;}

    public void setTitle(String title) {
        titleStr = title;
    }

    public void setAddress(String address) {
        addressStr = address;
    }

    public void setDetailAddress(String desc) {
        detailAddressStr = desc;
    }

    public void setTemp(boolean temp) {
        tempDrawable = temp;
    }

    public void setPhone(String phone) {
        phoneStr = phone;
    }

    public void setLogoURL(String url){
        logoURL = url;
    }

    public void setQR() {
        strQR = addressStr + '/' + detailAddressStr + '/' + titleStr + '/' + phoneStr + '/' + logoURL;  // QR 코드에 들어갈 포맷 => 나중에 split 함수로 쪼개서 사용하면 됨
        // TODO 현국이가 logo 이미지 올릴 때 logoURL 초기화해줘야 할 듯
    }

    public Bitmap getIcon() {
        return this.iconBitmap;
    }

    public String getIconURI() {
        return this.iconURI;
    }

    public String getTitle() {
        return this.titleStr;
    }

    public String getAddress() {
        return this.addressStr;
    }

    public String getDetailAddress() {
        return this.detailAddressStr;
    }

    public boolean getTemp() {
        return this.tempDrawable;
    }

    public String getPhone() {
        return this.phoneStr;
    }

    public String getStrQR(){
        return this.strQR;
    }
}
