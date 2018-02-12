package coid.customer.pickupondemand.jet.model;

import com.google.gson.Gson;

public class PickupQRCode
{
    private String uid;
    private String sName;
    private String sPhone;
    private String cName;
    private String cPhone;
    private String cAddress;
    private String cProduct;

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getsName()
    {
        return sName;
    }

    public void setsName(String sName)
    {
        this.sName = sName;
    }

    public String getsPhone()
    {
        return sPhone;
    }

    public void setsPhone(String sPhone)
    {
        this.sPhone = sPhone;
    }

    public String getcName()
    {
        return cName;
    }

    public void setcName(String cName)
    {
        this.cName = cName;
    }

    public String getcPhone()
    {
        return cPhone;
    }

    public void setcPhone(String cPhone)
    {
        this.cPhone = cPhone;
    }

    public String getcAddress()
    {
        return cAddress;
    }

    public void setcAddress(String cAddress)
    {
        this.cAddress = cAddress;
    }

    public String getcProduct()
    {
        return cProduct;
    }

    public void setcProduct(String cProduct)
    {
        this.cProduct = cProduct;
    }

    public static PickupQRCode createFromDataString(String dataString)
    {
        try
        {
            Gson gson = new Gson();
            return gson.fromJson(dataString, PickupQRCode.class);
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}
