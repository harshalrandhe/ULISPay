package com.ulisfintech.artha.api;

import java.util.HashMap;
import java.util.Map;

class GatewayRequest {
    String url;
    String method;
    Map<String, String> extraHeaders = new HashMap<>();
    GatewayParams payload;
}