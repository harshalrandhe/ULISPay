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

    static final int CONNECTION_TIMEOUT = 15000;
    static final int READ_TIMEOUT = 60000;
    private final String BASE_ORDER_URL = "https://pach.dev.pay.ulis.co.uk/order/";

    static final int REQUEST_SECURE = 10000;
    private final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();


    public Gateway() {
    }

    /**
     * @param activity    calling activity
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
                SyncMessage syncMessage = data.getParcelableExtra(PaymentActivity.EXTRA_TXN_RESULT);
                callback.onTransactionComplete(syncMessage);
            } else {
                SyncMessage syncMessage = new SyncMessage();
                syncMessage.message = "Transaction cancel!";
                syncMessage.status = false;
                callback.onTransactionCancel(syncMessage);
            }

            return true;
        }

        return false;
    }

    /**
     * Check Order Status
     *
     * @param orderId    order id
     * @param headerBean API headers
     * @return API request
     */
    GatewayRequest buildOrderStatusRequest(String orderId, HeaderBean headerBean) {
        GatewayRequest request = new GatewayRequest();
        request.URL = BASE_ORDER_URL;
        request.method = GatewayRequest.POST;
        request.payload = new OrderStatusPayload(new OrderIdBean(orderId));
        request.extraHeaders = getHeaders(headerBean);
        return request;
    }

    /**
     * Request
     * Create Order
     *
     * @param orderBean initial product data for order
     * @return place order or create order request
     */
    GatewayRequest buildCreateOrderRequest(OrderBean orderBean) {
        GatewayRequest request = new GatewayRequest();
        request.URL = BASE_ORDER_URL;
        request.method = GatewayRequest.POST;
        request.payload = new OrderPayload(orderBean);
        request.extraHeaders = getHeaders(orderBean.getHeaders());
        return request;
    }

    /**
     * Request
     * Buy order
     *
     * @param bean payment data
     * @return payment request
     */
    GatewayRequest buildPaymentRequest(PaymentRequestBean bean) {
        GatewayRequest request = new GatewayRequest();
        request.URL = BASE_ORDER_URL;
        request.method = GatewayRequest.POST;
        request.payload = new PaymentPayload(bean);
        request.extraHeaders = getHeaders(bean.getHeaders());
        return request;
    }

    /**
     * API Headers
     *
     * @param headerBean headers
     * @return headers
     */
    private Map<String, String> getHeaders(HeaderBean headerBean) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("X-Key", headerBean.getX_KEY());
        headers.put("X-Password", headerBean.getX_PASSWORD());
        return headers;
    }

    /**
     * API Call async
     *
     * @param request  API Request
     * @param callback API Callback
     */
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

    /**
     * Handler callback method when executing a request on a new thread
     *
     * @param callback API response callback
     * @param arg      response data
     * @return handler status
     */
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

    /**
     * Execute API Request On Background
     *
     * @param request API Request
     * @return API Response
     * @throws Exception Any exception occur during
     */
    GatewayMap executeGatewayRequest(GatewayRequest request) throws Exception {
        // init connection
        HttpsURLConnection httpsUrlConnection = createHttpsUrlConnection(request);

        // encode request data to json
        String requestData = gson.toJson(request.payload);

        // log request data
//       logger.logRequest(c, requestData);
        Log.e("<<Request>>", requestData);

        // write request data
        if (requestData != null) {
            OutputStream os = httpsUrlConnection.getOutputStream();
            os.write(requestData.getBytes("UTF-8"));
            os.close();
        }

        // initiate the connection
        httpsUrlConnection.connect();

        String responseData = null;
        int statusCode = httpsUrlConnection.getResponseCode();
        boolean isStatusOk = (statusCode >= 200 && statusCode < 300);

        // if connection has output stream, get the data
        // socket time-out exceptions will be thrown here
        if (httpsUrlConnection.getDoInput()) {
            InputStream is = isStatusOk ? httpsUrlConnection.getInputStream() : httpsUrlConnection.getErrorStream();
            responseData = inputStreamToString(is);
            is.close();
        }

        httpsUrlConnection.disconnect();

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

    /**
     * HTTP Connection
     *
     * @param request API Request
     * @return @HttpsURLConnection
     * @throws Exception
     */
    HttpsURLConnection createHttpsUrlConnection(GatewayRequest request) throws Exception {
        // parse url
        URL url = new URL(request.URL);

        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
        httpsURLConnection.setSSLSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault());
        httpsURLConnection.setConnectTimeout(CONNECTION_TIMEOUT);
        httpsURLConnection.setReadTimeout(READ_TIMEOUT);
        httpsURLConnection.setRequestMethod(request.method);
        httpsURLConnection.setDoOutput(true);

        httpsURLConnection.setRequestProperty("Content-Type", "application/json");
        httpsURLConnection.setRequestProperty("X-Environment", "android");

        // add extra headers
        if (request.extraHeaders != null) {
            for (String key : request.extraHeaders.keySet()) {
                httpsURLConnection.setRequestProperty(key, request.extraHeaders.get(key));
            }
        }
        return httpsURLConnection;
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
}
