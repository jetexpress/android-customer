package coid.customer.pickupondemand.jet.network;

import com.google.gson.JsonElement;

import org.json.JSONObject;

import coid.customer.pickupondemand.jet.model.APIResult;
import coid.customer.pickupondemand.jet.model.Login;
import coid.customer.pickupondemand.jet.model.UserProfile;
import coid.customer.pickupondemand.jet.model.ValidateOTP;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IAuthService
{
    @FormUrlEncoded
    @POST("oauth/token?role=customer")
    Call<Login> login(@Field("username") String username,
                      @Field("password") String password,
                      @Field("client_id") String clientId,
                      @Field("grant_type") String grantType);

    @FormUrlEncoded
    @POST("oauth/token")
    Call<Login> refresh(@Field("refresh_token") String refreshToken,
                        @Field("client_id") String clientId,
                        @Field("grant_type") String grantType);

    @FormUrlEncoded
    @POST("api/account/externallogin")
    Call<Login> loginExternal(@Field("accessToken") String accessToken, @Field("provider") String provider);

    @FormUrlEncoded
    @POST("api/account/register")
    Call<Void> register(@Field("username") String username,
                        @Field("password") String password,
                        @Field("confirmpassword") String confirmPassword,
                        @Field("fullname") String fullName,
                        @Field("email") String email);

    @GET("me/profile")
    Call<UserProfile> getProfile();

    @FormUrlEncoded
    @POST("api/account/changepassword")
    Call<APIResult> changePassword(@Field("username") String username,
                                   @Field("oldpassword") String oldPassword,
                                   @Field("newpassword") String newPassword);

    @FormUrlEncoded
    @POST("me/changefullname")
    Call<Void> changeProfile(@Field("fullname") String fullName,
                             @Field("phone") String phone,
                             @Field("address") String address);
    @FormUrlEncoded
    @POST("api/account/v2/register")
    Call<Void> registerOtp(@Field("username") String username,
                                  @Field("password") String password,
                                  @Field("confirmpassword") String confirmPassword,
                                  @Field("fullname") String fullName,
                                  @Field("email") String email,
                                  @Field("phonenumber") String phonenumber);


    @GET("api/account/validateotp")
    Call<ValidateOTP> validateOtp(@Query("email") String email, @Query("code") String code, @Query("phonenumber") String phonenumber);

    @FormUrlEncoded
    @POST("api/account/requestotp")
    Call<Void> createOtp(@Field("email") String email,
                         @Field("phonenumber") String phonenumber,
                         @Field("remarks") String remarks
    );
}
