package com.gipl.notifyme.ui.me;

import androidx.databinding.ObservableField;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.data.model.api.sendotp.User;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

public class MeViewModel extends BaseViewModel {
    public ObservableField<String> getEmpName() {
        return empName;
    }

    public ObservableField<String> getEmpCode() {
        return empCode;
    }

    public ObservableField<String> getEmpPlant() {
        return empPlant;
    }

    public ObservableField<String> getEmpMoNumber() {
        return empMoNumber;
    }

    private ObservableField<String> empName = new ObservableField<>("");
    private ObservableField<String> empCode = new ObservableField<>("");
    private ObservableField<String> empPlant = new ObservableField<>("");
    private ObservableField<String> empMoNumber = new ObservableField<>("");

    public MeViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);
        User user = dataManager.getUserObj();
        empName.set(user.getName());
        empCode.set(user.getEmpId());
        empMoNumber.set(user.getMobileNumber());
        empPlant.set("Gadre");
    }
}
