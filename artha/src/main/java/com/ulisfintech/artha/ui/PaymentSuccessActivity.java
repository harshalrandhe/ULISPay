package com.ulisfintech.artha.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ulisfintech.artha.R;
import com.ulisfintech.artha.databinding.ActivityPaymentSuccessBinding;
import com.ulisfintech.artha.helper.PaymentData;
import com.ulisfintech.artha.helper.SyncMessage;

public class PaymentSuccessActivity extends AppCompatActivity {

    private ActivityPaymentSuccessBinding binding;
    private SdkUtils sdkUtils;
    private PaymentViewModel paymentViewModel;
    private PaymentData paymentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentSuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        paymentViewModel = new ViewModelProvider(this).get(PaymentViewModel.class);
        sdkUtils = new SdkUtils();

        /**
         * Button
         */
        binding.btnDonePayment.setOnClickListener(view -> onBackPressed());

        /**
         * Observer
         * Intent Data Observer
         */
        paymentViewModel.getPaymentDataMutableLiveData().observe(this, intentDataObserver());

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //Process intent data
        paymentViewModel.setReceiptIntent(this, intent);

        if (intent.hasExtra(PaymentActivity.TRANSACTION_MESSAGE)) {
            SyncMessage syncMessage = getIntent().getParcelableExtra(PaymentActivity.TRANSACTION_MESSAGE);
            if (syncMessage.status) {
                binding.tvStatus.setText(syncMessage.message);
                binding.tvStatus.setTextColor(getColor(R.color.success_stroke_color));
                binding.ivError.setVisibility(View.GONE);
                binding.gifImage.setVisibility(View.VISIBLE);
            } else {
                binding.tvStatus.setText(syncMessage.message);
                binding.tvStatus.setTextColor(getColor(R.color.error_stroke_color));
                binding.ivError.setVisibility(View.VISIBLE);
                binding.gifImage.setVisibility(View.GONE);
            }
            binding.tvOrderId.setText(syncMessage.orderId == null ? "-" : syncMessage.orderId);
            binding.tvTransactionId.setText(syncMessage.transactionId == null ? "-" : syncMessage.transactionId);
        }
    }

    @Override
    public void onBackPressed() {
        //Post result back
        setResult(RESULT_OK, getIntent());
        finish();
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

            this.paymentData = paymentData;

            String mobile = paymentData.getVendorMobile();
            String strMobile = "XXXXXXXX" + mobile.substring(mobile.length() - 2);

            binding.tvVendorName.setText(paymentData.getVendorName());
            binding.tvVendorMobile.setText(strMobile);
            binding.tvProductName.setText(paymentData.getProduct());
            binding.tvProductPrice.setText(paymentData.getCurrency() + " " + paymentData.getPrice());
        };
    }
}