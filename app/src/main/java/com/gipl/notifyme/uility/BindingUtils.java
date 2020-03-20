package com.gipl.notifyme.uility;

import android.widget.EditText;

import androidx.databinding.BindingAdapter;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public final class BindingUtils {

    private BindingUtils() {
        // This class is not publicly instantiable
    }


    @BindingAdapter("setSelection")
    public static void setSelection(EditText view, String value) {
        if (value != null && !value.isEmpty()) {
            view.setSelection(view.getText().length());
        }
    }

    @BindingAdapter("setError")
    public static void setError(TextInputLayout inputEditText, String error) {
        inputEditText.setError(error);
    }

}