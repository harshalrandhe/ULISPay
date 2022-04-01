package com.ulisfintech.artha.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.ulisfintech.artha.R;
import com.ulisfintech.artha.SweetAlert.SweetAlertDialog;
import com.ulisfintech.artha.cardreader.CardNfcAsyncTask;
import com.ulisfintech.artha.cardreader.CardNfcUtils;
import com.ulisfintech.artha.databinding.ActivityPaymentBinding;
import com.ulisfintech.artha.helper.OrderResponse;
import com.ulisfintech.artha.helper.PaymentData;
import com.ulisfintech.artha.helper.SyncMessage;
import com.ulisfintech.artha.hostservice.KHostApduService;

import java.util.List;


public class PaymentActivity extends AbsActivity {

    public static final String NDEF_MESSAGE = "com.ulisfintech.artha.android.ndefMessage";
    static final String ORDER_MESSAGE = "com.ulisfintech.artha.android.orderMessage";
    /**
     * The ACS Result data after performing 3DS
     */
    public static final String EXTRA_TXN_RESULT = "com.ulisfintech.artha.android.TXN_RESULT";

    private ActivityPaymentBinding binding;
    private PaymentViewModel paymentViewModel;
    private PaymentData paymentData;
    private OrderResponse orderResponse;
    private NfcAdapter nfcAdapter;

    private CardNfcUtils cardNfcUtils;

    private CardNfcAsyncTask cardNfcAsyncTask;
    private boolean intentFromCreate;
    private boolean isScanNow;
    private SweetAlertDialog progressDialog;
    private final String PAYMENT_TYPE_CARD_PAY = "card_pay";
    private boolean isSharePaymentRunning;
    private final String ARTHA_TAP_AND_PAY = "Artha ( Tap & Pay)";
    private final String ARTHA_SHARE_PAY = "Artha Share Pay";
    String[] list = {ARTHA_TAP_AND_PAY, ARTHA_SHARE_PAY};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnCancel.setOnClickListener(view -> finish());

        paymentViewModel = getDefaultViewModelProviderFactory().create(PaymentViewModel.class);

        /**
         * Button
         */
        binding.btnChangePaymentMethod.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Pay with")
                    .setCancelable(false)
                    .setItems(list, (dialogInterface, which) -> {

                        if (list[which].equalsIgnoreCase(ARTHA_TAP_AND_PAY)) {
                            //
                            startNFCPaymentService(orderResponse);
                        } else {
                            String packageName = "com.ulisfintech.arthacustomer";
                            if (isPackageInstalled(this, packageName)) {
                                //
                                startSharePayment(packageName, orderResponse);
                            } else {
                                applicationNotInstalled();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })
                    .show();
        });

        /**
         * Observer
         * Intent Data Observer
         */
        paymentViewModel.getPaymentDataMutableLiveData().observe(this, paymentData -> {

            if (paymentData == null) {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("ERROR!")
                        .setContentText("Payments details are not available!")
                        .setConfirmText("Okay")
                        .setConfirmClickListener(Dialog::dismiss)
                        .show();
                return;
            }

            String mobile = paymentData.getVendorMobile();
            String strMobile = "XXXXXXXX" + mobile.substring(mobile.length() - 2);

            binding.tvVendorName.setText(paymentData.getVendorName());
            binding.tvVendorMobile.setText(strMobile);
            binding.tvProductName.setText(paymentData.getProduct());
            binding.tvProductPrice.setText("₹" + paymentData.getPrice());

            this.paymentData = paymentData;
        });

