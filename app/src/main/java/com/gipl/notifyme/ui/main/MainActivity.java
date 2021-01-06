package com.gipl.notifyme.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import com.gipl.notifyme.BuildConfig;
import com.gipl.notifyme.R;
import com.gipl.notifyme.databinding.ActivityMainBinding;
import com.gipl.notifyme.ui.base.BaseActivity;
import com.gipl.notifyme.uility.NotificationUtils;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements HasSupportFragmentInjector {
    @Inject
    MainViewModel mainViewModel;
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

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

//        String channelId = getApplicationContext().getString(R.string.notification_channel_id_default);
//        NotificationUtils.sendNotification(this, channelId, "Open app", "this is notification display from app", null, true);

    }

    @Override
    protected void onDestroy() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                String channelId = getApplicationContext().getString(R.string.notification_channel_id_default);
//                NotificationUtils.sendNotification(MainActivity.this, channelId, "Exit app", "this is notification display from app", null, true);
//            }
//        }, 3000);
        super.onDestroy();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }
}
