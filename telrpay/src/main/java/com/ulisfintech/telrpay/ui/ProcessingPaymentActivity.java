package com.ulisfintech.telrpay.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.ulisfintech.telrpay.R;
import com.ulisfintech.telrpay.databinding.ActivityProcessingPaymentBinding;
import com.ulisfintech.telrpay.helper.OrderResponse;
import com.ulisfintech.telrpay.helper.PaymentData;
import com.ulisfintech.telrpay.helper.SyncMessage;

public class ProcessingPaymentActivity extends AppCompatActivity {

    public static final String EXTRA_TXN_RESULT = "com.ulisfintech.telrpay.android.TXN_RESULT";

    private ActivityProcessingPaymentBinding binding;
    private PaymentViewModel paymentViewModel;
    private SdkUtils sdkUtils;
    private PaymentData paymentData;
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
                binding.tvPaymentStatus.setText(getString(R.string.label_order_failed));
                return;
            }

            if (orderResponse.getStatus().equalsIgnoreCase("fail")) {
                binding.tvPaymentStatus.setTextColor(getColor(R.color.red_btn_bg_pressed_color));
                binding.tvPaymentStatus.setText(getString(R.string.label_order_failed));
//                new Handler().postDelayed(() -> {
//                    setResponseAndExit(getString(R.string.label_order_failed), false);
//                }, 1500);
            } else {
                binding.tvPaymentStatus.setTextColor(getColor(R.color.material_deep_teal_50));
                this.orderResponse = orderResponse;
                isOrderCreated = true;

                Intent intent = new Intent(this, WebCheckoutActivity.class);
                intent.putExtra(PaymentActivity.ORDER_RESPONSE, orderResponse);
                mStartForResult.launch(intent);
            }
        };
    }

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    // Handle the Intent
                    setResponseAndExit(getString(R.string.label_txn_success), false);
                }else{
                    setResponseAndExit(getString(R.string.label_order_failed), false);
                }
            });

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
        intent.putExtra(EXTRA_TXN_RESULT, response);
        setResult(RESULT_OK, intent);
        finish();
    }
}