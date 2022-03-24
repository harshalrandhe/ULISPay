package com.ulisfintech.artha.ui;

import java.util.HashMap;
import java.util.Map;

class GatewayRequest {
    public static String POST = "POST";
    public static String GET = "GET";
    String URL;
    String method;
    Map<String, String> extraHeaders = new HashMap<>();
    OrderPayload payload;
}