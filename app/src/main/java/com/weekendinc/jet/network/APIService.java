package com.weekendinc.jet.network;

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

import java.util.List;

import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface APIService {

    @FormUrlEncoded
    @POST("/oauth/token")
    public Observable<ResultLogin> doLogin(
            @Field("username") String username,
            @Field("password") String password,
            @Field("client_id") String clientId,
            @Field("grant_type") String grantType
    );

    @FormUrlEncoded
    @POST("/api/account/register")
    public Observable<ResultRegister> doRegister(
            @Field("username") String username,
            @Field("password") String password,
            @Field("confirmpassword") String confirmPassword,
            @Field("fullname") String fullName,
            @Field("email") String email
    );

    @GET("/v1/tracks/waybills")
    public Observable<List<Waybill>> getWayBills(
            @Header("authorization") String accessToken,
            @Query("awbNumbers") String... awbNumbers
    );

    @GET("/me/profile")
    public Observable<Profile> getProfile(
            @Header("authorization") String accessToken
    );

    @GET("/v1/tracks/waybills/{awbNumber}/items/{connoteCode}")
    public Observable<List<List<ConnoteTrack>>> getWayBillTracks(
            @Path("awbNumber") String awbNumber,
            @Path("connoteCode") String connoteCode
    );

    @GET("/v1/pricings/locations")
    public Observable<List<Location>> getLocations(
            @Query("keyword") String keyword
    );

    @FormUrlEncoded
    @POST("/v1/pricings")
    public Observable<List<Price>> getPrice(
            @Field("origin_value") String originValue,
            @Field("destination_value") String destinationValue,
            @Field("weight") Integer weight
    );

    @POST("/v1/tracks/pinwaybill/{awbNumber}")
    public Observable<PinWaybill> getPinWaybill(
            @Header("authorization") String accessToken,
            @Path("awbNumber") String awbNumber
    );

    @DELETE("/v1/tracks/pinwaybill/{awbNumber}")
    public Observable<UnpinWaybill> getUnpinWaybill(
            @Header("authorization") String accessToken,
            @Path("awbNumber") String awbNumber
    );

    @GET("/v1/me/tracks")
    Observable<List<Waybill>> getTrackingList(
            @Header("authorization") String accessToken,
            @Query("isFinished") boolean isFinished,
            @Query("size") long size,
            @Query("page") long page
    );

    @FormUrlEncoded
    @POST("/api/account/changepassword")
    public Observable<ResultChangePassword> doChangePassword(
            @Field("username") String username,
            @Field("oldpassword") String oldPassword,
            @Field("newpassword") String newPassword
    );

    @FormUrlEncoded
    @POST("/me/changefullname")
    public Observable<ResultChangeFullName> doChangeName(
            @Header("authorization") String accessToken,
            @Field("fullname") String fullName
    );
}