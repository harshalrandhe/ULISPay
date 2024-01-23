package com.ulisfintech.telrpay.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.ulisfintech.telrpay.R;
import com.ulisfintech.telrpay.databinding.ActivityProcessingPaymentBinding;
import com.ulisfintech.telrpay.helper.AppConstants;
import com.ulisfintech.telrpay.helper.OrderResponse;
import com.ulisfintech.telrpay.helper.PaymentData;
import com.ulisfintech.telrpay.helper.SyncMessage;

public class ProcessingPaymentActivity extends AppCompatActivity {

    /**
     * Transaction Success Result Launcher
     */
    ActivityResultLauncher<Intent> successResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        SyncMessage syncMessage = result.getData().getParcelableExtra(PaymentActivity.TRANSACTION_MESSAGE);
                        //Intent
                        postResultBack(syncMessage);
                    }
                }
            }
    );
    private ActivityProcessingPaymentBinding binding;
    private PaymentViewModel paymentViewModel;
    private SdkUtils sdkUtils;
    private PaymentData paymentData;
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        SyncMessage syncMessage = intent.getParcelableExtra(AppConstants.EXTRA_TXN_RESULT);
                        OrderResponse orderRes = intent.getParcelableExtra(PaymentActivity.ORDER_RESPONSE);
                        if (syncMessage != null && syncMessage.status) {

                            // Call API
                            checkOrderStatus(orderRes.getData().getOrder_id(), orderRes.getData().getToken());

                        }
                    }
                    // Handle the Intent
