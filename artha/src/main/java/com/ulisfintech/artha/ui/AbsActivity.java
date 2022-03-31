package com.ulisfintech.artha.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ulisfintech.artha.helper.ArthaConstants;
import com.ulisfintech.artha.helper.NFCListener;
import com.ulisfintech.artha.helper.OrderResponse;
import com.ulisfintech.artha.helper.SyncMessage;
import com.ulisfintech.artha.hostservice.KHostApduService;

abstract class AbsActivity extends AppCompatActivity implements NFCListener {


    private TransactionNotifier transactionNotifier;

    protected abstract void handleResponse(SyncMessage syncMessage);

    @Override
    protected void onResume() {
        super.onResume();
        if (transactionNotifier == null) transactionNotifier = new TransactionNotifier();
        registerReceiver(transactionNotifier, new IntentFilter(ArthaConstants.ACTION_TRANSACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (transactionNotifier != null) unregisterReceiver(transactionNotifier);
        //Stop service
        stopService(new Intent(this, KHostApduService.class));
    }

    @Override
    public void readSuccess(OrderResponse orderResponse) {

        SyncMessage syncMessage = new SyncMessage();
        syncMessage.message = "Payment successful";
        syncMessage.status = true;
        syncMessage.data = orderResponse;
        handleResponse(syncMessage);

        Log.e("<STATUS>", "<<<<<<<<" + syncMessage.message);
    }

    @Override
    public void readError() {

        SyncMessage syncMessage = new SyncMessage();
        syncMessage.message = "Payment successful";
        syncMessage.status = false;
        handleResponse(syncMessage);

        Log.e("<STATUS>", "<<<<<<<<" + syncMessage.message);
    }

    private class TransactionNotifier extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals(ArthaConstants.ACTION_TRANSACTION)) {
                OrderResponse orderResponse = intent.getParcelableExtra(PaymentActivity.NDEF_MESSAGE);
                if (orderResponse != null) {
                    readSuccess(orderResponse);
                } else {
                    readError();
                }
            }
        }
    }
}
