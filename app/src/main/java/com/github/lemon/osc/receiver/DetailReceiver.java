package com.github.lemon.osc.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.lemon.osc.event.ServerEvent;
import com.github.lemon.osc.ui.activity.DetailActivity;
import com.github.lemon.osc.util.AppManager;

import org.greenrobot.eventbus.EventBus;

/**
 * @author muziling
 * @date :2022/9/22
 * @description:
 */
public class DetailReceiver extends BroadcastReceiver {
    public static String action = "android.content.movie.detail.Action";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (action.equals(intent.getAction()) && intent.getExtras() != null) {
            if (AppManager.getInstance().getActivity(DetailActivity.class) != null) {
                AppManager.getInstance().finishActivity(DetailActivity.class);
            }
            Intent newIntent = new Intent(context, DetailActivity.class);
            newIntent.putExtra("id", intent.getExtras().getString("id"));
            newIntent.putExtra("sourceKey", intent.getExtras().getString("sourceKey"));
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(newIntent);
        }
    }
}