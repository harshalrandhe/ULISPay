package com.ulisfintech.artha.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ulisfintech.artha.SweetAlert.SweetAlertDialog;
import com.ulisfintech.artha.databinding.ActivityUpipinBinding;
import com.ulisfintech.artha.helper.OrderResponse;

public class UPIPinActivity extends AppCompatActivity {

    static final String UPI_KEY = "upi_key";
    static final String VENDOR_NAME_KEY = "vendor_name_key";
    private PaymentViewModel paymentViewModel;
    private ActivityUpipinBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpipinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        paymentViewModel = new ViewModelProvider(this).get(PaymentViewModel.class);
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getStringExtra(UPI_KEY) != null) {
            String upi = intent.getStringExtra(UPI_KEY);

        }
        if (intent.getStringExtra(VENDOR_NAME_KEY) != null) {
            String vendorName = intent.getStringExtra(VENDOR_NAME_KEY);
            binding.tvUPI.setText(vendorName);
        }

        if (intent.getParcelableExtra(PaymentActivity.ORDER_MESSAGE) != null) {
            OrderResponse orderResponse = intent.getParcelableExtra(PaymentActivity.ORDER_MESSAGE);
            if (orderResponse != null) {
                if (orderResponse.getProductBean() != null) {
                    String price = "₹" + orderResponse.getProductBean().getPrice();
                    binding.tvPrice.setText(price);
                    binding.tvProductName.setText(orderResponse.getProductBean().getName());

                }
            }
        }

        Handler handler = new Handler();

        binding.edtPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.edtPin.getText().toString().length() == 6) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.edtPin.getWindowToken(), 0);

                    if (binding.edtPin.getText().toString().equalsIgnoreCase("123456")) {

                        binding.progressBar.setVisibility(View.VISIBLE);
                        handler.postDelayed(() -> {

                            binding.progressBar.setVisibility(View.GONE);
                            setResult(RESULT_OK, getIntent());
                            finish();

                        }, 4000);

                    } else {
                        new SweetAlertDialog(UPIPinActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Wrong Pin!")
                                .setContentText("Please enter correct UPI pin!")
                                .setConfirmText("Okay")
                                .setConfirmClickListener(sweetAlertDialog -> {
                                    sweetAlertDialog.dismiss();
                                    binding.edtPin.setText("");
                                    binding.edtPin.setFocusable(true);
                                    imm.hideSoftInputFromWindow(binding.edtPin.getWindowToken(), 1);
                                })
                                .show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, getIntent());
        finish();
    }
}