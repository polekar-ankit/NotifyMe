package com.gipl.swayam.ui.addleave;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.gipl.swayam.R;
import com.gipl.swayam.data.model.api.applyleave.AddModifyLeaveReq;
import com.gipl.swayam.data.model.api.leavetype.LeaveApproval;
import com.gipl.swayam.data.model.api.lib.Utility;
import com.gipl.swayam.databinding.FragmentAddEditLeaveBinding;
import com.gipl.swayam.exceptions.ErrorMessageFactory;
import com.gipl.swayam.ui.base.BaseFragment;
import com.gipl.swayam.ui.image.ImagePreviewActivity;
import com.gipl.swayam.ui.leaveconfirm.LeaveConfirmationDialog;
import com.gipl.swayam.ui.model.LeaveFor;
import com.gipl.swayam.ui.model.Reason;
import com.gipl.swayam.ui.model.Response;
import com.gipl.swayam.uility.AppUtility;
import com.gipl.swayam.uility.DialogUtility;
import com.gipl.swayam.uility.TimeUtility;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

/**
 * Current Leave apply logic is consider as below
 * <p>
 * Same day
 * + first half to first half  - (0.5)
 * + first half to  second half  - (1)
 * + second half to  second half  - (0.5)
 * + full
 * <p>
 * Day diff:
 * + full to full
 * 13-15(3)
 * <p>
 * + full to first half
 * 13-15(2.5)
 * <p>
 * + full to second half
 * 13-15(3)
 * + first half to full
 * 13-15(3)
 * + second half to full
 * 13-15(2.5)
 * <p>
 * + second half to second half
 * 13-15=  2.5
 * + second half to first half
 * 13-15=  2
 * <p>
 * + first half to second half
 * 13-15=  3
 * <p>
 * + first half to first half
 * 13-15 = (2.5)
 */

