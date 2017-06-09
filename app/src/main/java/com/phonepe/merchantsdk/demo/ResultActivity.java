package com.phonepe.merchantsdk.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResultActivity extends AppCompatActivity {

    private String mTxnId;
    private boolean mIsCanceled = false;

    private static final String KEY_TXN_ID = "key_txn_id";
    private static final String KEY_IS_CANCELED = "key_was_canceled";

    @BindView(R.id.id_result)
    TextView mTextView;

    @BindView(R.id.id_progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.id_back_button)
    Button mBackButton;

    @OnClick(R.id.id_back_button)
    void onBackButtonPressed() {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
        readBundle(getIntent().getExtras());
        trackTxnStatus(mTxnId, mIsCanceled);
    }

    //*********************************************************************
    // Private methods
    //*********************************************************************

    private void readBundle(Bundle bundle) {
        if (bundle != null && bundle.containsKey(KEY_TXN_ID)) {
            mTxnId = bundle.getString(KEY_TXN_ID);
        } else {
            throw new RuntimeException("TransactionId cannot be null");
        }

        if (bundle.containsKey(KEY_IS_CANCELED)) {
            mIsCanceled = bundle.getBoolean(KEY_IS_CANCELED);
        }
    }

    private void trackTxnStatus(String txnId, boolean wasCanceled) {
        if (wasCanceled) {
            mProgressBar.setVisibility(View.GONE);
            mBackButton.setVisibility(View.VISIBLE);
            mTextView.setText("Transaction was canceled");
            return;
        }

        mProgressBar.setVisibility(View.VISIBLE);
        mBackButton.setVisibility(View.GONE);
        mTextView.setText("Fetching status ...");

       /* String checksum = CheckSumUtils.getCheckSumForTransactionStatus(Constants.MERCHANT_ID, txnId, Constants.SALT, Constants.SALT_KEY_INDEX);
        PhonePe.fetchTransactionStatus(checksum, txnId, new DataListener<TransactionStatus>() {
            @Override
            public void onSuccess(TransactionStatus transactionStatus) {
                mProgressBar.setVisibility(View.GONE);
                mBackButton.setVisibility(View.VISIBLE);
                if (transactionStatus != null) {
                    mTextView.setText(transactionStatus.getMessage());
                } else {
                    mTextView.setText("Failed to load status of transaction");
                }
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                mProgressBar.setVisibility(View.GONE);
                mBackButton.setVisibility(View.VISIBLE);
                mTextView.setText("Failed to load status of transaction");
            }
        });*/
    }


    //*********************************************************************
    // Utility methods
    //*********************************************************************

    public static Intent getInstance(Context context, @NonNull String txnId, boolean isCanceled) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TXN_ID, txnId);
        bundle.putBoolean(KEY_IS_CANCELED, isCanceled);
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    //*********************************************************************
    // End of the class
    //*********************************************************************
}
