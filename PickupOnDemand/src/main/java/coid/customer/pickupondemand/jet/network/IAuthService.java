package coid.customer.pickupondemand.jet.network;

import coid.customer.pickupondemand.jet.model.APIResult;
import coid.customer.pickupondemand.jet.model.Login;
import coid.customer.pickupondemand.jet.model.UserProfile;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

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
}
