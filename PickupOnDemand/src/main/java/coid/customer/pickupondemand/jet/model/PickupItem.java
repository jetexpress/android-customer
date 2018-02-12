package coid.customer.pickupondemand.jet.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.model.db.DBQuery;
import coid.customer.pickupondemand.jet.utility.NumberFormatter;

public class PickupItem implements Parcelable
{
    public static final String STATUS_LOCKED = "LOCKED";
    public static final String STATUS_UNLOCKED = "UNLOCKED";
    public static final String STATUS_COMPLETED = "COMPLETED";

    public PickupItem()
    {
        UserProfile userProfile = DBQuery.getSingle(UserProfile.class);
        if (userProfile != null)
        {
            dropshipperName = userProfile.getFullName();
            dropshipperPhone = userProfile.getPhoneNumber();
            dropshipperAddress = userProfile.getAddress();
        }
    }

    private Long id;
    private String code;
    private String consigneeJetIDCode;
    private String consigneeName;
    private String consigneePhone;
    private String consigneeAddress;
    private String consigneeAddressDetail;
    private Double consigneeLatitude;
    private Double consigneeLongitude;
    private String dropshipperName;
    private String dropshipperPhone;
    private String dropshipperAddress;
    private Double weight;
    private Double length;
    private Double width;
    private Double height;
    private String productCode;
    private String productName;
    private String packagingItemCode;
    private String packagingItemName;
    private Boolean isInsured;
    private String description;
    private Double itemValue;
    private String status;
    private String locationCode;
    private String locationName;
    private Double fee;
    private String imageBase64;
    private Double insuranceFee;
    private Double packagingFee;
    private Double discount;
    private Double totalFee;
    private String shippingLabelId;
    private String unlockCode;
    private String waybillNumber;

    @Override
    public boolean equals(Object obj)
    {
        boolean result = false;
        if (obj == null || obj.getClass() != getClass())
        {
            result = false;
        }
        else
        {
            PickupItem pickupItem = (PickupItem) obj;
            if ((this.code != null && this.code.equals(pickupItem.getCode())) || (this.shippingLabelId != null && this.shippingLabelId.equals(pickupItem.getShippingLabelId())))
                result = true;
        }
        return result;
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

    public String getConsigneeJetIDCode()
    {
        return consigneeJetIDCode;
    }

    public void setConsigneeJetIDCode(String consigneeJetIDCode)
    {
        this.consigneeJetIDCode = consigneeJetIDCode;
    }

    public String getConsigneeName()
    {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName)
    {
        this.consigneeName = consigneeName;
    }

    public String getConsigneePhone()
    {
        return consigneePhone;
    }

    public void setConsigneePhone(String consigneePhone)
    {
        this.consigneePhone = consigneePhone;
    }

    public String getConsigneeAddress()
    {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress)
    {
        this.consigneeAddress = consigneeAddress;
    }

    public String getConsigneeAddressDetail()
    {
        return consigneeAddressDetail;
    }

    public void setConsigneeAddressDetail(String consigneeAddressDetail)
    {
        this.consigneeAddressDetail = consigneeAddressDetail;
    }

    public Double getConsigneeLatitude()
    {
        return consigneeLatitude;
    }

    public void setConsigneeLatitude(Double consigneeLatitude)
    {
        this.consigneeLatitude = consigneeLatitude;
    }

    public Double getConsigneeLongitude()
    {
        return consigneeLongitude;
    }

    public void setConsigneeLongitude(Double consigneeLongitude)
    {
        this.consigneeLongitude = consigneeLongitude;
    }

    public String getDropshipperName()
    {
        return dropshipperName;
    }

    public void setDropshipperName(String dropshipperName)
    {
        this.dropshipperName = dropshipperName;
    }

    public String getDropshipperPhone()
    {
        return dropshipperPhone;
    }

    public void setDropshipperPhone(String dropshipperPhone)
    {
        this.dropshipperPhone = dropshipperPhone;
    }

    public String getDropshipperAddress()
    {
        return dropshipperAddress;
    }

    public void setDropshipperAddress(String dropshipperAddress)
    {
        this.dropshipperAddress = dropshipperAddress;
    }

    public Double getWeight()
    {
        return weight;
    }

    public String getWeightString()
    {
        if (this.weight == null)
            return "";
        return NumberFormatter.doubleToString(this.weight, 2);
    }

    public void setWeight(Double weight)
    {
        this.weight = weight;
    }

    public Double getLength()
    {
        return length;
    }

    public String getLengthString()
    {
        if (this.length == null)
            return "";
        return NumberFormatter.doubleToString(this.length, 0);
    }

    public void setLength(Double length)
    {
        this.length = length;
    }

    public Double getWidth()
    {
        return width;
    }

    public String getWidthString()
    {
        if (this.width == null)
            return "";
        return NumberFormatter.doubleToString(this.width, 0);
    }

    public void setWidth(Double width)
    {
        this.width = width;
    }

    public Double getHeight()
    {
        return height;
    }

    public String getHeightString()
    {
        if (this.height == null)
            return "";
        return NumberFormatter.doubleToString(this.height, 0);
    }

    public void setHeight(Double height)
    {
        this.height = height;
    }

    public String getProductCode()
    {
        return productCode;
    }

    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }

