package com.gipl.notifyme.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.library.baseAdapters.BR;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.gipl.notifyme.BuildConfig;
import com.gipl.notifyme.R;
import com.gipl.notifyme.databinding.ActivityMainBinding;
import com.gipl.notifyme.ui.base.BaseActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements HasAndroidInjector {
    @Inject
    MainViewModel mainViewModel;
    @Inject
    DispatchingAndroidInjector<Object> fragmentDispatchingAndroidInjector;

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        ((BaseActivity) context).finish();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainViewModel getViewModel() {
        return mainViewModel;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar
        int id = item.getItemId();
        if (id == R.id.menu_item_share) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",
                    getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavController navController = Navigation.findNavController(this, R.id.fcv);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_user, R.id.nav_notification)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(getViewDataBinding().bottomNavigationView, navController);
        ActionBar actionBar = getSupportActionBar();
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (getViewDataBinding().bottomNavigationView.getVisibility() == View.GONE) {
                getViewDataBinding().bottomNavigationView.setVisibility(View.VISIBLE);
            }
            if (destination.getId() == R.id.punchingSlipFragment||destination.getId() == R.id.missPunchListFragment) {
                getViewDataBinding().bottomNavigationView.setVisibility(View.GONE);
                actionBar.setTitle(getString(R.string.activity_punching_slip));

            }
            else if (destination.getId()==R.id.leaveListFragment2||destination.getId()==R.id.addModifyLeaveFragment2){
                getViewDataBinding().bottomNavigationView.setVisibility(View.GONE);
                actionBar.setTitle(R.string.activity_leave_list);
            }else if (destination.getId()==R.id.overtimeListFragment||destination.getId()==R.id.addOverTimeFragment){
                getViewDataBinding().bottomNavigationView.setVisibility(View.GONE);
                actionBar.setTitle(R.string.activity_overtime);
            }
            else {
                if (actionBar != null) {
                    actionBar.setTitle(getString(R.string.activity_notification) + " - " + BuildConfig.VERSION_CODE + ".0 - Beta");
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public AndroidInjector<Object> androidInjector() {
        return fragmentDispatchingAndroidInjector;
    }
}
