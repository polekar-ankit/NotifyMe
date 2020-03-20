package com.gipl.notifyme.ui.base;

import android.content.Context;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gipl.notifyme.data.DataManager;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.uility.NetworkUtils;
import com.gipl.notifyme.uility.rx.SchedulerProvider;

import io.reactivex.disposables.CompositeDisposable;


public abstract class BaseViewModel extends ViewModel {

    private final DataManager mDataManager;

    private final ObservableBoolean mIsLoading = new ObservableBoolean(false);

    private final SchedulerProvider mSchedulerProvider;
    private final MutableLiveData<Response> mResponseMutableLiveData = new MutableLiveData<>();
    private CompositeDisposable mCompositeDisposable;


    public BaseViewModel(DataManager dataManager,
                         SchedulerProvider schedulerProvider) {
        this.mDataManager = dataManager;
        this.mSchedulerProvider = schedulerProvider;
        this.mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onCleared() {
        mCompositeDisposable.dispose();
        super.onCleared();
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    public DataManager getDataManager() {
        return mDataManager;
    }

    public ObservableBoolean isLoading() {
        return mIsLoading;
    }

    public void setIsLoading(boolean isLoading) {
        mIsLoading.set(isLoading);
    }


    public SchedulerProvider getSchedulerProvider() {
        return mSchedulerProvider;
    }

    public MutableLiveData<Response> getResponseMutableLiveData() {
        return mResponseMutableLiveData;
    }


    public boolean isNetworkConnected(Context context) {
        return NetworkUtils.isNetworkConnected(context);
    }

}
