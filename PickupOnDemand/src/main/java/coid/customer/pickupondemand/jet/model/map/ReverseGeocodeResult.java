package coid.customer.pickupondemand.jet.model.map;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReverseGeocodeResult
{
    @SerializedName("address_components")
    private List<AddressComponent> addressComponentList;
    @SerializedName("formatted_address")
    private String formattedAddress;
    @SerializedName("place_id")
    private String placeId;
    private List<String> types;

    public List<AddressComponent> getAddressComponentList()
    {
        return addressComponentList;
    }

    public void setAddressComponentList(List<AddressComponent> addressComponentList)
    {
        this.addressComponentList = addressComponentList;
    }

    public String getFormattedAddress()
    {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress)
    {
        this.formattedAddress = formattedAddress;
    }

    public String getPlaceId()
    {
        return placeId;
    }

    public void setPlaceId(String placeId)
    {
        this.placeId = placeId;
    }

    public List<String> getTypes()
    {
        return types;
    }

    public void setTypes(List<String> types)
    {
        this.types = types;
    }
}
