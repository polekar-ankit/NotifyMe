package com.gipl.notifyme.ui.colist;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gipl.notifyme.BR;
import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.colist.CO;
import com.gipl.notifyme.data.model.api.mispunchlist.LiMissPunch;
import com.gipl.notifyme.databinding.FragmentCoListBinding;
import com.gipl.notifyme.exceptions.ErrorMessageFactory;
import com.gipl.notifyme.ui.base.BaseFragment;
import com.gipl.notifyme.ui.colist.adapters.CoListAdapter;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.DialogUtility;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CoListFragment extends BaseFragment<FragmentCoListBinding, CoListViewModel> {
    @Inject
    CoListViewModel viewModel;
    private CoListAdapter coListAdapter;

    @Override
    public int getBindingVariable() {
        return BR.co;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_co_list;
    }

    @Override
    public CoListViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.getResponseMutableLiveData().observe(this, this::processResponse);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getCOList();
        coListAdapter = new CoListAdapter();
        getViewDataBinding().rvCo.setAdapter(coListAdapter);
        getViewDataBinding().rvCo.setLayoutManager(new LinearLayoutManager(requireContext()));

        getViewDataBinding().pullDown.setRefreshing(false);
        getViewDataBinding().pullDown.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getViewDataBinding().pullDown.setRefreshing(false);
                viewModel.getCOList();
            }
        });

        getViewDataBinding().rvCo.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getViewDataBinding().rvCo.getLayoutManager();
                if (linearLayoutManager != null) {
                    int firstVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    getViewDataBinding().pullDown.setEnabled(firstVisibleItem == 0);
                }
            }
        });

        getViewDataBinding().fabAdd.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_coListFragment_to_addCoFragment);
        });
    }

    private void processResponse(Response response) {
        switch (response.status) {
            case LOADING:
                showLoading();
                break;
            case SUCCESS:
                hideLoading();
                List<CO> liMissPunchList = (List<CO>) response.data;
                coListAdapter.addItems((ArrayList<CO>) liMissPunchList);
                break;
            case ERROR:
                hideLoading();
                if (response.error != null) {
                    DialogUtility.showSnackbar(getViewDataBinding().getRoot(), ErrorMessageFactory.create(requireContext(), (Exception) response.error));
                }
                break;
        }
    }
}
