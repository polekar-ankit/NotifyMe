package com.gipl.notifyme.ui.checkin;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gipl.notifyme.BR;
import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.lib.Shifts;
import com.gipl.notifyme.databinding.ActivityCheckInBinding;
import com.gipl.notifyme.exceptions.ErrorMessageFactory;
import com.gipl.notifyme.ui.base.BaseActivity;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.DialogUtility;

import org.json.JSONException;

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
    public String getScreenName() {
        return CheckInActivity.class.getSimpleName();
    }

    @Override
    public CheckInViewModel getViewModel() {
        return checkInViewModel;
    }

    private void processShift(ArrayList<Shifts> shifts) {
        shifts.add(0, new Shifts("Select"));
        ArrayAdapter<Shifts> shiftsArrayAdapter = new ArrayAdapter<>(this, R.layout.layout_spinner_item, shifts);
        getViewDataBinding().spinner.setAdapter(shiftsArrayAdapter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle(R.string.activity_title_check_in);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkInViewModel.getShiftLiveData().observe(this, this::processShift);
        checkInViewModel.getResponseMutableLiveData().observe(this, this::processResponse);

        mScannerView = new ZBarScannerView(this);

        getViewDataBinding().btnCheckIn.setEnabled(false);
        getViewDataBinding().contentFrame.addView(mScannerView);

        getViewDataBinding().btnRescan.setOnClickListener(v -> {
            getViewDataBinding().btnRescan.setEnabled(false);
            getViewDataBinding().btnCheckIn.setEnabled(false);
            getViewDataBinding().tvScanBarcodeError.setVisibility(View.VISIBLE);
            mScannerView.resumeCameraPreview(CheckInActivity.this);
        });
        getViewDataBinding().btnCheckIn.setOnClickListener(v -> {
            Shifts shifts = (Shifts) getViewDataBinding().spinner.getSelectedItem();
            checkInViewModel.checkIn(shifts.getSuidShift());
        });
        getViewDataBinding().spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    checkInViewModel.setShiftError(getString(R.string.shift_not_select_error));

                } else
                    checkInViewModel.setShiftError("");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void processResponse(Response response) {
        switch (response.status) {
            case LOADING:
                showLoading();
                break;
            case SUCCESS:
                hideLoading();
                finish();
                break;
            case ERROR:
                hideLoading();
                DialogUtility.showSnackbar(getViewDataBinding().getRoot(), ErrorMessageFactory.create(this, (Exception) response.error));
                break;
        }
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
        getViewDataBinding().btnCheckIn.setEnabled(true);
        getViewDataBinding().tvScanBarcodeError.setVisibility(View.GONE);
//        Toast.makeText(this, "Contents = " + rawResult.getContents() +
//                ", Format = " + rawResult.getBarcodeFormat().getName(), Toast.LENGTH_SHORT).show();

        try {
            checkInViewModel.setScanBarcode(rawResult.getContents());
        } catch (JSONException e) {
            DialogUtility.showToast(this, getString(R.string.invalid_barcode_scan));
        }

    }
}
