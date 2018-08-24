package com.uber.kush.interfaces;

import com.uber.kush.helper.Result;

public interface INetworkCallBack {

    /**
     * Called When Network call has been started
     */
    void onNetWorkCallStarted();

    /**
     * Called When Network call has been completed
     */
    void onNetWorkCallCompleted(Result result);
}
