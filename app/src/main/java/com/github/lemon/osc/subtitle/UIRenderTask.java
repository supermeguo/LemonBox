package com.github.lemon.osc.subtitle;

import com.github.lemon.osc.subtitle.model.Subtitle;
import com.github.lemon.osc.subtitle.runtime.AppTaskExecutor;

/**
 * @author AveryZhong.
 */

public class UIRenderTask implements Runnable {

    private Subtitle mSubtitle;
    private SubtitleEngine.OnSubtitleChangeListener mOnSubtitleChangeListener;

    public UIRenderTask(final SubtitleEngine.OnSubtitleChangeListener l) {
        mOnSubtitleChangeListener = l;
    }

    @Override
    public void run() {
        if (mOnSubtitleChangeListener != null) {
            mOnSubtitleChangeListener.onSubtitleChanged(mSubtitle);
        }
    }

    public void execute(final Subtitle subtitle) {
        mSubtitle = subtitle;
        AppTaskExecutor.mainThread().execute(this);
    }

}