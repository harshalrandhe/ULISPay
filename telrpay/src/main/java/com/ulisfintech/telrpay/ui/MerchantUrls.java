package com.ulisfintech.telrpay.ui;

import com.google.gson.annotations.Expose;

 class MerchantUrls {

    @Expose
    private String success;
    @Expose
    private String cancel;
    @Expose
    private String failure;

    public MerchantUrls() {
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }

    public String getFailure() {
        return failure;
    }

    public void setFailure(String failure) {
        this.failure = failure;
    }
}
