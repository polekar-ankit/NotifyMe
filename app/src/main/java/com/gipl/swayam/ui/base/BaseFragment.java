package com.gipl.swayam.ui.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.gipl.swayam.BuildConfig;
import com.gipl.swayam.data.local.prefs.AppPreferencesHelper;
import com.gipl.swayam.ui.changelng.ChangeLanguageFragment;
import com.gipl.swayam.uility.MyContextWrapper;
import com.google.firebase.analytics.FirebaseAnalytics;

import dagger.android.support.AndroidSupportInjection;


public abstract class BaseFragment<T extends ViewDataBinding, V extends BaseViewModel> extends Fragment {

    private BaseActivity mActivity;
    private View mRootView;
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

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    public abstract V getViewModel();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            this.mActivity = (BaseActivity) context;

        }

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        performDependencyInjection();
        super.onCreate(savedInstanceState);
        mViewModel = getViewModel();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, this.getClass().getSimpleName());
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, this.getClass().getSimpleName());
        FirebaseAnalytics.getInstance(requireContext()).logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

        SharedPreferences mPrefs = getBaseActivity().getSharedPreferences(BuildConfig.PREF_NAME, Context.MODE_PRIVATE);
        Context context = MyContextWrapper.wrap(getBaseActivity(), mPrefs.getString(AppPreferencesHelper.KEY_LANG_CODE, ChangeLanguageFragment.englishCode));
        getResources().updateConfiguration(context.getResources().getConfiguration(), context.getResources().getDisplayMetrics());

        setHasOptionsMenu(false);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        mRootView = mViewDataBinding.getRoot();
        return mRootView;
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        mViewDataBinding.executePendingBindings();
    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    public T getViewDataBinding() {
        return mViewDataBinding;
    }

    public void hideKeyboard() {
        if (mActivity != null) {
            mActivity.hideKeyboard();
        }
    }

    public boolean isNetworkConnected() {
        return mActivity != null && mActivity.isNetworkConnected();
    }


    private void performDependencyInjection() {
        AndroidSupportInjection.inject(this);
    }


    public void showLoading(int... msgId) {
        if (mActivity != null) {
            mActivity.showLoading(msgId);
        }
    }


    public void hideLoading() {
        if (mActivity != null) {
            mActivity.hideLoading();
        }
    }
}