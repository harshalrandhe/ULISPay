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
        JSONObject data;
        try {
            data = new JSONObject(intent.getStringExtra(ArthaConstants.NDEF_MESSAGE));
            String vendorName = data.getString("vendorName");
            String vendorMobile = data.getString("vendorMobile");
            String product = data.getString("product");
            double price = data.getDouble("price");

            String strMobile = "XXXXXXXX" + vendorMobile.substring(vendorMobile.length() - 2);

            paymentDataMutableLiveData.setValue(new PaymentData(vendorName, strMobile, product, price))
            ;
        } catch (JSONException e) {
            e.printStackTrace();
            paymentDataMutableLiveData.setValue(null);
        }
    }
}
