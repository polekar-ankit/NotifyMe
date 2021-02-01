package com.gipl.notifyme.ui.punchingslip;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gipl.notifyme.BR;
import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.lib.Shifts;
import com.gipl.notifyme.databinding.FragmentPunchingSlipBinding;
import com.gipl.notifyme.exceptions.ErrorMessageFactory;
import com.gipl.notifyme.ui.base.BaseFragment;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.DialogUtility;
import com.gipl.notifyme.uility.TimeUtility;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;

public class PunchingSlipFragment extends BaseFragment<FragmentPunchingSlipBinding, PunchingSlipViewModel> {

    @Inject
    PunchingSlipViewModel punchingSlipViewModel;
    DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, day) -> {
        getViewDataBinding().tvFrom.setText(TimeUtility.getDisplayFormattedDate(year, month, day));
    };
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog inTimePickerDialog;

    @Override
    public int getBindingVariable() {
        return BR.slip;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_punching_slip;
    }

    @Override
    public PunchingSlipViewModel getViewModel() {
        return punchingSlipViewModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        punchingSlipViewModel.getShiftLiveData().observe(this, this::processShift);
        punchingSlipViewModel.getResponseMutableLiveData().observe(this, this::processResponse);
    }

    private void processResponse(Response response) {
        switch (response.status) {
            case LOADING:
                showLoading();
                break;
            case SUCCESS:
                DialogUtility.showToast(requireContext(),getString(R.string.msg_punching_slip_created));
                getBaseActivity().onBackPressed();
                break;
            case ERROR:
                if (response.error != null) {
                    DialogUtility.showToast(requireContext(), ErrorMessageFactory.create(requireContext(), (Exception) response.error));
                }
                break;
        }
    }

    private void processShift(ArrayList<Shifts> shifts) {
        shifts.add(0, new Shifts("Select"));
        ArrayAdapter<Shifts> shiftsArrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.layout_spinner_item, shifts);
        getViewDataBinding().spinner.setAdapter(shiftsArrayAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().tvFrom.setText(TimeUtility.getTodayOnlyDateInDisplayFormat());

        getViewDataBinding().spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    punchingSlipViewModel.setShiftError(getString(R.string.shift_not_select_error));

                } else
                    punchingSlipViewModel.setShiftError("");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getViewDataBinding().tvInTime.setOnClickListener(v -> {
            Calendar calendar = getTimeCalender(getViewDataBinding().tvInTime.getText().toString());
            if (inTimePickerDialog != null && inTimePickerDialog.isShowing())
                inTimePickerDialog.dismiss();
            inTimePickerDialog = new TimePickerDialog(requireContext(),
                    (timePicker, h24, m24) -> {
                        try {
                            getViewDataBinding().tvInTime.setText(TimeUtility.convert24HrTimeTo12Hr(h24 + ":" + m24));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
            inTimePickerDialog.setTitle("In Time");
            inTimePickerDialog.show();
        });

        getViewDataBinding().tvOutTime.setOnClickListener(v -> {
            Calendar calendar = getTimeCalender(getViewDataBinding().tvOutTime.getText().toString());
            if (inTimePickerDialog != null && inTimePickerDialog.isShowing())
                inTimePickerDialog.dismiss();
            inTimePickerDialog = new TimePickerDialog(requireContext(),
                    (timePicker, h24, m24) -> {
                        try {
                            getViewDataBinding().tvOutTime.setText(TimeUtility.convert24HrTimeTo12Hr(h24 + ":" + m24));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
            inTimePickerDialog.setTitle("Out Time");
            inTimePickerDialog.show();
        });

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

        getViewDataBinding().btnApply.setOnClickListener(v -> {

            punchingSlipViewModel.addPunchingSlip(getViewDataBinding().tvInTime.getText().toString(),
                    getViewDataBinding().tvOutTime.getText().toString(),
                    getViewDataBinding().tvFrom.getText().toString());
        });
    }

    private Calendar getTimeCalender(String inOutTime) {
        Calendar time = Calendar.getInstance();
        try {
            time = TimeUtility.convert12HrTimeToCalender(inOutTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
}
