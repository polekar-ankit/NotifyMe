package com.gipl.swayam.ui.changelng;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;


import com.gipl.swayam.BR;
import com.gipl.swayam.R;
import com.gipl.swayam.databinding.FragmentChangeLanguageBinding;
import com.gipl.swayam.ui.base.BaseFragment;

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