    public String getProductName()
    {
        return productName;
    }

    public String getFormattedProductName()
    {
        if (this.productCode == null)
            return "";
        return this.productCode + " - " + this.productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getPackagingItemCode()
    {
        return packagingItemCode;
    }

    public void setPackagingItemCode(String packagingItemCode)
    {
        this.packagingItemCode = packagingItemCode;
    }

    public String getPackagingItemName()
    {
        if (this.packagingItemCode == null || this.packagingItemCode.isEmpty())
            return PackagingItem.getDefaultPackaging().getName();
        return packagingItemName;
    }

    public void setPackagingItemName(String packagingItemName)
    {
        this.packagingItemName = packagingItemName;
    }

    public Boolean getInsured()
    {
        return isInsured != null ? isInsured : false;
    }

    public void setInsured(Boolean insured)
    {
        isInsured = insured;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Double getItemValue()
    {
        if (itemValue == null)
            return 0D;
        return itemValue;
    }

    public String getItemValueString()
    {
        return NumberFormatter.doubleToString(getItemValue(), 0);
    }

    public String getFormattedItemValueString()
    {
        return Utility.Message.get(R.string.pod_currency) + " " + NumberFormatter.doubleToString(getItemValue(), 0);
    }

    public void setItemValue(Double itemValue)
    {
        this.itemValue = itemValue;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getLocationCode()
    {
        return locationCode;
    }

    public void setLocationCode(String locationCode)
    {
        this.locationCode = locationCode;
    }

    public String getLocationName()
    {
        return locationName;
    }

    public void setLocationName(String locationName)
    {
        this.locationName = locationName;
    }

    public Double getFee()
    {
        if (fee == null)
            return 0D;
        return fee;
    }

    public String getFeeString()
    {
        if (getFee() <= 0)
            return "-";
        return NumberFormatter.doubleToString(getFee(), 0);
    }

    public String getFormattedFeeString()
    {
        return Utility.Message.get(R.string.pod_currency) + " " + getFeeString();
    }

    public void setFee(Double fee)
    {
        this.fee = fee;
    }

    public String getImageBase64()
    {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64)
    {
        this.imageBase64 = imageBase64;
    }

    public Double getInsuranceFee()
    {
        if (insuranceFee == null)
            return 0D;
        return insuranceFee;
    }

    public String getInsuranceFeeString()
    {
        return NumberFormatter.doubleToString(getInsuranceFee(), 0);
    }

    public String getFormattedInsuranceFeeString()
    {
        return Utility.Message.get(R.string.pod_currency) + " " + getInsuranceFeeString();
    }

    public void setInsuranceFee(Double insuranceFee)
    {
        this.insuranceFee = insuranceFee;
    }

    public Double getPackagingFee()
    {
        if (packagingFee == null)
            return 0D;
        return packagingFee;
    }

    public String getPackagingFeeString()
    {
        return NumberFormatter.doubleToString(getPackagingFee(), 0);
    }

    public String getFormattedPackagingFeeString()
    {
        return Utility.Message.get(R.string.pod_currency) + " " + getPackagingFeeString();
    }

    public void setPackagingFee(Double packagingFee)
    {
        this.packagingFee = packagingFee;
    }

    public Double getDiscount()
    {
        if (discount == null)
            return 0D;
        return discount;
    }

    public String getDiscountString()
    {
        return NumberFormatter.doubleToString(getDiscount(), 0);
    }

    public String getFormattedDiscountString()
    {
        return Utility.Message.get(R.string.pod_currency) + " " + getDiscountString();
    }

    public void setDiscount(Double discount)
    {
        this.discount = discount;
    }

    public Double getTotalFee()
    {
        if (totalFee == null)
            return 0D;
        return totalFee;
    }

    public String getTotalFeeString()
    {
        if (getTotalFee() <= 0)
            return "-";
        return NumberFormatter.doubleToString(getTotalFee(), 0);
    }

    public String getFormattedTotalFeeString()
    {
        return Utility.Message.get(R.string.pod_currency) + " " + getTotalFeeString();
    }

    public void setTotalFee(Double totalFee)
    {
        this.totalFee = totalFee;
    }

    public String getShippingLabelId()
    {
        return shippingLabelId;
    }

    public void setShippingLabelId(String shippingLabelId)
    {
        this.shippingLabelId = shippingLabelId;
    }

    public String getUnlockCode()
    {
        return unlockCode;
    }

    public void setUnlockCode(String unlockCode)
    {
        this.unlockCode = unlockCode;
    }

    public String getWaybillNumber()
    {
        return waybillNumber;
    }

    public String getFormattedWaybillNumber()
    {
        return "Waybill no" + waybillNumber;
    }
    public void setWaybillNumber(String waybillNumber)
    {
        this.waybillNumber = waybillNumber;
    }

    public String getPrefixLockedCode()
    {
        return this.unlockCode.substring(0, this.unlockCode.length() - 4);
    }

    public String getLockedCode()
    {
        return getPrefixLockedCode() + "****";
    }

    public boolean isLocked()
    {
        return this.status.equals(STATUS_LOCKED);
    }

    public boolean isUnlocked()
    {
        return this.status.equals(STATUS_UNLOCKED);
    }

    public boolean isCompleted()
    {
        return this.status.equals(STATUS_COMPLETED);
    }

    public boolean isValid()
    {
        return isCompleted() || isUnlocked() || isLocked();
    }

    public boolean isWaybillCreated()
    {
        return this.waybillNumber != null && !this.waybillNumber.isEmpty();
    }

    public static PickupItem createFromDataString(String dataString)
    {
        try
        {
            Gson gson = new Gson();
            return gson.fromJson(dataString, PickupItem.class);
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public static PickupItem createFromQRCode(PickupQRCode pickupQRCode)
    {
        PickupItem pickupItem = new PickupItem();

        pickupItem.setShippingLabelId(pickupQRCode.getUid());
        pickupItem.setDropshipperName(pickupQRCode.getsName());
        pickupItem.setDropshipperPhone(pickupQRCode.getsPhone());
        pickupItem.setConsigneeName(pickupQRCode.getcName());
        pickupItem.setConsigneePhone(pickupQRCode.getcPhone());
        pickupItem.setConsigneeAddressDetail(pickupQRCode.getcAddress());
        pickupItem.setProductCode(pickupQRCode.getcProduct());

        return pickupItem;
    }

    public void update(PickupItem pickupItem)
    {
        this.id = pickupItem.getId();
        this.code = pickupItem.getCode();
        this.consigneeJetIDCode = pickupItem.getConsigneeJetIDCode();
        this.consigneeName = pickupItem.getConsigneeName();
        this.consigneePhone = pickupItem.getConsigneePhone();
        this.consigneeAddress = pickupItem.getConsigneeAddress();
        this.consigneeAddressDetail = pickupItem.getConsigneeAddressDetail();
        this.consigneeLatitude = pickupItem.getConsigneeLatitude();
        this.consigneeLongitude = pickupItem.getConsigneeLongitude();
        this.dropshipperName = pickupItem.getDropshipperName();
        this.dropshipperPhone = pickupItem.getDropshipperPhone();
        this.dropshipperAddress = pickupItem.getDropshipperAddress();
        this.weight = pickupItem.getWeight();
        this.length = pickupItem.getLength();
        this.width = pickupItem.getWidth();
        this.height = pickupItem.getHeight();
        this.productCode = pickupItem.getProductCode();
        this.productName = pickupItem.getProductName();
        this.packagingItemCode = pickupItem.getPackagingItemCode();
        this.packagingItemName = pickupItem.getPackagingItemName();
        this.isInsured = pickupItem.getInsured();
        this.description = pickupItem.getDescription();
        this.itemValue = pickupItem.getItemValue();
        this.status = pickupItem.getStatus();
        this.locationCode = pickupItem.getLocationCode();
        this.locationName = pickupItem.getLocationName();
        this.fee = pickupItem.getFee();
        this.imageBase64 = pickupItem.getImageBase64();
        this.insuranceFee = pickupItem.getInsuranceFee();
        this.packagingFee = pickupItem.getPackagingFee();
        this.discount = pickupItem.discount;
        this.totalFee = pickupItem.getTotalFee();
        this.shippingLabelId = pickupItem.getShippingLabelId();
        this.unlockCode = pickupItem.getUnlockCode();
        this.waybillNumber = pickupItem.getWaybillNumber();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeLong(id != null ? id : 0);
        dest.writeString(code);
        dest.writeString(consigneeJetIDCode);
        dest.writeString(consigneeName);
        dest.writeString(consigneePhone);
        dest.writeString(consigneeAddress);
        dest.writeString(consigneeAddressDetail);
        dest.writeDouble(consigneeLatitude != null ? consigneeLatitude : 0);
        dest.writeDouble(consigneeLongitude != null ? consigneeLongitude : 0);
        dest.writeString(dropshipperName);
        dest.writeString(dropshipperPhone);
        dest.writeString(dropshipperAddress);
        dest.writeDouble(weight != null ? weight : 0);
        dest.writeDouble(length != null ? length : 0);
        dest.writeDouble(width != null ? width : 0);
        dest.writeDouble(height != null ? height : 0);
        dest.writeString(productCode);
        dest.writeString(productName);
        dest.writeString(packagingItemCode);
        dest.writeString(packagingItemName);
        dest.writeInt(isInsured ? 1 : 0);
        dest.writeString(description);
        dest.writeDouble(itemValue != null ? itemValue : 0);
        dest.writeString(status);
        dest.writeString(locationCode);
        dest.writeString(locationName);
        dest.writeDouble(fee != null ? fee : 0);
        dest.writeString(imageBase64);
        dest.writeDouble(insuranceFee != null ? insuranceFee : 0);
        dest.writeDouble(packagingFee != null ? packagingFee : 0);
        dest.writeDouble(discount != null ? discount : 0);
        dest.writeDouble(totalFee != null ? totalFee : 0);
        dest.writeString(shippingLabelId);
        dest.writeString(unlockCode);
        dest.writeString(waybillNumber);
    }

    private PickupItem(Parcel in)
    {
        id = in.readLong();
        code = in.readString();
        consigneeJetIDCode = in.readString();
        consigneeName = in.readString();
        consigneePhone = in.readString();
        consigneeAddress = in.readString();
        consigneeAddressDetail = in.readString();
        consigneeLatitude = in.readDouble();
        consigneeLongitude = in.readDouble();
        dropshipperName = in.readString();
        dropshipperPhone = in.readString();
        dropshipperAddress = in.readString();
        weight = in.readDouble();
        length = in.readDouble();
        width = in.readDouble();
        height = in.readDouble();
        productCode = in.readString();
        productName = in.readString();
        packagingItemCode = in.readString();
        packagingItemName = in.readString();
        isInsured = in.readInt() != 0;
        description = in.readString();
        itemValue = in.readDouble();
        status = in.readString();
        locationCode = in.readString();
        locationName = in.readString();
        fee = in.readDouble();
        imageBase64 = in.readString();
        insuranceFee = in.readDouble();
        packagingFee = in.readDouble();
        discount = in.readDouble();
        totalFee = in.readDouble();
        shippingLabelId = in.readString();
        unlockCode = in.readString();
        waybillNumber = in.readString();
    }

    public static final Parcelable.Creator<PickupItem> CREATOR = new Parcelable.Creator<PickupItem>()
    {
        public PickupItem createFromParcel(Parcel in) {
            return new PickupItem(in);
        }
        public PickupItem[] newArray(int size) {
            return new PickupItem[size];
        }
    };
}
