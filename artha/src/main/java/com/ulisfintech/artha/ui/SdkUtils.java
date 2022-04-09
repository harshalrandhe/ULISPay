package com.ulisfintech.artha.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.tech.Ndef;
import android.view.HapticFeedbackConstants;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.ulisfintech.artha.BuildConfig;
import com.ulisfintech.artha.R;
import com.ulisfintech.artha.SweetAlert.SweetAlertDialog;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
