package com.gipl.notifyme.ui.image;


import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class ImagePreviewModule {

    @Provides
    ImagePreviewViewModel providesImagePreviewViewModel(DataManager dataManager, SchedulerProvider schedulerProvider){
        return new ImagePreviewViewModel(dataManager,schedulerProvider);
    }
}