        /**
         * Observer
         * Get called after successful order is created.
         * @orderResponse order data (order id and token).
         */
        paymentViewModel.getIsOrderCreated().observe(this, orderResponse -> {

            binding.tvOrderId.setText(orderResponse.getOrder_id());

            this.orderResponse = orderResponse;

            new AlertDialog.Builder(this)
                    .setTitle("Pay with")
                    .setCancelable(false)
                    .setItems(list, (dialogInterface, which) -> {

                        if (list[which].equalsIgnoreCase(ARTHA_TAP_AND_PAY)) {
                            //
                            startNFCPaymentService(orderResponse);
                        } else {
                            String packageName = "com.ulisfintech.arthacustomer";
                            if (isPackageInstalled(this, packageName)) {
                                //
                                startSharePayment(packageName, orderResponse);
                            } else {
                                applicationNotInstalled();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })
                    .show();
        });

        /**
         * Observer
         * Check order status
         * @orderStatusBean order data
         */
        paymentViewModel.getOrderStatusBeanMutableLiveData().observe(this, orderStatusBean -> {
            if (orderStatusBean.getOrder_status().equalsIgnoreCase(APIConstant.ORDER_STATUS_COMPLETED)) {

                orderStatusBean.setMessage("Transaction is successful!");

                SyncMessage syncMessage = new SyncMessage();
                syncMessage.data = orderStatusBean;
                syncMessage.message = "Transaction is successful!";
                syncMessage.status = true;
                //Intent
                postResultBack(syncMessage);

            } else if (orderStatusBean.getOrder_status().equalsIgnoreCase(APIConstant.ORDER_STATUS_FAILED)) {

                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("ERROR!")
                        .setContentText("Transaction failed!, please try again")
                        .setConfirmText("Okay")
                        .setConfirmClickListener(sweetAlertDialog -> {
                            sweetAlertDialog.dismiss();
                            onBackPressed();
                        })
                        .show();
            }
        });

        /**
         * Observer
         * Check Transaction status
         * @transactionResponseBean transaction data
         */
        paymentViewModel.getTransactionResponseBeanMutableLiveData().observe(this, transactionResponseBean -> {

            cardNfcUtils.enableDispatch();

            if (transactionResponseBean != null) {
                if (transactionResponseBean.getStatus().equalsIgnoreCase(APIConstant.ORDER_STATUS_COMPLETED)) {

                    SyncMessage syncMessage = new SyncMessage();
                    syncMessage.data = transactionResponseBean;
                    syncMessage.message = "Transaction is successful!";
                    syncMessage.status = true;
                    //Intent
                    postResultBack(syncMessage);

                } else {
                    new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(transactionResponseBean.getStatus())
                            .setContentText(transactionResponseBean.getResult_message())
                            .setConfirmText("Okay")
                            .setConfirmClickListener(sweetAlertDialog -> {

                                sweetAlertDialog.dismiss();

                                SyncMessage syncMessage = new SyncMessage();
                                syncMessage.data = transactionResponseBean;
                                syncMessage.message = "Transaction is failed!";
                                syncMessage.status = true;
                                //Intent
                                postResultBack(syncMessage);
                            })
                            .show();
                }
            } else {
                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Payment")
                        .setContentText("Payment not processed, please try again!")
                        .setConfirmText("Retry")
                        .setConfirmClickListener(sweetAlertDialog -> {
                            sweetAlertDialog.dismiss();
                            onNewIntent(getIntent());
                        })
                        .setCancelText("Cancel")
                        .setCancelClickListener(sweetAlertDialog -> {
                            sweetAlertDialog.dismiss();
                            onBackPressed();
                        })
                        .show();
            }
        });

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("NFC")
                    .setContentText("NFC is not available on this phone.")
                    .setConfirmText("Okay")
                    .setConfirmClickListener(sweetAlertDialog -> {

                        sweetAlertDialog.dismiss();

                        SyncMessage syncMessage = new SyncMessage();
                        syncMessage.data = null;
                        syncMessage.message = "NFC is not available on this phone.";
                        syncMessage.status = false;
                        //Intent
                        postResultBack(syncMessage);

                    })
                    .show();
        } else {
            cardNfcUtils = new CardNfcUtils(this);
            intentFromCreate = true;
        }
        createProgressDialog();
        onNewIntent(getIntent());
    }

    private void applicationNotInstalled() {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Error")
                .setContentText("Application is not currently installed.")
                .setConfirmText("Okay")
                .setConfirmClickListener(SweetAlertDialog::dismissWithAnimation)
                .show();
    }

    private void startNFCPaymentService(OrderResponse orderResponse) {
        binding.paymentMethodLabel.setText(getString(R.string.tap_and_pay));
        //Intent
        Intent payIntent = new Intent(this, KHostApduService.class);
        payIntent.putExtra(NDEF_MESSAGE, orderResponse);
        startService(payIntent);
    }

    private void stopNFCPaymentService() {
        //Intent
        Intent payIntent = new Intent(this, KHostApduService.class);
        stopService(payIntent);
    }

    private void startSharePayment(String packageName, OrderResponse orderResponse) {
        stopNFCPaymentService();
        binding.paymentMethodLabel.setText(getString(R.string.share_pay));
        orderResponse.setProductBean(paymentData.getProductBean());
        Intent intentForPackage = getPackageManager().getLaunchIntentForPackage(packageName);
        intentForPackage.putExtra(ORDER_MESSAGE, new Gson().toJson(orderResponse));
        intentForPackage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentForPackage.setAction("receive_data_from_artha");
        startActivity(intentForPackage);
        isSharePaymentRunning = true;
    }

