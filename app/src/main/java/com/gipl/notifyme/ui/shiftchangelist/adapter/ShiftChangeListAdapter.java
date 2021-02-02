package com.gipl.notifyme.ui.shiftchangelist.adapter;

import android.view.View;

import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.shiftchangelist.Scr;
import com.gipl.notifyme.databinding.LayoutShiftChangeListRowBinding;
import com.gipl.notifyme.ui.base.BaseAdapter;
import com.gipl.notifyme.ui.base.BaseViewHolder;

public class ShiftChangeListAdapter extends BaseAdapter<ShiftChangeListAdapter.ShiftChangeHolder, Scr> {

    @Override
    public int getItemId() {
        return R.layout.layout_shift_change_list_row;
    }

    @Override
    public ShiftChangeHolder getHolder(View view) {
        return new ShiftChangeHolder(view);
    }

    @Override
    public void onViewSet(ShiftChangeHolder shiftChangeHolder, int position, Scr scr) {
        shiftChangeHolder.getBindVariable().setScr(scr);
    }

    class ShiftChangeHolder extends BaseViewHolder<LayoutShiftChangeListRowBinding> {
        public ShiftChangeHolder(View itemView) {
            super(itemView);
        }
    }
}
