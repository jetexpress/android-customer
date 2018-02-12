package coid.customer.pickupondemand.jet.model.map;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddressComponent
{
    public static final String TYPE_ADMINISTRATIVE_AREA_LEVEL_1 = "administrative_area_level_1";
    public static final String TYPE_ROUTE = "route";
    public static final String TYPE_PREMISE = "premise";

    @SerializedName("long_name")
    private String longName;
    @SerializedName("short_name")
    private String shortName;
    private List<String> types;

    public String getLongName()
    {
        return longName;
    }

    public void setLongName(String longName)
    {
        this.longName = longName;
    }

    public String getShortName()
    {
        return shortName;
    }

    public void setShortName(String shortName)
    {
        this.shortName = shortName;
    }

    public List<String> getTypes()
    {
        return types;
    }

    public void setTypes(List<String> types)
    {
        this.types = types;
    }

    public Boolean isAdministrativeArea1()
    {
        return this.types.contains(TYPE_ADMINISTRATIVE_AREA_LEVEL_1);
    }

    public Boolean isRoute()
    {
        return this.types.contains(TYPE_ROUTE);
    }

    public Boolean isPremise()
    {
        return this.types.contains(TYPE_PREMISE);
    }
}
