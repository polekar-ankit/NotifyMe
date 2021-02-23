package com.gipl.notifyme.ui.changelng;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.gipl.notifyme.BR;
import com.gipl.notifyme.R;
import com.gipl.notifyme.databinding.FragmentChangeLanguageBinding;
import com.gipl.notifyme.ui.base.BaseFragment;

import java.util.Locale;

import javax.inject.Inject;

public class ChangeLanguageFragment extends BaseFragment<FragmentChangeLanguageBinding, ChangeLanguageViewModel> {
    public static final String marathiCode = "mr";
    public static final String englishCode = "en";
    @Inject
    ChangeLanguageViewModel viewModel;

    @Override
    public int getBindingVariable() {
        return BR.changeLanguage;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_change_language;
    }

    @Override
    public ChangeLanguageViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String lanCode = viewModel.getLanguageCode();
        if (lanCode.equals(marathiCode)) {
            getViewDataBinding().rbMarathi.setChecked(true);
        } else
            getViewDataBinding().rbEnglish.setChecked(true);

        getViewDataBinding().btnChangeLanguage.setOnClickListener(v -> {
            String code = englishCode;
            if (getViewDataBinding().rbMarathi.isChecked()) {
                code = marathiCode;
            }

            viewModel.setLanguageCode(code);
            if (getBaseActivity().updateBaseContextLocale(getBaseActivity().getApplicationContext()) != null)
                reloadActivity();
        });
    }

    private void reloadActivity() {
        getBaseActivity().finish();
        getBaseActivity().startActivity(getBaseActivity().getIntent());
    }
}
