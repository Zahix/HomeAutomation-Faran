package com.example.zahid.homeautomation.Model;

public class Month {
    private String monthcode;
    private String devicemac;
    private String momaxampere;
    private String momaxvoltage;
    private String motimestamp;
    private String mounits;
    private String Month;

    public String getMonthcode() {
        return monthcode;
    }

    public void setMonthcode(String monthcode) {
        this.monthcode = monthcode;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getDevicemac() {
        return devicemac;
    }

    public void setDevicemac(String devicemac) {
        this.devicemac = devicemac;
    }

    public String getMomaxampere() {
        return momaxampere;
    }

    public void setMomaxampere(String momaxampere) {
        this.momaxampere = momaxampere;
    }

    public String getMomaxvoltage() {
        return momaxvoltage;
    }

    public void setMomaxvoltage(String momaxvoltage) {
        this.momaxvoltage = momaxvoltage;
    }

    public String getMotimestamp() {
        return motimestamp;
    }

    public void setMotimestamp(String motimestamp) {
        this.motimestamp = motimestamp;
    }

    public String getMounits() {
        return mounits;
    }

    public void setMounits(String mounits) {
        this.mounits = mounits;
    }

    @Override
    public String toString() {
        return "Month{" +
                "devicemac='" + devicemac + '\'' +
                ", momaxampere='" + momaxampere + '\'' +
                ", momaxvoltage='" + momaxvoltage + '\'' +
                ", motimestamp='" + motimestamp + '\'' +
                ", mounits='" + mounits + '\'' +
                ", Month='" + Month + '\'' +
                '}';
    }
}
