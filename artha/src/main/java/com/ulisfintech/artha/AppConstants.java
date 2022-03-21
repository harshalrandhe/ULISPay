package com.ulisfintech.artha;

import org.jetbrains.annotations.NotNull;

public interface AppConstants {

    String DIALOG_CANCEL = "Cancel";
    String DIALOG_OK = "Ok";
    String DIALOG_YES = "Yes";
    String DIALOG_NO = "No";

    String CARD_ITEM = "card_item";
    String CARD_TXN_ITEM = "card_txn_item";

    String TABLE_CARD = "card";
    String TABLE_USER = "user";
    String TABLE_CARD_TRN = "card_trans";

    String ITEM_POSITION = "item_position";

    String NDEF_MESSAGE = "ndefMessage";
    String PAYMENT_ACTIVITY = "payment_activity";
    @NotNull
    String DEF_NDEF_MSG = "Hello From Aurtha";
    String PAY_BTN_LABEL = "Cancel Transaction";

    String IS_CARDS_DATA_CHANGE = "isCardsDataChange";
    int EVENT_TYPE_VERIFY_MOBILE = 101;
    int EVENT_TYPE_CHANGE_MOBILE = 102;
    int EVENT_TYPE_OTP_VERIFIED = 103;
    int EVENT_TYPE_REGISTERED = 104;
    int EVENT_TYPE_PAYMENT_RECEIVED = 105;
    String USER_NAME = "username";
    String USER_DATA = "userdata";
    String ACTION_TRANSACTION = "action_transaction_";
    String REASON = "reason";
}
