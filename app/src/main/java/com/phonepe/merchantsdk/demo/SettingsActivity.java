package com.phonepe.merchantsdk.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.phonepe.merchantsdk.demo.utils.CacheUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.id_user_id)
    EditText mUserIdText;

    @BindView(R.id.id_amount)
    EditText mAmountText;

    @BindView(R.id.id_name)
    EditText mNameText;

    @BindView(R.id.id_mobile)
    EditText mMobileText;

    @BindView(R.id.id_email)
    EditText mEmailText;

    @OnClick(R.id.id_save)
    void saveUserId() {
        String userId = mUserIdText.getText().toString();
        if (!userId.trim().equals("")) {
            CacheUtils.getInstance(this).saveUserId(userId);
        }

        CacheUtils.getInstance(this).saveMobile(mMobileText.getText().toString());
        CacheUtils.getInstance(this).saveEmail(mEmailText.getText().toString());
        CacheUtils.getInstance(this).saveName(mNameText.getText().toString());


        String amountString = mAmountText.getText().toString();
        if (!amountString.trim().equals("")) {
            Long amount = 2L;
            try {
                amount = Long.parseLong(amountString);
            } catch (NumberFormatException e) {
            }

            CacheUtils.getInstance(this).saveAmountForTransaction(amount);
        }

        setResult(Activity.RESULT_OK, new Intent());
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setDefaults();
    }

    private void setDefaults() {
        mUserIdText.setText(CacheUtils.getInstance(this).getUserId());
        mNameText.setText(CacheUtils.getInstance(this).getName());
        mMobileText.setText(CacheUtils.getInstance(this).getMobile());
        mEmailText.setText(CacheUtils.getInstance(this).getEmail());
        mAmountText.setText(Long.toString(CacheUtils.getInstance(this).getAmountForTransaction()));
    }
}
