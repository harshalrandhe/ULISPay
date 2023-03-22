package com.ulisfintech.telrpay.ui;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.ulisfintech.telrpay.R;
import com.ulisfintech.telrpay.SweetAlert.SweetAlertDialog;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

public class SdkUtils {

    final String ARTHA_TAP_AND_PAY = "Artha ( Tap & Pay)";
    final String ARTHA_SHARE_PAY = "Artha Share Pay";

    public SdkUtils() {
    }

    /**
     * Turn On NFC
     * NFC setting dialog
     */
    void showTurnOnNfcDialog(Activity context) {
        new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(context.getString(R.string.ad_nfcTurnOn_title))
                .setContentText(context.getString(R.string.ad_nfcTurnOn_message))
                .setContentText(context.getString(R.string.ad_nfcTurnOn_pos))
                .setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismiss();
                    // Send the user to the settings page and hope they turn it on
                    context.startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
                })
                .setCancelText(context.getString(R.string.ad_nfcTurnOn_neg))
                .setCancelClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismiss();
                    context.onBackPressed();
                })
                .show();
    }

    /**
     * Check Application Is Installed
     *
     * @param context     activity context
     * @param packageName application package name
     * @return true if an app is available or false if not
     */
    boolean isPackageInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent == null) {
            return false;
        }
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return !list.isEmpty();
    }

    /**
     * Haptic Effect
     *
     * @param view any view
     */
    void setHapticEffect(View view) {
        view.setHapticFeedbackEnabled(true);
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY,
                HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
    }

    /**
     * @param context app context
     * @return IP address
     */
    String getMyIp(Context context) {
        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }

     String getLocalIpAddress() {
         try {
             for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                  en.hasMoreElements();) {
                 NetworkInterface intf = en.nextElement();
                 for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                     InetAddress inetAddress = enumIpAddr.nextElement();
                     if (!inetAddress.isLoopbackAddress()) {
                         return inetAddress.getHostAddress();
                     }
                 }
             }
         } catch (Exception ex) {
             Log.e("IP Address", ex.toString());
         }
         return "";
    }


    /**
     * Create Progress Dialog
     */
    SweetAlertDialog createProgressDialog(Context context) {
        SweetAlertDialog progressDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText(context.getString(R.string.ad_progressBar_title));
        progressDialog.setContentText(context.getString(R.string.ad_progressBar_mess));
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    void errorAlert(Context context, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("ERROR!")
                .setContentText(message)
                .setConfirmText("Okay")
                .setConfirmClickListener(Dialog::dismiss)
                .show();
    }

    void errorAlert(Activity context, String message, boolean onBackPress) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("ERROR!")
                .setContentText(message)
                .setConfirmText("Okay")
                .setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismiss();
                    if (onBackPress) context.onBackPressed();
                })
                .show();
    }

    String[] getList() {
        return new String[]{ARTHA_TAP_AND_PAY, ARTHA_SHARE_PAY};
    }

    /**
     * Dialog
     * Show Payment Method
     */
    void showSelectPaymentMethodDialog(Context context, PaymentOptionListener listener) {
        new AlertDialog.Builder(context)
                .setTitle("Pay with")
                .setCancelable(false)
                .setItems(getList(), (dialogInterface, which) -> {
                    if (getList()[which].equalsIgnoreCase(ARTHA_TAP_AND_PAY)) {
                        listener.onTapAndPay();
                    } else {
                        listener.onSharePay();
                    }
                })
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }


    interface PaymentOptionListener {
        void onTapAndPay();

        void onSharePay();
    }
}
