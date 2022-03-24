package com.ulisfintech.artha.ui;

import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;

public class PaymentViewModel extends ViewModel {

    private MutableLiveData<PaymentData> paymentDataMutableLiveData;
    private MutableLiveData<OrderResponse> orderResponseMutableLiveData;

    public PaymentViewModel() {
        this.paymentDataMutableLiveData = new MutableLiveData<>();
        this.orderResponseMutableLiveData = new MutableLiveData<>();
    }

    /**
     * Observer Method
     *
     * @return payment data
     */
    public MutableLiveData<PaymentData> getPaymentDataMutableLiveData() {
        return paymentDataMutableLiveData;
    }

    /**
     * Observer Method
     *
     * @return product order data
     */
    public MutableLiveData<OrderResponse> getIsOrderCreated() {
        return orderResponseMutableLiveData;
    }

    /**
     * Intent Received From Marchant
     *
     * @param intent product data
     */
    public void setIntent(Intent intent) {
        PaymentData paymentData = intent.getParcelableExtra(PaymentActivity.NDEF_MESSAGE);
        String vendorMobile = paymentData.getVendorMobile();
        String strMobile = "XXXXXXXX" + vendorMobile.substring(vendorMobile.length() - 2);
        paymentData.setVendorMobile(strMobile);
        //Update
        paymentDataMutableLiveData.setValue(paymentData);

        /**
         *  Place New Order
         */
        createOrder(paymentData);
    }

    public void createOrder(PaymentData paymentData) {

        OrderBean orderBean = new OrderBean();
        orderBean.setAmount(paymentData.getPrice());
        orderBean.setCurrency("USD");
        orderBean.setCustomer_name("John");
        orderBean.setCustomer_email("john.d@ulistechnology.com");
        orderBean.setCustomer_mobile("0987654321");
        orderBean.setReturn_url("https://ulis.co.uk/payment_status");
        orderBean.setResource("API");
        HeaderBean headerBean = new HeaderBean();
        headerBean.setX_KEY(paymentData.getMerchantKey());
        headerBean.setX_PASSWORD(paymentData.getMerchantSecret());
        orderBean.setHeaders(headerBean);

        Gateway gateway = new Gateway();
        GatewayRequest request = gateway.buildGatewayRequest(orderBean);
        request.URL += "Create";
        gateway.call(request, new GatewayCallback() {
            @Override
            public void onSuccess(GatewayMap response) {
                Log.e("API>>", response.toString());
                Gson gson = new Gson();
                OrderResponse orderResponse = gson.fromJson(gson.toJson(response), OrderResponse.class);
                orderResponseMutableLiveData.setValue(orderResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("API>>", throwable.getMessage());
            }
        });
    }

    void checkOrderStatusAsync(HeaderBean headerBean) {
        Gateway gateway = new Gateway();
        GatewayRequest request = gateway.buildOrderStatusRequest("", headerBean);
        request.URL += "Details";
        gateway.call(request, new GatewayCallback() {
            @Override
            public void onSuccess(GatewayMap response) {
                Log.e("API>>", response.toString());
                Gson gson = new Gson();
//                OrderResponse orderResponse = gson.fromJson(gson.toJson(response), OrderResponse.class);
//                orderResponseMutableLiveData.setValue(orderResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("API>>", throwable.getMessage());
            }
        });
    }
}
