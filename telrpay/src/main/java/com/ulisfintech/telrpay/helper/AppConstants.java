package com.ulisfintech.telrpay.helper;
 public interface AppConstants {

    String ACTION_TRANSACTION = "action_transaction_";
    String ACTION_DESTROY = "action_destroy_called";
    String REASON = "reason";
    String HANDSHAKE = "Hello From Artha";

    int PAYMENT_TYPE_TAP_AND_PAY = 1;
    int PAYMENT_TYPE_ARTHA_PAY = 2;
    int PAYMENT_TYPE_CARD_PAY = 3;


    String EXTRA_TXN_RESULT = "com.ulisfintech.telrpay.android.TXN_RESULT";
}
