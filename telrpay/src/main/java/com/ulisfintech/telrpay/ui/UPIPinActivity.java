package com.ulisfintech.telrpay.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ulisfintech.telrpay.databinding.ActivityUpipinBinding;
import com.ulisfintech.telrpay.helper.OrderResponse;
import com.ulisfintech.telrpay.helper.PaymentData;
import com.ulisfintech.telrpay.helper.SyncMessage;

public class UPIPinActivity extends AppCompatActivity {

    static final String UPI_KEY = "upi_key";
    static final String VENDOR_NAME_KEY = "vendor_name_key";
    private PaymentViewModel paymentViewModel;
    private ActivityUpipinBinding binding;
    private final String PAYMENT_TYPE_UPI_PAY = "upi_pay";
    String upi = null;
    OrderResponse orderResponse = null;
    PaymentData paymentData = null;
    SdkUtils sdkUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpipinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        paymentViewModel = new ViewModelProvider(this).get(PaymentViewModel.class);

        /**
         * Observer
         */
        paymentViewModel.getTransactionResponseBeanMutableLiveData().observe(this, transactionResponseBean -> {
            sdkUtils = new SdkUtils();
            sdkUtils.setHapticEffect(binding.getRoot());

            if (transactionResponseBean != null) {

                SyncMessage syncMessage = new SyncMessage();
                syncMessage.orderId = transactionResponseBean.getOrder_id();
                syncMessage.transactionId = transactionResponseBean.getTransaction_id();
                syncMessage.transactionResponseBean = transactionResponseBean;

                if (transactionResponseBean.getStatus().equalsIgnoreCase(APIConstant.ORDER_STATUS_COMPLETED)) {

                    syncMessage.message = "Transaction is successful!";
                    syncMessage.status = true;

                } else {

                    syncMessage.message = "Transaction is failed!";
                    syncMessage.status = false;

                }

                //Intent
                postResultBack(syncMessage);
            }
        });

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getStringExtra(UPI_KEY) != null) {
            upi = intent.getStringExtra(UPI_KEY);

        }
        if (intent.getStringExtra(VENDOR_NAME_KEY) != null) {
            String vendorName = intent.getStringExtra(VENDOR_NAME_KEY);
            binding.tvUPI.setText(vendorName);
        }

        if (intent.getParcelableExtra(PaymentActivity.ORDER_MESSAGE) != null) {
            orderResponse = intent.getParcelableExtra(PaymentActivity.ORDER_MESSAGE);
            if (orderResponse != null) {
                if (orderResponse.getProductBean() != null) {
                    String price = "₹" + orderResponse.getProductBean().getPrice();
                    binding.tvPrice.setText(price);
                    binding.tvProductName.setText(orderResponse.getProductBean().getName());

                }
            }
        }
        if (intent.getParcelableExtra(PaymentActivity.PAYMENT_REQUEST) != null) {
            paymentData = intent.getParcelableExtra(PaymentActivity.PAYMENT_REQUEST);
        }

        binding.btnPay.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.edtPin.getWindowToken(), 0);

            upiPaymentAPICall(upi, orderResponse, paymentData);

        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, getIntent());
        finish();
    }


    /**
     * Payment API Call
     *
     * @param upi
     * @param orderResponse
     * @param paymentData
     */
    private void upiPaymentAPICall(String upi, OrderResponse orderResponse, PaymentData paymentData) {

        String pin = binding.edtPin.getText().toString().trim();
        String desc = binding.edtDesc.getText().toString().trim();

        UPIPaymentRequestBean upiPaymentRequestBean = new UPIPaymentRequestBean();
        //Card Details
        upiPaymentRequestBean.setName("John");
        upiPaymentRequestBean.setPin(pin);
        upiPaymentRequestBean.setUpi(upi);
        //Payment Description
        upiPaymentRequestBean.setDescription(desc);
        upiPaymentRequestBean.setType(PAYMENT_TYPE_UPI_PAY);

        if (orderResponse != null) {
            upiPaymentRequestBean.setToken(orderResponse.getToken());
            upiPaymentRequestBean.setOrder_id(orderResponse.getOrder_id());
        }

        if (paymentData != null) {
            //Headers
            HeaderBean headerBean = new HeaderBean();
            headerBean.setXusername(APIConstant.X_USERNAME);
            headerBean.setXpassword(APIConstant.X_PASSWORD);
            headerBean.setMerchant_key(paymentData.getMerchantKey());
            headerBean.setMerchant_secret(paymentData.getMerchantSecret());
            upiPaymentRequestBean.setHeaders(headerBean);
        }

        //API Call
        paymentViewModel.proceedToUPIPaymentAsync(this, upiPaymentRequestBean);
    }

    /**
     * Post result back to the merchant activity
     *
     * @param response product order data
     */
    private void postResultBack(SyncMessage response) {
        Intent intent = getIntent();
        intent.putExtra(PaymentActivity.EXTRA_TXN_RESULT, response);
        setResult(RESULT_OK, intent);
        finish();
    }
}