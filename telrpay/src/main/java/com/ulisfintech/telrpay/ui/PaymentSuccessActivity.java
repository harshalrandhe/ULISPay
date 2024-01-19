package com.ulisfintech.telrpay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ulisfintech.telrpay.R;
import com.ulisfintech.telrpay.databinding.ActivityPaymentSuccessBinding;
import com.ulisfintech.telrpay.helper.PaymentData;
import com.ulisfintech.telrpay.helper.SyncMessage;

public class PaymentSuccessActivity extends AppCompatActivity {

    private ActivityPaymentSuccessBinding binding;
    private SdkUtils sdkUtils;
    private PaymentViewModel paymentViewModel;
    private PaymentData paymentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentSuccessBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        paymentViewModel = new ViewModelProvider(this).get(PaymentViewModel.class);
        sdkUtils = new SdkUtils();

        /**
         * Button
         */
        binding.btnDonePayment.setOnClickListener(view -> onBackPressed());

        /**
         * Observer
         * Intent Data Observer
         */
        paymentViewModel.getPaymentDataMutableLiveData().observe(this, intentDataObserver());

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //Process intent data
        paymentViewModel.setReceiptIntent(this, intent);

        if (intent.hasExtra(PaymentActivity.TRANSACTION_MESSAGE)) {
            SyncMessage syncMessage = getIntent().getParcelableExtra(PaymentActivity.TRANSACTION_MESSAGE);
            if (syncMessage.status) {
                binding.tvStatus.setText(syncMessage.message);
                binding.tvStatus.setTextColor(getColor(R.color.success_stroke_color));
                binding.ivError.setVisibility(View.GONE);
                binding.gifImage.setVisibility(View.VISIBLE);
            } else {
                binding.tvStatus.setText(syncMessage.message);
                binding.tvStatus.setTextColor(getColor(R.color.error_stroke_color));
                binding.ivError.setVisibility(View.VISIBLE);
                binding.gifImage.setVisibility(View.GONE);
            }

            TransactionResponseBean txnBean = syncMessage.orderStatusBean.getTransactions().get(0);
            OrderStatusBean statusBean = syncMessage.orderStatusBean;
            CustomerDetailsResponse customerDetails = syncMessage.orderStatusBean.getCustomer_details();

            binding.tvTxnReference.setText(txnBean.getTransaction_id() == null ? "-" : txnBean.getTransaction_id());
            binding.tvInvoiceNumber.setText(txnBean.getTransaction_id() == null ? "-" : txnBean.getTransaction_id());
            binding.tvTxnType.setText(txnBean.getType() == null ? "-" : txnBean.getType());
            binding.tvTxnAmount.setText(txnBean.getAmount() == null ? "-" : txnBean.getAmount());
            binding.tvTxnStatus.setText(txnBean.getGateway_code() == null ? "-" : txnBean.getGateway_code());
            binding.tvDescription.setText(statusBean.getDescription() == null ? "-" : statusBean.getDescription());
            binding.tvTxnTime.setText(txnBean.getDate_time() == null ? "-" : txnBean.getDate_time());
            binding.tvAuthCode.setText(statusBean.getAuthcode() == null ? "-" : statusBean.getAuthcode());
            binding.tvCard.setText(statusBean.getCard_brand() + "\n" + statusBean.getAccount_identifier());
            binding.tvEmailAddress.setText(customerDetails.getEmail() == null ? "-" : customerDetails.getEmail());
            binding.tvPhoneNumber.setText(customerDetails.getMobile_no() == null ? "-" : customerDetails.getMobile_no());
            binding.tvCountry.setText(customerDetails.getBilling_address().getCountry() == null ? "-" : customerDetails.getBilling_address().getCountry());

        }
    }

    @Override
    public void onBackPressed() {
        //Post result back
        setResult(RESULT_OK, getIntent());
        finish();
    }

    /**
     * Observer
     * Intent Data Observer
     */
    private Observer<? super PaymentData> intentDataObserver() {
        return paymentData -> {

            if (paymentData == null) {
                sdkUtils.errorAlert(this, "Payments details are not available!");
                return;
            }
            this.paymentData = paymentData;
//            String mobile = paymentData.getProductDetails().getVendorMobile();
//            String strMobile = "XXXXXXXX" + mobile.substring(mobile.length() - 2);
        };
    }
}