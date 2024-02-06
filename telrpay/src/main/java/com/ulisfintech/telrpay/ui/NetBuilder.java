package com.ulisfintech.telrpay.ui;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

class NetBuilder {

    static final int CONNECTION_TIMEOUT = 15000;
    static final int READ_TIMEOUT = 60000;
    private final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    void createService() {

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
    private boolean handleCallbackMessage(GatewayCallback callback, Object arg) {
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
    private GatewayMap executeGatewayRequest(GatewayRequest request) throws Exception {
        // init connection
        HttpsURLConnection httpsUrlConnection = createHttpsUrlConnection(request);

        // encode request data to json
        String requestData = gson.toJson(request.payload);

        // log request data
//       logger.logRequest(c, requestData);
//        Log.e("<<Request>>", requestData);

        // write request data
        if (requestData != null) {
            OutputStream os = httpsUrlConnection.getOutputStream();
            os.write(requestData.getBytes(StandardCharsets.UTF_8));
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
    private HttpsURLConnection createHttpsUrlConnection(GatewayRequest request) throws Exception {
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
//                Log.e("header...", key + " / " + request.extraHeaders.get(key));
                httpsURLConnection.setRequestProperty(key, request.extraHeaders.get(key));
            }
        }
        return httpsURLConnection;
    }

    /**
     * Convert InputStream InTo The String
     *
     * @param is input stream
     * @return string
     * @throws IOException
     */
    private String inputStreamToString(InputStream is) throws IOException {
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
