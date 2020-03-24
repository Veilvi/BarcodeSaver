package com.barcodescaner;

public class DataClass {

    String qrCode;
    String text1;
    String text2;
    String text3;

    TXTMechanic mechanic=new TXTMechanic();

    public void setQrCode (String qrCode)
    {
        this.qrCode=qrCode;
    }

    public void setText1(String text1) {
        this.text1 = mechanic.spaceFilter(text1);
    }

    public void setText2(String text2) {
        this.text2 = mechanic.spaceFilter(text2);
    }

    public void setText3(String text3) {
        this.text3 =  mechanic.spaceFilter(text3);
    }

    public String getQrCode() {
        return qrCode;
    }

    public String getText1() {
        return text1;
    }

    public String getText2() {
        return text2;
    }

    public String getText3() {
        return text3;
    }
}
