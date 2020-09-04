package com.gipl.notifyme.ui.checkout;

import androidx.lifecycle.ViewModel;

import com.gipl.notifyme.data.model.api.lib.utility.CheckOutType;

public class CheckOutViewModel extends ViewModel {

    public CheckOutType getCheckOutType() {
        return checkOutType;
    }

    public CheckOutViewModel(CheckOutType checkOutType) {
        this.checkOutType = checkOutType;
    }

    private CheckOutType checkOutType;
}
