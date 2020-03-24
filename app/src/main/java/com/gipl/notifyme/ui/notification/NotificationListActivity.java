package com.gipl.notifyme.ui.notification;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gipl.notifyme.BR;
import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.notification.GetNotificationRes;
import com.gipl.notifyme.data.model.api.notification.Notification;
import com.gipl.notifyme.data.model.api.sendotp.User;
import com.gipl.notifyme.databinding.LayoutNotificationListBinding;
import com.gipl.notifyme.exceptions.ErrorMessageFactory;
import com.gipl.notifyme.ui.base.BaseActivity;
import com.gipl.notifyme.ui.base.BaseAdapter;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.ui.notification.adapter.NotificationListAdapter;
import com.gipl.notifyme.ui.otpverify.OtpVerifyActivity;
import com.gipl.notifyme.uility.AppUtility;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import javax.inject.Inject;

public class NotificationListActivity extends BaseActivity<LayoutNotificationListBinding, NotificationListViewModel> {
    @Inject
    NotificationListViewModel notificationListViewModel;
    NotificationListAdapter adapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, NotificationListActivity.class);
        context.startActivity(intent);
        ((BaseActivity) context).finish();
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_notification_list;
    }

    @Override
    public NotificationListViewModel getViewModel() {
        return notificationListViewModel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Observe changes in response handled by view model
        getViewModel().getResponseMutableLiveData().observe(this, this::processResponse);

        //setup list view
        setListLayout(getViewDataBinding().rvNotifications);

        // Create list adapter
        adapter = new NotificationListAdapter();

        // set adapter
        setListAdapter(getViewDataBinding().rvNotifications, new ArrayList<>(), adapter);

        //call api to get list of announcements
        getViewModel().getAllNotifications();
        getViewDataBinding().pullDown.setEnabled(true);
        getViewDataBinding().pullDown.setOnRefreshListener(() -> {
            getViewDataBinding().pullDown.setRefreshing(false);
            adapter.clear();
            getViewModel().getAllNotifications();
        });

        // pull dpwn refresh
        getViewDataBinding().rvNotifications.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getViewDataBinding().rvNotifications.getLayoutManager();
                if (linearLayoutManager != null) {
                    int firstItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    if (firstItem != 0) {
                        getViewDataBinding().pullDown.setEnabled(false);
                    } else {
                        getViewDataBinding().pullDown.setEnabled(true);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_item_share:
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void processResponse(Response response) {
        switch (response.status) {
            case LOADING:
                showLoading();
                break;
            case SUCCESS:
                hideLoading();
                if (response.data instanceof GetNotificationRes) {
                    GetNotificationRes res = (GetNotificationRes) response.data;
                    adapter.addItems(res.getNotifications());
                    adapter.notifyDataSetChanged();
                }
                break;
            case ERROR:
                hideLoading();
                if (response.error != null) {
                    Snackbar mySnackbar = Snackbar.make(getViewDataBinding().getRoot(),
                            ErrorMessageFactory.create(this, (Exception) response.error),
                            Snackbar.LENGTH_INDEFINITE);
                    mySnackbar.setAction(getString(R.string.btn_ok), v -> mySnackbar.dismiss());
                    mySnackbar.show();
                }
                break;
        }
    }
    /**
     * This method sets layout of list
     * It also draws dividers between list items / rows.
     * To minimize code this method has been generalized and put in BaseActivity
     * Credits : Anuj Devasthali 24-08-2019
     * @param recyclerView
     */
    public void setListLayout(RecyclerView recyclerView) {
        // Setup List Layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Add divider in list items
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
    }

    /**
     * This method sets adapter to recycler view to show data in list format
     * To minimize code this method is generalized and put in BaseActivity
     * Credits : Anuj Devasthali, Ankit Polekar 24-08-2019
     *
     * @param recyclerView Your recycler view
     * @param userStatsList Arraylist of data to be shown
     * @param userDataAdapter Your list adapter
     * @param <T> Any object used by your adapter (This will be treated as a row)
     */
    public <T extends Object> void setListAdapter(RecyclerView recyclerView,
                                                  ArrayList<T> userStatsList,
                                                  BaseAdapter userDataAdapter) {
        // Set data to adapter
        userDataAdapter.addItems(userStatsList);
        // Attach adapter to list to Populate list view
        recyclerView.setAdapter(userDataAdapter);
    }
}
