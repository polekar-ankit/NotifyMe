package com.gipl.notifyme.ui.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

import com.gipl.notifyme.uility.DialogUtility;


/**
 * Creted by User on 28-Sep-18
 */
public abstract class BaseDialog<T extends ViewDataBinding, V extends ViewModel> {

    private Dialog dialog;
    private Context context;
    private T viewBinding;
    private V dialogViewModel;
    private ProgressDialog mProgressDialog;

    public abstract int getLayoutId();

    public abstract V getDialogViewModel();

    public abstract int getDialogBindingVariable();

    protected void init(Context context) {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        this.context = context;
        dialogViewModel = dialogViewModel == null ? getDialogViewModel() : dialogViewModel;
        viewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), getLayoutId(), null, false);
        dialog = DialogUtility.getDialog(context, viewBinding.getRoot());
        viewBinding.setVariable(getDialogBindingVariable(), dialogViewModel);
        viewBinding.executePendingBindings();
    }

    public T getViewBinding() {
        return viewBinding;
    }

    protected void dismissDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    public void setCancelable(boolean isCancalable) {
        if (dialog != null) {
            dialog.setCancelable(isCancalable);
        }
    }

    public Context getContext() {
        return context;
    }

    protected void showLoading() {
        hideLoading();
        mProgressDialog = DialogUtility.showLoadingDialog(context);
    }

    protected void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }
}
