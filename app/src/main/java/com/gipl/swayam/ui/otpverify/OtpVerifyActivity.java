package com.gipl.swayam.ui.otpverify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;

import com.gipl.swayam.BR;
import com.gipl.swayam.R;
import com.gipl.swayam.data.model.api.sendotp.User;
import com.gipl.swayam.databinding.ActivityVerifyOtpBinding;
import com.gipl.swayam.exceptions.ErrorMessageFactory;
import com.gipl.swayam.ui.base.BaseActivity;
import com.gipl.swayam.ui.main.MainActivity;
import com.gipl.swayam.ui.model.Response;
import com.gipl.swayam.uility.AppUtility;
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
    public String getScreenName() {
        return OtpVerifyActivity.class.getSimpleName();
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
                // User is verified successfully. Go to next screen
                String data = (String) response.data;
                if (data.equals("OTPResend")) {
                    mySnackbar = Snackbar.make(getViewDataBinding().getRoot(),
                            getString(R.string.msg_otp_resend),
                            Snackbar.LENGTH_INDEFINITE);
                    mySnackbar.setAction(getString(R.string.btn_ok), v -> mySnackbar.dismiss());
                    mySnackbar.show();
                } else
                    MainActivity.start(this);
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

        //Hide keyboard after clicking submit
        getViewDataBinding().tietOtp.onEditorAction(EditorInfo.IME_ACTION_DONE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getViewDataBinding().tietOtp.getWindowToken(), 0);
    }
}