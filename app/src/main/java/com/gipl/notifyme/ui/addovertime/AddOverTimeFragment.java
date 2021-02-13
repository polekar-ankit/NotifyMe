package com.gipl.notifyme.ui.addovertime;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gipl.notifyme.BR;
import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.lib.Utility;
import com.gipl.notifyme.databinding.FragmentAddOvertimeBinding;
import com.gipl.notifyme.exceptions.ErrorMessageFactory;
import com.gipl.notifyme.ui.base.BaseFragment;
import com.gipl.notifyme.ui.model.LeaveFor;
import com.gipl.notifyme.ui.model.Reason;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.AppUtility;
import com.gipl.notifyme.uility.DialogUtility;
import com.gipl.notifyme.uility.IFragmentListener;
import com.gipl.notifyme.uility.TimeUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class AddOverTimeFragment extends BaseFragment<FragmentAddOvertimeBinding, AddOverTimeViewModel> {
    @Inject
    AddOverTimeViewModel viewModel;
    DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, day) -> {
        getViewDataBinding().tvFrom.setText(TimeUtility.getDisplayFormattedDate(year, month, day));
    };
    private DatePickerDialog datePickerDialog;
    private IFragmentListener iFragmentListener;

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
        if (getArguments() != null) {
            iFragmentListener = getArguments().getParcelable(AppUtility.INTENT_EXTRA.KEY_FRAG_LIST_RESULT);
        }
        viewModel.getPreDefineReasonList().observe(getViewLifecycleOwner(), this::setReasonSpinner);
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
            Reason reason = (Reason) getViewDataBinding().spinnerReason.getSelectedItem();
            viewModel.getOtHours().set(getViewDataBinding().npHr.getValue() + "." + getViewDataBinding().npMin.getValue());
            viewModel.addOverTime(getViewDataBinding().tvFrom.getText().toString(), reason);
        });


        getViewDataBinding().npHr.setMinValue(2);
        getViewDataBinding().npHr.setMaxValue(16);
        getViewDataBinding().npMin.setMinValue(0);
        getViewDataBinding().npMin.setMaxValue(12);
        getViewDataBinding().npMin.setFormatter(value -> String.format(Locale.getDefault(), "%d", (value * 5)));

        getViewDataBinding().npHr.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if (newVal == 16) {
                getViewDataBinding().npMin.setMaxValue(0);
                getViewDataBinding().npMin.setValue(0);
            } else getViewDataBinding().npMin.setMaxValue(12);
        });

        getViewDataBinding().spinnerReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Reason reason = (Reason) getViewDataBinding().spinnerReason.getSelectedItem();
                getViewDataBinding().tietOvertimeReason.setVisibility(reason.getSuid() == 32 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setReasonSpinner(List<Reason> reasonArrayList) {
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
                if (response.data instanceof Boolean) {
                    DialogUtility.showToast(requireContext(), getString(R.string.msg_ot_added));
                    if (iFragmentListener != null) {
                        iFragmentListener.onActivityResult(null);
                    }
                    getBaseActivity().onBackPressed();
                }
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
