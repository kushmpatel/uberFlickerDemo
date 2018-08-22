package com.uber.kush.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.uber.kush.R;
import com.uber.kush.fragment.PhotoListFragment;

public class PhotoListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        PhotoListFragment mPhotoListFragment = new PhotoListFragment();
        replaceFragment(mPhotoListFragment,R.id.frameList,null,false,null,false,null);
    }

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
}