    /**
     * Post result back to the merchant activity
     *
     * @param response product order data
     */
    private void postResultBack(SyncMessage response) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TXN_RESULT, response);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        intentFromCreate = false;
        if (nfcAdapter != null && !nfcAdapter.isEnabled()) {
            showTurnOnNfcDialog();
            if (!isScanNow) {

            }
        }
        cardNfcUtils.enableDispatch();

        if (isSharePaymentRunning) {
            isSharePaymentRunning = false;
            //
            checkOrderStatus(this.orderResponse.getOrder_id());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (nfcAdapter != null) cardNfcUtils.disableDispatch();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


        if (intent.getAction() == null) {

            if (!intent.hasExtra(NDEF_MESSAGE)) {
                Log.e(this.getClass().getName(), "NDEF_MESSAGE not found!");
            }


            //Process intent data
            paymentViewModel.setIntent(this, intent);

        } else if (intent.getAction() != null && intent.getAction().equals("android.nfc.action.TAG_DISCOVERED") ||
                intent.getAction() != null && intent.getAction().equals("android.nfc.action.TECH_DISCOVERED")) {

            if (nfcAdapter != null && nfcAdapter.isEnabled()) {
                cardNfcAsyncTask = new CardNfcAsyncTask.Builder(new CardNfcAsyncTask.CardNfcInterface() {

                    @Override
                    public void startNfcReadCard() {
                        isScanNow = true;
                        progressDialog.show();
                    }

                    @Override
                    public void cardIsReadyToRead() {
                        paymentAPICall();
                    }

                    @Override
                    public void mobilePhoneDetected() {
                        Log.e("<mobileDetected>", "mobilePhoneDetected....");
                        if (nfcAdapter != null) cardNfcUtils.disableDispatch();
                        //Intent
                        Intent payIntent = new Intent(PaymentActivity.this, KHostApduService.class);
                        payIntent.putExtra(NDEF_MESSAGE, orderResponse);
                        startService(payIntent);
                    }

                    @Override
                    public void doNotMoveCardSoFast() {
                        showSnackBar(getString(R.string.snack_doNotMoveCard));
                    }

                    @Override
                    public void unknownEmvCard() {
                        showSnackBar(getString(R.string.snack_unknownEmv));
                    }

                    @Override
                    public void cardWithLockedNfc() {
                        showSnackBar(getString(R.string.snack_lockedNfcCard));
                    }

                    @Override
                    public void finishNfcReadCard() {
                        progressDialog.dismiss();
                        cardNfcAsyncTask = null;
                        isScanNow = false;
                        Log.e("<mobileDetected>", "mobilePhoneDetected....");
                        if (nfcAdapter != null) cardNfcUtils.disableDispatch();

                        //Intent
                        Intent payIntent = new Intent(PaymentActivity.this, KHostApduService.class);
                        payIntent.putExtra(NDEF_MESSAGE, orderResponse);
                        startService(payIntent);
                    }
                }, intent, intentFromCreate).build();
            }
        }
    }

    /**
     * Payment API Call
     */
    private void paymentAPICall() {

        if (cardNfcAsyncTask == null) {
            Log.e("<<cardNfcAsyncTask>>", "cardNfcAsyncTask is null!!");
            return;
        }

        String cardNumber = cardNfcAsyncTask.getCardNumber().trim();
        String expiredDate = cardNfcAsyncTask.getCardExpireDate();
        String cardType = cardNfcAsyncTask.getCardType();

        PaymentRequestBean paymentRequestBean = new PaymentRequestBean();
        //Card Details
        paymentRequestBean.setName("");
        paymentRequestBean.setCard_number(cardNumber);
        paymentRequestBean.setExpiration_date(expiredDate);
        paymentRequestBean.setCvv("");
        //Payment Description
        paymentRequestBean.setDescription("Card Pay");
        paymentRequestBean.setType(PAYMENT_TYPE_CARD_PAY);
        //Address Details
        paymentRequestBean.setAddress1("Address1");
        paymentRequestBean.setAddress2("Address2");
        paymentRequestBean.setCity("New York");
        paymentRequestBean.setState("CF");
        paymentRequestBean.setCountry("USA");
        paymentRequestBean.setPostalcode("123456");


        if (orderResponse != null) {
            paymentRequestBean.setToken(orderResponse.getToken());
            paymentRequestBean.setOrder_id(orderResponse.getOrder_id());
        }

        if (paymentData != null) {
            //Headers
            HeaderBean headerBean = new HeaderBean();
            headerBean.setX_KEY(paymentData.getMerchantKey());
            headerBean.setX_PASSWORD(paymentData.getMerchantSecret());
            paymentRequestBean.setHeaders(headerBean);
        }

        paymentRequestBean.setSource("sdk");

        //API Call
        paymentViewModel.proceedToPaymentAsync(PaymentActivity.this, paymentRequestBean);
    }

    private void showSnackBar(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Create Progress Dialog
     */
    private void createProgressDialog() {
        String title = getString(R.string.ad_progressBar_title);
        String mess = getString(R.string.ad_progressBar_mess);
        progressDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText(title);
        progressDialog.setContentText(mess);
        progressDialog.setCancelable(false);
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

    /**
     * Turn On NFC
     * NFC setting dialog
     */
    private void showTurnOnNfcDialog() {
        String title = getString(R.string.ad_nfcTurnOn_title);
        String mess = getString(R.string.ad_nfcTurnOn_message);
        String pos = getString(R.string.ad_nfcTurnOn_pos);
        String neg = getString(R.string.ad_nfcTurnOn_neg);
        new SweetAlertDialog(this, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(title)
                .setContentText(mess)
                .setContentText(pos)
                .setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismiss();
                    // Send the user to the settings page and hope they turn it on
                    startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
                })
                .setCancelText(neg)
                .setCancelClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismiss();
                    onBackPressed();
                })
                .show();
    }

    public boolean isPackageInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent == null) {
            return false;
        }
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return !list.isEmpty();
    }
}