package com.ulisfintech.artha.ui;

public interface GatewayCallback {

    /**
     * Callback on a successful call to the Gateway API
     *
     * @param response A response map
     */
    void onSuccess(GatewayMap response);

    /**
     * Callback executed when error thrown during call to Gateway API
     *
     * @param throwable The exception thrown
     */
    void onError(Throwable throwable);

}