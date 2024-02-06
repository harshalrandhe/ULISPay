package com.ulisfintech.telrpay.ui;

import java.util.HashMap;
import java.util.Map;

class GatewayRequestBuilder {

    private final String BASE_ORDER_URL = "https://ulis.live:4014/api/v1/orders/";

    public GatewayRequestBuilder() {
    }

    /**
     * Check Order Status
     *
     * @param orderId    order id
     * @param endPoint      order status api url
     * @param headerBean API headers
     * @return API request
     */
    GatewayRequest buildOrderStatusRequest(String orderId, String endPoint, HeaderBean headerBean) {
        GatewayRequest request = new GatewayRequest();
        request.URL = BASE_ORDER_URL + endPoint;
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
        request.URL = BASE_ORDER_URL + "details";
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
        request.URL = BASE_ORDER_URL + "create";
        request.method = GatewayRequest.POST;
        request.payload = new OrderPayload(orderBean);
//        request.payload = new OrderPayload(jsonObject);
        request.extraHeaders = getHeaders(orderBean.getHeaders());
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
