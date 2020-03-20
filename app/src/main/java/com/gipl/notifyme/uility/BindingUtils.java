package com.gipl.notifyme.uility;

import android.widget.EditText;

import androidx.databinding.BindingAdapter;


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


}