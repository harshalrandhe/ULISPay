package com.ulisfintech.artha.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ulisfintech.artha.databinding.ActivityPaymentSuccessBinding;
import com.ulisfintech.artha.helper.PaymentData;

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
         * Observer
         * Intent Data Observer
         */
        paymentViewModel.getPaymentDataMutableLiveData().observe(this, intentDataObserver());

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

//        Intent intent = new Intent(activity, PaymentSuccessActivity.class);
//        intent.putExtra(PaymentActivity.NDEF_MESSAGE, paymentData);
//        activity.startActivityForResult(intent, REQUEST_SECURE);

        if (intent.getParcelableExtra(PaymentActivity.NDEF_MESSAGE) != null) {
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

            String mobile = paymentData.getVendorMobile();
            String strMobile = "XXXXXXXX" + mobile.substring(mobile.length() - 2);

            binding.tvVendorName.setText(paymentData.getVendorName());
            binding.tvVendorMobile.setText(strMobile);
            binding.tvProductName.setText(paymentData.getProduct());
            binding.tvProductPrice.setText("₹" + paymentData.getPrice());
            binding.tvOrderId.setText("");

            this.paymentData = paymentData;
        };
    }
}