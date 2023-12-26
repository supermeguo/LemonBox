package com.github.lemon.osc.callback;

public interface RequestCallback {
    void onSuccess(String result);

    void onError(String errorMSG);
}
