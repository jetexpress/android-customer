package coid.customer.pickupondemand.jet.model.map;

import com.google.gson.annotations.SerializedName;

public class Step
{
    @SerializedName("end_location")
    private Location endLocation;
    @SerializedName("start_location")
    private Location startLocation;
    private Polyline polyline;

    public Location getEndLocation()
    {
        return endLocation;
    }

    public void setEndLocation(Location endLocation)
    {
        this.endLocation = endLocation;
    }

    public Location getStartLocation()
    {
        return startLocation;
    }

    public void setStartLocation(Location startLocation)
    {
        this.startLocation = startLocation;
    }

    public Polyline getPolyline()
    {
        return polyline;
    }

    public void setPolyline(Polyline polyline)
    {
        this.polyline = polyline;
    }
}
