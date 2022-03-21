package com.ulisfintech.myapplication.ui.products;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.ulisfintech.artha.helper.ArthaConstants;
import com.ulisfintech.artha.helper.JSONConvector;
import com.ulisfintech.artha.helper.PaymentData;
import com.ulisfintech.artha.ui.PaymentActivity;
import com.ulisfintech.myapplication.R;
import com.ulisfintech.myapplication.databinding.ActivityProductDetailsBinding;

public class ProductDetailsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private ActivityProductDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toolbar toolbar = binding.toolbar;
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
//        toolbar.setNavigationIcon(getDrawable(R.drawable.ic_back));
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

        binding.btnBuyNow.setOnClickListener(view -> {
            binding.paymentLayout.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);
        });

        binding.btnPay.setOnClickListener(view -> {
            if (binding.radioArtha.isChecked()) {
                PaymentData paymentData = new PaymentData();
                paymentData.setVendorName("ABC Vendor");
                paymentData.setVendorMobile("1122334455");
                paymentData.setProduct(productBean.getName());
                paymentData.setPrice(productBean.getPrice());
                Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra(ArthaConstants.NDEF_MESSAGE, JSONConvector.toJSON(paymentData));
                startActivity(intent);
            }else{
                Toast.makeText(this, "Not available at this time", Toast.LENGTH_SHORT).show();
            }
        });
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

}