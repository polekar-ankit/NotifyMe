package com.gipl.notifyme.ui.addleave;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.AppUtility;
import com.gipl.notifyme.uility.DialogUtility;
import com.gipl.notifyme.uility.TimeUtility;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

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
                        DialogUtility.showToast(requireContext(), "Your Leave has been successfully applied");
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
        getViewDataBinding().cvAttachment.setVisibility(View.GONE);
        setLeaveFor();
        addModifyLeaveViewModel.getLeaveList();
        getViewDataBinding().tvFrom.setText(TimeUtility.getTodayOnlyDateInDisplayFormat());
        getViewDataBinding().tvTo.setText(TimeUtility.getTodayOnlyDateInDisplayFormat());

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
            LeaveFor leaveFor = (LeaveFor) getViewDataBinding().spinnerLeaveFor.getSelectedItem();
            LeaveApproval leaveApproval = (LeaveApproval) getViewDataBinding().spinnerLeaveType.getSelectedItem();

            addModifyLeaveViewModel.addModifyLeave(getViewDataBinding().tvFrom.getText().toString(),
                    getViewDataBinding().tvTo.getText().toString(), leaveFor, leaveApproval);
        });

        getViewDataBinding().btnMedicalCertificate.setOnClickListener(v -> showFilePickerOptions());

        getViewDataBinding().spinnerLeaveType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                LeaveApproval leave = (LeaveApproval) getViewDataBinding().spinnerLeaveType.getSelectedItem();
                if (leave.getSLeaveType().equals("SL")){
                    getViewDataBinding().btnMedicalCertificate.setVisibility(View.VISIBLE);
                }
                else {
                    getViewDataBinding().btnMedicalCertificate.setVisibility(View.GONE);
                    getViewDataBinding().cvAttachment.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
