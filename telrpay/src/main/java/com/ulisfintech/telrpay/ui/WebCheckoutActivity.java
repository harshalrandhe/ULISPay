package com.ulisfintech.telrpay.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.ulisfintech.telrpay.R;
import com.ulisfintech.telrpay.databinding.ActivityWebCheckoutBinding;
import com.ulisfintech.telrpay.helper.OrderResponse;

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
        window.setStatusBarColor(getColor(R.color.purple_200));

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
//            binding.webView.loadUrl("https://ulis.live:8080/initiate/ORD20240307005/eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJvcmRlcl9pZCI6Ik9SRDIwMjQwMzA3MDA1IiwiYW1vdW50IjoiMzY2LjQ1IiwiY3VycmVuY3kiOiJBRUQiLCJyZXR1cm5fdXJsIjoiaHR0cHM6Ly9kZXYudGxyLmZlLnVsaXMubGl2ZS9tZXJjaGFudC9wYXltZW50L3N0YXR1cyIsImVudiI6ImxpdmUiLCJtZXJjaGFudF9pZCI6MSwiaWF0IjoxNjc5NDY5MzgzLCJleHAiOjE2Nzk1NTU3ODN9.7txCmPNHWTNsGsyELEroT2Z9mRhkq-uZ64jfPnS8xJ4");
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
    public void onPageStarted(String url, Bitmap favicon) {
        Log.e("onPageStarted...", url);
    }

    @Override
    public void onPageFinished(String url) {
        Log.e("onPageFinished...", url);
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        Log.e("onPageError...", errorCode + "");
        Log.e("onPageError...", description);
        Log.e("onPageError...", failingUrl);
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
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
