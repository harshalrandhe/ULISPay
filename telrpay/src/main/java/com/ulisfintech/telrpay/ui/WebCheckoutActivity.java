package com.ulisfintech.telrpay.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebMessagePort;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ulisfintech.telrpay.R;
import com.ulisfintech.telrpay.databinding.ActivityWebCheckoutBinding;
import com.ulisfintech.telrpay.helper.AppConstants;
import com.ulisfintech.telrpay.helper.OrderResponse;
import com.ulisfintech.telrpay.helper.SyncMessage;

import androidx.webkit.JavaScriptReplyProxy;
import androidx.webkit.WebMessageCompat;
import androidx.webkit.WebViewCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class WebCheckoutActivity extends AppCompatActivity implements AdvancedWebView.Listener {

    private ActivityWebCheckoutBinding binding;

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
        window.setStatusBarColor(getColor(R.color.material_deep_teal_50));

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null) {

            OrderResponse orderResponse = intent.getParcelableExtra(PaymentActivity.ORDER_RESPONSE);

            binding.webView.setWebViewClient(new CheckoutWebClient());
            binding.webView.setListener(this, this);
            binding.webView.setMixedContentAllowed(false);
            binding.webView.loadUrl(orderResponse.getPayment_link());
//            binding.webView.loadUrl("https://ulis.live:8080");

            HashSet allowedOriginRules = new HashSet(List.of("https://ulis.live:8080"));
            // Add WebMessageListeners.
            WebViewCompat.addWebMessageListener(binding.webView, "replyObject", allowedOriginRules,
                    new ReplyMessageListener());

        }
    }

    private static class ReplyMessageListener implements WebViewCompat.WebMessageListener {

        @Override
        public void onPostMessage(@NonNull WebView view, WebMessageCompat message, @NonNull Uri sourceOrigin,
                                  boolean isMainFrame, @NonNull JavaScriptReplyProxy replyProxy) {

            Log.e("onPostMessage....", message.getData());

            if (message.getData().equals("initialization")) {

            }
        }
    }
    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
        binding.webView.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    public void onPause() {
        binding.webView.onPause();
        // ...
        super.onPause();
    }

    @Override
    public void onDestroy() {
        binding.webView.onDestroy();
        // ...
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
        if(url.equalsIgnoreCase("https://ulis.live:8080/result")){
            //PostBack
            setResponseAndExit("Transaction complete", true);
        }
    }

    @Override
    public void onPageFinished(String url) {
        Log.e("onPageFinished...", url);
        binding.progressBar.setVisibility(View.GONE);
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

    @Override
    public void onExternalPageRequest(String url) {
        Log.e("onExternalPageRequest...", url);
    }

    private static class CheckoutWebClient extends WebViewClient {

        private boolean flag;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {


//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    URL aURL = null;
//                    InputStream inputStream = null;
//                    try {
//
//                        aURL = new URL(url);
//                        URLConnection conn = aURL.openConnection();
//                        conn.connect();
//                        inputStream = conn.getInputStream();
//
//                        // read inputstream to get the json..
//                        ByteArrayOutputStream result = new ByteArrayOutputStream();
//                        byte[] buffer = new byte[1024];
//                        for (int length; (length = inputStream.read(buffer)) != -1; ) {
//                            result.write(buffer, 0, length);
//                        }
//
//                        // StandardCharsets.UTF_8.name() > JDK 7
//                        String response =  result.toString("UTF-8");
//                        Log.e("<<Url>>", url);
//                        Log.e("<<Response>>", response);
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();

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
        Intent intent = getIntent();
        intent.putExtra(AppConstants.EXTRA_TXN_RESULT, response);
        setResult(RESULT_OK, intent);
        finish();
    }

    static class MessageEvent {
        @JavascriptInterface
        public void shareData(String data) {
            Log.e(">>>>>>>", data);
        }
    }
}
