package com.gipl.notifyme.ui.addleave;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.library.baseAdapters.BR;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.applyleave.AddModifyLeaveReq;
import com.gipl.notifyme.data.model.api.leavetype.LeaveApproval;
import com.gipl.notifyme.data.model.api.lib.Utility;
import com.gipl.notifyme.databinding.FragmentAddEditLeaveBinding;
import com.gipl.notifyme.exceptions.ErrorMessageFactory;
import com.gipl.notifyme.ui.base.BaseFragment;
import com.gipl.notifyme.ui.image.ImagePreviewActivity;
import com.gipl.notifyme.ui.model.LeaveFor;
import com.gipl.notifyme.ui.model.Reason;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.AppUtility;
import com.gipl.notifyme.uility.DialogUtility;
import com.gipl.notifyme.uility.IFragmentListener;
import com.gipl.notifyme.uility.TimeUtility;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.text.DecimalFormat;
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
    private IFragmentListener iFragmentListener;
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
    }

    private void processValidLeave(double finalNoOfLeaveDays) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        if (numberOfLeaveDays > 1) {
            DecimalFormat decimalFormat = new DecimalFormat("0.#####");
            String displayDays = decimalFormat.format(finalNoOfLeaveDays);
            builder.setTitle(getString(R.string.title_leave_for, displayDays));
            builder.setMessage(getString(R.string.msg_multiple_days_leave, displayDays,
                    getViewDataBinding().tvFrom.getText().toString(), getViewDataBinding().tvTo.getText().toString()));
        } else {
            builder.setTitle(getString(R.string.title_leave_half_day));
            LeaveFor leaveForFrom = (LeaveFor) getViewDataBinding().spinnerLeaveFor.getSelectedItem();
            builder.setMessage(getString(R.string.msg_single_day_leave,
                    getViewDataBinding().tvFrom.getText().toString(), leaveForFrom.getName()));
        }
        builder.setPositiveButton(R.string.btn_lbl_leave_confirm, (dialog, which) -> {
            addModifyLeaveViewModel.addModifyLeave();
            dialog.dismiss();
        });
        builder.setNegativeButton(R.string.btn_lbl_leave_confirm_wrong, (dialog, which) -> dialog.dismiss());
        builder.create().show();
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
                        DialogUtility.showToast(requireContext(), "Your Leave has been successfully applied");
                        if (iFragmentListener != null) {
                            iFragmentListener.onActivityResult(null);
                        }
                        getBaseActivity().onBackPressed();
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
        if (getArguments() != null) {
            iFragmentListener = getArguments().getParcelable(AppUtility.INTENT_EXTRA.KEY_FRAG_LIST_RESULT);
        }
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
            datePickerDialog = DialogUtility.getDatePickerDialog(requireContext(),
                    getViewDataBinding().tvFrom.getText().toString(),
                    0,
                    0,
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
            Intent intent = new Intent(getContext(), FilePickerActivity.class);
            if (item.getItemId() == 1) {
                intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                        .setCheckPermission(true)
                        .setShowImages(true)
                        .setShowVideos(false)
                        .enableImageCapture(true)
                        .setMaxSelection(1)
                        .setSkipZeroSizeFiles(true)
                        .build());
            } else if (item.getItemId() == 2) {
                intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                        .setCheckPermission(true)
                        .setShowImages(false)
                        .setShowVideos(false)
                        .setShowFiles(true)
                        .setSuffixes("pdf")
                        .setMaxSelection(1)
                        .setSkipZeroSizeFiles(true)
                        .build());
            }

            startActivityForResult(intent, 123);
            return true;
        });

        popup.show(); //showing popup menu

    }

    private void setReasonSpinner(List<Reason> reasonArrayList) {
        ArrayAdapter<Reason> reasonArrayAdapter = new ArrayAdapter<>(requireContext(), R.layout.layout_spinner_item, reasonArrayList);
        getViewDataBinding().spinnerReason.setAdapter(reasonArrayAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<MediaFile> files;
            getViewDataBinding().cvAttachment.setVisibility(View.VISIBLE);
            files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
            MediaFile mediaFile = files.get(0);
            addModifyLeaveViewModel.setAttachment(mediaFile);
            Glide.with(requireContext())
                    .load(mediaFile.getUri())
                    .centerInside()
                    .priority(Priority.IMMEDIATE)
                    .centerCrop()
                    .placeholder(R.drawable.ic_file)
                    .into(getViewDataBinding().ivFilePreview);
            getViewDataBinding().tvFileName.setText(mediaFile.getName());

            getViewDataBinding().cvAttachment.setOnClickListener(v -> {
                if (mediaFile.getMimeType().contains("image")) {
                    ImagePreviewActivity.start(requireContext(), mediaFile.getUri().toString());
                } else {
                    AppUtility.openPdf(mediaFile.getUri(), requireContext(), getViewDataBinding().getRoot());
                }
            });
        }
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
