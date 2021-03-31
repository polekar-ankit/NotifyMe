package com.gipl.swayam.ui.image;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;

import com.gipl.swayam.R;
import com.gipl.swayam.databinding.LayoutImagePreviewBinding;
import com.gipl.swayam.ui.base.BaseActivity;


import javax.inject.Inject;

public class ImagePreviewActivity extends BaseActivity<LayoutImagePreviewBinding,ImagePreviewViewModel> {

    private int position;
    private String url;
    private static final String KEY_POSITION = "position";
    private static final String KEY_IMAGE_URL = "imageUrl";

    @Inject
    ImagePreviewViewModel imagePreviewViewModel;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return  R.layout.layout_image_preview;
    }

    @Override
    public String getScreenName() {
        return ImagePreviewActivity.class.getSimpleName();
    }

    @Override
    public ImagePreviewViewModel getViewModel() {
        return imagePreviewViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        url = getIntent().getStringExtra(KEY_IMAGE_URL);
        //setActionBar(getViewDataBinding().toolbar,"");

        if (url != null){
            getViewModel().setImageUrl(url);
        }
    }

    public static void start(Context context, String uri) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra(KEY_IMAGE_URL,uri);
        context.startActivity(intent);
    }
}
