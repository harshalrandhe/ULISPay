package com.ulisfintech.telrpay.ui;

import javax.net.ssl.HttpsURLConnection;

interface Logger {
    void logRequest(HttpsURLConnection c, String data);
    void logResponse(HttpsURLConnection c, String data);
    void logDebug(String message);
}