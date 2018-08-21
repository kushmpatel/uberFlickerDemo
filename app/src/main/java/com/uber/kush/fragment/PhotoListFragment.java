package com.uber.kush.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.uber.kush.R;
import com.uber.kush.backgroundtask.NetworkCallAsync;
import com.uber.kush.helper.JSonResponseParser;
import com.uber.kush.helper.Result;
import com.uber.kush.helper.UberLog;
import com.uber.kush.interfaces.INetworkCallBack;
import com.uber.kush.model.PhotoResponseVO;

public class PhotoListFragment extends Fragment implements SearchView.OnQueryTextListener,INetworkCallBack {

    private AppCompatActivity mActivity;
    private View view;
    private RecyclerView rvPhotoList;
    private Toolbar toolbar;
    private INetworkCallBack mINetworkCallBack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        UberLog.i(this.getClass().getSimpleName(), "onAttach");
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        UberLog.i(this.getClass().getSimpleName(), "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        UberLog.i(this.getClass().getSimpleName(),"onCreateView");
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_photo_list, null);
            setLayoutView(view);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
    }

    private void setLayoutView(View view) {
        rvPhotoList = view.findViewById(R.id.rvPhotoList);
        toolbar = view.findViewById(R.id.toolbar);

        setRecyclerLayoutManager();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem myActionMenuItem = menu.findItem( R.id.search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(mActivity, "Searched Query is "+query, Toast.LENGTH_SHORT).show();
        NetworkCallAsync mNetworkCallAsync = new NetworkCallAsync(this);
        mNetworkCallAsync.execute(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void setRecyclerLayoutManager(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity,3, LinearLayoutManager.HORIZONTAL,false);
        rvPhotoList.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onNetWorkCallCompleted(Result result) {
        String responseJson = result.mResponse;
        JSonResponseParser mJSonResponseParser = new JSonResponseParser();
        PhotoResponseVO mPhotoResponseVO = mJSonResponseParser.getPhotoResponseVO(responseJson);
        UberLog.d(PhotoListFragment.class.getSimpleName(),"Parsed Successfully");
    }
}
