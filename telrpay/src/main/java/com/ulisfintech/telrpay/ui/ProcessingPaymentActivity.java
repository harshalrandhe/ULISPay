package com.ulisfintech.telrpay.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.ulisfintech.telrpay.R;
import com.ulisfintech.telrpay.databinding.ActivityProcessingPaymentBinding;

public class ProcessingPaymentActivity extends AppCompatActivity {

    private ActivityProcessingPaymentBinding binding;

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


        new Handler().postDelayed(() -> {

//            startActivity(new Intent(this, PaymentSuccessActivity.class));

        }, 4000);
    }
}