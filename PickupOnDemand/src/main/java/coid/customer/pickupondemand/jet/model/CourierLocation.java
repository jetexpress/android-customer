package coid.customer.pickupondemand.jet.model;

import com.google.android.gms.maps.model.LatLng;

public class CourierLocation
{
    private Long id;
    private String courierCode;
    private String courierName;
    private String activity;
    private Double longitude;
    private Double latitude;
    private Boolean active;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getCourierCode()
    {
        return courierCode;
    }

    public void setCourierCode(String courierCode)
    {
        this.courierCode = courierCode;
    }

    public String getCourierName()
    {
        return courierName;
    }

    public void setCourierName(String courierName)
    {
        this.courierName = courierName;
    }

    public String getActivity()
    {
        return activity;
    }

    public void setActivity(String activity)
    {
        this.activity = activity;
    }

    public Double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }

    public Double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }

    public Boolean getActive()
    {
        return active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }

    public LatLng getLatLng()
    {
        return new LatLng(this.latitude, this.longitude);
    }
}
