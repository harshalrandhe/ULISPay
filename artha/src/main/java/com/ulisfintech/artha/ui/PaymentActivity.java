package com.ulisfintech.artha.ui;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.ulisfintech.artha.R;
import com.ulisfintech.artha.databinding.ActivityPaymentBinding;
import com.ulisfintech.artha.hostservice.KHostApduService;

public class PaymentActivity extends AbsActivity {

    public static final String NDEF_MESSAGE = "com.ulisfintech.artha.android.ndefMessage";
    /**
     * The ACS Result data after performing 3DS
     */
    public static final String EXTRA_TXN_RESULT = "com.ulisfintech.artha.android.TXN_RESULT";

    private ActivityPaymentBinding binding;
    private PaymentViewModel paymentViewModel;
    private PaymentData paymentData;
    private NfcAdapter nfcAdapter;
    private AlertDialog.Builder mTurnNfcDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnCancel.setOnClickListener(view -> finish());

        paymentViewModel = getDefaultViewModelProviderFactory().create(PaymentViewModel.class);

        /**
         * Observer
         * Intent Data Observer
         */
        paymentViewModel.getPaymentDataMutableLiveData().observe(this, paymentData -> {

            if (paymentData == null) {
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Payments details are not available!")
                        .setPositiveButton("Okay", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            onBackPressed();
                        }).show();
                return;
            }

            String mobile = paymentData.getVendorMobile();
            String strMobile = "XXXXXXXX" + mobile.substring(mobile.length() - 2);

            binding.tvVendorName.setText(paymentData.getVendorName());
            binding.tvVendorMobile.setText(strMobile);
            binding.tvProductName.setText(paymentData.getProduct());
            binding.tvProductPrice.setText("₹" + paymentData.getPrice());

            this.paymentData = paymentData;


//            checkOrderStatus("ord_kyv5o7123n");
        });

        /**
         * Observer
         * Get call after successful order is created.
         * @orderResponse order data (order id and token).
         */
        paymentViewModel.getIsOrderCreated().observe(this, orderResponse -> {

            binding.tvOrderId.setText(orderResponse.getOrder_id());

            //Intent
            Intent payIntent = new Intent(this, KHostApduService.class);
            payIntent.putExtra(NDEF_MESSAGE, orderResponse);
            startService(payIntent);
        });

        /**
         * Observer
         * Check order status
         * @orderStatusBean order data
         */
        paymentViewModel.getOrderStatusBeanMutableLiveData().observe(this, orderStatusBean -> {
            if (orderStatusBean.getOrder_status().equalsIgnoreCase(APIConstant.ORDER_STATUS_COMPLETED)) {

                orderStatusBean.setMessage("Transaction is successful!");

                Intent intent = new Intent();
                intent.putExtra(EXTRA_TXN_RESULT, orderStatusBean);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle("NFC")
                    .setMessage("NFC is not available on this phone.")
                    .setPositiveButton("Okay", (dialogInterface, i) -> dialogInterface.dismiss())
                    .create();
        }

        onNewIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null && !nfcAdapter.isEnabled()) {
            showTurnOnNfcDialog();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (!intent.hasExtra(NDEF_MESSAGE)) {
            Log.e(this.getClass().getName(), "NDEF_MESSAGE not found!");
        }

        //Process intent data
        paymentViewModel.setIntent(this, intent);
    }

    @Override
    protected void handleResponse(SyncMessage syncMessage) {
        /**
         * After successful data read from this device through NFC
         * We have to check an order status here
         * Check Order Status
         */

        if (syncMessage.status) {
            checkOrderStatus(((OrderResponse) syncMessage.data).getOrder_id());
        }
    }

    /**
     * API Call
     * Check Order status
     *
     * @param orderId Oder id
     */
    private void checkOrderStatus(String orderId) {
        if (paymentData != null) {
            HeaderBean headerBean = new HeaderBean();
            headerBean.setX_KEY(paymentData.getMerchantKey());
            headerBean.setX_PASSWORD(paymentData.getMerchantSecret());
            //Call
            paymentViewModel.checkOrderStatusAsync(PaymentActivity.this, headerBean, orderId);
        }
    }

    private void showTurnOnNfcDialog() {
        if (mTurnNfcDialog == null) {
            String title = getString(R.string.ad_nfcTurnOn_title);
            String mess = getString(R.string.ad_nfcTurnOn_message);
            String pos = getString(R.string.ad_nfcTurnOn_pos);
            String neg = getString(R.string.ad_nfcTurnOn_neg);
            mTurnNfcDialog = new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(mess)
                    .setPositiveButton(pos, (dialogInterface, i) -> {
                        // Send the user to the settings page and hope they turn it on
                        startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
                    })
                    .setNegativeButton(neg, (dialogInterface, i) -> dialogInterface.dismiss());

        }
        mTurnNfcDialog.show();
    }
}