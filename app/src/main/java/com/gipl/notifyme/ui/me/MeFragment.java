package com.gipl.notifyme.ui.me;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.gipl.notifyme.BR;
import com.gipl.notifyme.BuildConfig;
import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.lib.Shifts;
import com.gipl.notifyme.databinding.FragmentMeBinding;
import com.gipl.notifyme.ui.base.BaseFragment;

import java.util.ArrayList;

import javax.inject.Inject;

public class MeFragment extends BaseFragment<FragmentMeBinding,MeViewModel> {
    @Inject
    MeViewModel meViewModel;
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
        // Set title
        getBaseActivity().getSupportActionBar().setTitle(getString(R.string.activity_notification) + " - " + BuildConfig.VERSION_CODE + ".0");
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewDataBinding().btnCheckIn.setOnClickListener(v-> Navigation.findNavController(v).navigate(R.id.action_nav_user_to_checkInActivity));
    }


}
