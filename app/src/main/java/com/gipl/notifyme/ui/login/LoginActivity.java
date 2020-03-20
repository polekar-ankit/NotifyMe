package com.gipl.notifyme.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.gipl.notifyme.BR;
import com.gipl.notifyme.R;
import com.gipl.notifyme.databinding.ActivityLoginBinding;
import com.gipl.notifyme.exceptions.CustomException;
import com.gipl.notifyme.exceptions.ErrorMessageFactory;
import com.gipl.notifyme.ui.base.BaseActivity;
import com.gipl.notifyme.ui.model.Response;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;

import javax.inject.Inject;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {
    @Inject
    LoginViewModel loginViewModel;
    private static final String TAG = "LoginActivity";

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

        generateFBTokenAndAuth();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mySnackbar != null)
            mySnackbar.dismiss();
    }

    private Snackbar mySnackbar;

    private void processResponse(Response response) {
        switch (response.status) {
            case LOADING:
                showLoading();
                break;
            case SUCCESS:
                hideLoading();
                break;
            case ERROR:
                hideLoading();
                if (response.error != null) {
                    Snackbar mySnackbar = Snackbar.make(getViewDataBinding().getRoot(),
                            ErrorMessageFactory.create(this, (Exception) response.error),
                            Snackbar.LENGTH_INDEFINITE);
                    mySnackbar.setAction(getString(R.string.btn_ok), v -> mySnackbar.dismiss());
                    mySnackbar.show();
                }
                break;
        }
    }


    // TODO: 20-03-2020 This function should be called before Verify OTP API
    /**
     * generating firebase token
     */
    public void generateFBTokenAndAuth() {
        //getResponseMutableLiveData().setValue(Response.loading());

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        //getResponseMutableLiveData().setValue(Response.error(new CustomException(getDataManager().getContext().getString(R.string.error_firebase_token))));
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();

                    // start authentication
                    //authorize(token);
                    // Log and toast
                    //String msg = getDataManager().getContext().getString(R.string.msg_token_fmt, token);
                    Log.d(TAG, "fb token : " + token);
                    //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                });
    }
}
