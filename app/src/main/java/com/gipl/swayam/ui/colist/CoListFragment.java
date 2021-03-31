package com.gipl.swayam.ui.colist;

import android.os.Bundle;
import android.os.Parcel;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gipl.swayam.BR;
import com.gipl.swayam.R;
import com.gipl.swayam.data.model.api.colist.CO;
import com.gipl.swayam.databinding.FragmentCoListBinding;
import com.gipl.swayam.exceptions.ErrorMessageFactory;
import com.gipl.swayam.ui.base.BaseFragment;
import com.gipl.swayam.ui.colist.adapters.CoListAdapter;
import com.gipl.swayam.ui.model.Response;
import com.gipl.swayam.uility.AppUtility;
import com.gipl.swayam.uility.DialogUtility;
import com.gipl.swayam.uility.IFragmentListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CoListFragment extends BaseFragment<FragmentCoListBinding, CoListViewModel> {
    @Inject
    CoListViewModel viewModel;
    private CoListAdapter coListAdapter;

    private final IFragmentListener iFragmentListener = new IFragmentListener() {
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }

        @Override
        public void onActivityResult(Bundle bundle) {
            viewModel.getCOList();
        }
    };

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
        coListAdapter = new CoListAdapter();
        viewModel.getCOList();
        viewModel.getResponseMutableLiveData().observe(this, this::processResponse);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().rvCo.setAdapter(coListAdapter);
        getViewDataBinding().rvCo.setLayoutManager(new LinearLayoutManager(requireContext()));

        getViewDataBinding().pullDown.setRefreshing(false);
        getViewDataBinding().pullDown.setOnRefreshListener(() -> {
            getViewDataBinding().pullDown.setRefreshing(false);
            viewModel.getCOList();
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
            Bundle bundle = new Bundle();
            bundle.putParcelable(AppUtility.INTENT_EXTRA.KEY_FRAG_LIST_RESULT, iFragmentListener);
            Navigation.findNavController(v).navigate(R.id.action_coListFragment_to_addCoFragment, bundle);
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