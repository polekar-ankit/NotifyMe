package com.gipl.notifyme.ui.shiftchange;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gipl.notifyme.BR;
import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.lib.Shifts;
import com.gipl.notifyme.databinding.FragmentShiftChangeBinding;
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

public class ShiftChangeFragment extends BaseFragment<FragmentShiftChangeBinding, ShiftChangeViewModel> {
    @Inject
    ShiftChangeViewModel viewModel;
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
    private IFragmentListener iFragmentListener;

    @Override
    public int getBindingVariable() {
        return BR.shiftChange;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_shift_change;
    }

    @Override
    public ShiftChangeViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.getShiftLiveData().observe(this, this::processShift);
        viewModel.getResponseMutableLiveData().observe(this, this::processResponse);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            iFragmentListener = getArguments().getParcelable(AppUtility.INTENT_EXTRA.KEY_FRAG_LIST_RESULT);
        }

        setReasonSpinner();
        getViewDataBinding().tvFrom.setText(TimeUtility.getTodayOnlyDateInDisplayFormat());
        getViewDataBinding().tvTo.setText(TimeUtility.getTodayOnlyDateInDisplayFormat());

        getViewDataBinding().tvFrom.setOnClickListener(v -> {
            Calendar calMin = Calendar.getInstance();
            calMin.set(Calendar.MONTH, calMin.get(Calendar.MONTH) - 1);
            calMin.set(Calendar.DAY_OF_MONTH, 1);

            Calendar calMax = Calendar.getInstance();
            calMax.set(Calendar.MONTH, calMax.get(Calendar.MONTH) + 1);
            calMax.set(Calendar.DAY_OF_MONTH, calMax.getActualMaximum(Calendar.DATE));
            if (datePickerDialog != null)
                datePickerDialog.dismiss();

            datePickerDialog = DialogUtility.getDatePickerDialog(requireContext(),
                    getViewDataBinding().tvFrom.getText().toString(),
                    calMax.getTimeInMillis(),
                    calMin.getTimeInMillis(),
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

        getViewDataBinding().btnAddShiftChange.setOnClickListener(v -> {
            Shifts shiftFrom = (Shifts) getViewDataBinding().spinner.getSelectedItem();
            Shifts shiftTo = (Shifts) getViewDataBinding().spinnerTo.getSelectedItem();
            Reason reason = (Reason) getViewDataBinding().spinnerReason.getSelectedItem();
            hideKeyboard();
            viewModel.addShiftChangedRequest(getViewDataBinding().tvFrom.getText().toString(),
                    getViewDataBinding().tvTo.getText().toString(),
                    shiftFrom.getSuidShift(),
                    shiftTo.getSuidShift(),
                    reason
            );
        });
        getViewDataBinding().spinnerReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Reason reason = (Reason) getViewDataBinding().spinnerReason.getSelectedItem();
                getViewDataBinding().etShiftchangeReason.setVisibility(reason.getSuid() == 32 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void processShift(ArrayList<Shifts> shifts) {
        shifts.add(0, new Shifts("Select"));
        ArrayAdapter<Shifts> shiftsArrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.layout_spinner_item, shifts);
        getViewDataBinding().spinner.setAdapter(shiftsArrayAdapter);

        getViewDataBinding().spinnerTo.setAdapter(shiftsArrayAdapter);
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

    private void processResponse(Response response) {
        switch (response.status) {
            case LOADING:
                showLoading();
                break;
            case SUCCESS:
                hideLoading();
                DialogUtility.showToast(requireContext(), getString(R.string.msg_shift_changed));
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
}
