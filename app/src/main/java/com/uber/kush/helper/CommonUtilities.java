package com.uber.kush.helper;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

public class CommonUtilities {

    public static String getApplicationDataPath(Context context) {
        if (isInstalledOnSdCard(context)) {
            return getExtStorageDataDirPath(context);
        } else {
            return getIntStorageDataDirPath(context);
        }
    }


    /**
     * This method returns whether application is installed on device storage or external SD card.
     *
     * @param context
     * @return
     */
    private static boolean isInstalledOnSdCard(Context context) {

        // check for API level 8 and higher
        if (Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
            PackageManager pm = context.getPackageManager();
            try {
                PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
                ApplicationInfo ai = pi.applicationInfo;
                return (ai.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE;
            } catch (PackageManager.NameNotFoundException e) {
                // ignore
            }
        }

        // check for API level 7 - check files dir
        try {
            String filesDir = context.getFilesDir().getAbsolutePath();
            if (filesDir.startsWith("/data/")) {
                return false;
            } else if (filesDir.contains("/mnt/") || filesDir.contains("/sdcard/")) {
                return true;
            }
        } catch (Throwable e) {
            // ignore
        }

        return false;
    }

    /**
     * This method returns device storage directory path
     *
     * @param context
     * @return
     */
    private static String getIntStorageDataDirPath(Context context) {
        String path = context.getFilesDir().getAbsolutePath();
        return path.substring(0, path.lastIndexOf("/files"));
    }

    /**
     * This method returns storage directory path on external SD card.
     *
     * @param context
     * @return
     */
    private static String getExtStorageDataDirPath(Context context) {
        String path = context.getExternalFilesDir(null).getAbsolutePath();
        return path.substring(0, path.lastIndexOf("/files"));
    }
}
