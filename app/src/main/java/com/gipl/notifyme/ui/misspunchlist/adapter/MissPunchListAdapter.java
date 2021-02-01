package com.gipl.notifyme.ui.misspunchlist.adapter;

import android.view.View;

import com.gipl.notifyme.R;
import com.gipl.notifyme.data.model.api.mispunchlist.LiMissPunch;
import com.gipl.notifyme.databinding.LayoutMissPunchListRowBinding;
import com.gipl.notifyme.ui.base.BaseAdapter;
import com.gipl.notifyme.ui.base.BaseViewHolder;

public class MissPunchListAdapter extends BaseAdapter<MissPunchListAdapter.MissPunchHolder, LiMissPunch> {

    @Override
    public int getItemId() {
        return R.layout.layout_miss_punch_list_row;
    }

    @Override
    public MissPunchHolder getHolder(View view) {
        return new MissPunchHolder(view);
    }

    @Override
    public void onViewSet(MissPunchHolder missPunchHolder, int position, LiMissPunch liMissPunch) {
        missPunchHolder.getBindVariable().setMispunch(liMissPunch);
    }

    class MissPunchHolder extends BaseViewHolder<LayoutMissPunchListRowBinding> {
        public MissPunchHolder(View itemView) {
            super(itemView);
        }
    }
}
