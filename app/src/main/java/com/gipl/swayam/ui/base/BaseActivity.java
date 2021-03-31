package com.gipl.swayam.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.gipl.swayam.BuildConfig;
import com.gipl.swayam.data.local.prefs.AppPreferencesHelper;
import com.gipl.swayam.ui.changelng.ChangeLanguageFragment;
import com.gipl.swayam.uility.DialogUtility;
import com.gipl.swayam.uility.MyContextWrapper;
import com.gipl.swayam.uility.NetworkUtils;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Locale;

import dagger.android.AndroidInjection;


public abstract class BaseActivity<T extends ViewDataBinding, V extends BaseViewModel>
        extends AppCompatActivity {

    // TODO
    // this can probably depend on isLoading variable of BaseViewModel,
    // since its going to be common for all the activities
    private ProgressDialog mProgressDialog;
    private T mViewDataBinding;
    private V mViewModel;
    private FirebaseAnalytics mFirebaseAnalytics;

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    public abstract int getBindingVariable();

    /**
     * @return layout resource id
     */
    public abstract
    @LayoutRes
    int getLayoutId();

    public abstract String getScreenName();

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    public abstract V getViewModel();

    public FirebaseAnalytics getmFirebaseAnalytics() {
        return mFirebaseAnalytics;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        performDependencyInjection();
        super.onCreate(savedInstanceState);
        performDataBinding();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, getScreenName());
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, getScreenName());
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

        mFirebaseAnalytics.setUserId(getViewModel().getDataManager().getEmpCode());

        SharedPreferences mPrefs = getBaseContext().getSharedPreferences(BuildConfig.PREF_NAME, Context.MODE_PRIVATE);
        Context context = MyContextWrapper.wrap(this, mPrefs.getString(AppPreferencesHelper.KEY_LANG_CODE, ChangeLanguageFragment.englishCode));

        getResources().updateConfiguration(context.getResources().getConfiguration(), context.getResources().getDisplayMetrics());

    }


    @Override
    protected void onResume() {
        super.onResume();
//        hideKeyboardOnLaunch();
    }

    public T getViewDataBinding() {
        return mViewDataBinding;
    }

    public boolean hasPermission(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            hideKeyboard(view);
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    public void hideKeyboardOnLaunch() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        hideKeyboard();
    }


    public void hideLoading() {
        if (mProgressDialog != null
                && mProgressDialog.isShowing()
                && !this.isFinishing()) {
            mProgressDialog.cancel();
            mViewModel.setIsLoading(false);
        }
    }

    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }


    public void performDependencyInjection() {
        AndroidInjection.inject(this);
    }

    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    public void showLoading(int... msgId) {
        hideLoading();
        mProgressDialog = DialogUtility.showLoadingDialog(this, msgId);
        mViewModel.setIsLoading(true);
    }


    private void performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        this.mViewModel = mViewModel == null ? getViewModel() : mViewModel;
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        mViewDataBinding.executePendingBindings();


    }


    public boolean isLoading() {
        return mViewModel.isLoading().get();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void setActivityTitle(String sTitle) {
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null)
            actionbar.setTitle(sTitle);
    }

    public void setActionBar(Toolbar toolbar, String sTitle) {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setDisplayShowHomeEnabled(true);
            actionbar.setTitle(sTitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    protected void addFragment(int containerViewId, Fragment fragment, boolean fAddToBackStack, @Nullable String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (tag != null) {
            transaction.add(containerViewId, fragment, tag);
        } else {
            transaction.add(containerViewId, fragment);
        }

        if (fAddToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void replaceFragment(int containerViewId, Fragment fragment, boolean fAddToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerViewId, fragment);

        if (fAddToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(ViewPumpContextWrapper.wrap(updateBaseContextLocale(newBase)));
        SharedPreferences mPrefs = newBase.getSharedPreferences(BuildConfig.PREF_NAME, Context.MODE_PRIVATE);
        super.attachBaseContext(MyContextWrapper.wrap(newBase, mPrefs.getString(AppPreferencesHelper.KEY_LANG_CODE, ChangeLanguageFragment.englishCode)));
    }

    public Context updateBaseContextLocale(Context newBase) {
        SharedPreferences mPrefs = newBase.getSharedPreferences(BuildConfig.PREF_NAME, Context.MODE_PRIVATE);
        Locale locale = new Locale(mPrefs.getString(AppPreferencesHelper.KEY_LANG_CODE, ChangeLanguageFragment.englishCode));
        Locale.setDefault(locale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResourcesLocale(newBase, locale);
        }

        return updateResourcesLocaleLegacy(newBase, locale);
    }

    private Context updateResourcesLocaleLegacy(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();

        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }

    private Context updateResourcesLocale(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

//        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context.createConfigurationContext(configuration);
    }


}

