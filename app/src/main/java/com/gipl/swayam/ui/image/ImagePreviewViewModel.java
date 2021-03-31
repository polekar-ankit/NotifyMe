package com.gipl.swayam.ui.image;

import androidx.databinding.ObservableField;

import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.ui.base.BaseViewModel;
import com.gipl.swayam.uility.rx.SchedulerProvider;


public class ImagePreviewViewModel extends BaseViewModel {

    private final ObservableField<String> imageUrl = new ObservableField<>();

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
