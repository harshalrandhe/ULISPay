package com.ulisfintech.artha.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

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
        intent.putExtra(PaymentActivity.NDEF_MESSAGE, paymentData);
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
