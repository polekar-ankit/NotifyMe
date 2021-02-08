package com.gipl.notifyme.ui.otlist;

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
import com.gipl.notifyme.data.model.api.overtimelist.OT;
import com.gipl.notifyme.databinding.FragmentOtListBinding;
import com.gipl.notifyme.exceptions.ErrorMessageFactory;
import com.gipl.notifyme.ui.base.BaseFragment;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.ui.otlist.adapter.OvertimeListAdapter;
import com.gipl.notifyme.uility.AppUtility;
import com.gipl.notifyme.uility.DialogUtility;
import com.gipl.notifyme.uility.IFragmentListener;

import java.util.ArrayList;

import javax.inject.Inject;

public class OvertimeListFragment extends BaseFragment<FragmentOtListBinding, OvertimeListViewModel> {
    @Inject
    OvertimeListViewModel viewModel;
    private OvertimeListAdapter overtimeListAdapter;
    private IFragmentListener iFragmentListener = new IFragmentListener() {
        @Override
        public void onActivityResult(Bundle bundle) {
            viewModel.getOtList();
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
        return BR.otList;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_ot_list;
    }

    @Override
    public OvertimeListViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overtimeListAdapter = new OvertimeListAdapter();
        viewModel.getOtList();
        viewModel.getResponseMutableLiveData().observe(this, this::processResponse);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getViewDataBinding().rvOt.setAdapter(overtimeListAdapter);
        getViewDataBinding().rvOt.setLayoutManager(new LinearLayoutManager(requireContext()));
        getViewDataBinding().pullDown.setRefreshing(false);

        getViewDataBinding().fabAdd.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(AppUtility.INTENT_EXTRA.KEY_FRAG_LIST_RESULT, iFragmentListener);
            Navigation.findNavController(v).navigate(R.id.action_overtimeListFragment_to_addOverTimeFragment, bundle);
        });
        getViewDataBinding().pullDown.setOnRefreshListener(() -> {
            getViewDataBinding().pullDown.setRefreshing(false);
            viewModel.getOtList();
        });
        getViewDataBinding().rvOt.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getViewDataBinding().rvOt.getLayoutManager();
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
                overtimeListAdapter.addItems((ArrayList<OT>) response.data);
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
