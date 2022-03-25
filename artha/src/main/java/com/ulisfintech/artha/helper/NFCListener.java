package com.ulisfintech.artha.helper;

import com.ulisfintech.artha.ui.OrderResponse;

public interface NFCListener {
    void readSuccess(OrderResponse orderResponse);
    void readError();
}
