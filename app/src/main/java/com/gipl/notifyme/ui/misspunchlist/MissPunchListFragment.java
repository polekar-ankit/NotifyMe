package com.gipl.notifyme.ui.misspunchlist;

import android.os.Bundle;
import android.os.Parcel;
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
import com.gipl.notifyme.uility.AppUtility;
import com.gipl.notifyme.uility.DialogUtility;
import com.gipl.notifyme.uility.IFragmentListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MissPunchListFragment extends BaseFragment<FragmentMissPunchListBinding, MissPunchListViewModel> {
    @Inject
    MissPunchListViewModel viewModel;
    private MissPunchListAdapter missPunchListAdapter;
    private IFragmentListener iFragmentListener = new IFragmentListener() {
        @Override
        public void onActivityResult(Bundle bundle) {
            viewModel.getMissPunchList();
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
        missPunchListAdapter = new MissPunchListAdapter();
        viewModel.getMissPunchList();
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

        getViewDataBinding().rvMissPunch.setLayoutManager(new LinearLayoutManager(requireContext()));
        getViewDataBinding().rvMissPunch.setAdapter(missPunchListAdapter);

        getViewDataBinding().pullDown.setRefreshing(false);

        getViewDataBinding().fabAdd.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(AppUtility.INTENT_EXTRA.KEY_FRAG_LIST_RESULT, iFragmentListener);
            Navigation.findNavController(v).navigate(R.id.action_missPunchListFragment_to_punchingSlipFragment, bundle);
        });
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
