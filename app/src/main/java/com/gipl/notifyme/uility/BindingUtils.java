package com.gipl.notifyme.uility;

import android.content.Context;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.gipl.notifyme.BuildConfig;
import com.gipl.notifyme.R;
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

    @BindingAdapter("loadImage")
    public static void loadImage(ImageView imageView, String url) {
        Glide
                .with(imageView.getContext())
                .load(url)
                .priority(Priority.IMMEDIATE)
                .centerCrop()
                .placeholder(R.drawable.image_error)
                .into(imageView);
    }

    @BindingAdapter("loadImageWithProgress")
    public static void loadImageWithProgress(ImageView imageView, String url) {
        Context context = imageView.getContext();
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(8f);
        circularProgressDrawable.setCenterRadius(40f);
        circularProgressDrawable
                .setColorSchemeColors(ContextCompat.getColor(context, R.color.colorAccent),
                        ContextCompat.getColor(context, R.color.colorPrimary));
        circularProgressDrawable.setProgressRotation(1f);
        circularProgressDrawable.start();

        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(circularProgressDrawable)
                .error(R.drawable.image_error)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

    @BindingAdapter("app:setLinkLabel")
    public static void setLinkLabel(TextView view, String label) {
        String linkLabel = "";
        if (AppUtility.LINK_TYPE.IMAGE.equalsIgnoreCase(label)) {
            linkLabel = view.getContext().getString(R.string.label_open_image);
        }
        else if (AppUtility.LINK_TYPE.VIDEO.equalsIgnoreCase(label)) {
            linkLabel = view.getContext().getString(R.string.label_open_video);
        }

        view.setText(linkLabel);
    }

}