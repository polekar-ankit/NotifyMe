package com.gipl.notifyme.ui.leavelist.adapter;

import android.view.View;

import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.leaves.LeaveRequest;
import com.gipl.notifyme.databinding.LayoutLeaveRequestListItemBinding;
import com.gipl.notifyme.ui.base.BaseAdapter;
import com.gipl.notifyme.ui.base.BaseViewHolder;

public class LeaveRequestListAdapter extends BaseAdapter<LeaveRequestListAdapter.LeaveHolder, LeaveRequest> {

    @Override
    public int getItemId() {
        return R.layout.layout_leave_request_list_item;
    }

    @Override
    public LeaveHolder getHolder(View view) {
        return new LeaveHolder(view);
    }

    @Override
    public void onViewSet(LeaveHolder leaveHolder, int position, LeaveRequest leaveRequest) {
        leaveHolder.getBindVariable().setLeaveReq(leaveRequest);
    }

    class LeaveHolder extends BaseViewHolder<LayoutLeaveRequestListItemBinding> {
        public LeaveHolder(View itemView) {
            super(itemView);
        }
    }
}
