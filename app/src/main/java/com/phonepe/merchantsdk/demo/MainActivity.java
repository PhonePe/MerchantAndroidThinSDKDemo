package com.phonepe.merchantsdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.phonepe.android.sdk.api.PhonePe;
import com.phonepe.android.sdk.api.builders.AccountingInfoBuilder;
import com.phonepe.android.sdk.api.builders.DebitRequestBuilder;
import com.phonepe.android.sdk.api.builders.OrderInfoBuilder;
import com.phonepe.android.sdk.api.builders.ProfileRequestBuilder;
import com.phonepe.android.sdk.api.builders.SignUpRequestBuilder;
import com.phonepe.android.sdk.api.builders.UserInfoBuilder;
import com.phonepe.android.sdk.api.models.AccountingInfo;
import com.phonepe.android.sdk.api.models.DebitRequest;
import com.phonepe.android.sdk.api.models.OrderInfo;
import com.phonepe.android.sdk.api.models.SignUpRequest;
import com.phonepe.android.sdk.api.utils.BundleConstants;
import com.phonepe.android.sdk.api.utils.CheckSumUtils;
import com.phonepe.merchantsdk.demo.utils.CacheUtils;
import com.phonepe.merchantsdk.demo.utils.Constants;

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

    @OnClick(R.id.id_debit)
    void showDebitDemo() {
        startDebit();
    }

    private String mMobileNo;
    private String mEmail;
    private String mName;

    @OnClick(R.id.id_account)
    void showAccountDetails() {
        String userId = CacheUtils.getInstance(this).getUserId();

        final String txnId = UUID.randomUUID().toString().substring(0, 15);
        ProfileRequestBuilder profileRequestBuilder = new ProfileRequestBuilder();


        UserInfoBuilder userInfoBuilder = new UserInfoBuilder()
                .setUserId(userId);


        if (!isEmpty(mMobileNo)) {
            userInfoBuilder.setMobileNumber(mMobileNo);
        }

        if (!isEmpty(mEmail)) {
            userInfoBuilder.setEmail(mEmail);
        }

        if (!isEmpty(mName)) {
            userInfoBuilder.setShortName(mName);
        }

        profileRequestBuilder
                .setAPIVersion("1")
                .setChecksum("someChecksum")
                .setTransactionId(txnId)
                .setUserInfo(userInfoBuilder.build())
                .setMerchantInfo(null);

        PhonePe.showAccountDetails(profileRequestBuilder.build());
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
        if ((requestCode == 300) || (requestCode == 500)) {
            Bundle bundle;
            String txnStatus = null;
            if (data != null) {
                bundle = data.getExtras();
                txnStatus = bundle.getString(BundleConstants.KEY_TRANSACTION_STATUS);
            }
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(MainActivity.this, txnStatus, Toast.LENGTH_SHORT).show();

//                trackTxnStatus(txnId, false, merchantId);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
            }


        } else if (requestCode == 400) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                boolean result = bundle.getBoolean(BundleConstants.KEY_IS_PHONEPE_ONBOARDING_SUCCESS);
                Toast.makeText(this, "Result:" + result, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }

        } else if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            setDefaults();
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
        Long amount = CacheUtils.getInstance(this).getAmountForTransaction();
        final String txnId = UUID.randomUUID().toString().substring(0, 35);
        String userId = CacheUtils.getInstance(this).getUserId();
        String checksum = CheckSumUtils.getCheckSumForPayment(Constants.MERCHANT_ID, txnId, amount * 100, Constants.SALT, Constants.SALT_KEY_INDEX);

        OfferInfo oInfo = new OfferInfo("offerId", "Amazing offer");
        DiscountInfo discountInfo = new DiscountInfo("discountId", "Discount info", "Some Info");

        HashMap<String, Object> hMap = new HashMap<>();
        hMap.put("offer", oInfo);
        hMap.put("discount", discountInfo);

        UserInfoBuilder userInfoBuilder = new UserInfoBuilder()
                .setUserId(userId);


        if (!isEmpty(mMobileNo)) {
            userInfoBuilder.setMobileNumber(mMobileNo);
        }

        if (!isEmpty(mEmail)) {
            userInfoBuilder.setEmail(mEmail);
        }

        if (!isEmpty(mName)) {
            userInfoBuilder.setShortName(mName);
        }


        OrderInfo orderInfo = new OrderInfoBuilder()
                .setOrderId("OD139924923")
                .setMessage("Payment towards order No. OD139924923.")
                .build();

        AccountingInfo accountingInfo = new AccountingInfoBuilder().setSubMerchant("xMerchantId").build();

        DebitRequest debitRequest = new DebitRequestBuilder()
                .setTransactionId(txnId)
                .setAmount(amount * 100)
                .setAccountingInfo(accountingInfo)
                .setOrderInfo(orderInfo)
                .setUserInfo(userInfoBuilder.build())
                .setMerchantInfo(hMap)
                .setChecksum(checksum)
                .setAPIVersion("1")
                .build();

        startActivityForResult(PhonePe.getDebitIntent(this, debitRequest), 300);
    }

    private void trackTxnStatus(final String txnId, final boolean wascanceled) {
        startActivity(ResultActivity.getInstance(this, txnId, wascanceled));
        overridePendingTransition(0, 0);
    }

    //*********************************************************************
    // End of the class
    //*********************************************************************
}
