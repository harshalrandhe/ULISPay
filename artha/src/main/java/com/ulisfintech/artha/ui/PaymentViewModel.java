package com.ulisfintech.artha.ui;

import android.content.Intent;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ulisfintech.artha.helper.ArthaConstants;
import com.ulisfintech.artha.helper.PaymentData;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentViewModel extends ViewModel {

    private MutableLiveData<PaymentData> paymentDataMutableLiveData;

    public PaymentViewModel() {
        this.paymentDataMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<PaymentData> getPaymentDataMutableLiveData() {
        return paymentDataMutableLiveData;
    }

    public void setIntent(Intent intent) {

        PaymentData paymentData = intent.getParcelableExtra(PaymentActivity.NDEF_MESSAGE);
        String vendorName = paymentData.getVendorName();
        String vendorMobile = paymentData.getVendorMobile();
        String product = paymentData.getProduct();
        double price = paymentData.getPrice();

        String strMobile = "XXXXXXXX" + vendorMobile.substring(vendorMobile.length() - 2);

        paymentDataMutableLiveData.setValue(new PaymentData(vendorName, strMobile, product, price))
        ;
    }
}
