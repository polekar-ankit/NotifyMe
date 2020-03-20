package com.gipl.notifyme.ui.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.gipl.notifyme.uility.DialogUtility;
import com.gipl.notifyme.uility.NetworkUtils;

import dagger.android.AndroidInjection;


public abstract class BaseActivity<T extends ViewDataBinding, V extends BaseViewModel>
        extends AppCompatActivity {

    // TODO
    // this can probably depend on isLoading variable of BaseViewModel,
    // since its going to be common for all the activities
    private ProgressDialog mProgressDialog;
    private T mViewDataBinding;
    private V mViewModel;

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

//    public abstract String getScreenName();

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    public abstract V getViewModel();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        FirebaseAnalytics.getInstance(this).setCurrentScreen(this, this.getScreenName(), this.getScreenName());


        performDependencyInjection();
        super.onCreate(savedInstanceState);

        performDataBinding();


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


}

