package com.gipl.notifyme.ui.me;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.gipl.notifyme.BR;
import com.gipl.notifyme.BuildConfig;
import com.gipl.notifyme.R;
import com.gipl.notifyme.databinding.FragmentMeBinding;
import com.gipl.notifyme.exceptions.ErrorMessageFactory;
import com.gipl.notifyme.ui.base.BaseFragment;
import com.gipl.notifyme.ui.checkout.CheckOutDialog;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.DialogUtility;

import javax.inject.Inject;

public class MeFragment extends BaseFragment<FragmentMeBinding, MeViewModel> {
    @Inject
    MeViewModel meViewModel;
    private CheckOutDialog checkOutDialog;

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
        checkOutDialog = new CheckOutDialog(requireContext(), meViewModel.getDataManager().getUtility().getCheckOutType());
        checkOutDialog.getCheckOutTypeLiveData().observe(this, this::processCheckOut);
        meViewModel.getResponseMutableLiveData().observe(this, this::processReponse);
        // Set title

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar
        int id = item.getItemId();
        if (id == R.id.menu_item_share) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",
                    requireContext().getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
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

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        getViewDataBinding().btnPunchingSlip.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_user_to_missPunchListFragment));
        getViewDataBinding().btnAddOvertime.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_user_to_overtimeListFragment));
        getViewDataBinding().btnApplyLeave.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_user_to_leaveListFragment2));

        getViewDataBinding().btnShiftChange.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_user_to_shiftChangeListFragment));
        getViewDataBinding().btnAddCo.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_user_to_coListFragment));
    }


}
