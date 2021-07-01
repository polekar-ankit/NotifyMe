package com.gipl.swayam.ui.leavelist;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gipl.swayam.BR;
import com.gipl.swayam.R;
import com.gipl.swayam.data.model.api.leavebalance.LeaveBalance;
import com.gipl.swayam.data.model.api.leaves.LeaveRequest;
import com.gipl.swayam.databinding.FragmentLeaveListBinding;
import com.gipl.swayam.exceptions.ErrorMessageFactory;
import com.gipl.swayam.ui.base.BaseFragment;
import com.gipl.swayam.ui.leavelist.adapter.LeaveBalanceAdapter;
import com.gipl.swayam.ui.leavelist.adapter.LeaveRequestListAdapter;
import com.gipl.swayam.ui.model.Response;
import com.gipl.swayam.uility.AppUtility;
import com.gipl.swayam.uility.DialogUtility;

import java.util.ArrayList;

import javax.inject.Inject;

public class LeaveListFragment extends BaseFragment<FragmentLeaveListBinding, LeaveListViewModel> {
    @Inject
    LeaveListViewModel leaveListViewModel;
    LeaveRequestListAdapter leaveRequestListAdapter;
    private LeaveBalanceAdapter adapter;


    @Override
    public int getBindingVariable() {
        return BR.leaveList;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_leave_list;
    }

    @Override
    public LeaveListViewModel getViewModel() {
        return leaveListViewModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        leaveListViewModel.getLeaveList();
        leaveRequestListAdapter = new LeaveRequestListAdapter();
        leaveListViewModel.getResponseMutableLiveData().observe(this, this::processLiveData);
        leaveListViewModel.getLeaveBalanceLiveData().observe(this, this::processLeaveBalance);
        getParentFragmentManager().setFragmentResultListener(AppUtility.INTENT_EXTRA.KEY_FRAG_LIST_RESULT, this, (s, bundle) -> leaveListViewModel.getLeaveList());
    }

    private void processLeaveBalance(ArrayList<LeaveBalance> leaveBalances) {
        if (leaveBalances.size() > 0) {//other wise it will create 'IllegalArgumentException: Span count should be at least 1. Provided 0' exception
            getViewDataBinding().tvEmptyLeave.setVisibility(View.GONE);
            getViewDataBinding().tvLeaveBalance.setVisibility(View.VISIBLE);
            getViewDataBinding().rvLeaveBalance.setLayoutManager(new GridLayoutManager(requireContext(), leaveBalances.size()));
            adapter.addItems(leaveBalances);
        } else {
            getViewDataBinding().tvEmptyLeave.setVisibility(View.VISIBLE);
        }
    }

    private void processLiveData(Response response) {
        switch (response.status) {
            case LOADING:
                showLoading();
                break;
            case SUCCESS:
                hideLoading();
                if (response.data instanceof Boolean)
                    break;
                leaveRequestListAdapter.addItems((ArrayList<LeaveRequest>) response.data);
                break;
            case ERROR:
                hideLoading();
                if (response.error != null) {
                    DialogUtility.showSnackbar(getViewDataBinding().getRoot(), ErrorMessageFactory.create(requireContext(), (Exception) response.error));
                }
                break;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new LeaveBalanceAdapter();
        leaveListViewModel.getLeaveBalance();

        getViewDataBinding().rvLeaveBalance.setAdapter(adapter);

        getViewDataBinding().pullDown.setRefreshing(false);
        getViewDataBinding().recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        getViewDataBinding().recyclerView.setAdapter(leaveRequestListAdapter);

        getViewDataBinding().pullDown.setOnRefreshListener(() -> {
            getViewDataBinding().pullDown.setRefreshing(false);
            leaveListViewModel.getLeaveList();
        });
        getViewDataBinding().fabAdd.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_leaveListFragment2_to_addModifyLeaveFragment2));
        getViewDataBinding().recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getViewDataBinding().recyclerView.getLayoutManager();
                if (linearLayoutManager != null) {
                    int firstVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    getViewDataBinding().pullDown.setEnabled(firstVisibleItem == 0);
                }
            }
        });
    }
}
