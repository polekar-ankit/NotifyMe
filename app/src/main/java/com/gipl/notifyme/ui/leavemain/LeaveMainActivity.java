package com.gipl.notifyme.ui.leavemain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;

import com.gipl.notifyme.R;
import com.gipl.notifyme.databinding.ActivityLeaveMainBinding;
import com.gipl.notifyme.ui.base.BaseActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

public class LeaveMainActivity extends BaseActivity<ActivityLeaveMainBinding, LeaveMainViewModel> implements HasAndroidInjector {
    @Inject
    LeaveMainViewModel mainViewModel;
    @Inject
    DispatchingAndroidInjector<Object> fragmentDispatchingAndroidInjector;

    public static void start(Context context) {
        Intent intent = new Intent(context, LeaveMainActivity.class);
        context.startActivity(intent);
//        ((BaseActivity) context).finish();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_leave_main;
    }

    @Override
    public LeaveMainViewModel getViewModel() {
        return mainViewModel;
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return fragmentDispatchingAndroidInjector;
    }
}
