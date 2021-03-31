package com.gipl.swayam.ui.shiftchangelist.adapter;

import android.view.View;

import com.gipl.swayam.R;
import com.gipl.swayam.data.model.api.shiftchangelist.Scr;
import com.gipl.swayam.databinding.LayoutShiftChangeListRowBinding;
import com.gipl.swayam.ui.base.BaseAdapter;
import com.gipl.swayam.ui.base.BaseViewHolder;

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
