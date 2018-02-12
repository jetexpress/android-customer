package coid.customer.pickupondemand.jet.model;

import com.google.gson.annotations.SerializedName;

public class ErrorResult
{
    @SerializedName("key_")
    String key;

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }
}
