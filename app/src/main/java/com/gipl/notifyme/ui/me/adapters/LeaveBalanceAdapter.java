package com.gipl.notifyme.ui.me.adapters;

import android.view.View;

import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.leavebalance.LeaveBalance;
import com.gipl.notifyme.databinding.LayoutLeaveBalanceRowBinding;
import com.gipl.notifyme.ui.base.BaseAdapter;
import com.gipl.notifyme.ui.base.BaseViewHolder;

public class LeaveBalanceAdapter extends BaseAdapter<LeaveBalanceAdapter.BalanceHolder, LeaveBalance> {

    @Override
    public int getItemId() {
        return R.layout.layout_leave_balance_row;
    }

    @Override
    public BalanceHolder getHolder(View view) {
        return new BalanceHolder(view);
    }

    @Override
    public void onViewSet(BalanceHolder balanceHolder, int position, LeaveBalance leaveBalance) {
        balanceHolder.getBindVariable().setLeaveBal(leaveBalance);
    }

    class BalanceHolder extends BaseViewHolder<LayoutLeaveBalanceRowBinding> {
        public BalanceHolder(View itemView) {
            super(itemView);
        }
    }
}
