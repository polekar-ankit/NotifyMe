package com.gipl.notifyme.ui.addco;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gipl.notifyme.BR;
import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.lib.Utility;
import com.gipl.notifyme.databinding.FragmentAddCoBinding;
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

import javax.inject.Inject;

public class AddCoFragment extends BaseFragment<FragmentAddCoBinding, AddCoViewModel> {

    @Inject
    AddCoViewModel viewModel;

    DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, day) -> {
        getViewDataBinding().tvFrom.setText(TimeUtility.getDisplayFormattedDate(year, month, day));
    };
    private DatePickerDialog datePickerDialog;
    private IFragmentListener iFragmentListener;

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

        if (getArguments() != null) {
            iFragmentListener = getArguments().getParcelable(AppUtility.INTENT_EXTRA.KEY_FRAG_LIST_RESULT);
        }
        setLeaveFor();
        setReasonSpinner();
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
                DialogUtility.showToast(requireContext(), getString(R.string.msg_co_added));
                getBaseActivity().onBackPressed();
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

    private void setLeaveFor() {
        ArrayList<LeaveFor> forArrayList = new ArrayList<>();
        forArrayList.add(new LeaveFor("Select", -1));
        Utility utility = viewModel.getDataManager().getUtility();
        forArrayList.add(new LeaveFor(getString(R.string.lbl_full_day), utility.getLeaveFor().getBitFullDay()));
        forArrayList.add(new LeaveFor(getString(R.string.lbl_co_half_day), utility.getLeaveFor().getBitFirstHalfDay()));
//        forArrayList.add(new LeaveFor(getString(R.string.lbl_second_half), utility.getLeaveFor().getBitSecondHalfDay()));
        ArrayAdapter<LeaveFor> leaveForArrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.layout_spinner_item, forArrayList);
        getViewDataBinding().spinnerCoFor.setAdapter(leaveForArrayAdapter);
    }
}
