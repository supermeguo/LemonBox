package com.github.lemon.osc.ui.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.github.lemon.osc.R;

import org.jetbrains.annotations.NotNull;

public class AboutDialog extends BaseDialog {

    public AboutDialog(@NonNull @NotNull Context context) {
        super(context);
        setContentView(R.layout.dialog_about);
    }
}