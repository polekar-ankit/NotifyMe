package com.gipl.notifyme.ui.notification.adapter;

import android.view.View;
import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.notification.Notification;
import com.gipl.notifyme.databinding.LayoutListItemNotificationBinding;
import com.gipl.notifyme.ui.base.BaseAdapter;
import com.gipl.notifyme.ui.base.BaseViewHolder;

public class NotificationListAdapter extends BaseAdapter<NotificationListAdapter.ItemHolder, Notification> {
    @Override
    public int getItemId() {
        return R.layout.layout_list_item_notification;
    }

    @Override
    public ItemHolder getHolder(View view) {
        return new ItemHolder(view);
    }

    @Override
    public void onViewSet(ItemHolder itemHolder, int position, Notification notification) {
        // Set data for row
        itemHolder.getBindVariable().setNotification(notification);
        // set onclick listener
        //itemHolder.itemView.setOnClickListener(view-> listener.onItemClick(notification));
    }

    public class ItemHolder extends BaseViewHolder<LayoutListItemNotificationBinding> {
        public ItemHolder(View itemView) {
            super(itemView);
        }
    }
}
