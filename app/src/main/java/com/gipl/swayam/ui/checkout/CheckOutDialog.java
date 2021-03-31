package com.gipl.swayam.ui.checkout;

import android.content.Context;

import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.MutableLiveData;

import com.gipl.swayam.R;
import com.gipl.swayam.data.model.api.lib.utility.CheckOutType;
import com.gipl.swayam.databinding.DialogCheckOutBinding;
import com.gipl.swayam.ui.base.BaseDialog;

public class CheckOutDialog extends BaseDialog<DialogCheckOutBinding, CheckOutViewModel> {
    private final CheckOutViewModel checkOutViewModel;
    private final MutableLiveData<Integer> checkOutTypeLiveData = new MutableLiveData<>();
    private final Context context;

    public CheckOutDialog(Context context, CheckOutType checkOutType) {
        checkOutViewModel = new CheckOutViewModel(checkOutType);
        this.context = context;
    }

    public MutableLiveData<Integer> getCheckOutTypeLiveData() {
        return checkOutTypeLiveData;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_check_out;
    }

    @Override
    public CheckOutViewModel getDialogViewModel() {
        return checkOutViewModel;
    }

    @Override
    public int getDialogBindingVariable() {
        return BR.viewModel;
    }

    public void show() {
        init(context);
        getViewBinding().btnDayEnd.setOnClickListener(v->{
            getCheckOutTypeLiveData().postValue(checkOutViewModel.getCheckOutType().getBitDayEnd());
            dismissDialog();
        });
        getViewBinding().btnLunch.setOnClickListener(v->{
            getCheckOutTypeLiveData().postValue(checkOutViewModel.getCheckOutType().getBitLunch());
            dismissDialog();
        });
        getViewBinding().btnOfficeOut.setOnClickListener(v->{
            getCheckOutTypeLiveData().postValue(checkOutViewModel.getCheckOutType().getBitOfficialOut());
            dismissDialog();
        });
        getViewBinding().ivCancel.setOnClickListener(v->{
            dismissDialog();
        });
    }
}
