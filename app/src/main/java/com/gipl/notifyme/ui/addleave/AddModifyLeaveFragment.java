package com.gipl.notifyme.ui.addleave;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;

import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.applyleave.AddModifyLeaveReq;
import com.gipl.notifyme.data.model.api.leavetype.LeaveApproval;
import com.gipl.notifyme.data.model.api.lib.Utility;
import com.gipl.notifyme.databinding.FragmentAddEditLeaveBinding;
import com.gipl.notifyme.exceptions.ErrorMessageFactory;
import com.gipl.notifyme.ui.base.BaseFragment;
import com.gipl.notifyme.ui.model.LeaveFor;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.DialogUtility;
import com.gipl.notifyme.uility.TimeUtility;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;

public class AddModifyLeaveFragment extends BaseFragment<FragmentAddEditLeaveBinding, AddModifyLeaveViewModel> {
    @Inject
    AddModifyLeaveViewModel addModifyLeaveViewModel;

    DatePickerDialog.OnDateSetListener toDateSetListener = (view, year, month, day) -> {
        getViewDataBinding().tvTo.setText(TimeUtility.getDisplayFormattedDate(year, month, day));
    };
    private DatePickerDialog datePickerDialog, toDatePickerDialog;
    private DatePickerDialog.OnDateSetListener fromOnDateSetListener = (view, year, month, day) -> {
        String fromDate = TimeUtility.getDisplayFormattedDate(year, month, day);
        getViewDataBinding().tvFrom.setText(fromDate);
        try {
            Calendar fromCalender = TimeUtility.convertDisplayDateTimeToCalender(fromDate);
            Calendar toCalendar = TimeUtility.convertDisplayDateTimeToCalender(getViewDataBinding().tvTo.getText().toString());
            if (fromCalender.after(toCalendar)) {
                getViewDataBinding().tvTo.setText(fromDate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    };

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_add_edit_leave;
    }

    @Override
    public AddModifyLeaveViewModel getViewModel() {
        return addModifyLeaveViewModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addModifyLeaveViewModel.getLeaveTypeLiveData().observe(this, this::processLeaveType);
        addModifyLeaveViewModel.getResponseMutableLiveData().observe(this, this::processRespnse);
    }

    private void processRespnse(Response response) {
        switch (response.status) {
            case LOADING:
                showLoading();
                break;
            case SUCCESS:
                hideLoading();
                if (response.data instanceof String) {
                    String name = (String) response.data;
                    if (name.equals(AddModifyLeaveReq.class.getSimpleName())) {
                        DialogUtility.showToast(requireContext(),"Your Leave has been successfully applied");
                        getBaseActivity().onBackPressed();
                    }
                }
                break;
            case ERROR:
                hideLoading();
                if (response.error != null) {
                    DialogUtility.showToast(requireContext(), ErrorMessageFactory.create(requireContext(), (Exception) response.error));
                }
                break;
        }
    }

    private void processLeaveType(ArrayList<LeaveApproval> leaveApprovals) {
        leaveApprovals.add(0, new LeaveApproval("Select"));
        ArrayAdapter<LeaveApproval> leaveForArrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.layout_spinner_item, leaveApprovals);
        getViewDataBinding().spinnerLeaveType.setAdapter(leaveForArrayAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setLeaveFor();
        addModifyLeaveViewModel.getLeaveList();
        getViewDataBinding().tvFrom.setText(TimeUtility.getTodayOnlyDateInDisplayFormat());
        getViewDataBinding().tvTo.setText(TimeUtility.getTodayOnlyDateInDisplayFormat());

        getViewDataBinding().tvFrom.setOnClickListener(v -> {
//            if (datePickerDialog == null)
            datePickerDialog = DialogUtility.getDatePickerDialog(requireContext(),
                    getViewDataBinding().tvFrom.getText().toString(),
                    0,
                    Calendar.getInstance().getTime().getTime(),
                    fromOnDateSetListener);
            if (datePickerDialog != null && !datePickerDialog.isShowing()) datePickerDialog.show();
        });

        getViewDataBinding().tvTo.setOnClickListener(v -> {
            Calendar fromDate = null;
            try {
                fromDate = TimeUtility.convertDisplayDateTimeToCalender(getViewDataBinding().tvFrom.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            toDatePickerDialog = DialogUtility.getDatePickerDialog(requireContext(),
                    getViewDataBinding().tvTo.getText().toString(),
                    0,
                    fromDate != null ? fromDate.getTime().getTime() : 0,
                    toDateSetListener);

            if (toDatePickerDialog != null && !toDatePickerDialog.isShowing())
                toDatePickerDialog.show();
        });

        getViewDataBinding().btnApply.setOnClickListener(v -> {
            LeaveFor leaveFor = (LeaveFor) getViewDataBinding().spinnerLeaveFor.getSelectedItem();
            LeaveApproval leaveApproval = (LeaveApproval) getViewDataBinding().spinnerLeaveType.getSelectedItem();

            addModifyLeaveViewModel.addModifyLeave(getViewDataBinding().tvFrom.getText().toString(),
                    getViewDataBinding().tvTo.getText().toString(), leaveFor, leaveApproval);
        });

    }

    private void setLeaveFor() {
        ArrayList<LeaveFor> forArrayList = new ArrayList<>();
        forArrayList.add(new LeaveFor("Select", -1));
        Utility utility = addModifyLeaveViewModel.getDataManager().getUtility();
        forArrayList.add(new LeaveFor("Full Day", utility.getLeaveFor().getBitFullDay()));
        forArrayList.add(new LeaveFor("First Half Day", utility.getLeaveFor().getBitFirstHalfDay()));
        forArrayList.add(new LeaveFor("Second Half Day", utility.getLeaveFor().getBitSecondHalfDay()));
        ArrayAdapter<LeaveFor> leaveForArrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.layout_spinner_item, forArrayList);
        getViewDataBinding().spinnerLeaveFor.setAdapter(leaveForArrayAdapter);
    }
}
