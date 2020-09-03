package com.gipl.notifyme.ui.checkin;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gipl.notifyme.BR;
import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.lib.Shifts;
import com.gipl.notifyme.databinding.ActivityCheckInBinding;
import com.gipl.notifyme.ui.base.BaseActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class CheckInActivity extends BaseActivity<ActivityCheckInBinding, CheckInViewModel> implements ZBarScannerView.ResultHandler {
    private static final int CAMERA_PERMISSION = 1242;
    @Inject
    CheckInViewModel checkInViewModel;
    private ZBarScannerView mScannerView;

    @Override
    public int getBindingVariable() {
        return BR.checkIn;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_check_in;
    }

    @Override
    public CheckInViewModel getViewModel() {
        return checkInViewModel;
    }

    private void processShift(ArrayList<Shifts> shifts) {
        ArrayAdapter<Shifts> shiftsArrayAdapter = new ArrayAdapter<>(this, R.layout.layout_spinner_item, shifts);
        getViewDataBinding().spinner.setAdapter(shiftsArrayAdapter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle(R.string.activity_title_check_in);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkInViewModel.getShiftLiveData().observe(this, this::processShift);

        mScannerView = new ZBarScannerView(this);
        getViewDataBinding().contentFrame.addView(mScannerView);
        getViewDataBinding().btnRescan.setOnClickListener(v -> {
            getViewDataBinding().btnRescan.setEnabled(false);
            mScannerView.resumeCameraPreview(CheckInActivity.this);
        });
        getViewDataBinding().btnCheckIn.setOnClickListener(v -> {
            Shifts shifts = (Shifts) getViewDataBinding().spinner.getSelectedItem();
            checkInViewModel.checkIn(shifts.getSuidShift());
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mScannerView.setResultHandler(this);
                mScannerView.startCamera();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getViewDataBinding().btnRescan.setEnabled(false);
        if (hasPermission(Manifest.permission.CAMERA)) {
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        } else {
            requestPermissionsSafely(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        getViewDataBinding().btnRescan.setEnabled(true);
        Toast.makeText(this, "Contents = " + rawResult.getContents() +
                ", Format = " + rawResult.getBarcodeFormat().getName(), Toast.LENGTH_SHORT).show();
    }
}
