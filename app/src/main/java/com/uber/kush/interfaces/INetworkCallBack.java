package com.uber.kush.interfaces;

import com.uber.kush.helper.Result;

public interface INetworkCallBack {
    void onNetWorkCallStarted();
    void onNetWorkCallCompleted(Result result);
}
