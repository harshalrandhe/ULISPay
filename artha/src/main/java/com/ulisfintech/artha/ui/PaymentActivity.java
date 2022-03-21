package com.ulisfintech.artha.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;


import com.ulisfintech.artha.databinding.ActivityPaymentBinding;
import com.ulisfintech.artha.helper.ArthaConstants;
import com.ulisfintech.artha.helper.JSONConvector;
import com.ulisfintech.artha.hostservice.KHostApduService;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentActivity extends AbsActivity {

    private ActivityPaymentBinding binding;
    private PaymentViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnCancel.setOnClickListener(view -> finish());

        viewModel = getDefaultViewModelProviderFactory().create(PaymentViewModel.class);
        viewModel.getPaymentDataMutableLiveData().observe(this, paymentData -> {
            if (paymentData == null) {
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Payments details are not available!")
                        .setPositiveButton("Okay", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            onBackPressed();
                        }).show();
                return;
            }

            String mobile = paymentData.getVendorMobile();
            String strMobile = "XXXXXXXX" + mobile.substring(mobile.length() - 2);

            binding.tvVendorName.setText(paymentData.getVendorName());
            binding.tvVendorMobile.setText(strMobile);
            binding.tvProductName.setText(paymentData.getProduct());
            binding.tvProductPrice.setText("₹" + paymentData.getPrice());

            //Intent
            Intent payIntent = new Intent(this, KHostApduService.class);
            payIntent.putExtra(ArthaConstants.NDEF_MESSAGE, JSONConvector.toJSON(paymentData));
            startService(payIntent);
        });

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (!intent.hasExtra(intent.getStringExtra(ArthaConstants.NDEF_MESSAGE))) {
            Log.e(this.getClass().getName(), "NDEF_MESSAGE not found!");
        }

        viewModel.setIntent(intent);
    }

    @Override
    protected void handleResponse() {

        new AlertDialog.Builder(this)
                .setTitle("SUCCESS")
                .setMessage("Transaction is successful!")
                .setPositiveButton("Okay", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    onBackPressed();
                }).show();

    }
}