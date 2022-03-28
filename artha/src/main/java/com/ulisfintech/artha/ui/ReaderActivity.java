package com.ulisfintech.artha.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ulisfintech.artha.R;
import com.ulisfintech.artha.cardreader.CardNfcAsyncTask;

public class ReaderActivity extends AppCompatActivity {
    private CardNfcAsyncTask cardNfcAsyncTask;

    private boolean intentFromCreate;
    private boolean isScanNow;
    private ProgressDialog progressDialog;
    private final String PAYMENT_TYPE_CARD_PAY = "card_pay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        if (intent.getAction() != null && intent.getAction().equals("android.nfc.action.TAG_DISCOVERED")) {
//
//                cardNfcAsyncTask = new CardNfcAsyncTask.Builder(new CardNfcAsyncTask.CardNfcInterface() {
//                    @Override
//                    public void startNfcReadCard() {
//                        isScanNow = true;
//                        progressDialog.show();
//                    }
//
//                    @Override
//                    public void cardIsReadyToRead() {
//
//                        String cardNumber = cardNfcAsyncTask.getCardNumber().trim();
//                        String expiredDate = cardNfcAsyncTask.getCardExpireDate();
//                        String cardType = cardNfcAsyncTask.getCardType();
//
//                        PaymentRequestBean paymentRequestBean = new PaymentRequestBean();
//                        //Card Details
//                        paymentRequestBean.setName("");
//                        paymentRequestBean.setCard_number(cardNumber);
//                        paymentRequestBean.setExpiration_date(expiredDate);
//                        paymentRequestBean.setCvv("");
//                        //Payment Description
//                        paymentRequestBean.setDescription("Card Pay");
//                        paymentRequestBean.setType(PAYMENT_TYPE_CARD_PAY);
//                        //Address Details
//                        paymentRequestBean.setAddress1("Address1");
//                        paymentRequestBean.setAddress2("Address2");
//                        paymentRequestBean.setCity("New York");
//                        paymentRequestBean.setState("CF");
//                        paymentRequestBean.setCountry("USA");
//                        paymentRequestBean.setPostalcode("123456");
//
//
//                        if (orderResponse != null) {
//                            paymentRequestBean.setToken(orderResponse.getToken());
//                            paymentRequestBean.setOrder_id(orderResponse.getOrder_id());
//                        }
//
//                        if (paymentData != null) {
//                            //Headers
//                            HeaderBean headerBean = new HeaderBean();
//                            headerBean.setX_KEY(paymentData.getMerchantKey());
//                            headerBean.setX_PASSWORD(paymentData.getMerchantSecret());
//                            paymentRequestBean.setHeaders(headerBean);
//                        }
//
//                        //API Call
//                        paymentViewModel.proceedToPaymentAsync(PaymentActivity.this, paymentRequestBean);
//                    }
//
//                    @Override
//                    public void doNotMoveCardSoFast() {
//                        showSnackBar(getString(R.string.snack_doNotMoveCard));
//                    }
//
//                    @Override
//                    public void unknownEmvCard() {
//                        showSnackBar(getString(R.string.snack_unknownEmv));
//                    }
//
//                    @Override
//                    public void cardWithLockedNfc() {
//                        showSnackBar(getString(R.string.snack_lockedNfcCard));
//                    }
//
//                    @Override
//                    public void finishNfcReadCard() {
//                        progressDialog.dismiss();
//                        cardNfcAsyncTask = null;
//                        isScanNow = false;
//                    }
//                }, intent, intentFromCreate).build();
//        }
    }


    private void showSnackBar(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void createProgressDialog() {
        String title = getString(R.string.ad_progressBar_title);
        String mess = getString(R.string.ad_progressBar_mess);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(title);
        progressDialog.setMessage(mess);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
    }
}