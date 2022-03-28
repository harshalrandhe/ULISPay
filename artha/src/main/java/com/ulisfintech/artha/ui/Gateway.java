package com.ulisfintech.artha.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public class Gateway {

    static final int MIN_API_VERSION = 39;
    static final int CONNECTION_TIMEOUT = 15000;
    static final int READ_TIMEOUT = 60000;
    static final int REQUEST_3D_SECURE = 10000;
    static final int REQUEST_GOOGLE_PAY_LOAD_PAYMENT_DATA = 10001;
    static final String API_OPERATION = "UPDATE_PAYER_DATA";
    static final String USER_AGENT = "Artha-Android-SDK/";//+ BuildConfig.VERSION_NAME;
    private final String BASE_ORDER_URL = "https://pach.dev.pay.ulis.co.uk/order/";
    ;

    static final int REQUEST_SECURE = 10000;
    String merchantId;
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();


    public Gateway() {
    }

    /**
     * @param activity calling activity
     * @param paymentData required data for payment transaction(Product information)
     */
    public static void startReceivingPaymentActivity(Activity activity, PaymentData paymentData) {
        Intent intent = new Intent(activity, PaymentActivity.class);
        intent.putExtra(PaymentActivity.NDEF_MESSAGE, paymentData);
        activity.startActivityForResult(intent, REQUEST_SECURE);
    }

    public static boolean handleSecureResult(int requestCode, int resultCode, Intent data, GatewaySecureCallback callback) {
        if (callback == null) {
            return false;
        }

        if (requestCode == REQUEST_SECURE) {
            if (resultCode == Activity.RESULT_OK) {
                OrderStatusBean txnResult = data.getParcelableExtra(PaymentActivity.EXTRA_TXN_RESULT);
                callback.onTransactionComplete(txnResult);
            } else {
                callback.onTransactionCancel(new BaseResponse(false, "Transaction cancel!"));
            }

            return true;
        }

        return false;
    }

    /**
     * Gets the current Merchant ID
     *
     * @return The current Merchant ID
     */
    public String getMerchantId() {
        return merchantId;
    }

    /**
     * Sets the current Merchant ID
     *
     * @param merchantId A valid Merchant ID
     * @throws IllegalArgumentException If the provided Merchant ID is null
     */
    public void setMerchantId(String merchantId) {
        if (merchantId == null) {
            throw new IllegalArgumentException("Merchant ID may not be null");
        }
        this.merchantId = merchantId;
    }

    GatewayRequest buildOrderStatusRequest(String orderId, HeaderBean headerBean) {
        GatewayRequest request = new GatewayRequest();
        request.URL = BASE_ORDER_URL;
        request.method = GatewayRequest.POST;
        request.payload = new OrderStatusPayload(new OrderIdBean(orderId));
        request.extraHeaders = getHeaders(headerBean);
        return request;
    }

    GatewayRequest buildGatewayRequest(OrderBean orderBean) {
        GatewayRequest request = new GatewayRequest();
        request.URL = BASE_ORDER_URL;
        request.method = GatewayRequest.POST;
        request.payload = new OrderPayload(orderBean);
        request.extraHeaders = getHeaders(orderBean.getHeaders());
        return request;
    }

    GatewayRequest buildPaymentRequest(PaymentRequestBean bean) {
        GatewayRequest request = new GatewayRequest();
        request.URL = BASE_ORDER_URL;
        request.method = GatewayRequest.POST;
        request.payload = new PaymentPayload(bean);
        request.extraHeaders = getHeaders(bean.getHeaders());
        return request;
    }

    private Map<String, String> getHeaders(HeaderBean headerBean) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Key", headerBean.getX_KEY());
        headers.put("X-Password", headerBean.getX_PASSWORD());
        return headers;
    }

    void call(GatewayRequest request, GatewayCallback callback) {
        // create handler on current thread
        Handler handler = new Handler(msg -> handleCallbackMessage(callback, msg.obj));

        new Thread(() -> {
            Message m = handler.obtainMessage();
            try {
                m.obj = executeGatewayRequest(request);
            } catch (Exception e) {
                m.obj = e;
            }

            handler.sendMessage(m);
        }).start();
    }

    // handler callback method when executing a request on a new thread
    @SuppressWarnings("unchecked")
    boolean handleCallbackMessage(GatewayCallback callback, Object arg) {
        if (callback != null) {
            if (arg instanceof Throwable) {
                callback.onError((Throwable) arg);
            } else {
                callback.onSuccess((GatewayMap) arg);
            }
        }
        return true;
    }

    GatewayMap executeGatewayRequest(GatewayRequest request) throws Exception {
        // init connection
        HttpsURLConnection c = createHttpsUrlConnection(request);

        // encode request data to json
        String requestData = gson.toJson(request.payload);

        // log request data
//       logger.logRequest(c, requestData);
        Log.e("<<Request>>", requestData);

        // write request data
        if (requestData != null) {
            OutputStream os = c.getOutputStream();
            os.write(requestData.getBytes("UTF-8"));
            os.close();
        }

        // initiate the connection
        c.connect();

        String responseData = null;
        int statusCode = c.getResponseCode();
        boolean isStatusOk = (statusCode >= 200 && statusCode < 300);

        // if connection has output stream, get the data
        // socket time-out exceptions will be thrown here
        if (c.getDoInput()) {
            InputStream is = isStatusOk ? c.getInputStream() : c.getErrorStream();
            responseData = inputStreamToString(is);
            is.close();
        }

        c.disconnect();

        // log response
//       logger.logResponse(c, responseData);

        // parse the response body
        GatewayMap response = new GatewayMap(responseData);

        // if response static is good, return response
        if (isStatusOk) {
            return response;
        }

        // otherwise, create a gateway exception and throw it
        String message = (String) response.get("error.explanation");
        if (message == null) {
            message = "An error occurred";
        }

        GatewayException exception = new GatewayException(message);
        exception.setStatusCode(statusCode);
        exception.setErrorResponse(response);

        throw exception;
    }

    HttpsURLConnection createHttpsUrlConnection(GatewayRequest request) throws Exception {
        // parse url
        URL url = new URL(request.URL);

        HttpsURLConnection c = (HttpsURLConnection) url.openConnection();
        c.setSSLSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault());
        c.setConnectTimeout(CONNECTION_TIMEOUT);
        c.setReadTimeout(READ_TIMEOUT);
        c.setRequestMethod(request.method);
        c.setDoOutput(true);

        c.setRequestProperty("Content-Type", "application/json");
        c.setRequestProperty("X-Environment", "android");

        // add extra headers
        if (request.extraHeaders != null) {
            for (String key : request.extraHeaders.keySet()) {
                c.setRequestProperty(key, request.extraHeaders.get(key));
            }
        }

        return c;
    }

    String inputStreamToString(InputStream is) throws IOException {
        // get buffered reader from stream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        // read stream into string builder
        StringBuilder total = new StringBuilder();

        String line;
        while ((line = rd.readLine()) != null) {
            total.append(line);
        }

        return total.toString();
    }

    String createAuthHeader(String sessionId) {
        String value = "merchant." + merchantId + ":" + sessionId;
        return "Basic " + Base64.encodeToString(value.getBytes(), Base64.NO_WRAP);
    }
}
