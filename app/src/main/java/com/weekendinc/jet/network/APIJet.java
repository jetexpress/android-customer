package com.weekendinc.jet.network;

import android.net.Uri;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.weekendinc.jet.TheApplication;
import com.weekendinc.jet.model.pojo.ConnoteTrack;
import com.weekendinc.jet.model.pojo.Location;
import com.weekendinc.jet.model.pojo.PinWaybill;
import com.weekendinc.jet.model.pojo.Price;
import com.weekendinc.jet.model.pojo.Profile;
import com.weekendinc.jet.model.pojo.ResultChangeFullName;
import com.weekendinc.jet.model.pojo.ResultChangePassword;
import com.weekendinc.jet.model.pojo.ResultLogin;
import com.weekendinc.jet.model.pojo.ResultRegister;
import com.weekendinc.jet.model.pojo.UnpinWaybill;
import com.weekendinc.jet.model.pojo.Waybill;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import rx.Observable;

/**
 * Entry point for all requests to API.
 * Uses Retrofit library to abstract the actual REST API into a service.
 */
public class APIJet {

    private static APIJet instance;
    private static long SIZE_OF_CACHE = 100 * 1024 * 1024;
    private String token = "";
    private APIService resourceService, authService;
    private OkHttpClient client;
    private Picasso mPicasso;

    /**
     * Private singleton constructor.
     */
    private APIJet() {
        this.client = getHttpClient();
        OkClient okClient = new OkClient(client);

        RestAdapter authRestAdapter = buildAuthRestAdapter(okClient);
        RestAdapter resourceRestAdapter = buildResourceRestAdapter(okClient);

        this.authService = authRestAdapter.create(APIService.class);
        this.resourceService = resourceRestAdapter.create(APIService.class);

        mPicasso = new Picasso.Builder(TheApplication.getInstance().getApplicationContext())
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        // Log.e("URI", uri.toString());
                    }

                })
                .downloader(new OkHttpDownloader(client))
                .build();
        Picasso.setSingletonInstance(mPicasso);
    }

    /**
     * Returns the instance of this singleton.
     */
    public static APIJet getInstance() {
        if (instance == null) {
            instance = new APIJet();
        }
        return instance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Creates the RestAdapter by setting custom HttpClient.
     */
    private RestAdapter buildAuthRestAdapter(Client client) {
        Executor executor = Executors.newCachedThreadPool();
        return new RestAdapter.Builder()
                .setEndpoint(ApiConstants.AUTH_BASE_URL)
                .setExecutors(executor, executor)
                .setClient(client)
                .setConverter(new JacksonConverter())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(new APIRequestInterceptor())
//                .setErrorHandler(new APIErrorHandling())
                .build();
    }

    private RestAdapter buildResourceRestAdapter(Client client) {
        Executor executor = Executors.newCachedThreadPool();
        return new RestAdapter.Builder()
                .setEndpoint(ApiConstants.RESOURCE_BASE_URL)
                .setExecutors(executor, executor)
                .setClient(client)
                .setConverter(new JacksonConverter())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(new APIRequestInterceptor())
//                .setErrorHandler(new APIErrorHandling())
                .build();
    }

    /**
     * Custom Http Client to define connection timeouts.
     */
    private OkHttpClient getHttpClient() {
        OkHttpClient httpClient = new OkHttpClient();

//        httpClient.setSslSocketFactory(StagingEnv.getSSLAllTrust());
//        httpClient.setHostnameVerifier(new HostnameVerifier() {
//            @Override
//            public boolean verify(String hostname, SSLSession session) {
//                return true;
//            }
//        });

        try {
            File cacheDirectory = new File(
                    TheApplication.getInstance().getCacheDir().getAbsolutePath(), "HttpCache");
            Cache cache = new Cache(cacheDirectory, SIZE_OF_CACHE);
            httpClient.setCache(cache);
        } catch (Exception e) {
        }

//        httpClient.networkInterceptors().add(new CacheControlInterceptor());

        httpClient.setConnectTimeout(ApiConstants.HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        httpClient.setReadTimeout(ApiConstants.HTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS);
        httpClient.setRetryOnConnectionFailure(true);

        return httpClient;
    }

    @Deprecated
    private Cache getCache() throws IOException {
//        int cacheSize = 100 * 1024 * 1024; // 100 MiB
        File cacheDirectory = new File(TheApplication.getInstance().getCacheDir().getAbsolutePath(), "HttpCache");
        Cache cache = new Cache(cacheDirectory, SIZE_OF_CACHE);
        return cache;
    }

    public Observable<ResultLogin> submitLogin(String username, String password) {
        return authService.doLogin(username, password, "jet-customer", "password");
    }

    public Observable<ResultRegister> submitRegistration(String username, String password, String confirmPassword, String fullName, String email) {
        return authService.doRegister(username, password, confirmPassword, fullName, email);
    }

    public Observable<Profile> submitProfile(String accessToken) {
        return authService.getProfile(accessToken);
    }

    public Observable<ResultChangePassword> submitChangePassword(String username, String oldPassword, String newPassword) {
        return authService.doChangePassword(username, oldPassword, newPassword);
    }

    public Observable<ResultChangeFullName> submitChangeName(String accessToken, String fullName) {
        return authService.doChangeName(accessToken, fullName);
    }

    public Observable<List<Waybill>> submitWayBills(String accessToken, String... awbNumbers) {
        return resourceService.getWayBills(accessToken, awbNumbers);
    }

    public Observable<List<List<ConnoteTrack>>> submitWayBillTracks(String awbNumber, String connoteCode) {
        return resourceService.getWayBillTracks(awbNumber, connoteCode);
    }

    public Observable<List<Location>> submitLocations(String keyword) {
        return resourceService.getLocations(keyword);
    }

    public Observable<List<Price>> submitPrice(String originValue, String destinationValue, int weight) {
        return resourceService.getPrice(originValue, destinationValue, weight);
    }

    public Observable<PinWaybill> doPinWaybill(String accessToken, String awbNumber) {
        return  resourceService.getPinWaybill(accessToken, awbNumber);
    }

    public Observable<UnpinWaybill> doUnpinWaybill(String accessToken, String awbNumber) {
        return resourceService.getUnpinWaybill(accessToken, awbNumber);
    }

    public Observable<List<Waybill>> getTrackingList(String accessToken, Boolean isFinished, long size, long page) {
        return resourceService.getTrackingList(accessToken, isFinished, size, page);
    }
}
