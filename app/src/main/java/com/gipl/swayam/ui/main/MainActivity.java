package com.gipl.swayam.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.gipl.swayam.R;
import com.gipl.swayam.databinding.ActivityMainBinding;
import com.gipl.swayam.ui.base.BaseActivity;
import com.gipl.swayam.uility.TimeUtility;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

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
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
    public String getScreenName() {
        return MainActivity.class.getSimpleName();
    }

    @Override
    public MainViewModel getViewModel() {
        return mainViewModel;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavController navController = Navigation.findNavController(this, R.id.fcv);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_user, R.id.nav_notification, R.id.nav_change_lang)
                .build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


        NavigationUI.setupWithNavController(getViewDataBinding().bottomNavigationView, navController);
        ActionBar actionBar = getSupportActionBar();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.START_DATE, TimeUtility.getCurrentUtcDateTimeForApi());
        getmFirebaseAnalytics().logEvent(FirebaseAnalytics.Event.LOGIN, null);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (getViewDataBinding().bottomNavigationView.getVisibility() == View.GONE) {
                getViewDataBinding().bottomNavigationView.setVisibility(View.VISIBLE);
            }
            if (destination.getId() == R.id.punchingSlipFragment || destination.getId() == R.id.missPunchListFragment) {
                getViewDataBinding().bottomNavigationView.setVisibility(View.GONE);
                actionBar.setTitle(getString(R.string.activity_punching_slip));
            } else if (destination.getId() == R.id.leaveListFragment2 || destination.getId() == R.id.addModifyLeaveFragment2) {
                getViewDataBinding().bottomNavigationView.setVisibility(View.GONE);
                actionBar.setTitle(R.string.activity_leave_list);
            } else if (destination.getId() == R.id.overtimeListFragment || destination.getId() == R.id.addOverTimeFragment) {
                getViewDataBinding().bottomNavigationView.setVisibility(View.GONE);
                actionBar.setTitle(R.string.activity_overtime);
            } else if (destination.getId() == R.id.shiftChangeFragment || destination.getId() == R.id.shiftChangeListFragment) {
                getViewDataBinding().bottomNavigationView.setVisibility(View.GONE);
                actionBar.setTitle(R.string.activity_shift_change);
            } else if (destination.getId() == R.id.coListFragment || destination.getId() == R.id.addCoFragment) {
                getViewDataBinding().bottomNavigationView.setVisibility(View.GONE);
                actionBar.setTitle(R.string.activity_co);
            } else {
                if (actionBar != null) {
                    actionBar.setTitle(getString(R.string.activity_notification));
                }
            }
        });


    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        ArrayList<Fragment> fragmentArrayList = (ArrayList<Fragment>) getSupportFragmentManager().getFragments().get(0).getChildFragmentManager().getFragments();
//        if (fragmentArrayList.size() > 0)
//            fragmentArrayList.get(0).onActivityResult(requestCode, resultCode, data);
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public AndroidInjector<Object> androidInjector() {
        return fragmentDispatchingAndroidInjector;
    }
}
