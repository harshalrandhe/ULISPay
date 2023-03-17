package com.ulisfintech.telrpay.ui;

import android.app.Activity;
import android.content.Intent;

import com.ulisfintech.telrpay.helper.PaymentData;
import com.ulisfintech.telrpay.helper.SyncMessage;

public class Gateway {

    static final int REQUEST_SECURE = 10000;

    public Gateway() {
    }

    /**
     * @param activity    calling activity
     * @param paymentData required data for payment transaction(Product information)
     */
    public static void startReceivingPaymentActivity(Activity activity, PaymentData paymentData) {
        Intent intent = new Intent(activity, PaymentActivity.class);
        intent.putExtra(PaymentActivity.PAYMENT_REQUEST, paymentData);
        activity.startActivityForResult(intent, REQUEST_SECURE);
    }

    /**
     * Handle Activity Result
     *
     * @param requestCode activity request code
     * @param resultCode  activity result code
     * @param data        result data
     * @param callback    gateway callback to handle result
     */
    public static void handleSecureResult(int requestCode, int resultCode, Intent data, GatewaySecureCallback callback) {

        if (callback == null) return;

        if (requestCode == REQUEST_SECURE) {
            if (resultCode == Activity.RESULT_OK) {
                SyncMessage syncMessage = data.getParcelableExtra(PaymentActivity.EXTRA_TXN_RESULT);
                callback.onTransactionComplete(syncMessage);
            } else {
                SyncMessage syncMessage = new SyncMessage();
                syncMessage.message = "Transaction cancel!";
                syncMessage.status = false;
                callback.onTransactionCancel(syncMessage);
            }
        }
    }
}