//                    setResponseAndExit(getString(R.string.label_txn_success), false);
                } else {
                    setResponseAndExit(getString(R.string.label_order_failed), false);
                }
            });
    private OrderResponse orderResponse;
    private boolean isOrderCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProcessingPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*
         * Here set status bar background color same as screen header
         */
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.gray_btn_bg_color));

        sdkUtils = new SdkUtils();


        binding.btnDonePayment.setOnClickListener(v -> {

            setResponseAndExit(getString(R.string.label_order_failed_retry), false);

        });

        paymentViewModel = getDefaultViewModelProviderFactory().create(PaymentViewModel.class);

        /**
         * Observer
         * Intent Data Observer
         */
        paymentViewModel.getPaymentDataMutableLiveData().observe(this, intentDataObserver());

        /**
         * Observer
         * Order Is Created
         * Get called after successful order is created.
         * @orderResponse order data (order id and token).
         */
        paymentViewModel.getIsOrderCreated().observe(this, orderIsCreatedObserver());

        /**
         * Observer
         * Check order status
         * @orderStatusBean order data
         */
        paymentViewModel.getOrderStatusBeanMutableLiveData().observe(this, checkOrderStatusObserver());

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null) {
            //Process intent data
            paymentViewModel.setIntent(this, intent);
        }

    }

    /**
     * Observer
     * Intent Data Observer
     */
    private Observer<? super PaymentData> intentDataObserver() {
        return paymentData -> {

            if (paymentData == null) {

                sdkUtils.errorAlert(this, "Payments details are not available!");

                return;
            }

            String mobile = paymentData.getProductDetails().getVendorMobile();
            String strMobile = "XXXXXXXX" + mobile.substring(mobile.length() - 2);
            String orderName = paymentData.getProductDetails().getProductName();
            String orderPrice = paymentData.getProductDetails().getCurrency() + " " +
                    paymentData.getProductDetails().getProductPrice();
            String imageUrl = paymentData.getProductDetails().getImage();

//            binding.tvOrderName.setText(orderName);
//            binding.tvOrderId.setText("");
//            binding.tvOrderAmount.setText(orderPrice);

//            new Thread(() -> downloadImageFromPath(imageUrl)).start();

            this.paymentData = paymentData;
        };
    }

    /**
     * Observer Method
     * Get called after successful order is created.
     */
    private Observer<? super OrderResponse> orderIsCreatedObserver() {
        return orderResponse -> {

            sdkUtils.setHapticEffect(binding.getRoot());

            if (orderResponse == null) {
                // In case of any exception
                binding.btnDonePayment.setVisibility(View.VISIBLE);
                binding.tvPaymentStatus.setTextColor(getColor(R.color.red_btn_bg_pressed_color));
                binding.gifImage.setGifImageResource(R.drawable.warning_circle);
                binding.tvPaymentStatus.setText(getString(R.string.label_order_failed_retry));

//                Intent intent = new Intent(this, WebCheckoutActivity.class);
//                intent.putExtra(PaymentActivity.ORDER_RESPONSE, orderResponse);
//                mStartForResult.launch(intent);
                return;
            }

            if (orderResponse.getStatus().equalsIgnoreCase("fail")) {
                binding.tvPaymentStatus.setTextColor(getColor(R.color.red_btn_bg_pressed_color));
//                binding.tvPaymentStatus.setText(getString(R.string.label_order_failed));
                binding.tvPaymentStatus.setText(orderResponse.getMessage());
                new Handler().postDelayed(() -> {
                    setResponseAndExit(orderResponse.getMessage(), false);
                }, 1500);
            } else {
                binding.tvPaymentStatus.setTextColor(getColor(R.color.material_deep_teal_50));
                this.orderResponse = orderResponse;
                orderResponse.setReturnUrl(this.paymentData.getOrder_details().getReturn_url());
                orderResponse.setMerchantUrls(this.paymentData.getMerchant_urls());
                isOrderCreated = true;

                Intent intent = new Intent(this, WebCheckoutActivity.class);
                intent.putExtra(PaymentActivity.ORDER_RESPONSE, orderResponse);
                mStartForResult.launch(intent);
            }
        };
    }

    /**
     * Observer Method
     * Check order status
     */
    private Observer<? super OrderStatusBean> checkOrderStatusObserver() {
        return orderStatusBean -> {

            if (orderStatusBean != null) {

                if (orderStatusBean.getTransactions().size() > 0) {

                    TransactionResponseBean transactionResponseBean = orderStatusBean.getTransactions().get(0);
                    String status = transactionResponseBean.getGateway_code();

                    SyncMessage syncMessage = new SyncMessage();
                    syncMessage.orderId = orderStatusBean.getOrder_id();
                    syncMessage.transactionId = transactionResponseBean.getTransaction_id();
                    syncMessage.orderStatusBean = orderStatusBean;

                    if (status.equalsIgnoreCase(APIConstant.ORDER_STATUS_AUTHORISED)) {
                        syncMessage.message = "Transaction is successful!";
                        syncMessage.status = true;
                    } else if (status.equalsIgnoreCase(APIConstant.ORDER_STATUS_FAILED)) {
                        syncMessage.message = "Transaction is failed!";
                        syncMessage.status = false;
                    } else if (status.equalsIgnoreCase(APIConstant.ORDER_STATUS_CANCELLED)) {
                        syncMessage.message = "Transaction is cancelled!";
                        syncMessage.status = false;
                    } else if (status.equalsIgnoreCase(APIConstant.ORDER_STATUS_DECLINED)) {
                        syncMessage.message = "Transaction is declined!";
                        syncMessage.status = false;
                    } else {
                        syncMessage.message = "Pending!";
                        syncMessage.status = false;
                    }

                    //Show
                    showTransactionReceipt(syncMessage);
                }else{

                    setResponseAndExit("Transaction error!", false);
                }
            }
        };
    }

    /**
     * API Call
     * Check Order status
     *
     * @param orderId Order id
     * @param token   Order token
     */
    private void checkOrderStatus(String orderId, String token) {
        if (paymentData != null) {
            HeaderBean headerBean = new HeaderBean();
            headerBean.setXusername(APIConstant.X_USERNAME);
            headerBean.setXpassword(APIConstant.X_PASSWORD);
            headerBean.setMerchant_key(paymentData.getMerchantKey());
            headerBean.setMerchant_secret(paymentData.getMerchantSecret());
            headerBean.setIp(sdkUtils.getMyIp(this));
            //Call
            paymentViewModel.checkOrderStatusAsync(this, headerBean, orderId, token);
        }
    }

    /**
     * Transaction Receipt Screen
     *
     * @param syncMessage transaction data
     */
    private void showTransactionReceipt(SyncMessage syncMessage) {
        Intent intent = new Intent(this, PaymentSuccessActivity.class);
        intent.putExtra(PaymentActivity.PAYMENT_REQUEST, paymentData);
        intent.putExtra(PaymentActivity.TRANSACTION_MESSAGE, syncMessage);
        successResultLauncher.launch(intent);
    }

    /**
     * Post Result With Response Back
     *
     * @param message transaction status message
     * @param status  transaction status
     */
    private void setResponseAndExit(String message, boolean status) {
        SyncMessage syncMessage = new SyncMessage();
        syncMessage.data = null;
        syncMessage.message = message;
        syncMessage.status = status;
        //Intent
        postResultBack(syncMessage);
    }

    /**
     * Post result back to the merchant activity
     *
     * @param response product order data
     */
    private void postResultBack(SyncMessage response) {
        Intent intent = new Intent();
        intent.putExtra(AppConstants.EXTRA_TXN_RESULT, response);
        setResult(RESULT_OK, intent);
        finish();
    }
}