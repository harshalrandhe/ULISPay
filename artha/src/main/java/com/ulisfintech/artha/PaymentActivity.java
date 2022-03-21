package com.ulisfintech.artha;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;


import com.ulisfintech.artha.databinding.ActivityPaymentBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentActivity extends AbsActivity {

    private ActivityPaymentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        onNewIntent(getIntent());
        binding.btnCancel.setOnClickListener(view -> finish());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        JSONObject data;
        try {
            data = new JSONObject(intent.getStringExtra(AppConstants.NDEF_MESSAGE));
            String vendorName = data.getString("vendorName");
            String vendorMobile = data.getString("vendorMobile");
            String product = data.getString("product");
            String price = data.getString("price");

            String strMobile = "XXXXXXXX" + vendorMobile.substring(vendorMobile.length() - 2);

            binding.tvVendorName.setText(vendorName);
            binding.tvVendorMobile.setText(strMobile);
            binding.tvProductName.setText(product);
            binding.tvProductPrice.setText("₹" + price);

            Intent payIntent = new Intent(this, KHostApduService.class);
            payIntent.putExtra(AppConstants.NDEF_MESSAGE, intent.getStringExtra(AppConstants.NDEF_MESSAGE));
            startService(payIntent);

        } catch (JSONException e) {
            e.printStackTrace();
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Payments details are not available!")
                    .setPositiveButton("Okay", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        onBackPressed();
                    }).show();
        }
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