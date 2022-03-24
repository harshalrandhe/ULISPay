package com.ulisfintech.myapplication.ui.products;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.ulisfintech.artha.ui.PaymentData;
import com.ulisfintech.artha.ui.BaseResponse;
import com.ulisfintech.artha.ui.Gateway;
import com.ulisfintech.artha.ui.GatewaySecureCallback;
import com.ulisfintech.myapplication.BuildConfig;
import com.ulisfintech.myapplication.R;
import com.ulisfintech.myapplication.databinding.ActivityProductDetailsBinding;

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
        binding.radioArtha.setOnCheckedChangeListener(this);

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
            if (binding.radioArtha.isChecked()) {

                PaymentData paymentData = new PaymentData();
                paymentData.setVendorName("ABC Vendor");
                paymentData.setVendorMobile("1122334455");
                paymentData.setProduct(productBean.getName());
                paymentData.setPrice(productBean.getPrice());

                paymentData.setMerchantKey(BuildConfig.X_KEY);
                paymentData.setMerchantSecret(BuildConfig.X_PASSWORD);

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
        Gateway.handleSecureResult(requestCode, resultCode, data,this);
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
        binding.radioArtha.setChecked(false);

        compoundButton.setChecked(check);
    }

    @Override
    public void onSecureComplete(BaseResponse txnResult) {
        new AlertDialog.Builder(this)
                .setTitle("SUCCESS")
                .setMessage(txnResult.getMessage())
                .setPositiveButton("Okay", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    onBackPressed();
                }).show();
    }

    @Override
    public void onSecureCancel(BaseResponse txnResult) {
        new AlertDialog.Builder(this)
                .setTitle("CANCEL")
                .setMessage(txnResult.getMessage())
                .setPositiveButton("Okay", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    onBackPressed();
                }).show();
    }
}