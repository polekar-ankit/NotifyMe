package com.gipl.notifyme.ui.shiftchangelist;

import android.os.Bundle;
import android.os.Parcel;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gipl.notifyme.BR;
import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.shiftchangelist.Scr;
import com.gipl.notifyme.databinding.FragmentShiftChangeListBinding;
import com.gipl.notifyme.exceptions.ErrorMessageFactory;
import com.gipl.notifyme.ui.base.BaseFragment;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.ui.shiftchangelist.adapter.ShiftChangeListAdapter;
import com.gipl.notifyme.uility.AppUtility;
import com.gipl.notifyme.uility.DialogUtility;
import com.gipl.notifyme.uility.IFragmentListener;

import java.util.ArrayList;

import javax.inject.Inject;

public class ShiftChangeListFragment extends BaseFragment<FragmentShiftChangeListBinding, ShiftChangeListViewModel> {
    @Inject
    ShiftChangeListViewModel viewModel;
    private ShiftChangeListAdapter shiftChangeListAdapter;
    private IFragmentListener iFragmentListener = new IFragmentListener() {
        @Override
        public void onActivityResult(Bundle bundle) {
            viewModel.getShiftChangeList();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }
    };

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
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().pullDown.setRefreshing(false);

        getViewDataBinding().rvScr.setLayoutManager(new LinearLayoutManager(requireContext()));
        getViewDataBinding().rvScr.setAdapter(shiftChangeListAdapter);

        getViewDataBinding().fabAdd.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(AppUtility.INTENT_EXTRA.KEY_FRAG_LIST_RESULT, iFragmentListener);
            Navigation.findNavController(v).navigate(R.id.action_shiftChangeListFragment_to_shiftChangeFragment, bundle);
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