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
import com.ulisfintech.telrpay.helper.PaymentData;
import com.ulisfintech.telrpay.helper.SyncMessage;
import com.ulisfintech.telrpay.helper.TransactionBean;
import com.ulisfintech.telrpay.ui.Gateway;
import com.ulisfintech.telrpay.ui.GatewaySecureCallback;
import com.ulisfintech.telrpay.ui.order.BillingDetails;
import com.ulisfintech.telrpay.ui.order.CustomerDetails;
import com.ulisfintech.telrpay.ui.order.MerchantUrls;
import com.ulisfintech.telrpay.ui.order.OrderDetails;
import com.ulisfintech.telrpay.ui.order.ProductDetails;
import com.ulisfintech.telrpay.ui.order.ShippingDetails;
import com.ulisfintech.telrsdkexample.R;
import com.ulisfintech.telrsdkexample.databinding.ActivityProductDetailsBinding;

import org.json.JSONException;
import org.json.JSONObject;

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

            ProductDetails productDetails = new ProductDetails();
            productDetails.setVendorName("ABC Vendor");
            productDetails.setVendorMobile("1122334455");
            productDetails.setProductName(productBean.getName());
            productDetails.setProductPrice(productBean.getPrice());
            productDetails.setCurrency("AED");
            productDetails.setImage(productBean.getImg());
            paymentData.setProductDetails(productDetails);

            CustomerDetails customerDetails = new CustomerDetails();
            customerDetails.setName("PayTM USER");
            customerDetails.setEmail("rasibas152@kameili.com");
            customerDetails.setMobile("5353453533");
            customerDetails.setMobile_code("91");
            paymentData.setCustomer_details(customerDetails);

            // Set Billing Details
            BillingDetails billingDetails = new BillingDetails();
            billingDetails.setAddress_line1("Wardhman nagar ,nagpur");
            billingDetails.setAddress_line2("");
            billingDetails.setCity("Nagpur");
            billingDetails.setCountry("India");
            billingDetails.setProvince("Maharashtra");
            billingDetails.setPin("");
            paymentData.setBilling_details(billingDetails);

            // Set Shipping Details
            ShippingDetails shippingDetails = new ShippingDetails();
            shippingDetails.setAddress_line1("");
            shippingDetails.setAddress_line2("");
            shippingDetails.setCity("");
            shippingDetails.setCountry("");
            shippingDetails.setProvince("");
            shippingDetails.setPin("");
            paymentData.setShipping_details(shippingDetails);

            //Set Order Details
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrder_id("ORD" + System.currentTimeMillis());
            orderDetails.setAmount(paymentData.getProductDetails().getProductPrice());
            orderDetails.setCurrency(paymentData.getProductDetails().getCurrency());
            orderDetails.setDescription("Mobile Payment");
            orderDetails.setReturn_url("https://ulis.live:8081/status");
            paymentData.setOrder_details(orderDetails);

            MerchantUrls merchantUrls = new MerchantUrls();
            merchantUrls.setSuccess("https://ulis.live:8081/status");
            merchantUrls.setCancel("https://ulis.live:8081/status");
            merchantUrls.setFailure("https://ulis.live:8081/status");
            paymentData.setMerchant_urls(merchantUrls);

//            paymentData.setMerchantKey(BuildConfig.MERCHANT_KEY);
//            paymentData.setMerchantSecret(BuildConfig.MERCHANT_PASSWORD);
            paymentData.setMerchantKey("live-SH10ZQM18IQ");
            paymentData.setMerchantSecret("sec-IW101K818CW");
//            paymentData.setMerchantKey("test-ZR1OGP6NR");
//            paymentData.setMerchantSecret("sec-3P1LHI6GR");

            Gson gson = new Gson();
            paymentData.setProductBean(gson.fromJson(gson.toJson(productBean),
                    com.ulisfintech.telrpay.helper.ProductBean.class));

            paymentData.setTransaction(new TransactionBean("ecom", "MOBILESDK"));

            if (binding.radioTelrPay.isChecked()) {

//                paymentData.setPaymentType(AppConstants.PAYMENT_TYPE_TAP_AND_PAY);

                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(gson.toJson(paymentData));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                /**
                 * Start payment receiver
                 */
                try {
                    Gateway.startReceivingPaymentActivity(this, jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
        Log.e("onTransactionComplete: ", new Gson().toJson(syncMessage));
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