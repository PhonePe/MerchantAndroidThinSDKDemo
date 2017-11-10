package com.phonepe.merchantsdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.phonepe.android.sdk.api.PhonePe;
import com.phonepe.android.sdk.api.PhonePeInitException;
import com.phonepe.android.sdk.api.builders.TransactionRequestBuilder;
import com.phonepe.android.sdk.api.utils.BundleConstants;
import com.phonepe.android.sdk.api.utils.CheckSumUtils;
import com.phonepe.android.sdk.base.model.TransactionRequest;
import com.phonepe.merchantsdk.demo.utils.CacheUtils;
import com.phonepe.merchantsdk.demo.utils.Constants;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.phonepe.merchantsdk.demo.utils.AppUtils.isEmpty;

public class MainActivity extends AppCompatActivity {
    private static final long ITEM_AMOUNT = 420l;

    @BindView(R.id.id_debit_amount)
    TextView mDebitAmountTextView;

    @BindView(R.id.id_result)
    TextView resultTextView;
    private String mMobileNo;
    private String mEmail;
    private String mName;

    @OnClick(R.id.id_debit)
    void showDebitDemo() {
        startDebit();
    }

    @OnClick(R.id.id_account)
    void showAccountDetails() {
        //*********************************************************************
        // Your server does the following work
        //*********************************************************************
        final String txnId = UUID.randomUUID().toString().substring(0, 35);
        String userId = CacheUtils.getInstance(this).getUserId();
        String apiEndPoint = "/v3/profile";

        HashMap<String, Object> data = new HashMap<>();
        data.put("merchantId", Constants.MERCHANT_ID);
        data.put("transactionId", txnId);
        data.put("merchantUserId", userId);

        if (!isEmpty(mMobileNo)) {
            data.put("mobileNumber", mMobileNo);
        }

        if (!isEmpty(mEmail)) {
            data.put("email", mEmail);
        }

        if (!isEmpty(mName)) {
            data.put("shortName", mName);
        }

        String dataString = new Gson().toJson(data);
        String dataString64 = null;

        try {
            dataString64 = Base64.encodeToString(dataString.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String checksumV2 = CheckSumUtils.getCheckSum(dataString64, apiEndPoint, Constants.SALT, Constants.SALT_KEY_INDEX);
        //******************************************************************************
        // Your server give back a json containing dataString64, checksumV2, apiEndPoint
        //**********************************************************************On********


        //************************************************************************************************************
        // Your App takes the json containing dataString64, checksumV2, apiEndPoint and make TransactionRequest object
        //************************************************************************************************************
        TransactionRequest profileRequest = new TransactionRequestBuilder()
                .setData(dataString64)
                .setChecksum(checksumV2)
                .setUrl(apiEndPoint)
                .build();

        try {
            startActivity(PhonePe.getTransactionIntent(this, profileRequest));
        } catch (PhonePeInitException e) {
            e.printStackTrace();
        }
        //************************************************************************************************************
        // Your App startActivity to show Profile
        //************************************************************************************************************
    }

    //*********************************************************************
    // Life cycles
    //*********************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        ButterKnife.bind(this);
        setDefaults();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300) {
            Bundle bundle;
            String txnResult = null;
            if (data != null) {
                bundle = data.getExtras();
                txnResult = bundle.getString(BundleConstants.KEY_TRANSACTION_RESULT);

            }
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(MainActivity.this, txnResult, Toast.LENGTH_SHORT).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //*********************************************************************
    // Menu related
    //*********************************************************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivityForResult(new Intent(MainActivity.this, SettingsActivity.class), 100);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //*********************************************************************
    // Private class
    //*********************************************************************

    private void setDefaults() {
        mDebitAmountTextView.setText("Rs. " + CacheUtils.getInstance(this).getAmountForTransaction());

        mMobileNo = CacheUtils.getInstance(this).getMobile();
        mEmail = CacheUtils.getInstance(this).getEmail();
        mName = CacheUtils.getInstance(this).getName();
    }

    private void startDebit() {
        //*********************************************************************
        // Your server does the following work
        //*********************************************************************
        Long amount = CacheUtils.getInstance(this).getAmountForTransaction();
        final String txnId = UUID.randomUUID().toString().substring(0, 35);
        String userId = CacheUtils.getInstance(this).getUserId();
        String apiEndPoint = "/v3/debit";

        OfferInfo oInfo = new OfferInfo("offerId", "Amazing offer");
        DiscountInfo discountInfo = new DiscountInfo("discountId", "Discount info", "Some Info");

        HashMap<String, Object> data = new HashMap<>();
        data.put("merchantId", Constants.MERCHANT_ID);
        data.put("transactionId", txnId);
        data.put("amount", amount * 100);
        data.put("merchantOrderId", "OD139924923");
        data.put("message", "Payment towards order No. OD139924923.");
        data.put("merchantUserId", userId);
//        data.put("offer", oInfo);
//        data.put("discount", discountInfo);

        Log.d("Transaction id" , txnId);

        if (!isEmpty(mMobileNo)) {
            data.put("mobileNumber", mMobileNo);
        }

        if (!isEmpty(mEmail)) {
            data.put("email", mEmail);
        }

        if (!isEmpty(mName)) {
            data.put("shortName", mName);
        }

        data.put("providerName", "xMerchantId");

        String dataString = new Gson().toJson(data);
        String dataString64 = null;

        try {
            dataString64 = Base64.encodeToString(dataString.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String checksumV2 = CheckSumUtils.getCheckSum(dataString64, apiEndPoint, Constants.SALT, Constants.SALT_KEY_INDEX);
        //******************************************************************************
        // Your server give back a json containing dataString64, checksumV2, apiEndPoint
        //******************************************************************************


        //************************************************************************************************************
        // Your App takes the json containing dataString64, checksumV2, apiEndPoint and make TransactionRequest object
        //************************************************************************************************************
        TransactionRequest transactionRequest2 = new TransactionRequestBuilder()
                .setData(dataString64)
                .setChecksum(checksumV2)
                .setUrl(apiEndPoint)
                .build();

        try {
            startActivityForResult(PhonePe.getTransactionIntent(this, transactionRequest2), 300);
        } catch (PhonePeInitException e) {
            e.printStackTrace();
        }
        //************************************************************************************************************
        // Your App startActivityForResult and get a callback onActivityResult
        //************************************************************************************************************
    }

    private void trackTxnStatus(final String txnId, final boolean wascanceled) {
        startActivity(ResultActivity.getInstance(this, txnId, wascanceled));
        overridePendingTransition(0, 0);
    }

    //*********************************************************************
    // End of the class
    //*********************************************************************
}
