package com.gipl.swayam.ui.base;

import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;


public abstract class BaseViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {

    private T binding;

    public T getBindVariable() {
        return binding;
    }


    public BaseViewHolder(View itemView/*, V v*/) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }

    public void unbind() {
        if (binding != null) {
            binding.unbind();
        }
    }

    public void bind() {
        if (binding == null) {
            binding = DataBindingUtil.bind(itemView);
        }
    }


}
