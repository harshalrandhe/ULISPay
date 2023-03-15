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
     * @param headerBean API headers
     * @return API request
     */
    GatewayRequest buildOrderStatusRequest(String orderId, HeaderBean headerBean) {
        GatewayRequest request = new GatewayRequest();
        request.URL = BASE_ORDER_URL + "Details";
        request.method = GatewayRequest.POST;
        request.payload = new OrderStatusPayload(new OrderIdBean(orderId));
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
        request.extraHeaders = getHeaders(orderBean.getHeaders());
        return request;
    }

    /**
     * Request
     * Buy order
     *
     * @param bean payment data
     * @return payment request
     */
    GatewayRequest buildPaymentRequest(PaymentRequestBean bean) {
        GatewayRequest request = new GatewayRequest();
        request.URL = BASE_ORDER_URL + "Pay";
        request.method = GatewayRequest.POST;
        request.payload = new PaymentPayload(bean);
        request.extraHeaders = getHeaders(bean.getHeaders());
        return request;
    }

    /**
     * Request
     * Buy using UPI order
     *
     * @param bean payment data
     * @return payment request
     */
    GatewayRequest buildUPIPaymentRequest(UPIPaymentRequestBean bean) {
        GatewayRequest request = new GatewayRequest();
        request.URL = BASE_ORDER_URL + "Pay";
        request.method = GatewayRequest.POST;
        request.payload = new PaymentPayload(bean);
        request.extraHeaders = getHeaders(bean.getHeaders());
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
        return headers;
    }
}
