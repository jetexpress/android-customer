package coid.customer.pickupondemand.jet.model;

import android.os.Parcel;
import android.os.Parcelable;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.base.IBaseItemSelectModel;

public class PackagingItem implements IBaseItemSelectModel, Parcelable
{
    private Long id;
    private String code;
    private String name;
    private String description;
    private String calculateBy;
    private Boolean isFixedCost;
    private Boolean isRequirePackaging;
    private Double multiplier;
    private Double defaultPricing;
    private Double defaultWeight;
    private Double basePrice;
    private Double additionalDimension;

    public PackagingItem()
    {

    }

    public PackagingItem(String code, String name)
    {
        this.code = code;
        this.name = name;
    }

    public static PackagingItem getDefaultPackaging()
    {
        PackagingItem packagingItem = new PackagingItem();
        packagingItem.setCode("");
        packagingItem.setName(Utility.Message.get(R.string.pod_pickup_item_default_packaging));
        return packagingItem;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getCalculateBy()
    {
        return calculateBy;
    }

    public void setCalculateBy(String calculateBy)
    {
        this.calculateBy = calculateBy;
    }

    public Boolean getFixedCost()
    {
        return isFixedCost;
    }

    public void setFixedCost(Boolean fixedCost)
    {
        isFixedCost = fixedCost;
    }

    public Boolean getRequirePackaging()
    {
        return isRequirePackaging;
    }

    public void setRequirePackaging(Boolean requirePackaging)
    {
        isRequirePackaging = requirePackaging;
    }

    public Double getMultiplier()
    {
        return multiplier;
    }

    public void setMultiplier(Double multiplier)
    {
        this.multiplier = multiplier;
    }

    public Double getDefaultPricing()
    {
        return defaultPricing;
    }

    public void setDefaultPricing(Double defaultPricing)
    {
        this.defaultPricing = defaultPricing;
    }

    public Double getDefaultWeight()
    {
        return defaultWeight;
    }

    public void setDefaultWeight(Double defaultWeight)
    {
        this.defaultWeight = defaultWeight;
    }

    public Double getBasePrice()
    {
        return basePrice;
    }

    public void setBasePrice(Double basePrice)
    {
        this.basePrice = basePrice;
    }

    public Double getAdditionalDimension()
    {
        return additionalDimension;
    }

    public void setAdditionalDimension(Double additionalDimension)
    {
        this.additionalDimension = additionalDimension;
    }

    @Override
    public CharSequence getItemSelectCode()
    {
        return this.code;
    }

    @Override
    public CharSequence getItemSelectDescription()
    {
        return this.name;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeLong(id);
        dest.writeString(code);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(calculateBy);
        dest.writeInt(isFixedCost ? 1 : 0);
        dest.writeInt(isRequirePackaging ? 1 : 0);
        dest.writeDouble(multiplier);
        dest.writeDouble(defaultPricing);
        dest.writeDouble(defaultWeight);
        dest.writeDouble(basePrice);
        dest.writeDouble(additionalDimension);
    }

    private PackagingItem(Parcel in)
    {
        id = in.readLong();
        code = in.readString();
        name = in.readString();
        description = in.readString();
        calculateBy = in.readString();
        isFixedCost = in.readInt() == 1;
        isRequirePackaging = in.readInt() == 1;
        multiplier = in.readDouble();
        defaultPricing = in.readDouble();
        defaultWeight = in.readDouble();
        basePrice = in.readDouble();
        additionalDimension = in.readDouble();
    }

    public static final Creator<PackagingItem> CREATOR = new Creator<PackagingItem>()
    {
        @Override
        public PackagingItem createFromParcel(Parcel source)
        {
            return new PackagingItem(source);
        }

        @Override
        public PackagingItem[] newArray(int size)
        {
            return new PackagingItem[size];
        }
    };
}
