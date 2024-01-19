package com.ulisfintech.telrpay.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.ulisfintech.telrpay.R;
import com.ulisfintech.telrpay.databinding.ActivityWebCheckoutBinding;
import com.ulisfintech.telrpay.helper.AppConstants;
import com.ulisfintech.telrpay.helper.OrderResponse;
import com.ulisfintech.telrpay.helper.SyncMessage;
import com.ulisfintech.telrpay.ui.order.MerchantUrls;

public class WebCheckoutActivity extends AppCompatActivity implements AdvancedWebView.Listener {

    private ActivityWebCheckoutBinding binding;
    private MerchantUrls merchantUrls;
    private String returnUrl;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*
         * Here set status bar background color same as screen header
         */
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.teal_700));


        binding.btnRedirecting.setOnClickListener(v -> {
            //PostBack
            setResponseAndExit();
        });

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null) {

            OrderResponse orderResponse = intent.getParcelableExtra(PaymentActivity.ORDER_RESPONSE);
            // Set Merchant Urls
            this.merchantUrls = orderResponse.getMerchantUrls();
            this.returnUrl = orderResponse.getReturnUrl();

            binding.webView.setWebViewClient(new CheckoutWebClient());
            binding.webView.setListener(this, this);
            binding.webView.setMixedContentAllowed(false);
            binding.webView.clearCache(true);
            binding.webView.loadUrl(orderResponse.getData().getPayment_link());
//            binding.webView.loadUrl("https://ulis.live:8080");

//            HashSet allowedOriginRules = new HashSet(List.of("https://ulis.live:8080"));
            // Add WebMessageListeners.
//            WebViewCompat.addWebMessageListener(binding.webView, "replyObject", allowedOriginRules,
//                    new ReplyMessageListener());

        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
        binding.webView.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    public void onPause() {
        binding.webView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        binding.webView.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (binding.webView.canGoBack()) {
            binding.webView.goBack();
        } else {
            finish();
        }
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        Log.e("onPageStarted...", url);
        binding.progressBar.setVisibility(View.VISIBLE);
        if(url.equalsIgnoreCase(
                "https://ulis.live:8081/error?m=Transaction%20is%20already%20being%20processed.")){
            setResponseAndExit();
        }
    }

    @Override
    public void onPageFinished(String url) {
        Log.e("onPageFinished...", url);
        binding.progressBar.setVisibility(View.GONE);
        if(url.equalsIgnoreCase(this.returnUrl)){
            binding.layoutRedirecting.setVisibility(View.VISIBLE);
            startTimer();
        }
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        Log.e("onPageError...", errorCode + "");
        Log.e("onPageError...", description);
        Log.e("onPageError...", failingUrl);
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType,
                                    long contentLength, String contentDisposition, String userAgent) {
        Log.e("onDownloadRequested...", url);
    }

    private void startTimer(){
        countDownTimer = new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                binding.tvRedirecting.setText("Screen redirect in " + millisUntilFinished / 1000 + " seconds");
            }

            public void onFinish() {
                setResponseAndExit();
            }
        }.start();
    }

    @Override
    public void onExternalPageRequest(String url) {
        Log.e("onExternalPageRequest...", url);
    }

    private static class CheckoutWebClient extends WebViewClient {

        private boolean flag;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (url.contains("form.html")) {
                flag = true;
            }
        }
    }

    /**
     * Post Result With Response Back
     */
    private void setResponseAndExit() {
        SyncMessage syncMessage = new SyncMessage();
        syncMessage.data = null;
        syncMessage.message = "Transaction complete";
        syncMessage.status = true;
        //Intent
        postResultBack(syncMessage);
    }

    /**
     * Post result back to the merchant activity
     *
     * @param response product order data
     */
    private void postResultBack(SyncMessage response) {
        Intent intent = getIntent();
        intent.putExtra(AppConstants.EXTRA_TXN_RESULT, response);
        setResult(RESULT_OK, intent);
        finish();
        Log.e("Payment.....", "Finished called.....");
    }

    static class MessageEvent {
        @JavascriptInterface
        public void shareData(String data) {
            Log.e(">>>>>>>", data);
        }
    }
}
