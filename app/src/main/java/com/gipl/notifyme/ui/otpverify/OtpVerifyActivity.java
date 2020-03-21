package com.gipl.notifyme.ui.otpverify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.gipl.notifyme.BR;
import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.sendotp.User;
import com.gipl.notifyme.databinding.ActivityVerifyOtpBinding;
import com.gipl.notifyme.exceptions.ErrorMessageFactory;
import com.gipl.notifyme.ui.base.BaseActivity;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.ui.notification.NotificationListActivity;
import com.gipl.notifyme.uility.AppUtility;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

public class OtpVerifyActivity extends BaseActivity<ActivityVerifyOtpBinding, OtpVerifyViewModel> {
    @Inject
    OtpVerifyViewModel otpVerifyViewModel;
    private Snackbar mySnackbar;

    public static void start(Context context, User user) {
        Intent intent = new Intent(context, OtpVerifyActivity.class);
        intent.putExtra(AppUtility.INTENT_EXTRA.KEY_USER, user);
        context.startActivity(intent);
        ((BaseActivity) context).finish();
    }

    @Override
    public int getBindingVariable() {
        return BR.verifyOtp;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_verify_otp;
    }

    @Override
    public OtpVerifyViewModel getViewModel() {
        return otpVerifyViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        otpVerifyViewModel.setData(getIntent());
        otpVerifyViewModel.getResponseMutableLiveData().observe(this, this::processResponse);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mySnackbar != null)
            mySnackbar.dismiss();
    }

    private void processResponse(Response response) {
        switch (response.status) {
            case LOADING:
                showLoading();
                break;
            case SUCCESS:
                hideLoading();
                // TODO: 21-03-2020 Save logged in user details (Currently only Emp Code is required)
                // User is verified successfully. Go to next screen
                NotificationListActivity.start(this);
                break;
            case ERROR:
                hideLoading();
                if (response.error != null) {
                    mySnackbar = Snackbar.make(getViewDataBinding().getRoot(),
                            ErrorMessageFactory.create(this, (Exception) response.error),
                            Snackbar.LENGTH_INDEFINITE);
                    mySnackbar.setAction(getString(R.string.btn_ok), v -> mySnackbar.dismiss());
                    mySnackbar.show();
                }
                break;
        }
    }
}
