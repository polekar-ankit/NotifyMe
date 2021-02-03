package com.gipl.notifyme.ui.addovertime;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gipl.notifyme.BR;
import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.lib.Utility;
import com.gipl.notifyme.databinding.FragmentAddOvertimeBinding;
import com.gipl.notifyme.exceptions.ErrorMessageFactory;
import com.gipl.notifyme.ui.base.BaseFragment;
import com.gipl.notifyme.ui.model.LeaveFor;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.DialogUtility;
import com.gipl.notifyme.uility.TimeUtility;

import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;

public class AddOverTimeFragment extends BaseFragment<FragmentAddOvertimeBinding, AddOverTimeViewModel> {
    @Inject
    AddOverTimeViewModel viewModel;
    DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, day) -> {
        getViewDataBinding().tvFrom.setText(TimeUtility.getDisplayFormattedDate(year, month, day));
    };
    private DatePickerDialog datePickerDialog;

    @Override
    public int getBindingVariable() {
        return BR.overtime;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_add_overtime;
    }

    @Override
    public AddOverTimeViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.getResponseMutableLiveData().observe(this, this::processResponse);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().tvFrom.setText(TimeUtility.getTodayOnlyDateInDisplayFormat());
        getViewDataBinding().tvFrom.setOnClickListener(v -> {
//            if (datePickerDialog == null)
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            if (datePickerDialog != null)
                datePickerDialog.dismiss();

            datePickerDialog = DialogUtility.getDatePickerDialog(requireContext(),
                    getViewDataBinding().tvFrom.getText().toString(),
                    Calendar.getInstance().getTimeInMillis(),
                    calendar.getTimeInMillis(),
                    dateSetListener);
            if (datePickerDialog != null && !datePickerDialog.isShowing()) datePickerDialog.show();
        });
        getViewDataBinding().btnAddOvertime.setOnClickListener(v -> {
            hideKeyboard();
            viewModel.addOverTime(getViewDataBinding().tvFrom.getText().toString());
        });
    }

    private void processResponse(Response response) {
        switch (response.status) {
            case LOADING:
                showLoading();
                break;
            case SUCCESS:
                hideLoading();
                DialogUtility.showToast(requireContext(), getString(R.string.msg_ot_added));
                getBaseActivity().onBackPressed();
                break;
            case ERROR:
                hideLoading();
                if (response.error != null) {
                    DialogUtility.showSnackbar(getViewDataBinding().getRoot(), ErrorMessageFactory.create(requireContext(), (Exception) response.error));
                }
                break;
        }
    }

}
