package com.gipl.notifyme.ui.checkout;

import android.content.Context;

import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.MutableLiveData;

import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.lib.utility.CheckOutType;
import com.gipl.notifyme.databinding.DialogCheckOutBinding;
import com.gipl.notifyme.ui.base.BaseDialog;

public class CheckOutDialog extends BaseDialog<DialogCheckOutBinding, CheckOutViewModel> {
    private CheckOutViewModel checkOutViewModel;
    private MutableLiveData<Integer> checkOutTypeLiveData = new MutableLiveData<>();
    private Context context;

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
    }
}
