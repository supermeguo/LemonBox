package com.github.lemon.osc.ui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.lemon.osc.R;
import com.github.lemon.osc.api.ApiConfig;
import com.github.lemon.osc.bean.Movie;

import java.util.ArrayList;

/**
 * @author pj567
 * @date :2020/12/23
 * @description:
 */
public class QuickSearchAdapter extends BaseQuickAdapter<Movie.Video, BaseViewHolder> {
    public QuickSearchAdapter() {
        super(R.layout.item_quick_search_lite, new ArrayList<>());
    }

    @Override
    protected void convert(BaseViewHolder helper, Movie.Video item) {
        // lite
        helper.setText(R.id.tvName, String.format("%s  %s %s %s", ApiConfig.get().getSource(item.sourceKey).getName(), item.name, item.type == null ? "" : item.type, item.note == null ? "" : item.note));        
    }
}