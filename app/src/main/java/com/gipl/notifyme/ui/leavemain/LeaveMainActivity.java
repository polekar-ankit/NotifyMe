package com.gipl.notifyme.ui.leavemain;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.gipl.notifyme.R;
import com.gipl.notifyme.databinding.ActivityLeaveMainBinding;
import com.gipl.notifyme.databinding.ActivityMainBinding;
import com.gipl.notifyme.ui.base.BaseActivity;
import com.gipl.notifyme.ui.main.MainViewModel;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class LeaveMainActivity extends BaseActivity<ActivityLeaveMainBinding, LeaveMainViewModel> implements HasSupportFragmentInjector {
    @Inject
    LeaveMainViewModel mainViewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

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
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }
}
