package com.ulisfintech.telrpay.ui;

import java.util.HashMap;
import java.util.Map;

class GatewayRequestBuilder {

    private final String BASE_ORDER_URL_UAE = "https://ulis.live:4014/api/v1/orders/";
    private final String BASE_ORDER_URL_KSA = "https://ulis.live:4016/api/v1/orders/";
    private final String BASE_REGION_URL = "https://ulis.live:4010/api/v1/merchant_key_secret/find/region";
    static final String KSA = "KSA";
    static final String UAE = "UAE";

    public GatewayRequestBuilder() {
    }

    private String getBaseUrl(String region){
        if (region.equalsIgnoreCase(UAE)) {
            return BASE_ORDER_URL_UAE;
        }else {
            return BASE_ORDER_URL_KSA;
        }
    }

    /**
     * Check Order Status
     *
     * @param orderId    order id
     * @param endPoint   order status api url
     * @param headerBean API headers
     * @return API request
     */
    GatewayRequest buildOrderStatusRequest(String orderId, String endPoint, HeaderBean headerBean) {
        GatewayRequest request = new GatewayRequest();
        request.URL = getBaseUrl(headerBean.getRegion()) + endPoint;
        request.method = GatewayRequest.POST;
        request.payload = new OrderIdBean(orderId);
        request.extraHeaders = getHeaders(headerBean);
        return request;
    }

    /**
     * Check order details
     *
     * @param orderId    order id
     * @param token      order token
     * @param headerBean API headers
     * @return API request
     */
    GatewayRequest buildOrderDetailsRequest(String orderId, String token, HeaderBean headerBean) {
        GatewayRequest request = new GatewayRequest();
        request.URL = getBaseUrl(headerBean.getRegion()) + "details";
        request.method = GatewayRequest.POST;
        request.payload = new OrderIdBean(orderId, token);
        request.extraHeaders = getHeaders(headerBean);
        return request;
    }

    /**
     * Request
     * Create Order
     *
     * @param orderBean initial product data for order
     * @return place order or create order request
     */
    GatewayRequest buildCreateOrderRequest(OrderBean orderBean) {
        GatewayRequest request = new GatewayRequest();
        request.URL = getBaseUrl(orderBean.getHeaders().getRegion()) + "create";
        request.method = GatewayRequest.POST;
        request.payload = new OrderPayload(orderBean);
//        request.payload = new OrderPayload(jsonObject);
        request.extraHeaders = getHeaders(orderBean.getHeaders());
        return request;
    }

    /**
     * @param headerBean
     * @return check merchant region ( UAE OR KSA)
     */
    GatewayRequest buildCheckRegionRequest(HeaderBean headerBean) {
        GatewayRequest request = new GatewayRequest();
        request.URL = BASE_REGION_URL;
        request.method = GatewayRequest.POST;
        request.payload = new OrderPayload();
        request.extraHeaders = getHeaders(headerBean);
        return request;
    }

    /**
     * API Headers
     *
     * @param headerBean headers
     * @return headers
     */
    private Map<String, String> getHeaders(HeaderBean headerBean) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("xusername", headerBean.getXusername());
        headers.put("xpassword", headerBean.getXpassword());
        headers.put("merchant_key", headerBean.getMerchant_key());
        headers.put("merchant_secret", headerBean.getMerchant_secret());
        headers.put("ip", headerBean.getIp());
        return headers;
    }
}
