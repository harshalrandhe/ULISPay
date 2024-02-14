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
import com.ulisfintech.telrpay.ui.order.OrderDetailsAPIResponse;

import org.json.JSONException;
import org.json.JSONObject;


public class PaymentViewModel extends ViewModel {

    private final MutableLiveData<PaymentData> paymentDataMutableLiveData;
    private final MutableLiveData<OrderResponse> orderResponseMutableLiveData;
    private final MutableLiveData<OrderStatusBean> orderStatusBeanMutableLiveData;
    private final NetBuilder netBuilder;
    private SweetAlertDialog progressDialog;

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

            //Update
            paymentDataMutableLiveData.setValue(paymentData);
            // API Call
            startProcessingPayment(context, paymentData);

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
     * Start processing payment
     * 1. Check merchant region UAE or KSA
     * 2. Create payment order
     * 3. Check order status
     *
     * @param context     calling activity context
     * @param paymentData order request
     */
    private void startProcessingPayment(Context context, PaymentData paymentData) {

        // Set Headers
        HeaderBean headerBean = new HeaderBean();
        headerBean.setXusername(APIConstant.X_USERNAME);
        headerBean.setXpassword(APIConstant.X_PASSWORD);
        headerBean.setMerchant_key(paymentData.getMerchantKey());
        headerBean.setMerchant_secret(paymentData.getMerchantSecret());
        headerBean.setIp(new SdkUtils().getMyIp(context));
        // API Call
        checkMerchantRegion(context, headerBean, paymentData);
    }


    /**
     * Check Merchant Region
     * and process payment according to region
     * (UAE & KSA) are run on different port
     *
     * @param context     calling activity context
     * @param headerBean  merchant key and secret
     * @param paymentData order request
     */
    void checkMerchantRegion(Context context, HeaderBean headerBean, PaymentData paymentData) {


        GatewayRequest request = new GatewayRequestBuilder().buildCheckRegionRequest(headerBean);
        netBuilder.call(request, new GatewayCallback() {
            @Override
            public void onSuccess(GatewayMap response) {

                Gson gson = new Gson();

                Log.e("<<URL>>", request.URL);
                Log.e("<<RESPONSE>>", gson.toJson(response));

                //TODO: handle API response

                RegionResponse regionResponse = gson.fromJson(gson.toJson(response.get("data")), RegionResponse.class);

                String region = regionResponse.getRegion();

                // Place New Order
                createOrderAsync(context, regionResponse.getRegion(), paymentData);
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
                            checkMerchantRegion(context, headerBean, paymentData);
                        }).setCancelClickListener(sweetAlertDialog -> {
                            sweetAlertDialog.dismiss();
                            orderStatusBeanMutableLiveData.setValue(null);
                        })
                        .show();
            }
        });
    }

    /**
     * Create Payment Order
     *
     * @param region
     * @param paymentData merchant details
     */
    void createOrderAsync(Context context, String region, PaymentData paymentData) {

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
        headerBean.setRegion(region);
        orderBean.setHeaders(headerBean);

        Log.e("<<REQUEST>>", new Gson().toJson(orderBean));

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
                    //Order Details
                    checkOrderDetailsAsync(context, headerBean, orderResponse);
                } else {
                    orderResponseMutableLiveData.setValue(null);
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
     * @param env
     */
    void checkOrderStatusAsync(Context context, HeaderBean headerBean, String orderId, String token, String env) {

        //API Call
        checkOrderStatus(context, headerBean, orderId, token, env);
    }


    /**
     * Check Order Details
     *
     * @param context       Calling activity context
     * @param headerBean    API Headers
     * @param orderResponse created order response
     */
    void checkOrderDetailsAsync(Context context, HeaderBean headerBean, OrderResponse orderResponse) {

        showProgress(context, "Processing your order","Do not press back button...");

        String orderId = orderResponse.getData().getOrder_id();
        String token = orderResponse.getData().getToken();

        GatewayRequest request = new GatewayRequestBuilder().buildOrderDetailsRequest(orderId, token, headerBean);
        netBuilder.call(request, new GatewayCallback() {
            @Override
            public void onSuccess(GatewayMap response) {

                if (progressDialog.isShowing()) progressDialog.dismiss();

                Gson gson = new Gson();

                Log.e("<<URL>>", request.URL);
                Log.e("<<RESPONSE>>", gson.toJson(response));

                OrderDetailsAPIResponse orderDetailsResponse = gson.fromJson(gson.toJson(response.get("data")),
                        OrderDetailsAPIResponse.class);

                if (orderDetailsResponse != null) {
                    orderResponse.setEnv(orderDetailsResponse.getOrder_details().getEnv());
                }

                orderResponse.setRegion(headerBean.getRegion());

                orderResponseMutableLiveData.setValue(orderResponse);
            }

            @Override
            public void onError(Throwable throwable) {

                if (progressDialog.isShowing()) progressDialog.dismiss();

                Log.e("<<URL>>", request.URL);
                Log.e("<<ERROR>>", throwable.getMessage());

                String endPoint = "transaction-details-print";
                //API Call
                checkOrderStatus(context, headerBean, orderId, token, endPoint);
            }
        });
    }


    /**
     * Check Order Details
     *
     * @param headerBean API Headers
     * @param orderId    Order Id
     * @param token      order token
     * @param env        Application Mode(Live/Test)
     */
    void checkOrderStatus(Context context, HeaderBean headerBean, String orderId, String token, String env) {

        String endPoint = "transaction-details-print";
        if (env.equalsIgnoreCase("test")) {
            endPoint = "test/transaction-details-print";
        }

        showProgress(context, "Transaction","Do not press back button...");

        GatewayRequest request = new GatewayRequestBuilder().buildOrderStatusRequest(orderId, endPoint, headerBean);
        netBuilder.call(request, new GatewayCallback() {
            @Override
            public void onSuccess(GatewayMap response) {

                if (progressDialog.isShowing()) progressDialog.dismiss();

                Gson gson = new Gson();

                Log.e("<<URL>>", request.URL);
                Log.e("<<RESPONSE>>", gson.toJson(response));

                OrderStatusResponse orderStatusResponse = gson.fromJson(gson.toJson(response),
                        OrderStatusResponse.class);

                if (orderStatusResponse != null && orderStatusResponse.getData() != null) {
                    orderStatusBeanMutableLiveData.setValue(orderStatusResponse.getData());
                } else {
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
                            checkOrderStatusAsync(context, headerBean, orderId, token, env);
                        }).setCancelClickListener(sweetAlertDialog -> {
                            sweetAlertDialog.dismiss();
                            orderStatusBeanMutableLiveData.setValue(null);
                        })
                        .show();
            }
        });
    }

    private void showProgress(Context context, String title, String message) {
        if (progressDialog == null) {
            progressDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            progressDialog.setTitleText(title);
            progressDialog.setContentText(message);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }
}
