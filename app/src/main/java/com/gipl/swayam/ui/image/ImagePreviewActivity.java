package com.gipl.swayam.ui.image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.navigation.Navigation;

import com.gipl.imagepicker.ImagePicker;
import com.gipl.imagepicker.ImagePickerDialog;
import com.gipl.imagepicker.exceptions.ImageErrors;
import com.gipl.imagepicker.listener.PickerResult;
import com.gipl.imagepicker.models.ImageResult;
import com.gipl.imagepicker.models.PickerConfiguration;
import com.gipl.imagepicker.resultwatcher.PickerResultObserver;
import com.gipl.swayam.R;
import com.gipl.swayam.databinding.LayoutImagePreviewBinding;
import com.gipl.swayam.exceptions.ErrorMessageFactory;
import com.gipl.swayam.ui.base.BaseActivity;
import com.gipl.swayam.ui.model.Response;
import com.gipl.swayam.uility.DialogUtility;


import javax.inject.Inject;

public class ImagePreviewActivity extends BaseActivity<LayoutImagePreviewBinding, ImagePreviewViewModel> {

    private int position;
    private String url;
    private static final String KEY_POSITION = "position";
    private static final String KEY_IMAGE_URL = "imageUrl";
    private static final String KEY_SHOW_EDIT = "showEdit";

    @Inject
    ImagePreviewViewModel imagePreviewViewModel;
    private ImagePickerDialog imagePickerDialog;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_image_preview;
    }

    @Override
    public String getScreenName() {
        return ImagePreviewActivity.class.getSimpleName();
    }

    @Override
    public ImagePreviewViewModel getViewModel() {
        return imagePreviewViewModel;
    }

    PickerConfiguration pickerConfiguration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imagePickerDialog = new ImagePickerDialog(this, getLifecycle(), new PickerResultObserver(getActivityResultRegistry()));

        getViewModel().getResponseMutableLiveData().observe(this, this::processReponse);

        url = getIntent().getStringExtra(KEY_IMAGE_URL);

        //setActionBar(getViewDataBinding().toolbar,"");

        pickerConfiguration = PickerConfiguration.build();
        pickerConfiguration.setSetCustomDialog(true)
                .setIconColor(Color.parseColor("#ed1c24"))
                .setTextColor(Color.parseColor("#7A1015"))
                .setImagePickerResult(new PickerResult() {
                    @Override
                    public void onImageGet(ImageResult imageResult) {
                        super.onImageGet(imageResult);
                        getViewModel().setEmpImage(imageResult);
                    }

                    @Override
                    public void onError(ImageErrors imageErrors) {
                        super.onError(imageErrors);
                    }
                });

        if (url != null) {
            getViewModel().setImageUrl(url);
        }
    }

    private void processReponse(Response response) {
        switch (response.status) {
            case LOADING:
                showLoading();
                break;
            case SUCCESS:
                hideLoading();
                break;
            case ERROR:
                hideLoading();
                if (response.error != null) {
                    DialogUtility.showToast(this, ErrorMessageFactory.create(this, (Exception) response.error));
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean isShowEdit = getIntent().getBooleanExtra(KEY_SHOW_EDIT, false);
        if (isShowEdit)
            getMenuInflater().inflate(R.menu.menu_image_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_edit) {
            if (imagePickerDialog != null) {
                imagePickerDialog.dismiss();
                imagePickerDialog.display(imagePickerDialog, getSupportFragmentManager(), pickerConfiguration);
            }
        }
        return super.onOptionsItemSelected(item);
    }



    public static void start(Context context, String uri, boolean isShowEdit) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra(KEY_IMAGE_URL, uri);
        intent.putExtra(KEY_SHOW_EDIT, isShowEdit);
        context.startActivity(intent);
    }
}
