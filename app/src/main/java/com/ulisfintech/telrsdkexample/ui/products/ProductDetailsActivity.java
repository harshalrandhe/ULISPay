package com.ulisfintech.telrsdkexample.ui.products;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ulisfintech.telrpay.SweetAlert.SweetAlertDialog;
import com.ulisfintech.telrpay.helper.ArthaConstants;
import com.ulisfintech.telrpay.helper.PaymentData;
import com.ulisfintech.telrpay.helper.SyncMessage;
import com.ulisfintech.telrpay.ui.Gateway;
import com.ulisfintech.telrpay.ui.GatewaySecureCallback;
import com.ulisfintech.telrsdkexample.BuildConfig;
import com.ulisfintech.telrsdkexample.R;
import com.ulisfintech.telrsdkexample.databinding.ActivityProductDetailsBinding;

public class ProductDetailsActivity extends AppCompatActivity implements CompoundButton
        .OnCheckedChangeListener, GatewaySecureCallback {

    private ActivityProductDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ProductBean productBean = getIntent().getParcelableExtra("product");
        binding.tvProductName.setText(productBean.getName());
        binding.tvProductDesc.setText(productBean.getDesc());
        binding.tvProductCategory.setText("Cat: " + productBean.getCategory());
        binding.tvProductPrice.setText("Price ₹ " + productBean.getPrice());

        binding.radioGPay.setOnCheckedChangeListener(this);
        binding.radioPhonePay.setOnCheckedChangeListener(this);
        binding.radioTelrPay.setOnCheckedChangeListener(this);

        String url = productBean.getImg();

        Glide.with(this)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.ivProductPoster);

        /**
         * Button
         */
        binding.btnBuyNow.setOnClickListener(view -> {
            binding.paymentLayout.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);
        });

        /**
         * Button
         */
        binding.btnPay.setOnClickListener(view -> {

            view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);

            PaymentData paymentData = new PaymentData();

            paymentData.setVendorName("ABC Vendor");
            paymentData.setVendorMobile("1122334455");
            paymentData.setProduct(productBean.getName());
            paymentData.setPrice(productBean.getPrice());
            paymentData.setCurrency("USD");

            paymentData.setCustomerName("Pawan Kushwaha");
            paymentData.setCustomerEmail("golu.r@ulistechnology.com");
            paymentData.setCustomerMobile("9011240343");
            paymentData.setReturnUrl("https://ulis.co.uk/payment_status");

            paymentData.setMerchantKey(BuildConfig.X_KEY);
            paymentData.setMerchantSecret(BuildConfig.X_PASSWORD);

            Gson gson = new Gson();
            paymentData.setProductBean(gson.fromJson(gson.toJson(productBean),
                    com.ulisfintech.telrpay.helper.ProductBean.class));

            if (binding.radioTelrPay.isChecked()) {

                paymentData.setPaymentType(ArthaConstants.PAYMENT_TYPE_TAP_AND_PAY);

                /**
                 * Start payment receiver
                 */
                Gateway.startReceivingPaymentActivity(this, paymentData);

            } else {
                Toast.makeText(this, "Not available at this time", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //After Result
        Gateway.handleSecureResult(requestCode, resultCode, data, this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean check) {
        binding.radioGPay.setChecked(false);
        binding.radioPhonePay.setChecked(false);
        binding.radioTelrPay.setChecked(false);

        compoundButton.setChecked(check);
    }

    @Override
    public void onTransactionComplete(SyncMessage syncMessage) {
        Log.e(this.getClass().getName(), new Gson().toJson(syncMessage));
        if (syncMessage.status) {
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("SUCCESS")
                    .setContentText(syncMessage.message)
                    .setConfirmText("Okay")
                    .setConfirmClickListener(Dialog::dismiss)
                    .show();
        } else {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Failed")
                    .setContentText(syncMessage.message)
                    .setConfirmText("Okay")
                    .setConfirmClickListener(Dialog::dismiss)
                    .show();
        }
    }

    @Override
    public void onTransactionCancel(SyncMessage syncMessage) {
        Log.e(this.getClass().getName(), new Gson().toJson(syncMessage));
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("CANCEL")
                .setContentText(syncMessage.message)
                .setConfirmText("Okay")
                .setConfirmClickListener(Dialog::dismiss)
                .show();
    }
}