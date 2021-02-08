package com.gipl.notifyme.ui.punchingslip;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gipl.notifyme.BR;
import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.lib.Shifts;
import com.gipl.notifyme.databinding.FragmentPunchingSlipBinding;
import com.gipl.notifyme.exceptions.ErrorMessageFactory;
import com.gipl.notifyme.ui.base.BaseFragment;
import com.gipl.notifyme.ui.model.Reason;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.AppUtility;
import com.gipl.notifyme.uility.DialogUtility;
import com.gipl.notifyme.uility.IFragmentListener;
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
                hideLoading();
                DialogUtility.showToast(requireContext(), getString(R.string.msg_punching_slip_created));
                getBaseActivity().onBackPressed();
                if (iFragmentListener != null)
                    iFragmentListener.onActivityResult(null);
                break;
            case ERROR:
                hideLoading();
                if (response.error != null) {
                    DialogUtility.showSnackbar(getViewDataBinding().getRoot(), ErrorMessageFactory.create(requireContext(), (Exception) response.error));
                }
                break;
        }
    }

    private void processShift(ArrayList<Shifts> shifts) {
        shifts.add(0, new Shifts("Select"));
        ArrayAdapter<Shifts> shiftsArrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.layout_spinner_item, shifts);
        getViewDataBinding().spinnerShift.setAdapter(shiftsArrayAdapter);
    }

    private IFragmentListener iFragmentListener;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            iFragmentListener = getArguments().getParcelable(AppUtility.INTENT_EXTRA.KEY_FRAG_LIST_RESULT);
        }

        getViewDataBinding().tvFrom.setText(TimeUtility.getTodayOnlyDateInDisplayFormat());
        setReasonSpinner();

        getViewDataBinding().spinnerShift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        getViewDataBinding().cbIn.setOnCheckedChangeListener((buttonView, isChecked) -> {
            getViewDataBinding().tvInTime.setEnabled(isChecked);
            if (isChecked) {
                if (getViewDataBinding().tvInTime.getText().length() == 0) {
                    getViewDataBinding().tvInTime.performClick();
                }
            }
        });
        getViewDataBinding().cbOut.setOnCheckedChangeListener((buttonView, isChecked) -> {
            getViewDataBinding().tvOutTime.setEnabled(isChecked);
            if (isChecked) {
                if (getViewDataBinding().tvOutTime.getText().length() == 0) {
                    getViewDataBinding().tvOutTime.performClick();
                }
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

        getViewDataBinding().spinnerReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Reason reason = (Reason) getViewDataBinding().spinnerReason.getSelectedItem();
                if (reason.getSuid() == 32) {
                    getViewDataBinding().tietMisspunchReason.setVisibility(View.VISIBLE);
                } else {
                    getViewDataBinding().tietMisspunchReason.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getViewDataBinding().btnApply.setOnClickListener(v -> {
            Shifts shifts = (Shifts) getViewDataBinding().spinnerShift.getSelectedItem();
            Reason reason = (Reason) getViewDataBinding().spinnerReason.getSelectedItem();
            punchingSlipViewModel.addPunchingSlip(getViewDataBinding().cbIn.isChecked() ? getViewDataBinding().tvInTime.getText().toString() : "",
                    getViewDataBinding().cbOut.isChecked() ? getViewDataBinding().tvOutTime.getText().toString() : "",
                    getViewDataBinding().tvFrom.getText().toString(),
                    shifts.getSuidShift(),
                    reason
            );
        });
    }

    private void setReasonSpinner() {
        ArrayList<Reason> reasonArrayList = new ArrayList<>();
        reasonArrayList.add(new Reason("Select Reason", -1));
        reasonArrayList.add(new Reason("reason 1", 1));
        reasonArrayList.add(new Reason("reason 2", 2));
        reasonArrayList.add(new Reason("reason 3", 4));
        reasonArrayList.add(new Reason("reason 4", 8));
        reasonArrayList.add(new Reason("reason 5", 16));
        reasonArrayList.add(new Reason("Other", 32));
        ArrayAdapter<Reason> reasonArrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.layout_spinner_item, reasonArrayList);
        getViewDataBinding().spinnerReason.setAdapter(reasonArrayAdapter);
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
