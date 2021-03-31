package com.gipl.swayam.ui.colist.adapters;

import android.view.View;

import com.gipl.swayam.R;
import com.gipl.swayam.data.model.api.colist.CO;
import com.gipl.swayam.databinding.LayoutCoListRowBinding;
import com.gipl.swayam.ui.base.BaseAdapter;
import com.gipl.swayam.ui.base.BaseViewHolder;

public class CoListAdapter extends BaseAdapter<CoListAdapter.CoHolder, CO> {

    @Override
    public int getItemId() {
        return R.layout.layout_co_list_row;
    }

    @Override
    public CoHolder getHolder(View view) {
        return new CoHolder(view);
    }

    @Override
    public void onViewSet(CoHolder coHolder, int position, CO co) {
        coHolder.getBindVariable().setCo(co);
    }

    class CoHolder extends BaseViewHolder<LayoutCoListRowBinding> {
        public CoHolder(View itemView) {
            super(itemView);
        }
    }
}
