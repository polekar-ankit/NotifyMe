package com.gipl.swayam.ui.image;


import com.gipl.swayam.data.DataManager;
import com.gipl.swayam.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class ImagePreviewModule {

    @Provides
    ImagePreviewViewModel providesImagePreviewViewModel(DataManager dataManager, SchedulerProvider schedulerProvider){
        return new ImagePreviewViewModel(dataManager,schedulerProvider);
    }
}
