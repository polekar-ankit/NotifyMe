package com.gipl.swayam.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.gipl.swayam.BR;
import com.gipl.swayam.BuildConfig;
import com.gipl.swayam.R;
import com.gipl.swayam.data.model.api.sendotp.User;
import com.gipl.swayam.databinding.ActivityLoginBinding;
import com.gipl.swayam.exceptions.ErrorMessageFactory;
import com.gipl.swayam.ui.base.BaseActivity;
import com.gipl.swayam.ui.model.Response;
import com.gipl.swayam.ui.otpverify.OtpVerifyActivity;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {
    private static final String TAG = "LoginActivity";
    @Inject
    LoginViewModel loginViewModel;
    private Snackbar mySnackbar;

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        ((BaseActivity) context).finish();
    }

    @Override
    public int getBindingVariable() {
        return BR.loginView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public String getScreenName() {
        return LoginActivity.class.getSimpleName();
    }

    @Override
    public LoginViewModel getViewModel() {
        return loginViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel.getResponseMutableLiveData().observe(this, this::processResponse);


    }

    @Override
    protected void onResume() {
        super.onResume();
//        hideKeyboardOnLaunch();
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
                if (BuildConfig.DEBUG) {
                    Toast.makeText(this, "" + loginViewModel.value, Toast.LENGTH_LONG).show();
                }
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

        //Hide keyboard after clicking submit
        getViewDataBinding().tietEmployeeId.onEditorAction(EditorInfo.IME_ACTION_DONE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getViewDataBinding().tietEmployeeId.getWindowToken(), 0);
        }
    }

}
