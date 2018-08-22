package com.uber.kush;

import android.app.Application;
import android.content.Context;

import com.uber.kush.helper.CommonUtilities;

public class ApplicationStore  extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static String getApplicationPath() {
        return CommonUtilities.getApplicationDataPath(context);
    }
}
