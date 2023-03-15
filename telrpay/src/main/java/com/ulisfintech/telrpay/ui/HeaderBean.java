package com.ulisfintech.telrpay.ui;

class HeaderBean {

    private String xusername;
    private String xpassword;
    private String merchant_key;
    private String merchant_secret;

    public String getXusername() {
        return xusername;
    }

    public void setXusername(String xusername) {
        this.xusername = xusername;
    }

    public String getXpassword() {
        return xpassword;
    }

    public void setXpassword(String xpassword) {
        this.xpassword = xpassword;
    }

    public String getMerchant_key() {
        return merchant_key;
    }

    public void setMerchant_key(String merchant_key) {
        this.merchant_key = merchant_key;
    }

    public String getMerchant_secret() {
        return merchant_secret;
    }

    public void setMerchant_secret(String merchant_secret) {
        this.merchant_secret = merchant_secret;
    }
}
