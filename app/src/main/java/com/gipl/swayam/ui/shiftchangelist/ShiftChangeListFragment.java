package com.gipl.swayam.ui.shiftchangelist;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gipl.swayam.BR;
import com.gipl.swayam.R;
import com.gipl.swayam.data.model.api.shiftchangelist.Scr;
import com.gipl.swayam.databinding.FragmentShiftChangeListBinding;
import com.gipl.swayam.exceptions.ErrorMessageFactory;
import com.gipl.swayam.ui.base.BaseFragment;
import com.gipl.swayam.ui.model.Response;
import com.gipl.swayam.ui.shiftchangelist.adapter.ShiftChangeListAdapter;
import com.gipl.swayam.uility.AppUtility;
import com.gipl.swayam.uility.DialogUtility;

import java.util.ArrayList;

import javax.inject.Inject;

public class ShiftChangeListFragment extends BaseFragment<FragmentShiftChangeListBinding, ShiftChangeListViewModel> {
    @Inject
    ShiftChangeListViewModel viewModel;
    private ShiftChangeListAdapter shiftChangeListAdapter;

    @Override
    public int getBindingVariable() {
        return BR.shiftChange;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_shift_change_list;
    }

    @Override
    public ShiftChangeListViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.getShiftChangeList();
        shiftChangeListAdapter = new ShiftChangeListAdapter();
        viewModel.getResponseMutableLiveData().observe(this, this::processResponse);
        getParentFragmentManager().setFragmentResultListener(AppUtility.INTENT_EXTRA.KEY_FRAG_LIST_RESULT, this, (s, bundle) -> viewModel.getShiftChangeList());
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().pullDown.setRefreshing(false);

        getViewDataBinding().rvScr.setLayoutManager(new LinearLayoutManager(requireContext()));
        getViewDataBinding().rvScr.setAdapter(shiftChangeListAdapter);

        getViewDataBinding().fabAdd.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_shiftChangeListFragment_to_shiftChangeFragment);
        });
        getViewDataBinding().pullDown.setOnRefreshListener(() -> {
            getViewDataBinding().pullDown.setRefreshing(false);
            viewModel.getShiftChangeList();
        });
        getViewDataBinding().rvScr.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getViewDataBinding().rvScr.getLayoutManager();
                if (linearLayoutManager != null) {
                    int firstVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    getViewDataBinding().pullDown.setEnabled(firstVisibleItem == 0);
                }
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
                shiftChangeListAdapter.addItems((ArrayList<Scr>) response.data);
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
