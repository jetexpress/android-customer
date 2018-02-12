package coid.customer.pickupondemand.jet.network;

import coid.customer.pickupondemand.jet.model.BroadcastRecipient;
import coid.customer.pickupondemand.jet.model.Config;
import coid.customer.pickupondemand.jet.model.CourierLocation;
import coid.customer.pickupondemand.jet.model.PackagingItem;
import coid.customer.pickupondemand.jet.model.PaymentMethod;
import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.model.PickupItem;
import coid.customer.pickupondemand.jet.model.PickupItemSimulation;
import coid.customer.pickupondemand.jet.model.Product;
import coid.customer.pickupondemand.jet.model.QueryData;
import coid.customer.pickupondemand.jet.model.QueryResult;
import coid.customer.pickupondemand.jet.model.UpdateInfo;
import coid.customer.pickupondemand.jet.model.UserProfile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IResourceService
{
    @GET("v2/pickup-requests/active/customer")
    Call<QueryResult<Pickup>> getActivePickupList(@Query("size") Long size, @Query("page") Long page);

    @GET("v2/pickup-requests/history/customer")
    Call<QueryResult<Pickup>> getHistoryPickupList(@Query("size") Long size, @Query("page") Long page);

    @FormUrlEncoded
    @POST("v2/pickup-requests/draft")
    Call<Pickup> createPickupDraft(@Field("latitude") Double latitude, @Field("longitude") Double longitude);

    @FormUrlEncoded
    @PATCH("v2/pickup-requests/draft/{pickupCode}/location")
    Call<Pickup> updatePickupLocation(@Path("pickupCode") String pickupCode, @Field("latitude") Double lat, @Field("longitude") Double lng);

    @FormUrlEncoded
    @PATCH("v2/pickup-requests/draft/{pickupCode}/image ")
    Call<Pickup> updatePickupImage(@Path("pickupCode") String pickupCode, @Field("imageBase64") String pickupImageBase64);

    @DELETE("v2/pickup-requests/draft/{pickupCode}")
    Call<Pickup> deletePickupDraft(@Path("pickupCode") String pickupCode);

    @POST("v2/pickup-requests/draft/{pickupCode}/request")
    Call<Pickup> createRequestedPickup(@Path("pickupCode") String pickupCode, @Body Pickup pickup);

    @POST("v2/pickup-requests/{pickupCode}/cancel")
    Call<Pickup> cancelPickup(@Path("pickupCode") String pickupCode);

    @GET("v2/pickup-requests/{pickupCode}")
    Call<Pickup> getPickupByCode(@Path("pickupCode") String pickupCode);

    @POST("v2/pickup-requests/draft/{pickupCode}/items")
    Call<PickupItem> createPickupItemDraft(@Path("pickupCode") String pickupCode, @Body PickupItem pickupItem);

    @PUT("v2/pickup-requests/draft/{pickupCode}/items/{pickupItemCode}")
    Call<PickupItem> updatePickupItem(@Path("pickupCode") String pickupCode, @Path("pickupItemCode") String pickupItemCode, @Body PickupItem pickupItem);

    @DELETE("v2/pickup-requests/draft/{pickupCode}/items/{pickupItemCode}")
    Call<PickupItem> deletePickupItem(@Path("pickupCode") String pickupCode, @Path("pickupItemCode") String pickupItemCode);

    @GET("v2/pickup-requests/{pickupCode}/items/{pickupItemCode}")
    Call<PickupItem> getPickupItemByCode(@Path("pickupCode") String pickupCode, @Path("pickupItemCode") String pickupItemCode);

    @POST("v2/pickup-requests/price-simulation")
    Call<PickupItemSimulation> simulatePrice(@Body PickupItemSimulation pickupItemSimulation);

    @GET("v2/pickup-requests/{pickupCode}/assigned-courier")
    Call<Void> getAssignedCourier(@Path("pickupCode") String pickupCode);

    @GET("v2/courier/locations/last/{courierUserId}")
    Call<CourierLocation> getCourierLastLocation(@Path("courierUserId") String courierUserId);

    @POST("v2/pickup-requests/{pickupCode}/broadcast")
    Call<BroadcastRecipient> broadcastPickup(@Path("pickupCode") String pickupCode, @Query("attempt") int attempt);

    @GET("v1/configurations")
    Call<Config> getConfig();

    @GET("v1/packagingItems")
    Call<QueryData<PackagingItem>> getPackagingItemList();

    @GET("v1/products/mobile-products")
    Call<QueryData<Product>> getProductList();

    @GET("v1/paymentMethods/mobile-payment-methods")
    Call<QueryData<PaymentMethod>> getPaymentMethodList();

    @FormUrlEncoded
    @PATCH("v2/pickup-requests/{pickupCode}/rating")
    Call<Void> rateCourier(@Path("pickupCode") String pickupCode, @Field("rating") Float rating);

    @GET("v2/pickup-requests/rating/customer")
    Call<QueryResult<Pickup>> getUnratedPickup();

    @GET("v2/pickup-requests-extra/customer-version/android/{version}")
    Call<UpdateInfo> getUpdateInfo(@Path("version") int version);
}
