package com.ulisfintech.artha.ui;

public class GatewayException extends Exception {

    int statusCode;
    GatewayMap error;

    public GatewayException() {
    }

    public GatewayException(String message) {
        super(message);
    }


    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public GatewayMap getErrorResponse() {
        return error;
    }

    public void setErrorResponse(GatewayMap error) {
        this.error = error;
    }
}