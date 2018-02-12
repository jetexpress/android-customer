package coid.customer.pickupondemand.jet.network;

import coid.customer.pickupondemand.jet.model.map.Direction;
import coid.customer.pickupondemand.jet.model.map.ReverseGeocode;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IMapService
{

//    @GET("directions/json")
//    Call<Direction> getDirectionByLatLng(@Query("origin")LatLng origin, @Query("destination")LatLng destination);

    @GET("directions/json?origin={originLatitude},{originLongitude}&destination={destinationLatitude},{destinationLongitude}")
    Call<Direction> getDirectionByPath(@Path("originLatitude") Double originLatitude,
                                       @Path("originLongitude") Double originLongitude,
                                       @Path("destinationLatitude") Double destinationLatitude,
                                       @Path("destinationLongitude") Double destinationLongitude);

    @GET("directions/json")
    Call<Direction> getDirection(@Query("origin")String origin, @Query("destination")String destination);

    @GET("geocode/json")
    Call<ReverseGeocode> getReverseGeocode(@Query("latlng")String latLng);
}
