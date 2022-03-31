package com.ulisfintech.artha.helper;

public interface NFCListener {
    void readSuccess(OrderResponse orderResponse);
    void readError();
}
