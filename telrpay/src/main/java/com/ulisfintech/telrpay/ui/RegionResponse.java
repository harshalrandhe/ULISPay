package com.ulisfintech.telrpay.ui;

import com.google.gson.annotations.Expose;

public class RegionResponse {

    @Expose
    private String region;

    public RegionResponse() {
    }

    public RegionResponse(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
