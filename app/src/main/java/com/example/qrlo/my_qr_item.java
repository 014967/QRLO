package com.example.qrlo;

import android.graphics.Bitmap;

public class my_qr_item {
    public static final String QR_CERTI = "qrlo-798fd";
    public static final String QR_CERTI_SPLIT_TOKEN = ";";
    public static final String QR_ADD_SPLIT_TOKEN = ",";

    private Bitmap iconBitmap;
    private String iconURI;
    private String iconNameStr;
    private String titleStr;
    private String addressStr;
    private String detailAddressStr;
    private boolean tempDrawable;
    private String phoneStr;
    private String strQR;
    private String keyStr;

    public void setIcon(Bitmap icon) {
        iconBitmap = icon;
    }

    public void setIconURI(String iconUri){iconURI = iconUri;}

    public void setIconName(String iconName){iconNameStr = iconName; };

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

    public void setStrQR(String qr){
        strQR = qr;
    }

    public void setKey(String key) {
        keyStr = key;
    }

    public void updateQR() {
        strQR = QR_CERTI + QR_CERTI_SPLIT_TOKEN + addressStr + QR_ADD_SPLIT_TOKEN + detailAddressStr + QR_ADD_SPLIT_TOKEN + titleStr + QR_ADD_SPLIT_TOKEN + phoneStr + QR_ADD_SPLIT_TOKEN + iconURI + QR_ADD_SPLIT_TOKEN + keyStr;  // QR 코드에 들어갈 포맷 => 나중에 split 함수로 쪼개서 사용하면 됨
    }

    public Bitmap getIcon() {
        return this.iconBitmap;
    }

    public String getIconURI() {
        return this.iconURI;
    }

    public String getIconName() {
        return this.iconNameStr;
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

    public String getKey() {
        return this.keyStr;
    }
}
