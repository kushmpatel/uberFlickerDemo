package com.uber.kush.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.uber.kush.IConstants;
import com.uber.kush.R;
import com.uber.kush.fragment.PhotoListFragment;
import com.uber.kush.helper.UberLog;

/**
 * Launcher Activity
 */
public class PhotoListActivity extends AppCompatActivity {
    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                openPhoneListFragment();
            } else {
                requestPermission();
            }
        } else {
            openPhoneListFragment();
        }

    }

    /**
     * Open PhoneListFragment
     */
    private void openPhoneListFragment() {
        PhotoListFragment mPhotoListFragment = new PhotoListFragment();
        replaceFragment(mPhotoListFragment,R.id.frameList,null,false,null,false,null);
    }

    /**
     * Replace Fragment
     * @param fragment
     * @param containerId
     * @param bundle
     * @param isAddToStack
     * @param addToStackString
     * @param isFromParentFrag
     * @param parentFragment
     */
    public void replaceFragment(Fragment fragment, int containerId, Bundle bundle, boolean isAddToStack, String addToStackString, boolean isFromParentFrag, Fragment parentFragment) {
        if (bundle != null)
            fragment.setArguments(bundle);

        FragmentManager manager;
        if (isFromParentFrag) {
            manager = parentFragment.getChildFragmentManager();
        } else {
            manager = getSupportFragmentManager();
        }
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(containerId, fragment);
        if (isAddToStack)
            transaction.addToBackStack(addToStackString);
        transaction.commitAllowingStateLoss();
    }

    /**
     * Check Whether app have all necessary permissions or not
     * @return
     */
    private boolean checkPermission() {
        int size = permissions.length;
        for (int i = 0; i < size; i++) {
            String permission = permissions[i];
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Request necessary permissions
     */
    private void requestPermission() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, IConstants.PERMISSION_REQUEST_CODE_FOR_WRITE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case IConstants.PERMISSION_REQUEST_CODE_FOR_WRITE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openPhoneListFragment();
                } else {
                    UberLog.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }
}
