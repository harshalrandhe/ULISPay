package com.ulisfintech.artha.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.ulisfintech.artha.BuildConfig;
import com.ulisfintech.artha.R;
import com.ulisfintech.artha.SweetAlert.SweetAlertDialog;
import com.ulisfintech.artha.cardreader.CardNfcAsyncTask;
import com.ulisfintech.artha.cardreader.CardNfcUtils;
import com.ulisfintech.artha.databinding.ActivityPaymentBinding;
import com.ulisfintech.artha.helper.OrderResponse;
import com.ulisfintech.artha.helper.PaymentData;
import com.ulisfintech.artha.helper.SyncMessage;
import com.ulisfintech.artha.hostservice.KHostApduService;

import java.util.concurrent.TimeUnit;


public class PaymentActivity extends AbsActivity {

    public static final String NDEF_MESSAGE = "com.ulisfintech.artha.android.ndefMessage";
    static final String ORDER_MESSAGE = "com.ulisfintech.artha.android.orderMessage";
    static final String TRANSACTION_MESSAGE = "com.ulisfintech.artha.android.transactionMessage";
    /**
     * The ACS Result data after performing 3DS
     */
    public static final String EXTRA_TXN_RESULT = "com.ulisfintech.artha.android.TXN_RESULT";
    private static final int TIMEOUT_TIMER = 60000;
    private static final int INTERVAL = 1000;

    private ActivityPaymentBinding binding;
    private PaymentViewModel paymentViewModel;
    private PaymentData paymentData;
    private OrderResponse orderResponse;
    private NfcAdapter nfcAdapter;

    private CardNfcUtils cardNfcUtils;

    private CardNfcAsyncTask cardNfcAsyncTask;
    private SweetAlertDialog progressDialog;
    private final String PAYMENT_TYPE_CARD_PAY = "card_pay";
    private boolean intentFromCreate;
    private boolean isScanNow;
    private boolean isSharePaymentRunning;
    private boolean isCardPaymentRunning;
    private boolean isMobilePaymentRunning;
    private boolean isUPIPaymentRunning;
    private boolean isOrderCreated;
    private boolean isSessionExpire;


    private CountDownTimer countDownTimer;
    private SdkUtils sdkUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sdkUtils = new SdkUtils();

        /**
         * Button
         * Cancel Transaction
         */
        binding.btnCancel.setOnClickListener(view -> {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Cancel Transaction")
                    .setContentText("Are you sure?")
                    .setCancelText("No")
                    .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                    .setConfirmText("Yes")
                    .setConfirmClickListener(sweetAlertDialog ->
                            setResponseAndExit("Transaction cancel!", false))
                    .show();
        });

        paymentViewModel = getDefaultViewModelProviderFactory().create(PaymentViewModel.class);

        /**
         * Button
         * Change Payment Method
         */
        binding.btnChangePaymentMethod.setOnClickListener(view -> {
            if (orderResponse != null) {
                //Dialog
                showSelectPaymentMethodDialog();
            }
        });

        /**
         * Observer
         * Intent Data Observer
         */
        paymentViewModel.getPaymentDataMutableLiveData().observe(this, intentDataObserver());

        /**
         * Observer
         * Order Is Created
         * Get called after successful order is created.
         * @orderResponse order data (order id and token).
         */
        paymentViewModel.getIsOrderCreated().observe(this, orderIsCreatedObserver());

        /**
         * Observer
         * Check order status
         * @orderStatusBean order data
         */
        paymentViewModel.getOrderStatusBeanMutableLiveData().observe(this, checkOrderStatusObserver());

        /**
         * Observer
         * Check Transaction status
         * @transactionResponseBean transaction data
         */
        paymentViewModel.getTransactionResponseBeanMutableLiveData().observe(this, transactionStatusObserver());

        /**
         * Initialize NfcAdapter
         * Getting null if NFC is not available on android phone.
         */
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {

            //Post response
            setResponseAndExit("NFC is not available on this phone.", false);

        } else {

            cardNfcUtils = new CardNfcUtils(this);
            intentFromCreate = true;
        }


        progressDialog = sdkUtils.createProgressDialog(this);

        onNewIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        intentFromCreate = false;
        if (nfcAdapter != null && !nfcAdapter.isEnabled()) {
            sdkUtils.showTurnOnNfcDialog(PaymentActivity.this);
        }

        if (!cardNfcUtils.isDispatchEnabled()) {
            cardNfcUtils.enableDispatch();
        }

