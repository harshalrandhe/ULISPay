package com.ulisfintech.artha;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public abstract class AbsActivity extends AppCompatActivity implements PaymentListener {


    private TransactionNotifier transactionNotifier;

    protected abstract void handleResponse();

    @Override
    protected void onResume() {
        super.onResume();
        if (transactionNotifier == null) transactionNotifier = new TransactionNotifier();
        registerReceiver(transactionNotifier, new IntentFilter(AppConstants.ACTION_TRANSACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (transactionNotifier != null) unregisterReceiver(transactionNotifier);
        //Stop service
        stopService(new Intent(this, KHostApduService.class));
    }

    @Override
    public void paymentSuccess() {
        Log.e("paymentSuccess", "<<<<<<<<");
        handleResponse();
    }

    @Override
    public void paymentError() {
        Log.e("paymentError", "<<<<<<<<");
        handleResponse();
    }

    private class TransactionNotifier extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals(AppConstants.ACTION_TRANSACTION)) {
                paymentSuccess();
            }
        }
    }
}
