package com.ulisfintech.telrpay.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.ulisfintech.telrpay.R;
import com.ulisfintech.telrpay.SweetAlert.SweetAlertDialog;
import com.ulisfintech.telrpay.databinding.ActivityPaymentBinding;
import com.ulisfintech.telrpay.helper.OrderResponse;
import com.ulisfintech.telrpay.helper.PaymentData;
import com.ulisfintech.telrpay.helper.SyncMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class PaymentActivity extends AppCompatActivity {

    public static final String PAYMENT_REQUEST = "com.ulisfintech.telrpay.android.request";
    /**
     * The ACS Result data after performing 3DS
     */
    public static final String EXTRA_TXN_RESULT = "com.ulisfintech.telrpay.android.TXN_RESULT";
    static final String ORDER_RESPONSE = "com.ulisfintech.telrpay.android.orderResponse";
    static final String ORDER_MESSAGE = "com.ulisfintech.telrpay.android.orderMessage";
    static final String TRANSACTION_MESSAGE = "com.ulisfintech.telrpay.android.transactionMessage";
    private static final int TIMEOUT_TIMER = 300000;
    private static final int INTERVAL = 1000;
    private final String PAYMENT_TYPE_CARD_PAY = "card_pay";
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
    private ActivityPaymentBinding binding;
    private PaymentViewModel paymentViewModel;
    private PaymentData paymentData;
    private OrderResponse orderResponse;
    private SweetAlertDialog progressDialog;
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

        /*
         * Here set status bar background color same as screen header
         */
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.teal_700));

        sdkUtils = new SdkUtils();

        /*
         *  UPI expandable layout listener
         */
        binding.upiExpandableLayout.setOnExpansionUpdateListener((expansionFraction, state) -> {
            if (state < 1) {
                binding.ivUpiIndicator.setRotation(0);
            } else {
                binding.ivUpiIndicator.setRotation(180);
            }
        });

        /*
         *  Cards expandable layout listener
         */
        binding.cardsExpandableLayout.setOnExpansionUpdateListener((expansionFraction, state) -> {
            if (state < 1) {
                binding.ivCardsIndicator.setRotation(0);
            } else {
                binding.ivCardsIndicator.setRotation(180);
            }
        });

        /*
         *  Net banking expandable layout listener
         */
        binding.netBankingExpandableLayout.setOnExpansionUpdateListener((expansionFraction, state) -> {
            if (state < 1) {
                binding.ivNetBankingIndicator.setRotation(0);
            } else {
                binding.ivNetBankingIndicator.setRotation(180);
            }
        });

        /*
         *  On Click
         *  UPI expandable layout listener
         */
        binding.layoutUpiTitle.setOnClickListener(v -> {
            if (binding.upiExpandableLayout.isExpanded()) {
                binding.upiExpandableLayout.collapse();

            } else {
                binding.upiExpandableLayout.expand();

                binding.cardsExpandableLayout.collapse();
                binding.netBankingExpandableLayout.collapse();
            }
        });

        /*
         *  On Click
         *  Cards expandable layout listener
         */
        binding.layoutCardTitle.setOnClickListener(v -> {
            if (binding.cardsExpandableLayout.isExpanded()) {
                binding.cardsExpandableLayout.collapse();

            } else {
                binding.cardsExpandableLayout.expand();

                binding.upiExpandableLayout.collapse();
                binding.netBankingExpandableLayout.collapse();
            }
        });

        /*
         *  On Click
         *  Net banking expandable layout listener
         */
        binding.layoutNetBankingTitle.setOnClickListener(v -> {
            if (binding.netBankingExpandableLayout.isExpanded()) {
                binding.netBankingExpandableLayout.collapse();
            } else {
                binding.netBankingExpandableLayout.expand();

                binding.upiExpandableLayout.collapse();
                binding.cardsExpandableLayout.collapse();
            }
        });

        /*
         * Button
         * Proceed Transaction
         */
        binding.btnProceed.setOnClickListener(view -> {

            Intent intent = new Intent(this, ProcessingPaymentActivity.class);
            intent.putExtra(PaymentActivity.PAYMENT_REQUEST, paymentData);
            startActivity(intent);

//            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
//                    .setTitleText("Cancel Transaction")
//                    .setContentText("Are you sure?")
//                    .setCancelText("No")
//                    .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
//                    .setConfirmText("Yes")
//                    .setConfirmClickListener(sweetAlertDialog ->
//                            setResponseAndExit("Transaction cancel!", false))
//                    .show();
        });

        paymentViewModel = getDefaultViewModelProviderFactory().create(PaymentViewModel.class);


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


        progressDialog = sdkUtils.createProgressDialog(this);


        if (binding.cardsExpandableLayout.isExpanded()) {
            binding.cardsExpandableLayout.collapse();
        } else {
            binding.cardsExpandableLayout.expand();

            binding.upiExpandableLayout.collapse();
            binding.netBankingExpandableLayout.collapse();
        }

        onNewIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();

        intentFromCreate = false;

        if (isSharePaymentRunning) {
            isSharePaymentRunning = false;
            //
            checkOrderStatus(this.orderResponse.getData().getOrder_id());
        }

        createCardView();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        stopSessionTimer();
    }

    private void createCardView() {

        LinearLayout parent = new LinearLayout(this);

        parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        parent.setOrientation(LinearLayout.VERTICAL);

        int i;
        for (i = 0; i < 3; i++) {

//            View view = LayoutInflater.from(this).inflate(R.layout.card_view, null);
            LinearLayout childLayout = new LinearLayout(this);
            childLayout.setOrientation(LinearLayout.HORIZONTAL);
            childLayout.setPadding(0, 15, 0, 15);

            /*
             *  Add Radio Button
             */
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId((int) (System.currentTimeMillis() + i));
            childLayout.addView(radioButton);

            /*
             *  Add Linear Layout With 2 Text View
             *  1. Card Number 2. Expiry Date
             */
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            LinearLayout childLayout2 = new LinearLayout(this);
            childLayout2.setOrientation(LinearLayout.VERTICAL);
            childLayout2.setLayoutParams(params);

            /*
             *  1. Card Number
             */
            TextView textView = new TextView(this);
            textView.setId((int) (System.currentTimeMillis() + i));
            textView.setText("XXXX XXXX XXXX 1234");
            textView.setTextSize(12);
            childLayout2.addView(textView);

            /*
             *  2. Expiry Date
             */
            TextView textView2 = new TextView(this);
            textView2.setId((int) (System.currentTimeMillis() + i));
            textView2.setText("Expiry " + "06/25");
            textView2.setTextSize(12);
            childLayout2.addView(textView2);

            childLayout.addView(childLayout2);

            /*
             *  Add CVV Edit Text
             */
            EditText editText = new EditText(this);
            editText.setId((int) (System.currentTimeMillis() + i));
            editText.setTag(radioButton);
            editText.setTextSize(12);
            editText.setHint("cvv");
            editText.setEms(4);
            editText.setGravity(Gravity.CENTER);
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(3);
            editText.setFilters(FilterArray);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.addTextChangedListener(new CardCVVTextWatcher());
            editText.setOnFocusChangeListener((view, hasFocus) -> {
                if (hasFocus) {
                    ((RadioButton) view.getTag()).setChecked(true);
                } else {
                    ((RadioButton) view.getTag()).setChecked(false);
                }
            });
            childLayout.addView(editText);

            parent.addView(childLayout);
        }

        binding.cardsExpandableLayout.addView(parent);
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

            String mobile = paymentData.getProductDetails().getVendorMobile();
            String strMobile = "XXXXXXXX" + mobile.substring(mobile.length() - 2);
            String orderName = paymentData.getProductDetails().getProductName();
            String orderPrice = paymentData.getProductDetails().getCurrency() + " " +
                    paymentData.getProductDetails().getProductPrice();
            String imageUrl = paymentData.getProductDetails().getImage();

            binding.tvOrderName.setText(orderName);
            binding.tvOrderId.setText("");
            binding.tvOrderAmount.setText(orderPrice);

            new Thread(() -> downloadImageFromPath(imageUrl)).start();

            this.paymentData = paymentData;
        };
    }

    /**
     * Download Image
     *
     * @param path Image path
     */
    public void downloadImageFromPath(String path) {
        InputStream in;
        Bitmap bmp;
        ImageView iv = binding.ivProduct;
        int responseCode;
        try {

            URL url = new URL(path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.connect();
            responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //download
                in = con.getInputStream();
                bmp = BitmapFactory.decodeStream(in);
                in.close();
                iv.setImageBitmap(bmp);
            }

        } catch (Exception ex) {
            Log.e("Exception", ex.toString());
        }
    }

    /**
     * Observer Method
     * Get called after successful order is created.
     */
    private Observer<? super OrderResponse> orderIsCreatedObserver() {
        return orderResponse -> {

            sdkUtils.setHapticEffect(binding.getRoot());


            this.orderResponse = orderResponse;

            isOrderCreated = true;


            startSessions();

        };
    }

    /**
     * Observer Method
     * Check order status
     */
    private Observer<? super OrderStatusBean> checkOrderStatusObserver() {
        return orderStatusBean -> {

            TransactionResponseBean responseBean = orderStatusBean.getTransactions().get(0);

            SyncMessage syncMessage = new SyncMessage();
            syncMessage.orderId = orderStatusBean.getOrder_id();
            syncMessage.transactionId = responseBean.getTransaction_id();
            syncMessage.orderStatusBean = orderStatusBean;

            if (responseBean.getGateway_code().equalsIgnoreCase(APIConstant.ORDER_STATUS_AUTHORISED)) {
                syncMessage.message = "Transaction is successful!";
                syncMessage.status = true;
            } else if (responseBean.getGateway_code().equalsIgnoreCase(APIConstant.ORDER_STATUS_FAILED)) {
                syncMessage.message = "Transaction failed!";
                syncMessage.status = false;
            } else if (responseBean.getGateway_code().equalsIgnoreCase(APIConstant.ORDER_STATUS_CANCELLED)) {
                syncMessage.message = "Transaction cancelled!";
                syncMessage.status = false;
            } else if (responseBean.getGateway_code().equalsIgnoreCase(APIConstant.ORDER_STATUS_DECLINED)) {
                syncMessage.message = "Transaction declined!";
                syncMessage.status = false;
            } else {
                syncMessage.message = "Pending";
                syncMessage.status = false;
            }

            //Show
            showTransactionReceipt(syncMessage);

            isMobilePaymentRunning = false;
        };
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

            //Process intent data
            paymentViewModel.setIntent(this, intent);

        }
    }

    /**
     * Toast
     *
     * @param message toast text message
     */
    private void showSnackBar(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
            headerBean.setXusername(APIConstant.X_USERNAME);
            headerBean.setXpassword(APIConstant.X_PASSWORD);
            headerBean.setMerchant_key(paymentData.getMerchantKey());
            headerBean.setMerchant_secret(paymentData.getMerchantSecret());
            //Call
            paymentViewModel.checkOrderStatusAsync(PaymentActivity.this, headerBean, orderId, orderId);
        }
    }

    /**
     * Session
     */
    private void startSessions() {
        countDownTimer = new CountDownTimer(TIMEOUT_TIMER, INTERVAL) {

            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000) / 60;
                int seconds = (int) ((millisUntilFinished / 1000) % 60);
                String str = "Timeout in " + minutes + ":" + seconds + " minute";
//                binding.tvTimeout.setText(str);
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


                isSessionExpire = true;
                //Post response
                setResponseAndExit("Request timeout..", false);
            }
        }.start();
    }

    /**
     * Transaction Receipt Screen
     *
     * @param syncMessage transaction data
     */
    private void showTransactionReceipt(SyncMessage syncMessage) {
        Intent intent = new Intent(this, PaymentSuccessActivity.class);
        intent.putExtra(PAYMENT_REQUEST, paymentData);
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

    private static class CardCVVTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}