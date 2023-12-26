package com.github.lemon.osc.ui.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.lemon.osc.R;
import com.github.lemon.osc.api.ApiConfig;
import com.github.lemon.osc.bean.Movie;
import com.github.lemon.osc.util.DefaultConfig;
import com.github.lemon.osc.util.ImgUtil;

import java.util.ArrayList;

import me.jessyan.autosize.utils.AutoSizeUtils;

public class FastSearchAdapter extends BaseQuickAdapter<Movie.Video, BaseViewHolder> {
    public FastSearchAdapter() {
        super(R.layout.item_search, new ArrayList<>());
    }

    @Override
    protected void convert(BaseViewHolder helper, Movie.Video item) {

        // with preview
        helper.setText(R.id.tvName, item.name);
        helper.setText(R.id.tvSite, ApiConfig.get().getSource(item.sourceKey).getName());
        helper.setVisible(R.id.tvNote, item.note != null && !item.note.isEmpty());
        if (item.note != null && !item.note.isEmpty()) {
            helper.setText(R.id.tvNote, item.note);
        }
        ImageView ivThumb = helper.getView(R.id.ivThumb);
        if (!TextUtils.isEmpty(item.pic)) {
            // takagen99 : Use Glide instead
            ImgUtil.load(item.pic, ivThumb, 14);
        } else {
            ivThumb.setImageResource(R.drawable.img_loading_placeholder);
        }
    }
}