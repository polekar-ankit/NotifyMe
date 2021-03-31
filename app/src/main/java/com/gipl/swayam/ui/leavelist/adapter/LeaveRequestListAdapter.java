package com.gipl.swayam.ui.leavelist.adapter;

import android.view.View;

import com.gipl.swayam.R;
import com.gipl.swayam.data.model.api.leaves.LeaveRequest;
import com.gipl.swayam.databinding.LayoutLeaveRequestListItemBinding;
import com.gipl.swayam.ui.base.BaseAdapter;
import com.gipl.swayam.ui.base.BaseViewHolder;

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
