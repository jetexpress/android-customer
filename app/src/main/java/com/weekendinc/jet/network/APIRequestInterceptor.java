package com.weekendinc.jet.network;

import com.weekendinc.jet.TheApplication;
import com.weekendinc.jet.TheConstant;

import retrofit.RequestInterceptor;

/**
 * Created by Fadhlan on 2/9/15.
 */
@Deprecated
public class APIRequestInterceptor implements RequestInterceptor {
    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("Accept", "application/json");

        // API : v1/tracks/waybills
        request.addHeader("x-jet-appid", "jet-customer");
        request.addHeader("content-type", "application/json");
        request.addHeader("content-type", "application/x-www-form-urlencoded");

        if (TheApplication.getInstance().isOnline()) {
            int maxAge = 0; // read from cache for 1 minute
            request.addHeader("Cache-Control", "public, max-age=" + maxAge);
            request.addHeader("Referer", TheConstant.APP_NAME);
        } else {
            int maxStale = 60 * 60 * 24 * 7; // tolerate 1-week stale
            request.addHeader("Cache-Control", "private, max-stale=" + maxStale);
        }
    }
}
