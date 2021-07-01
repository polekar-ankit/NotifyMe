package com.gipl.swayam.ui.addco;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gipl.swayam.BR;
import com.gipl.swayam.R;
import com.gipl.swayam.databinding.FragmentAddCoBinding;
import com.gipl.swayam.exceptions.ErrorMessageFactory;
import com.gipl.swayam.ui.base.BaseFragment;
import com.gipl.swayam.ui.model.LeaveFor;
import com.gipl.swayam.ui.model.Reason;
import com.gipl.swayam.ui.model.Response;
import com.gipl.swayam.uility.AppUtility;
import com.gipl.swayam.uility.DialogUtility;
import com.gipl.swayam.uility.TimeUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

public class AddCoFragment extends BaseFragment<FragmentAddCoBinding, AddCoViewModel> {

    @Inject
    AddCoViewModel viewModel;

    DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, day) -> {
        getViewDataBinding().tvFrom.setText(TimeUtility.getDisplayFormattedDate(year, month, day));
    };
    private DatePickerDialog datePickerDialog;

    @Override
    public int getBindingVariable() {
        return BR.addCo;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_add_co;
    }

    @Override
    public AddCoViewModel getViewModel() {
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
        setLeaveFor();

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
        getViewDataBinding().btnAddCo.setOnClickListener(v -> {
            hideKeyboard();
            LeaveFor coFor = (LeaveFor) getViewDataBinding().spinnerCoFor.getSelectedItem();
            Reason reason = (Reason) getViewDataBinding().spinnerReason.getSelectedItem();
            viewModel.addCo(getViewDataBinding().tvFrom.getText().toString(), coFor, reason);
        });

        getViewDataBinding().spinnerReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Reason reason = (Reason) getViewDataBinding().spinnerReason.getSelectedItem();
                if (reason.getSuid() == 32) {
                    getViewDataBinding().etReason.setVisibility(View.VISIBLE);
                } else {
                    getViewDataBinding().etReason.setVisibility(View.GONE);
                }
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
                    DialogUtility.showToast(requireContext(), getString(R.string.msg_co_added));
                    getBaseActivity().onBackPressed();
                    getParentFragmentManager().setFragmentResult(AppUtility.INTENT_EXTRA.KEY_FRAG_LIST_RESULT,new Bundle());
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

    private void setLeaveFor() {
        ArrayList<LeaveFor> forArrayList = new ArrayList<>();
        forArrayList.add(new LeaveFor("Select", -1));
        forArrayList.add(new LeaveFor(getString(R.string.lbl_full_day), CO_FOR.FULL_DAY.getValue()));
        forArrayList.add(new LeaveFor(getString(R.string.lbl_co_half_day), CO_FOR.HALF_DAY.getValue()));
        forArrayList.add(new LeaveFor(getString(R.string.lbl_co_one_half_day), CO_FOR.ONE_AND_HALF_DAYS.getValue()));
        forArrayList.add(new LeaveFor(getString(R.string.lbl_co_two), CO_FOR.TWO_DAYS.getValue()));
//        forArrayList.add(new LeaveFor(getString(R.string.lbl_second_half), utility.getLeaveFor().getBitSecondHalfDay()));
        ArrayAdapter<LeaveFor> leaveForArrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.layout_spinner_item, forArrayList);
        getViewDataBinding().spinnerCoFor.setAdapter(leaveForArrayAdapter);
    }

    public enum CO_FOR {
        HALF_DAY(1),
        FULL_DAY(2),
        ONE_AND_HALF_DAYS(3),
        TWO_DAYS(4);
        private int value;

        CO_FOR(int i) {
            this.value = i;
        }

        public int getValue() {
            return value;
        }
    }
}
