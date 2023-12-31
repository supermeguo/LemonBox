package com.github.lemon.osc.ui.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.lemon.osc.R;
import com.github.lemon.osc.base.BaseActivity;
import com.github.lemon.osc.bean.ParseBean;

import java.util.ArrayList;

public class ParseAdapter extends BaseQuickAdapter<ParseBean, BaseViewHolder> {
    public ParseAdapter() {
        super(R.layout.item_play_parse, new ArrayList<>());
    }

    @Override
    protected void convert(BaseViewHolder helper, ParseBean item) {
        TextView tvParse = helper.getView(R.id.tvParse);
        tvParse.setVisibility(View.VISIBLE);
        if (item.isDefault()) {
            // takagen99: Added Theme Color
//            tvParse.setTextColor(mContext.getResources().getColor(R.color.color_theme));
            tvParse.setTextColor(((BaseActivity) mContext).getThemeColor());
        } else {
            tvParse.setTextColor(Color.WHITE);
        }
        tvParse.setText(item.getName());
    }
}