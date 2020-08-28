package com.gipl.notifyme.ui.notification.adapter;

import android.text.util.Linkify;
import android.view.View;
import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.notification.Notification;
import com.gipl.notifyme.databinding.LayoutListItemNotificationBinding;
import com.gipl.notifyme.ui.base.BaseAdapter;
import com.gipl.notifyme.ui.base.BaseViewHolder;
import com.gipl.notifyme.uility.AppUtility;

public class NotificationListAdapter extends BaseAdapter<NotificationListAdapter.ItemHolder, Notification> {
    private IRecyclerLongClickListener longClickListener;

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
        /*// Set label that prompts to open attachment
        itemHolder.getBindVariable().setLinkLabel(notification.getLinkType());*/

        itemHolder.getBindVariable().ivDataIcon.setImageResource(notification.getResId());
        // set onclick listener only if it has additional link or attachment
        if (!notification.getLinkType().isEmpty()) {
            itemHolder.itemView.setOnClickListener(view -> listener.onItemClick(notification));
        }

        itemHolder.itemView.setOnLongClickListener(view -> {
            longClickListener.onItemLongClick(notification);
            return true;
        });

        // Decorate message to highlight links if any and make those url clickable
        Linkify.addLinks(itemHolder.getBindVariable().tvMassage, Linkify.WEB_URLS);
    }

    public class ItemHolder extends BaseViewHolder<LayoutListItemNotificationBinding> {
        public ItemHolder(View itemView) {
            super(itemView);
        }
    }

    public void setLongClickListener(IRecyclerLongClickListener listener) {
        this.longClickListener = listener;
    }

    public interface IRecyclerLongClickListener {
        void onItemLongClick(Notification notification);
    }
}
