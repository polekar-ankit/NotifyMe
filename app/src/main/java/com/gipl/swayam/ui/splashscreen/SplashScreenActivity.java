package com.gipl.swayam.ui.splashscreen;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;

import com.gipl.swayam.BuildConfig;
import com.gipl.swayam.R;
import com.gipl.swayam.databinding.ActivitySplashScreenBinding;
import com.gipl.swayam.ui.base.BaseActivity;
import com.gipl.swayam.ui.login.LoginActivity;
import com.gipl.swayam.ui.main.MainActivity;

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
