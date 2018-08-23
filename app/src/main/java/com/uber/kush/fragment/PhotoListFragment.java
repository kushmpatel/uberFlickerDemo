package com.uber.kush.fragment;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.uber.kush.R;
import com.uber.kush.adapter.AdapterPhotoList;
import com.uber.kush.backgroundtask.NetworkCallAsync;
import com.uber.kush.helper.JSonResponseParser;
import com.uber.kush.helper.Result;
import com.uber.kush.helper.UberLog;
import com.uber.kush.interfaces.INetworkCallBack;
import com.uber.kush.model.PhotoResponseVO;
import com.uber.kush.model.PhotoVO;
import com.uber.kush.ui.ItemOffsetDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.uber.kush.IConstants.QUERY_PARAM_API_KEY;
import static com.uber.kush.IConstants.QUERY_PARAM_FORMAT_KEY;
import static com.uber.kush.IConstants.QUERY_PARAM_METHOD_KEY;
import static com.uber.kush.IConstants.QUERY_PARAM_NO_JSON_CALLBACK_KEY;
import static com.uber.kush.IConstants.QUERY_PARAM_PAGE_KEY;
import static com.uber.kush.IConstants.QUERY_PARAM_PER_PAGE_KEY;
import static com.uber.kush.IConstants.QUERY_PARAM_SAFE_SEARCH_KEY;
import static com.uber.kush.IConstants.QUERY_PARAM_TEXT_KEY;
import static com.uber.kush.IConstants.QUERY_PARAM_VALUE_FLICKR_PHOTOS_SEARCH_VALUE;
import static com.uber.kush.IConstants.QUERY_PARAM_VALUE_JSON_VALUE;
import static com.uber.kush.IConstants.QUERY_PARAM_VALUE_NO_JSON_CALLBACK_VALUE;
import static com.uber.kush.IConstants.RECORD_SIZE;

public class PhotoListFragment extends Fragment implements SearchView.OnQueryTextListener,INetworkCallBack {

    private AppCompatActivity mActivity;
    private View view;
    private RecyclerView rvPhotoList;
    private Toolbar toolbar;
    private INetworkCallBack mINetworkCallBack;
    private int currentPage = 1;
    private boolean loading = true;
    private String mQuery;
    List<PhotoVO> listPhotos = new ArrayList<>();
    private AdapterPhotoList mAdapterPhotoList = null;
    private ProgressBar pbProgress;

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
        pbProgress = (ProgressBar) view.findViewById(R.id.pbProgress);
        rvPhotoList = view.findViewById(R.id.rvPhotoList);
        toolbar = view.findViewById(R.id.toolbar);
        rvPhotoList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    System.out.println("Scrolled Upwards");
                    int visibleItemCount = ((LinearLayoutManager) rvPhotoList.getLayoutManager()).getChildCount();
                    int totalItemCount = ((LinearLayoutManager) rvPhotoList.getLayoutManager()).getItemCount();
                    int pastVisibleItems = ((LinearLayoutManager) rvPhotoList.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                    if (loading) {
                        if ((visibleItemCount + pastVisibleItems) >= (totalItemCount)) {
                            loading = false;
                            try {
                                doCallNextPage();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        setRecyclerLayoutManager();
    }

    private void doCallNextPage() {
        currentPage++;

        HashMap<String, String> postDataParams = generatePostDataParams(mQuery);
        NetworkCallAsync mNetworkCallAsync = new NetworkCallAsync(this);
        mNetworkCallAsync.execute(postDataParams);
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
        hideKeyboard(mActivity);
        resetRecyclerView();
        query = query.trim();
        mQuery = query;
        HashMap<String, String> postDataParams = generatePostDataParams(query);
        NetworkCallAsync mNetworkCallAsync = new NetworkCallAsync(this);
        mNetworkCallAsync.execute(postDataParams);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void setRecyclerLayoutManager(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity,3, LinearLayoutManager.VERTICAL,false);
        rvPhotoList.setLayoutManager(gridLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mActivity, R.dimen.item_offset);
        rvPhotoList.addItemDecoration(itemDecoration);
    }

    @Override
    public void onNetWorkCallStarted() {
        if(currentPage == 1)
            pbProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNetWorkCallCompleted(Result result) {

        if(currentPage == 1)
            pbProgress.setVisibility(View.GONE);

        String responseJson = result.mResponse;
        JSonResponseParser mJSonResponseParser = new JSonResponseParser();
        PhotoResponseVO mPhotoResponseVO = mJSonResponseParser.getPhotoResponseVO(responseJson);
        List<PhotoVO> tempListPhotos = mPhotoResponseVO.getPhoto();
        loading = true;
        listPhotos.addAll(tempListPhotos);
        if(currentPage == 1) {
            mAdapterPhotoList = new AdapterPhotoList(mActivity, listPhotos);
            rvPhotoList.setAdapter(mAdapterPhotoList);
        } else{
            if(mAdapterPhotoList != null){
                mAdapterPhotoList.notifyDataSetChanged();
            }
        }
        UberLog.d(PhotoListFragment.class.getSimpleName(),"Parsed Successfully");
    }

    private void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private HashMap<String, String> generatePostDataParams(String query){
        HashMap<String, String> postDataParams = new HashMap();
        postDataParams.put(QUERY_PARAM_METHOD_KEY,QUERY_PARAM_VALUE_FLICKR_PHOTOS_SEARCH_VALUE);
        postDataParams.put(QUERY_PARAM_API_KEY,mActivity.getString(R.string.api_key));
        postDataParams.put(QUERY_PARAM_FORMAT_KEY,QUERY_PARAM_VALUE_JSON_VALUE);
        postDataParams.put(QUERY_PARAM_NO_JSON_CALLBACK_KEY,QUERY_PARAM_VALUE_NO_JSON_CALLBACK_VALUE);
        postDataParams.put(QUERY_PARAM_SAFE_SEARCH_KEY,"1");
        postDataParams.put(QUERY_PARAM_TEXT_KEY,query);
        postDataParams.put(QUERY_PARAM_PAGE_KEY,String.valueOf(currentPage));
        postDataParams.put(QUERY_PARAM_PER_PAGE_KEY,String.valueOf(RECORD_SIZE));
        return postDataParams;
    }

    private void resetRecyclerView(){
        currentPage = 1;
        listPhotos.clear();

        if(mAdapterPhotoList != null)
            mAdapterPhotoList.notifyDataSetChanged();
    }
}
