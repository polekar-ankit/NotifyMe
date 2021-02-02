package com.gipl.notifyme.ui.leavelist;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gipl.notifyme.BR;
import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.leaves.LeaveRequest;
import com.gipl.notifyme.databinding.FragmentLeaveListBinding;
import com.gipl.notifyme.exceptions.ErrorMessageFactory;
import com.gipl.notifyme.ui.base.BaseFragment;
import com.gipl.notifyme.ui.leavelist.adapter.LeaveRequestListAdapter;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.DialogUtility;

import java.util.ArrayList;

import javax.inject.Inject;

public class LeaveListFragment extends BaseFragment<FragmentLeaveListBinding,LeaveListViewModel> {
    @Inject
    LeaveListViewModel leaveListViewModel;
    LeaveRequestListAdapter leaveRequestListAdapter;

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
        leaveListViewModel.getResponseMutableLiveData().observe(this,this::processLiveData);
    }

    private void processLiveData(Response response) {
        switch (response.status){
            case LOADING:
                showLoading();
                break;
            case SUCCESS:
                hideLoading();
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
        leaveListViewModel.getLeaveList();

        leaveRequestListAdapter= new LeaveRequestListAdapter();
        getViewDataBinding().recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        getViewDataBinding().recyclerView.setAdapter(leaveRequestListAdapter);

        getViewDataBinding().fabAdd.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_leaveListFragment2_to_addModifyLeaveFragment2);
        });
    }
}
