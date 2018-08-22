package com.uber.kush;

import android.os.Environment;

public class IConstants {
    //public static final String CACHE_DIRECTORY_PATH = ApplicationStore.getApplicationPath() +"/uber/cache";
    public static final String CACHE_DIRECTORY_PATH = Environment.getExternalStorageDirectory() +"/uber/cache";
    public static final String HTTPS_SCHEME = "https:";
    public static final String FLICKR_DOMAIN = "api.flickr.com";
    public static final String REQUEST_PATH = "services/rest";
    public static final String QUERY_PARAM_METHOD_KEY = "method";
    public static final String QUERY_PARAM_API_KEY = "api_key";
    public static final String QUERY_PARAM_FORMAT_KEY = "format";
    public static final String QUERY_PARAM_NO_JSON_CALLBACK_KEY = "nojsoncallback";
    public static final String QUERY_PARAM_TEXT_KEY = "text";
    public static final String QUERY_PARAM_SAFE_SEARCH_KEY = "safe_search";
    public static final String QUERY_PARAM_PAGE_KEY = "page";
    public static final String QUERY_PARAM_PER_PAGE_KEY = "per_page";
    public static final String QUERY_PARAM_VALUE_FLICKR_PHOTOS_SEARCH_VALUE = "flickr.photos.search";
    public static final String QUERY_PARAM_VALUE_JSON_VALUE = "json";
    public static final String QUERY_PARAM_VALUE_NO_JSON_CALLBACK_VALUE = "1";
    public static final String QUERY_PARAM_VALUE_API_KEY = "api_key";
    public static final String QUERY_PARAM_VALUE_SEARCH_KEY = "search_key";

    public static final  int PERMISSION_REQUEST_CODE_FOR_WRITE = 3;
}
