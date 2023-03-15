package com.ulisfintech.telrpay.ui;

import com.ulisfintech.telrpay.helper.SyncMessage;

public interface GatewaySecureCallback {

    /**
     * Callback method when transaction is complete
     *
     * @param syncMessage A response map containing the Txn result
     */
//    void onSecureComplete(GatewayMap txnResult);
    void onTransactionComplete(SyncMessage syncMessage);

    /**
     * Callback when a user cancels the transaction flow. (typically on back press)
     */
//    void onSecureCancel();
    void onTransactionCancel(SyncMessage syncMessage);
}