public class AddModifyLeaveFragment extends BaseFragment<FragmentAddEditLeaveBinding, AddModifyLeaveViewModel> {
    private final DatePickerDialog.OnDateSetListener fromOnDateSetListener = (view, year, month, day) -> {
        String fromDate = TimeUtility.getDisplayFormattedDate(year, month, day);
        getViewDataBinding().tvFrom.setText(fromDate);
        try {
            Calendar fromCalender = TimeUtility.convertDisplayDateTimeToCalender(fromDate);
            Calendar toCalendar = TimeUtility.convertDisplayDateTimeToCalender(getViewDataBinding().tvTo.getText().toString());
            if (fromCalender.after(toCalendar)) {
                getViewDataBinding().tvTo.setText(fromDate);
            }
            checkDateSelectionAndEnableTo();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    };
    @Inject
    AddModifyLeaveViewModel addModifyLeaveViewModel;
    DatePickerDialog.OnDateSetListener toDateSetListener = (view, year, month, day) -> {
        getViewDataBinding().tvTo.setText(TimeUtility.getDisplayFormattedDate(year, month, day));
        checkDateSelectionAndEnableTo();
    };
    private DatePickerDialog datePickerDialog, toDatePickerDialog;
    private long numberOfLeaveDays;

    /**
     * this method calculate difference between to and from date if diff is zero(means same date selected)
     * then leave for option of to is disable
     */

    private void checkDateSelectionAndEnableTo() {
        try {
            long diff = TimeUtility.convertDisplayTimeToLong(getViewDataBinding().tvTo.getText().toString()) - TimeUtility.convertDisplayTimeToLong(getViewDataBinding().tvFrom.getText().toString());
            numberOfLeaveDays = TimeUtility.getDifferenceInDays(diff) + 1;
            getViewDataBinding().spinnerLeaveForTo.setEnabled(numberOfLeaveDays > 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

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
        addModifyLeaveViewModel.getIsLeaveDataValid().observe(this, this::processValidLeave);
        leaveConfirmationDialog = new LeaveConfirmationDialog(requireContext(), addModifyLeaveViewModel::addModifyLeave);
    }

    private LeaveConfirmationDialog leaveConfirmationDialog;

    private void processValidLeave(double finalNoOfLeaveDays) {
        LeaveFor leaveForFrom = (LeaveFor) getViewDataBinding().spinnerLeaveFor.getSelectedItem();
        LeaveApproval leaveApproval = (LeaveApproval) getViewDataBinding().spinnerLeaveType.getSelectedItem();
        leaveConfirmationDialog.show(numberOfLeaveDays, finalNoOfLeaveDays,
                getViewDataBinding().tvFrom.getText().toString(),
                getViewDataBinding().tvTo.getText().toString(),
                leaveForFrom.getName(),
                addModifyLeaveViewModel.getFinalReason(),
                leaveApproval.getSLeaveType());
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
                        DialogUtility.showToast(requireContext(), getString(R.string.msg_leave_applied));
                        getBaseActivity().onBackPressed();
                        getParentFragmentManager().setFragmentResult(AppUtility.INTENT_EXTRA.KEY_FRAG_LIST_RESULT, new Bundle());
                    }
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

    private void processLeaveType(ArrayList<LeaveApproval> leaveApprovals) {
        leaveApprovals.add(0, new LeaveApproval("Select"));
        ArrayAdapter<LeaveApproval> leaveForArrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.layout_spinner_item, leaveApprovals);
        getViewDataBinding().spinnerLeaveType.setAdapter(leaveForArrayAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().cvAttachment.setVisibility(View.GONE);
        getViewDataBinding().btnMedicalCertificate.setVisibility(View.GONE);

        setLeaveForFrom();
        setLeaveForTo();
        addModifyLeaveViewModel.getLeaveList();
        addModifyLeaveViewModel.getPreDefineReasonList().observe(getViewLifecycleOwner(), this::setReasonSpinner);

        getViewDataBinding().tvFrom.setText(TimeUtility.getTodayOnlyDateInDisplayFormat());
        getViewDataBinding().tvTo.setText(TimeUtility.getTodayOnlyDateInDisplayFormat());
        checkDateSelectionAndEnableTo();

        getViewDataBinding().tvFrom.setOnClickListener(v -> {
//            if (datePickerDialog == null)
            Calendar todaysDate = Calendar.getInstance();
            todaysDate.set(Calendar.DAY_OF_MONTH, todaysDate.getActualMaximum(Calendar.DAY_OF_MONTH));
            long maxDate = todaysDate.getTimeInMillis();

            todaysDate.set(Calendar.DAY_OF_MONTH, 1);
            todaysDate.set(Calendar.MONTH, todaysDate.get(Calendar.MONTH) - 1);
            long minDate = todaysDate.getTimeInMillis();

            datePickerDialog = DialogUtility.getDatePickerDialog(requireContext(),
                    getViewDataBinding().tvFrom.getText().toString(),
                    0,
                    minDate,
                    fromOnDateSetListener);
            if (datePickerDialog != null && !datePickerDialog.isShowing()) datePickerDialog.show();
        });

        getViewDataBinding().tvTo.setOnClickListener(v -> {
            Calendar fromDate = null;
            Calendar toDate;
            try {
                fromDate = TimeUtility.convertDisplayDateTimeToCalender(getViewDataBinding().tvFrom.getText().toString());
                toDate = Calendar.getInstance();
                toDate.setTimeInMillis(fromDate.getTimeInMillis());
                toDate.set(Calendar.DAY_OF_MONTH, toDate.get(Calendar.DAY_OF_MONTH) + 30);
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
            LeaveFor leaveForFrom = (LeaveFor) getViewDataBinding().spinnerLeaveFor.getSelectedItem();
            LeaveFor leaveForTo = (LeaveFor) getViewDataBinding().spinnerLeaveForTo.getSelectedItem();
            LeaveApproval leaveApproval = (LeaveApproval) getViewDataBinding().spinnerLeaveType.getSelectedItem();
            Reason reason = (Reason) getViewDataBinding().spinnerReason.getSelectedItem();
            hideKeyboard();
            addModifyLeaveViewModel.checkLeaveDataForError(getViewDataBinding().tvFrom.getText().toString(),
                    getViewDataBinding().tvTo.getText().toString(), leaveForFrom,
                    numberOfLeaveDays > 1 ? leaveForTo : leaveForFrom, leaveApproval, reason, numberOfLeaveDays);
        });

        getViewDataBinding().btnMedicalCertificate.setOnClickListener(v -> showFilePickerOptions());

        getViewDataBinding().spinnerLeaveType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                LeaveApproval leave = (LeaveApproval) getViewDataBinding().spinnerLeaveType.getSelectedItem();
                if (leave.getSLeaveType().equals("SL")) {
                    getViewDataBinding().btnMedicalCertificate.setVisibility(View.VISIBLE);
                } else {
                    getViewDataBinding().btnMedicalCertificate.setVisibility(View.GONE);
                    getViewDataBinding().cvAttachment.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
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

    private void showFilePickerOptions() {
        PopupMenu popup = new PopupMenu(requireContext(), getViewDataBinding().btnMedicalCertificate);
        popup.getMenu().add(0, 1, 0, "Image");
        popup.getMenu().add(0, 2, 0, "Pdf");
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(item -> {
//            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            if (item.getItemId() == 1) {
//                intent.setType("image/*");
                mGetContent.launch("image/*");

            } else if (item.getItemId() == 2) {
//                intent.setType("application/pdf");
                mGetContent.launch("application/pdf");
            }

//            startActivityForResult(intent, 123);
            return true;
        });

        popup.show(); //showing popup menu

    }

    private void setReasonSpinner(List<Reason> reasonArrayList) {
        ArrayAdapter<Reason> reasonArrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.layout_spinner_item, reasonArrayList);
        getViewDataBinding().spinnerReason.setAdapter(reasonArrayAdapter);
    }


    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
//                    Uri uri = data.getData();
                    addModifyLeaveViewModel.setAttachment(uri);
                    getViewDataBinding().cvAttachment.setVisibility(View.VISIBLE);


                    addModifyLeaveViewModel.setAttachment(uri);
                    Glide.with(requireContext())
                            .load(uri)
                            .centerInside()
                            .priority(Priority.IMMEDIATE)
                            .centerCrop()
                            .placeholder(R.drawable.ic_file)
                            .into(getViewDataBinding().ivFilePreview);
                    getViewDataBinding().tvFileName.setText(getFileName(uri));
//
                    getViewDataBinding().nsvLeave.post(() -> getViewDataBinding().nsvLeave.fullScroll(View.FOCUS_DOWN));
//
                    getViewDataBinding().cvAttachment.setOnClickListener(v -> {
                        if (requireContext().getContentResolver().getType(uri).contains("image")) {
                            ImagePreviewActivity.start(requireContext(), uri.toString(), false);
                        } else {
                            AppUtility.openPdf(uri, requireContext(), getViewDataBinding().getRoot());
                        }
                    });
                }
            });

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 123 && resultCode == Activity.RESULT_OK && data != null) {
//
//        }
//    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = requireContext().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    private void setLeaveForFrom() {
        ArrayList<LeaveFor> forArrayList = new ArrayList<>();
        forArrayList.add(new LeaveFor("Select", -1));
        Utility utility = addModifyLeaveViewModel.getDataManager().getUtility();
        forArrayList.add(new LeaveFor(getString(R.string.lbl_full_day), utility.getLeaveFor().getBitFullDay()));
        forArrayList.add(new LeaveFor(getString(R.string.lbl_first_half), utility.getLeaveFor().getBitFirstHalfDay()));
        forArrayList.add(new LeaveFor(getString(R.string.lbl_second_half), utility.getLeaveFor().getBitSecondHalfDay()));
        ArrayAdapter<LeaveFor> leaveForFromArrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.layout_spinner_item, forArrayList);
        getViewDataBinding().spinnerLeaveFor.setAdapter(leaveForFromArrayAdapter);

    }

    //here we added seprate  adapter to to spinner bocoz to spinner dont have second half option
    private void setLeaveForTo() {
        ArrayList<LeaveFor> forArrayList = new ArrayList<>();
        forArrayList.add(new LeaveFor("Select", -1));
        Utility utility = addModifyLeaveViewModel.getDataManager().getUtility();
        forArrayList.add(new LeaveFor(getString(R.string.lbl_full_day), utility.getLeaveFor().getBitFullDay()));
        forArrayList.add(new LeaveFor(getString(R.string.lbl_first_half), utility.getLeaveFor().getBitFirstHalfDay()));
        forArrayList.add(new LeaveFor(getString(R.string.lbl_second_half), utility.getLeaveFor().getBitSecondHalfDay()));
        ArrayAdapter<LeaveFor> leaveForToArrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.layout_spinner_item, forArrayList);
        getViewDataBinding().spinnerLeaveForTo.setAdapter(leaveForToArrayAdapter);
    }
}
