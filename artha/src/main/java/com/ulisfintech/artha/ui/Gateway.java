package com.ulisfintech.artha.ui;

import android.app.Activity;
import android.content.Intent;

import com.google.gson.Gson;
import com.ulisfintech.artha.helper.PaymentData;

public class Gateway {

    static final int REQUEST_SECURE = 10000;
    String merchantId;

    public Gateway() {
    }

    public static void startReceivingPaymentActivity(Activity activity, PaymentData paymentData) {
        Intent intent = new Intent(activity, PaymentActivity.class);
        intent.putExtra(PaymentActivity.NDEF_MESSAGE, paymentData);
        activity.startActivityForResult(intent, REQUEST_SECURE);
    }

    public static boolean handleSecureResult(int requestCode, int resultCode, Intent data, GatewaySecureCallback callback) {
        if (callback == null) {
            return false;
        }

        if (requestCode == REQUEST_SECURE) {
            if (resultCode == Activity.RESULT_OK) {
                BaseResponse txnResult = data.getParcelableExtra(PaymentActivity.EXTRA_TXN_RESULT);
//                String acsResultJson = data.getStringExtra(PaymentActivity.EXTRA_TXN_RESULT);
//                GatewayMap acsResult = new GatewayMap(acsResultJson);
                callback.onSecureComplete(txnResult);
            } else {
                callback.onSecureCancel(new BaseResponse(PaymentActivity.CANCEL,"Transaction cancel!"));
            }

            return true;
        }

        return false;
    }

    /**
     * Gets the current Merchant ID
     *
     * @return The current Merchant ID
     */
    public String getMerchantId() {
        return merchantId;
    }

    /**
     * Sets the current Merchant ID
     *
     * @param merchantId A valid Merchant ID
     * @return The <tt>Gateway</tt> instance
     * @throws IllegalArgumentException If the provided Merchant ID is null
     */
    public Gateway setMerchantId(String merchantId) {
        if (merchantId == null) {
            throw new IllegalArgumentException("Merchant ID may not be null");
        }

        this.merchantId = merchantId;

        return this;
    }
}
