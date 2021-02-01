package com.gipl.notifyme.ui.otlist.adapter;

import android.view.View;

import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.overtimelist.OT;
import com.gipl.notifyme.databinding.LayoutOtListRowBinding;
import com.gipl.notifyme.ui.base.BaseAdapter;
import com.gipl.notifyme.ui.base.BaseViewHolder;

public class OvertimeListAdapter extends BaseAdapter<OvertimeListAdapter.OverTimeHolder, OT> {

    @Override
    public int getItemId() {
        return R.layout.layout_ot_list_row;
    }

    @Override
    public OverTimeHolder getHolder(View view) {
        return new OverTimeHolder(view);
    }

    @Override
    public void onViewSet(OverTimeHolder timeHolder, int position, OT ot) {
        timeHolder.getBindVariable().setOt(ot);
    }

    class OverTimeHolder extends BaseViewHolder<LayoutOtListRowBinding> {
        public OverTimeHolder(View itemView) {
            super(itemView);
        }
    }
}