        if (isSharePaymentRunning) {
            isSharePaymentRunning = false;
            //
            checkOrderStatus(this.orderResponse.getOrder_id());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null && cardNfcUtils.isDispatchEnabled()) cardNfcUtils.disableDispatch();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopSessionTimer();
    }

    /**
     * Observer
     * Check Transaction status
     */
    private Observer<? super TransactionResponseBean> transactionStatusObserver() {
        return transactionResponseBean -> {

            sdkUtils.setHapticEffect(binding.getRoot());

            if (transactionResponseBean != null) {

                SyncMessage syncMessage = new SyncMessage();
                syncMessage.orderId = transactionResponseBean.getOrder_id();
                syncMessage.transactionId = transactionResponseBean.getTransaction_id();
                syncMessage.transactionResponseBean = transactionResponseBean;

                if (transactionResponseBean.getStatus().equalsIgnoreCase(APIConstant.ORDER_STATUS_COMPLETED)) {

                    syncMessage.message = "Transaction is successful!";
                    syncMessage.status = true;
                    //Show
                    showTransactionReceipt(syncMessage);

                } else {

                    syncMessage.message = "Transaction is failed!";
                    syncMessage.status = false;
                    //Show
                    showTransactionReceipt(syncMessage);
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

            isCardPaymentRunning = false;
        };
    }

    /**
     * Observer
     * Intent Data Observer
     */
    private Observer<? super PaymentData> intentDataObserver() {
        return paymentData -> {

            if (paymentData == null) {

                sdkUtils.errorAlert(this, "Payments details are not available!");

                return;
            }

            String mobile = paymentData.getVendorMobile();
            String strMobile = "XXXXXXXX" + mobile.substring(mobile.length() - 2);

            binding.tvVendorName.setText(paymentData.getVendorName());
            binding.tvVendorMobile.setText(strMobile);
            binding.tvProductName.setText(paymentData.getProduct());
            binding.tvProductPrice.setText("₹" + paymentData.getPrice());

            this.paymentData = paymentData;
        };
    }

    /**
     * Observer Method
     * Get called after successful order is created.
     */
    private Observer<? super OrderResponse> orderIsCreatedObserver() {
        return orderResponse -> {

            sdkUtils.setHapticEffect(binding.getRoot());

            binding.tvOrderId.setText(orderResponse.getOrder_id());

            this.orderResponse = orderResponse;

            isOrderCreated = true;

            //Dialog
            showSelectPaymentMethodDialog();

            startSessions();

        };
    }

    /**
     * Observer Method
     * Check order status
     */
    private Observer<? super OrderStatusBean> checkOrderStatusObserver() {
        return orderStatusBean -> {
            if (orderStatusBean.getOrder_status().equalsIgnoreCase(APIConstant.ORDER_STATUS_COMPLETED)) {

                orderStatusBean.setMessage("Transaction is successful!");

                SyncMessage syncMessage = new SyncMessage();
                syncMessage.orderId = orderStatusBean.getOrder_id();
                syncMessage.transactionId = orderStatusBean.getTransaction_id();
                syncMessage.orderStatusBean = orderStatusBean;
                syncMessage.message = "Transaction is successful!";
                syncMessage.status = true;

                //Show
                showTransactionReceipt(syncMessage);

            } else if (orderStatusBean.getOrder_status().equalsIgnoreCase(APIConstant.ORDER_STATUS_FAILED)) {

//                sdkUtils.errorAlert(this, "Transaction failed!, please try again", true);
                orderStatusBean.setMessage("Transaction failed!");

                SyncMessage syncMessage = new SyncMessage();
                syncMessage.orderId = orderStatusBean.getOrder_id();
                syncMessage.transactionId = orderStatusBean.getTransaction_id();
                syncMessage.orderStatusBean = orderStatusBean;
                syncMessage.message = "Transaction failed!";
                syncMessage.status = false;

                //Show
                showTransactionReceipt(syncMessage);

            }

            isMobilePaymentRunning = false;
        };
    }

    /**
     * Dialog
     * Show Payment Method
     */
    private void showSelectPaymentMethodDialog() {
        sdkUtils.showSelectPaymentMethodDialog(this, new SdkUtils.PaymentOptionListener() {
            @Override
            public void onTapAndPay() {
                //
                startNFCPaymentService(orderResponse);
            }

            @Override
            public void onSharePay() {
                String packageName = BuildConfig.FRIEND;
                if (sdkUtils.isPackageInstalled(PaymentActivity.this, packageName)) {
                    //
                    startSharePayment(packageName, orderResponse);
                } else {
                    String msg = "Application is not currently installed.";
                    sdkUtils.errorAlert(PaymentActivity.this, msg);
                }
            }
        });
    }

    /**
     * Process Payment
     * Start NFC Service With Order Data
     *
     * @param orderResponse product order data
     */
    private void startNFCPaymentService(OrderResponse orderResponse) {
        if (!isOrderCreated) {
            showOrderIsNotCreated();
            return;
        }

        binding.paymentMethodLabel.setText(getString(R.string.tap_and_pay));
        binding.ivScanCardPoster.setVisibility(View.VISIBLE);
        binding.ivScanCardPoster.setImageResource(R.drawable.nfc);

        //Intent
        Intent payIntent = new Intent(this, KHostApduService.class);
        payIntent.putExtra(NDEF_MESSAGE, orderResponse);
        startService(payIntent);
    }

    /**
     * Stop NFC Service
     */
    private void stopNFCPaymentService() {
        //Intent
        Intent payIntent = new Intent(this, KHostApduService.class);
        stopService(payIntent);
    }

    /**
     * Process Payment
     * Start share payment
     *
     * @param packageName   payment application package name
     * @param orderResponse product order data
     */
    private void startSharePayment(String packageName, OrderResponse orderResponse) {
        stopNFCPaymentService();
        binding.paymentMethodLabel.setText(getString(R.string.share_pay));
        binding.ivScanCardPoster.setVisibility(View.GONE);
        orderResponse.setProductBean(paymentData.getProductBean());
        Intent intentForPackage = getPackageManager().getLaunchIntentForPackage(packageName);
        intentForPackage.putExtra(ORDER_MESSAGE, new Gson().toJson(orderResponse));
        intentForPackage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intentForPackage.setAction("receive_data_from_artha");
        startActivity(intentForPackage);
        isSharePaymentRunning = true;
        stopSessionTimer();
    }

    private void stopSessionTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
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

            if (isSessionExpire) {
                return;
            }

            if (nfcAdapter != null && nfcAdapter.isEnabled() && !isScanNow) {
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
                        if (nfcAdapter != null && cardNfcUtils.isDispatchEnabled()) {
                            cardNfcUtils.disableDispatch();
                        }
                        //Intent
                        startNFCPaymentService(orderResponse);
                    }

                    @Override
                    public void doNotMoveCardSoFast() {
                        showSnackBar(getString(R.string.snack_doNotMoveCard));
                    }

                    @Override
                    public void unknownEmvCard() {
                        //showSnackBar(getString(R.string.snack_unknownEmv));
                        String msg = cardNfcAsyncTask.readFromTag(intent);
                        orderResponse.setProductBean(paymentData.getProductBean());
                        isUPIPaymentRunning = true;
                        stopSessionTimer();
                        Intent launcherIntent = new Intent(PaymentActivity.this, UPIPinActivity.class);
                        launcherIntent.putExtra(UPIPinActivity.UPI_KEY, msg);
                        if (paymentData != null && paymentData.getVendorName() != null) {
                            launcherIntent.putExtra(UPIPinActivity.VENDOR_NAME_KEY, paymentData.getVendorName());
                        }
                        launcherIntent.putExtra(ORDER_MESSAGE, orderResponse);
                        launcherIntent.putExtra(PaymentActivity.NDEF_MESSAGE, paymentData);
                        upiPaymentResultLauncher.launch(launcherIntent);
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
                        Log.e("<finishNfcReadCard>", "finishNfcReadCard....");
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

        if (!isOrderCreated) {
            showOrderIsNotCreated();
            return;
        }

        sdkUtils.setHapticEffect(binding.getRoot());

        isCardPaymentRunning = true;
        stopSessionTimer();

        String cardNumber = cardNfcAsyncTask.getCardNumber().trim();
        String expiredDate = cardNfcAsyncTask.getCardExpireDate();
        String cardType = cardNfcAsyncTask.getCardType();

        PaymentRequestBean paymentRequestBean = new PaymentRequestBean();
        //Card Details
        paymentRequestBean.setName("");
        paymentRequestBean.setCard_number(cardNumber);
        paymentRequestBean.setExpiration_date(expiredDate);
//        paymentRequestBean.setCvv("");
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


    /**
     * Dialog
     * Order is not created.
     * Recreate order
     */
    private void showOrderIsNotCreated() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Order")
                .setContentText("Order is not place yet, please place order and try again!")
                .setConfirmText("Recreate order")
                .setConfirmClickListener(sweetAlertDialog -> {
                    if (paymentData != null) {
                        //Recreate order
                        paymentViewModel.createOrderAsync(this, paymentData);
                    }
                }).setCancelText("Cancel")
                .setCancelClickListener(Dialog::dismiss)
                .show();
    }

    /**
     * Toast
     *
     * @param message toast text message
     */
    private void showSnackBar(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void handleResponse(SyncMessage syncMessage) {
        /**
         * After successful data read from this device through NFC
         * We have to check an order status here
         * Check Order Status
         */

        if (syncMessage.status) {

            isMobilePaymentRunning = true;
            stopSessionTimer();
            checkOrderStatus(((OrderResponse) syncMessage.data).getOrder_id());

        } else {

            if (isSessionExpire) {
                return;
            }

            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Request Timeout!")
                    .setContentText("Your payment request is timeout!, please retry again")
                    .setCancelText("Cancel")
                    .setCancelClickListener(sweetAlertDialog1 -> {
                        sweetAlertDialog1.dismiss();
                        onBackPressed();
                    })
                    .setConfirmText("Retry")
                    .setConfirmClickListener(sweetAlertDialog -> {
                        sweetAlertDialog.dismiss();
                        //Retry API
                        showSelectPaymentMethodDialog();
                    })
                    .show();
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
     * Session
     */
    private void startSessions() {
        countDownTimer = new CountDownTimer(TIMEOUT_TIMER, INTERVAL) {

            @Override
            public void onTick(long millisUntilFinished) {
                long minute = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long second = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
                binding.tvTimeout.setText("Timeout in " + minute + ":" + second + " minute");
            }

            @Override
            public void onFinish() {

                if (isCardPaymentRunning || isMobilePaymentRunning || isSharePaymentRunning || isUPIPaymentRunning) {
                    /**
                     * Payment transaction process is running, do not expire session.
                     * After the transaction is completed, the activity will be automatically closed.
                     */
                    return;
                }

                stopNFCPaymentService();
                isSessionExpire = true;
                //Post response
                setResponseAndExit("Request timeout..", false);
            }
        }.start();
    }

    /**
     * Result Launcher
     * UPI Payment result launcher
     */
    ActivityResultLauncher<Intent> upiPaymentResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {

                    SyncMessage syncMessage = null;
                    if (result.getData() != null) {
                        syncMessage = result.getData().getParcelableExtra(PaymentActivity.EXTRA_TXN_RESULT);
                    }
                    //Show
                    showTransactionReceipt(syncMessage);

                } else {

                    SyncMessage syncMessage = new SyncMessage();
                    if (result.getData() != null) {
                        OrderResponse orderResponse = result.getData().getParcelableExtra(ORDER_MESSAGE);
                        syncMessage.orderId = orderResponse.getOrder_id();
                        syncMessage.orderResponse = orderResponse;
                        syncMessage.transactionId = null;
                    }
                    syncMessage.message = "Transaction is canceled!";
                    syncMessage.status = false;

                    //Show
                    showTransactionReceipt(syncMessage);
                }

                isUPIPaymentRunning = false;
            });

    /**
     * Transaction Result Launcher
     */
    ActivityResultLauncher<Intent> successResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        SyncMessage syncMessage = result.getData().getParcelableExtra(TRANSACTION_MESSAGE);
                        //Intent
                        postResultBack(syncMessage);
                    }
                }
            }
    );

    /**
     * Transaction Receipt Screen
     *
     * @param syncMessage transaction data
     */
    private void showTransactionReceipt(SyncMessage syncMessage) {
        Intent intent = new Intent(this, PaymentSuccessActivity.class);
        intent.putExtra(NDEF_MESSAGE, paymentData);
        intent.putExtra(TRANSACTION_MESSAGE, syncMessage);
        successResultLauncher.launch(intent);
    }

    /**
     * Post Result With Response Back
     *
     * @param message transaction status message
     * @param status  transaction status
     */
    private void setResponseAndExit(String message, boolean status) {
        SyncMessage syncMessage = new SyncMessage();
        syncMessage.data = null;
        syncMessage.message = message;
        syncMessage.status = status;
        //Intent
        postResultBack(syncMessage);
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
}