package com.gipl.notifyme.ui.splashscreen;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;

import com.gipl.notifyme.BuildConfig;
import com.gipl.notifyme.R;
import com.gipl.notifyme.databinding.ActivitySplashScreenBinding;
import com.gipl.notifyme.ui.base.BaseActivity;
import com.gipl.notifyme.ui.login.LoginActivity;
import com.gipl.notifyme.ui.main.MainActivity;

import javax.inject.Inject;

public class SplashScreenActivity extends BaseActivity<ActivitySplashScreenBinding, SplashScreenViewModel> {
    @Inject
    SplashScreenViewModel splashScreenViewModel;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash_screen;
    }

    @Override
    public String getScreenName() {
        return SplashScreenActivity.class.getSimpleName();
    }

    @Override
    public SplashScreenViewModel getViewModel() {
        return splashScreenViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getViewDataBinding().tvVersion.setText(BuildConfig.VERSION_NAME);
        new Handler().postDelayed(() -> {
            // If already login then go to notification list
            if (splashScreenViewModel.getDataManager().isLogin()){
                MainActivity.start(SplashScreenActivity.this);
            }
            else{
                LoginActivity.start(SplashScreenActivity.this);
            }
        },3000);
    }
}
