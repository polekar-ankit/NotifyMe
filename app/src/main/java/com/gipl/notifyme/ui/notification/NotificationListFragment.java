package com.gipl.notifyme.ui.notification;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gipl.notifyme.BR;
import com.gipl.notifyme.BuildConfig;
import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.notification.Notification;
import com.gipl.notifyme.databinding.LayoutNotificationListBinding;
import com.gipl.notifyme.exceptions.ErrorMessageFactory;
import com.gipl.notifyme.ui.base.BaseAdapter;
import com.gipl.notifyme.ui.base.BaseFragment;
import com.gipl.notifyme.ui.image.ImagePreviewActivity;
import com.gipl.notifyme.ui.model.Response;
import com.gipl.notifyme.ui.notification.adapter.NotificationListAdapter;
import com.gipl.notifyme.ui.videoplayer.PlayerActivity;
import com.gipl.notifyme.uility.AppUtility;
import com.gipl.notifyme.uility.DialogUtility;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import javax.inject.Inject;

public class NotificationListFragment extends BaseFragment<LayoutNotificationListBinding, NotificationListViewModel> {
    @Inject
    NotificationListViewModel notificationListViewModel;
    NotificationListAdapter adapter;
    ClipData clipData;
    ClipboardManager clipboardManager;

//    public static void start(Context context) {
//        Intent intent = new Intent(context, NotificationListActivity.class);
//        context.startActivity(intent);
//        ((BaseActivity) context).finish();
//    }

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
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set title
        getBaseActivity().getSupportActionBar().setTitle(getString(R.string.activity_notification) + " - " + BuildConfig.VERSION_CODE + ".0 - Beta");

        // Observe changes in response handled by view model
        getViewModel().getResponseMutableLiveData().observe(getViewLifecycleOwner(), this::processResponse);

        //setup list view
        setListLayout(getViewDataBinding().rvNotifications);

        // Clipboard
        clipboardManager = (ClipboardManager) getBaseActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        // Create list adapter
        adapter = new NotificationListAdapter();

        // set adapter
        setListAdapter(getViewDataBinding().rvNotifications, new ArrayList<>(), adapter);

        // Set listener to list items
        adapter.setListener(notification -> {
            // Check if notification has any link attached and open it according to its type
            if (AppUtility.LINK_TYPE.IMAGE.equalsIgnoreCase(notification.getLinkType())) {
                ImagePreviewActivity.start(getBaseActivity(), notification.getLink());
            } else if (AppUtility.LINK_TYPE.VIDEO.equalsIgnoreCase(notification.getLinkType())) {
                PlayerActivity.start(getBaseActivity(), notification.getLink());
            } else if (AppUtility.LINK_TYPE.PDF.equalsIgnoreCase(notification.getLinkType())) {
                // Open pdf in browser
                /*Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(notification.getLink()));
                startActivity(browserIntent);*/
                AppUtility.openPdf(Uri.parse(notification.getLink()),requireContext(),getViewDataBinding().getRoot());

//                Intent intent = new Intent();
//                intent.setDataAndType(Uri.parse(notification.getLink()), "application/pdf");
//                // check if there is any app that can open pdf
//                try {
//                    startActivity(intent);
//                } catch (ActivityNotFoundException activityNotFound) {
//                    // Tell user that the need to install a pdf viewer
//                    Snackbar mySnackbar = Snackbar.make(getViewDataBinding().getRoot(),
//                            getString(R.string.error_pdf_viewer_not_installed),
//                            Snackbar.LENGTH_INDEFINITE);
//                    // Show snackbar
//                    mySnackbar.setAction(getString(R.string.btn_ok), v -> {
//                        // Redirect to Play Store
//                        Intent playIntent = new Intent(Intent.ACTION_VIEW);
//                        playIntent.setData(Uri.parse(
//                                "http://play.google.com/store/search?q=pdfviewer&c=apps"));
//                        playIntent.setPackage("com.android.vending");
//                        startActivity(playIntent);
//                    });
//                    mySnackbar.show();
//                }
            }
        });

        // Set long click listener
        adapter.setLongClickListener(notification -> {
            clipData = ClipData.newPlainText("notification", notification.getForGroup() + "\n\n" +
                    notification.getTitle() + "\n" +
                    notification.getMessage() + "\n\n" +
                    notification.getNotificationDate());
            clipboardManager.setPrimaryClip(clipData);

            DialogUtility.showToast(getBaseActivity(), "Text copied");
            //getString(R.string.message_text_copied)
        });

        //call api to get list of announcements
        getViewModel().getNotificationList().observe(getViewLifecycleOwner(), this::processNotificationList);
        getViewModel().getAllNotifications();

        getViewDataBinding().pullDown.setEnabled(true);
        getViewDataBinding().pullDown.setOnRefreshListener(() -> {
            getViewDataBinding().pullDown.setRefreshing(false);
//            adapter.clear();
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
                    getViewDataBinding().pullDown.setEnabled(firstItem == 0);
                }
            }
        });
    }


    private void processNotificationList(ArrayList<Notification> notifications) {
        if (notifications != null && notifications.size() > 0) {
            adapter.addItems(notifications);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void hideLoading() {
        getViewDataBinding().rvNotifications.setVisibility(View.VISIBLE);
        getViewDataBinding().loading.setVisibility(View.GONE);
    }

    private void processResponse(Response response) {
        switch (response.status) {
            case LOADING:
//                showLoading();
                getViewDataBinding().rvNotifications.setVisibility(View.GONE);
                getViewDataBinding().loading.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                hideLoading();
                break;
            case ERROR:
                hideLoading();
                if (response.error != null) {
                    Snackbar mySnackbar = Snackbar.make(getViewDataBinding().getRoot(),
                            ErrorMessageFactory.create(getBaseActivity(), (Exception) response.error),
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
     *
     * @param recyclerView
     */
    public void setListLayout(RecyclerView recyclerView) {
        // Setup List Layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity()));
        // Add divider in list items
//        DividerItemDecoration itemDecor = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
//        recyclerView.addItemDecoration(itemDecor);
    }

    /**
     * This method sets adapter to recycler view to show data in list format
     * To minimize code this method is generalized and put in BaseActivity
     * Credits : Anuj Devasthali, Ankit Polekar 24-08-2019
     *
     * @param recyclerView    Your recycler view
     * @param userStatsList   Arraylist of data to be shown
     * @param userDataAdapter Your list adapter
     * @param <T>             Any object used by your adapter (This will be treated as a row)
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
