package coid.customer.pickupondemand.jet.model;

public class PickupItemSimulation extends PickupItem
{
    private String branchCode;
    private Double chargePackaging;
    private Double chargeInsurance;
    private Double discountValue;
    private Double price;
    private Double chargeAdditional;
    private Double latitude;
    private Double longitude;

    public PickupItemSimulation()
    {

    }

    public PickupItemSimulation(PickupItem pickupItem)
    {
//        update(pickupItem);
        setWeight(pickupItem.getWeight() != null && pickupItem.getWeight() > 0 ? pickupItem.getWeight() : 1);
        setLength(pickupItem.getLength() != null && pickupItem.getLength() > 0 ? pickupItem.getLength() : 1);
        setWidth(pickupItem.getWidth() != null && pickupItem.getWidth() > 0 ? pickupItem.getWidth() : 1);
        setHeight(pickupItem.getHeight() != null && pickupItem.getHeight() > 0 ? pickupItem.getHeight() : 1);
        setProductCode(pickupItem.getProductCode() != null  ? pickupItem.getProductCode() : Product.CODE_REGULAR);
        setPackagingItemCode(pickupItem.getPackagingItemCode() != null ? pickupItem.getPackagingItemCode() : "");
        setInsured(pickupItem.getInsured() != null ? pickupItem.getInsured() : false);
        setItemValue(pickupItem.getItemValue() != null ? pickupItem.getItemValue() : 0);
        setConsigneeLatitude(pickupItem.getConsigneeLatitude());
        setConsigneeLongitude(pickupItem.getConsigneeLongitude());
    }

    public String getBranchCode()
    {
        return branchCode;
    }

    public void setBranchCode(String branchCode)
    {
        this.branchCode = branchCode;
    }

    public Double getChargePackaging()
    {
        return chargePackaging;
    }

    public void setChargePackaging(Double chargePackaging)
    {
        this.chargePackaging = chargePackaging;
    }

    public Double getChargeInsurance()
    {
        return chargeInsurance;
    }

    public void setChargeInsurance(Double chargeInsurance)
    {
        this.chargeInsurance = chargeInsurance;
    }

    public Double getDiscountValue()
    {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue)
    {
        this.discountValue = discountValue;
    }

    public Double getPrice()
    {
        return price;
    }

    public void setPrice(Double price)
    {
        this.price = price;
    }

    public Double getChargeAdditional()
    {
        return chargeAdditional;
    }

    public void setChargeAdditional(Double chargeAdditional)
    {
        this.chargeAdditional = chargeAdditional;
    }

    public Double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }

    public Double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }

    public void update(PickupItem pickupItem)
    {
        setWeight(pickupItem.getWeight());
        setLength(pickupItem.getLength());
        setWidth(pickupItem.getWidth());
        setHeight(pickupItem.getHeight());
        setProductCode(pickupItem.getProductCode());
        setPackagingItemCode(pickupItem.getPackagingItemCode());
        setInsured(pickupItem.getInsured());
        setItemValue(pickupItem.getItemValue());
        setConsigneeLatitude(pickupItem.getConsigneeLatitude());
        setConsigneeLongitude(pickupItem.getConsigneeLongitude());
        setTotalFee(pickupItem.getTotalFee());
    }
}