package com.example.zahid.homeautomation.Model;

public class UnitCalOffline {
    private String volt;
    private String Ampere;
    private String units;

    public UnitCalOffline(String volt, String ampere, String units) {
        this.volt = volt;
        Ampere = ampere;
        this.units = units;
    }

    public UnitCalOffline() {
    }

    @Override
    public String toString() {
        return "UnitCalOffline{" +
                "volt='" + volt + '\'' +
                ", Ampere='" + Ampere + '\'' +
                ", units='" + units + '\'' +
                '}';
    }

    public String getVolt() {
        return volt;
    }

    public void setVolt(String volt) {
        this.volt = volt;
    }

    public String getAmpere() {
        return Ampere;
    }

    public void setAmpere(String ampere) {
        Ampere = ampere;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
