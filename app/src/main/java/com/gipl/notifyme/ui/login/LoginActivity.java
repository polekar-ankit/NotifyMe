package com.gipl.notifyme.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.gipl.notifyme.BR;
import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.sendotp.User;
import com.gipl.notifyme.databinding.ActivityLoginBinding;
import com.gipl.notifyme.exceptions.CustomException;
import com.gipl.notifyme.exceptions.ErrorMessageFactory;
import com.gipl.notifyme.ui.base.BaseActivity;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.ui.notification.NotificationListActivity;
import com.gipl.notifyme.ui.otpverify.OtpVerifyActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;

import javax.inject.Inject;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {
    private static final String TAG = "LoginActivity";
    @Inject
    LoginViewModel loginViewModel;
    private Snackbar mySnackbar;

    @Override
    public int getBindingVariable() {
        return BR.loginView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public LoginViewModel getViewModel() {
        return loginViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel.getResponseMutableLiveData().observe(this, this::processResponse);

        // If already login then go to notification list
        if (getViewModel().getDataManager().isLogin()){
            NotificationListActivity.start(this);
        }
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
                OtpVerifyActivity.start(this, (User) response.data);
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
