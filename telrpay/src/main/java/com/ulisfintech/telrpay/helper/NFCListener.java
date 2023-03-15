package com.ulisfintech.telrpay.helper;

public interface NFCListener {
    void readSuccess(OrderResponse orderResponse);
    void readError();
}
