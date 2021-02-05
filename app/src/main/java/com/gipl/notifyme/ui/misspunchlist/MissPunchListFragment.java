package com.gipl.notifyme.ui.misspunchlist;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;

import com.gipl.notifyme.BR;
import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.mispunchlist.LiMissPunch;
import com.gipl.notifyme.databinding.FragmentMissPunchListBinding;
import com.gipl.notifyme.exceptions.ErrorMessageFactory;
import com.gipl.notifyme.ui.base.BaseFragment;
import com.gipl.notifyme.ui.misspunchlist.adapter.MissPunchListAdapter;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.DialogUtility;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MissPunchListFragment extends BaseFragment<FragmentMissPunchListBinding, MissPunchListViewModel> {
    @Inject
    MissPunchListViewModel viewModel;
    private MissPunchListAdapter missPunchListAdapter;

    @Override
    public int getBindingVariable() {
        return BR.list;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_miss_punch_list;
    }

    @Override
    public MissPunchListViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.getResponseMutableLiveData().observe(this, this::processResponse);
    }

    private void processResponse(Response response) {
        switch (response.status) {
            case LOADING:
                showLoading();
                break;
            case SUCCESS:
                hideLoading();
                List<LiMissPunch> liMissPunchList = (List<LiMissPunch>) response.data;
                missPunchListAdapter.addItems((ArrayList<LiMissPunch>) liMissPunchList);
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

        missPunchListAdapter = new MissPunchListAdapter();
        getViewDataBinding().rvMissPunch.setLayoutManager(new LinearLayoutManager(requireContext()));
        getViewDataBinding().rvMissPunch.setAdapter(missPunchListAdapter);

        viewModel.getMissPunchList();
        getViewDataBinding().pullDown.setRefreshing(false);

        getViewDataBinding().fabAdd.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_missPunchListFragment_to_punchingSlipFragment));
        getViewDataBinding().pullDown.setOnRefreshListener(() -> {
            getViewDataBinding().pullDown.setRefreshing(false);
            viewModel.getMissPunchList();
        });
        getViewDataBinding().rvMissPunch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getViewDataBinding().rvMissPunch.getLayoutManager();
                if (linearLayoutManager != null) {
                    int firstVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    getViewDataBinding().pullDown.setEnabled(firstVisibleItem == 0);
                }
            }
        });
    }
}
