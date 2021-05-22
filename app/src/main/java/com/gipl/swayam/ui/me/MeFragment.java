package com.gipl.swayam.ui.me;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.gipl.imagepicker.ImagePicker;
import com.gipl.imagepicker.ImagePickerDialog;
import com.gipl.imagepicker.ImageResult;
import com.gipl.imagepicker.PickerConfiguration;
import com.gipl.imagepicker.PickerResult;
import com.gipl.swayam.BR;
import com.gipl.swayam.R;
import com.gipl.swayam.databinding.FragmentMeBinding;
import com.gipl.swayam.exceptions.ErrorMessageFactory;
import com.gipl.swayam.ui.base.BaseFragment;
import com.gipl.swayam.ui.checkout.CheckOutDialog;
import com.gipl.swayam.ui.image.ImagePreviewActivity;
import com.gipl.swayam.ui.model.Response;
import com.gipl.swayam.uility.DialogUtility;

import javax.inject.Inject;

public class MeFragment extends BaseFragment<FragmentMeBinding, MeViewModel> {
    @Inject
    MeViewModel meViewModel;


    @Override
    public int getBindingVariable() {
        return BR.me;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    public MeViewModel getViewModel() {
        return meViewModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckOutDialog checkOutDialog = new CheckOutDialog(requireContext(), meViewModel.getDataManager().getUtility().getCheckOutType());
        checkOutDialog.getCheckOutTypeLiveData().observe(this, this::processCheckOut);
        meViewModel.getResponseMutableLiveData().observe(this, this::processReponse);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar
        int id = item.getItemId();
        if (id == R.id.menu_item_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle(R.string.title_logout);
            builder.setMessage(R.string.msg_logout);
            builder.setPositiveButton(R.string.btn_logout, (dialog, which) -> {
                dialog.dismiss();
                meViewModel.logout();
            });
            builder.setNegativeButton(R.string.btn_cancel, (dialog, which) -> dialog.dismiss());
            builder.create().show();

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStop() {
        super.onStop();
        meViewModel.endTimer();
        meViewModel.removeEmpCheckInStatusListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        meViewModel.setCheckInTimer();
        meViewModel.setEmpCheckInStatusListener();
    }

    private void processReponse(Response response) {
        switch (response.status) {
            case LOADING:
                showLoading();
                break;
            case SUCCESS:
                hideLoading();
                if (response.data instanceof Integer)
                    DialogUtility.showSnackbar(getViewDataBinding().getRoot(), getString((Integer) response.data));
                if (response.data instanceof String) {
                    Navigation.findNavController(getViewDataBinding().cardView).navigate(R.id.action_nav_user_to_loginActivity);
                    getBaseActivity().finish();
                }

                break;
            case ERROR:
                hideLoading();
                DialogUtility.showToast(requireContext(), ErrorMessageFactory.create(requireContext(), (Exception) response.error));
                break;
        }
    }

    private void processCheckOut(Integer type) {
        meViewModel.checkOut(type);
    }

    ImagePickerDialog imagePickerDialog;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);


        PickerConfiguration pickerConfiguration = PickerConfiguration.build();
        pickerConfiguration.setSetCustomDialog(true)
                .setIconColor(Color.parseColor("#ed1c24"))
                .setTextColor(Color.parseColor("#7A1015"))
                .setImagePickerResult(new PickerResult() {
                    @Override
                    public void onImageGet(ImageResult imageResult) {
                        super.onImageGet(imageResult);
                        meViewModel.setEmpImage(imageResult);
                    }

                    @Override
                    public void onError(ImagePicker.ImageErrors imageErrors) {
                        super.onError(imageErrors);
                    }
                });

        meViewModel.getDashboardCount();


        getViewDataBinding().ivEdit.setOnClickListener(v -> {
            if (imagePickerDialog != null)
                imagePickerDialog.dismiss();
            imagePickerDialog = ImagePickerDialog.display(getChildFragmentManager(), pickerConfiguration);
        });

        getViewDataBinding().ivUserImg.setOnClickListener(v -> ImagePreviewActivity.start(requireContext(), meViewModel.getEmpImage().get(),true));

        getViewDataBinding().btnPunchingSlip.setOnClickListener(v -> {
            getBaseActivity().getmFirebaseAnalytics().setUserProperty("slip", getViewDataBinding().btnPunchingSlip.getText().toString());
            Bundle param = new Bundle();
            param.putString("name", getViewDataBinding().btnPunchingSlip.getText().toString());

            getBaseActivity().getmFirebaseAnalytics().logEvent("button_click", param);

            Navigation.findNavController(view).navigate(R.id.action_nav_user_to_missPunchListFragment);
        });
        getViewDataBinding().btnAddOvertime.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_nav_user_to_overtimeListFragment);
            Bundle param = new Bundle();
            param.putString("name", getViewDataBinding().btnAddOvertime.getText().toString());
            getBaseActivity().getmFirebaseAnalytics().logEvent("button_click", param);
        });
        getViewDataBinding().btnApplyLeave.setOnClickListener(v -> {
            getBaseActivity().getmFirebaseAnalytics().setUserProperty("slip", getViewDataBinding().btnApplyLeave.getText().toString());
            Bundle param = new Bundle();
            param.putString("name", getViewDataBinding().btnApplyLeave.getText().toString());
            getBaseActivity().getmFirebaseAnalytics().logEvent("button_click", param);
            Navigation.findNavController(v).navigate(R.id.action_nav_user_to_leaveListFragment2);
        });

        getViewDataBinding().btnShiftChange.setOnClickListener(v -> {
            getBaseActivity().getmFirebaseAnalytics().setUserProperty("slip", getViewDataBinding().btnShiftChange.getText().toString());
            Bundle param = new Bundle();
            param.putString("name", getViewDataBinding().btnShiftChange.getText().toString());
            getBaseActivity().getmFirebaseAnalytics().logEvent("button_click", param);

            Navigation.findNavController(v).navigate(R.id.action_nav_user_to_shiftChangeListFragment);
        });
        getViewDataBinding().btnAddCo.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_nav_user_to_coListFragment);
            Bundle param = new Bundle();
            param.putString("name", getViewDataBinding().btnAddCo.getText().toString());
            getBaseActivity().getmFirebaseAnalytics().logEvent("button_click", param);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePickerDialog.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePickerDialog.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
