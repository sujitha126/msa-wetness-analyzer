package com.sujitha.evariant.model;

/**
 * Created by sneti on 6/14/17.
 * Model representing the WBAN record
 */
public class WBAN {

    private String wbanId;
    private String stationName;
    private String stateProvince;
    private String county;


    public WBAN(String wbanId, String stationName, String stateProvince, String county) {
        this.wbanId = wbanId;
        this.stationName = stationName;
        this.stateProvince = stateProvince;
        this.county = county;
    }

    public String getWbanId() {
        return wbanId;
    }

    public void setWbanId(String wbanId) {
        this.wbanId = wbanId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

}
