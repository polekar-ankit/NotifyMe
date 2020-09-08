package com.gipl.notifyme.ui.me;

import android.os.Bundle;
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
import com.gipl.notifyme.ui.leavemain.LeaveMainActivity;
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
        getBaseActivity().getSupportActionBar().setTitle(getString(R.string.activity_notification) + " - " + BuildConfig.VERSION_CODE + ".0");
    }

    @Override
    public void onStop() {
        super.onStop();
        meViewModel.endTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        meViewModel.setCheckInTimer();
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
        getViewDataBinding().btnCheckIn.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_user_to_checkInActivity));
        getViewDataBinding().btnCheckOut.setOnClickListener(v -> checkOutDialog.show());
        getViewDataBinding().btnApplyLeave.setOnClickListener(v->{
            LeaveMainActivity.start(requireContext());
        });
    }


}
