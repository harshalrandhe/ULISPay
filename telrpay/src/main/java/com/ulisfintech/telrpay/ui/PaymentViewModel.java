package com.ulisfintech.telrpay.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.ulisfintech.telrpay.R;
import com.ulisfintech.telrpay.SweetAlert.SweetAlertDialog;
import com.ulisfintech.telrpay.helper.OrderResponse;
import com.ulisfintech.telrpay.helper.PaymentData;

import org.json.JSONException;
import org.json.JSONObject;


public class PaymentViewModel extends ViewModel {

    private final MutableLiveData<PaymentData> paymentDataMutableLiveData;
    private final MutableLiveData<OrderResponse> orderResponseMutableLiveData;
    private final MutableLiveData<OrderStatusBean> orderStatusBeanMutableLiveData;
    private final NetBuilder netBuilder;
    private SweetAlertDialog progressDialog;
    private int statusCounter;

    public PaymentViewModel() {
        netBuilder = new NetBuilder();
        this.paymentDataMutableLiveData = new MutableLiveData<>();
        this.orderResponseMutableLiveData = new MutableLiveData<>();
        this.orderStatusBeanMutableLiveData = new MutableLiveData<>();
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
     * Observer Method
     *
     * @return order status and other data
     */
    public MutableLiveData<OrderStatusBean> getOrderStatusBeanMutableLiveData() {
        return orderStatusBeanMutableLiveData;
    }

    /**
     * Intent Received From Merchant
     *
     * @param intent product data
     */
    public void setIntent(Context context, Intent intent) {

        try {

            String paymentDataStr = intent.getStringExtra(PaymentActivity.PAYMENT_REQUEST);

            PaymentData paymentData = new Gson().fromJson(paymentDataStr, PaymentData.class);
            JSONObject jsonObject = new JSONObject(paymentDataStr);

//        Log.e("<<Intent>>", new Gson().toJson(paymentData));
//        String vendorMobile = paymentData.getProductDetails().getVendorMobile();
//        String strMobile = "XXXXXXXX" + vendorMobile.substring(vendorMobile.length() - 2);
//        paymentData.setVendorMobile(strMobile);

            //Update
            paymentDataMutableLiveData.setValue(paymentData);

            /**
             *  Place New Order
             */
            createOrderAsync(context, paymentData, jsonObject);

        } catch (JSONException err) {
            Log.d("Error", "Invalid parameters!");
        }
    }

    /**
     * Intent Received From Payment Gateway
     *
     * @param intent product and transaction data
     */
    public void setReceiptIntent(Context context, Intent intent) {

        PaymentData paymentData = intent.getParcelableExtra(PaymentActivity.PAYMENT_REQUEST);
        String vendorMobile = paymentData.getProductDetails().getVendorMobile();
        String strMobile = "XXXXXXXX" + vendorMobile.substring(vendorMobile.length() - 2);
//        paymentData.setVendorMobile(strMobile);
        //Update
        paymentDataMutableLiveData.setValue(paymentData);

    }

    /**
     * Create Payment Order
     *
     * @param paymentData merchant details
     */
    void createOrderAsync(Context context, PaymentData paymentData, JSONObject jsonObject) {

        OrderBean orderBean = new OrderBean();

        // Set Customer Details
        orderBean.setCustomer_details(paymentData.getCustomer_details());
        // Set Billing Details
        orderBean.setBilling_details(paymentData.getBilling_details());
        // Set Shipping Details
        orderBean.setShipping_details(paymentData.getShipping_details());
        // Set Order From Mobile SDK
//        orderBean.setMobile_sdk(1);
        // Set Order Details
        orderBean.setOrder_details(paymentData.getOrder_details());
        // Set Merchant Urls
        orderBean.setMerchant_urls(paymentData.getMerchant_urls());
        // Set Transaction
        orderBean.setTransaction(paymentData.getTransaction());

        // Set Headers
        HeaderBean headerBean = new HeaderBean();
        headerBean.setXusername(APIConstant.X_USERNAME);
        headerBean.setXpassword(APIConstant.X_PASSWORD);
        headerBean.setMerchant_key(paymentData.getMerchantKey());
        headerBean.setMerchant_secret(paymentData.getMerchantSecret());
        headerBean.setIp(new SdkUtils().getMyIp(context));
        orderBean.setHeaders(headerBean);

        GatewayRequest request = new GatewayRequestBuilder().buildCreateOrderRequest(orderBean);
        netBuilder.call(request, new GatewayCallback() {

            @Override
            public void onSuccess(GatewayMap response) {

//                Log.e("<<URL>>", request.URL);
                Log.e("<<REQUEST>>", new Gson().toJson(request));
                Log.e("<<RESPONSE>>", response.toString());
                Gson gson = new Gson();
                OrderResponse orderResponse = gson.fromJson(gson.toJson(response), OrderResponse.class);
                if (orderResponse != null) {
                    orderResponseMutableLiveData.setValue(orderResponse);
                }
            }

            @Override
            public void onError(Throwable throwable) {

                Log.e("<<URL>>", request.URL);
                Log.e("<<ERROR>>", throwable.getMessage());

                orderResponseMutableLiveData.setValue(null);
            }

        });
    }

    /**
     * Check Order Status
     *
     * @param headerBean API Headers
     * @param orderId    Order Id
     * @param token      token
     */
    void checkOrderStatusAsync(Context context, HeaderBean headerBean, String orderId, String token) {

        if (progressDialog == null) {
            progressDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            progressDialog.setTitleText("Transaction");
            progressDialog.setContentText("Do not press back button...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        } else {
            statusCounter++;
        }

        GatewayRequest request = new GatewayRequestBuilder().buildOrderStatusRequest(orderId, token, headerBean);
        netBuilder.call(request, new GatewayCallback() {
            @Override
            public void onSuccess(GatewayMap response) {

                Gson gson = new Gson();

                Log.e("<<URL>>", request.URL);
                Log.e("<<RESPONSE>>", gson.toJson(response));

                if (progressDialog.isShowing()) progressDialog.dismiss();

                OrderStatusResponse orderStatusResponse = gson.fromJson(gson.toJson(response),
                        OrderStatusResponse.class);

                if (orderStatusResponse != null && orderStatusResponse.getData() != null) {
                    orderStatusBeanMutableLiveData.setValue(orderStatusResponse.getData());
                }else{
                    orderStatusBeanMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onError(Throwable throwable) {

                if (progressDialog.isShowing()) progressDialog.dismiss();

                Log.e("<<URL>>", request.URL);
                Log.e("<<ERROR>>", throwable.getMessage());

                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("ERROR!")
                        .setContentText(context.getString(R.string.network_error_message))
                        .setCancelText("Cancel")
                        .setCancelClickListener(Dialog::dismiss)
                        .setConfirmText("Retry")
                        .setConfirmClickListener(sweetAlertDialog -> {
                            sweetAlertDialog.dismiss();
                            //Retry API
                            checkOrderStatusAsync(context, headerBean, orderId, token);

                        }).setCancelClickListener(sweetAlertDialog -> {
                            sweetAlertDialog.dismiss();
                            orderStatusBeanMutableLiveData.setValue(null);
                        })
                        .show();
            }
        });
    }
}
