package com.ulisfintech.telrsdkexample.communication.local;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Window;

import com.ulisfintech.telrsdkexample.R;

public class DBAsync<T> extends AsyncTask<String, Integer, T> {

    private OnResult<T> result;
    private Context context;
    private Dialog dialog;
    private boolean progress;

    public DBAsync(Context context, boolean progress) {
        this.context = context;
        this.progress = progress;
        if (progress) {
            dialog = new Dialog(context, R.style.indicator_dialog_title);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.progrss_dialog_indicator);
            dialog.setCanceledOnTouchOutside(false);
        }
    }

    public OnResult<T> getResult() {
        return result;
    }

    public void setResult(OnResult<T> result) {
        this.result = result;
        execute();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (dialog != null) dialog.show();
    }

    @Override
    protected T doInBackground(String... strings) {
        T t = result.executeTask();
        return t;
    }

    @Override
    protected void onPostExecute(T t) {
        super.onPostExecute(t);
        if (dialog != null) dialog.dismiss();
        result.onPostData(t);
    }

    public interface OnResult<T> {
        T executeTask();

        void onPostData(T t);
    }
}
