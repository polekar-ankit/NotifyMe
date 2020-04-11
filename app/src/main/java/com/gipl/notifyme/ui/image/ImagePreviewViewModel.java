package com.gipl.notifyme.ui.image;

import androidx.databinding.ObservableField;
import android.net.Uri;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.ui.base.BaseViewModel;
import com.gipl.notifyme.uility.rx.SchedulerProvider;


public class ImagePreviewViewModel extends BaseViewModel {

    private ObservableField<String> imageUrl = new ObservableField<>();

    public ImagePreviewViewModel(DataManager dataManager, SchedulerProvider schedulerProvider) {
        super(dataManager, schedulerProvider);

    }

    public ObservableField<String> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl.set(imageUrl);
    }
}
