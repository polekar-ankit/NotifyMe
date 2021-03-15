package com.gipl.notifyme.ui.me;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.gipl.notifyme.BR;
import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.leavebalance.LeaveBalance;
import com.gipl.notifyme.databinding.FragmentMeBinding;
import com.gipl.notifyme.exceptions.ErrorMessageFactory;
import com.gipl.notifyme.ui.base.BaseFragment;
import com.gipl.notifyme.ui.checkout.CheckOutDialog;
import com.gipl.notifyme.ui.me.adapters.LeaveBalanceAdapter;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.DialogUtility;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import javax.inject.Inject;

public class MeFragment extends BaseFragment<FragmentMeBinding, MeViewModel> {
    @Inject
    MeViewModel meViewModel;
    private LeaveBalanceAdapter adapter;

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
        meViewModel.getLeaveBalanceLiveData().observe(this, this::processLeaveBalance);
    }

    private void processLeaveBalance(ArrayList<LeaveBalance> leaveBalances) {
        if (leaveBalances.size() > 0) {//other wise it will create 'IllegalArgumentException: Span count should be at least 1. Provided 0' exception
            getViewDataBinding().tvLeaveBalance.setVisibility(View.VISIBLE);
            getViewDataBinding().rvLeaveBalance.setLayoutManager(new GridLayoutManager(requireContext(), leaveBalances.size()));
            adapter.addItems(leaveBalances);
        }
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

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        adapter = new LeaveBalanceAdapter();
        meViewModel.getLeaveBalance();
//        getViewDataBinding().rvLeaveBalance.setLayoutManager(new GridLayoutManager(requireContext()));

//        SnapHelper snapHelper = new PagerSnapHelper();
//        snapHelper.attachToRecyclerView(getViewDataBinding().rvLeaveBalance);

        getViewDataBinding().rvLeaveBalance.setAdapter(adapter);
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


}
