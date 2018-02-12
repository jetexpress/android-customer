package coid.customer.pickupondemand.jet.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import coid.customer.pickupondemand.jet.R;
import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.utility.NumberFormatter;

public class Pickup implements Parcelable
{
    public static final String STATUS_DRAFTED = "DRAFTED";
    public static final String STATUS_REQUESTED = "REQUESTED";
    public static final String STATUS_ASSIGNED = "ASSIGNED";
    public static final String STATUS_TRIP_STARTED = "TRIP STARTED";
    public static final String STATUS_TRIP_CANCELLED = "TRIP_CANCELLED";
    public static final String STATUS_ARRIVED = "ARRIVED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    public static final String STATUS_COMPLETED = "COMPLETED";


    public static String VEHICLE_BIKE = "BIKE";
    public static String VEHICLE_CAR = "CAR";

    private Long id;
    private String createdDate;
    private String code;
    private String jetIDCode;
    private String name;
    private String address;
    private String addressDetail;
    private String phone;
    private String email;
    private String pic;
    private String position;
    private String vehicleCode;
    private String vehicleName;
    private String city;
    private String province;
    private String branchCode;
    private String branchName;
    private String description;
    private String status;
    private String paymentMethodCode;
    private String paymentMethodName;
    private Double latitude;
    private Double longitude;
    private Double totalFee;
    @SerializedName("pickupRequestItems")
    private List<PickupItem> pickupItemList;
    private String imageBase64;
    private Courier courier;
    private Float rating;
    private Long totalItem;
    private Long quickPickupItemCount;
    private Long actualPickupItemCount;
    private Long waybillCreatedCount;

    public Pickup()
{

}

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(String createdDate)
    {
        this.createdDate = createdDate;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getJetIDCode()
    {
        return jetIDCode;
    }

    public void setJetIDCode(String jetIDCode)
    {
        this.jetIDCode = jetIDCode;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddressDetail()
    {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail)
    {
        this.addressDetail = addressDetail;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPic()
    {
        return pic;
    }

    public void setPic(String pic)
    {
        this.pic = pic;
    }

    public String getPosition()
    {
        return position;
    }

    public void setPosition(String position)
    {
        this.position = position;
    }

    public String getVehicleCode()
    {
        return vehicleCode;
    }

    public void setVehicleCode(String vehicleCode)
    {
        this.vehicleCode = vehicleCode;
    }

    public String getVehicleName()
    {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName)
    {
        this.vehicleName = vehicleName;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getBranchCode()
    {
        return branchCode;
    }

    public void setBranchCode(String branchCode)
    {
        this.branchCode = branchCode;
    }

    public String getBranchName()
    {
        return branchName;
    }

    public void setBranchName(String branchName)
    {
        this.branchName = branchName;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getStatus()
    {
        return status;
    }

    public String getReadableStatus()
    {
        switch (status)
        {
            case STATUS_DRAFTED : return Utility.Message.get(R.string.pod_pickup_status_drafted);
            case STATUS_REQUESTED : return Utility.Message.get(R.string.pod_pickup_status_requested);
            case STATUS_ASSIGNED : return Utility.Message.get(R.string.pod_pickup_status_assigned);
            case STATUS_TRIP_STARTED : return Utility.Message.get(R.string.pod_pickup_status_trip_started);
            case STATUS_ARRIVED : return Utility.Message.get(R.string.pod_pickup_status_arrived);
            case STATUS_CANCELLED : return Utility.Message.get(R.string.pod_pickup_status_cancelled);
            case STATUS_COMPLETED : return Utility.Message.get(R.string.pod_pickup_status_completed);
            default: return status;
        }
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getPaymentMethodCode()
    {
        return paymentMethodCode;
    }

    public void setPaymentMethodCode(String paymentMethodCode)
    {
        this.paymentMethodCode = paymentMethodCode;
    }

    public String getPaymentMethodName()
    {
        return paymentMethodName;
    }

    public void setPaymentMethodName(String paymentMethodName)
    {
        this.paymentMethodName = paymentMethodName;
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

    public List<PickupItem> getPickupItemList()
    {
        return pickupItemList;
    }

    public void setPickupItemList(List<PickupItem> pickupItemList)
    {
        this.pickupItemList = pickupItemList;
    }

    public Double getTotalFee()
    {
        if (totalFee == null)
            return 0D;
        return totalFee;
    }

    public void setTotalFee(Double totalFee)
    {
        this.totalFee = totalFee;
    }

    public String getImageBase64()
    {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64)
    {
        this.imageBase64 = imageBase64;
    }

    public Courier getCourier()
    {
        return courier;
    }

    public void setCourier(Courier courier)
    {
        this.courier = courier;
    }

    public Long getQuickPickupItemCount()
    {
        return quickPickupItemCount;
    }

    public String getQuickPickupItemCountString()
    {
        if (this.quickPickupItemCount != null)
            return String.valueOf(this.quickPickupItemCount);
        return "0";
    }

    public void setQuickPickupItemCount(Long quickPickupItemCount)
    {
        this.quickPickupItemCount = quickPickupItemCount;
    }

    public Float getRating()
    {
        return rating;
    }

    public void setRating(Float rating)
    {
        this.rating = rating;
    }

    public Long getTotalItem()
    {
        return totalItem;
    }

    public void setTotalItem(Long totalItem)
    {
        this.totalItem = totalItem;
    }

    public Long getActualPickupItemCount()
    {
        return actualPickupItemCount;
    }

    public String getActualPickupItemCountString()
    {
        if (this.actualPickupItemCount != null)
            return String.valueOf(this.actualPickupItemCount);
        return "0";
    }

    public void setActualPickupItemCount(Long actualPickupItemCount)
    {
        this.actualPickupItemCount = actualPickupItemCount;
    }

    public Long getWaybillCreatedCount()
    {
        return waybillCreatedCount;
    }

    public String getWaybillCreatedCountString()
    {
        if (this.waybillCreatedCount != null)
            return String.valueOf(this.waybillCreatedCount) + "/" + getActualPickupItemCountString();
        return "0";
    }

    public void setWaybillCreatedCount(Long waybillCreatedCount)
    {
        this.waybillCreatedCount = waybillCreatedCount;
    }

    public LatLng getLatLng()
    {
        return new LatLng(this.latitude, this.longitude);
    }

    public String getFormattedTotalFee()
    {
        if (getTotalFee() <= 0)
            return "-";
        return Utility.Message.get(R.string.pod_currency) + " " + NumberFormatter.doubleToString(getTotalFee(), 0);
    }

    public static String getFormattedEmptyFee()
    {
        return "-";
    }

    public String getFormattedCreatedDate()
    {
        String formattedDate = "-";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat print_sdf = new SimpleDateFormat("dd MMM yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        if(this.getCreatedDate() != null)
        {
            try{

                java.util.Date dt = sdf.parse(this.getCreatedDate());
                formattedDate =  print_sdf.format(dt);
            }catch(ParseException e){
                e.printStackTrace();
            }
        }
        return formattedDate;
    }

    public Double getTotalWeightFromPickupItemList()
    {
        Double totalWeight = 0D;
        for (PickupItem pickupItem : pickupItemList)
        {
            totalWeight += pickupItem.getWeight();
        }
        return totalWeight;
    }

    public Double getTotalFeeFromPickupItemList()
    {
        Double totalFee = 0D;
        for (PickupItem pickupItem : pickupItemList)
        {
            totalFee += pickupItem.getTotalFee();
        }
        return totalFee;
    }

    public boolean hasPickupItems()
    {
        return pickupItemList != null && pickupItemList.size() > 0;
    }

    public boolean isQuickPickup()
    {
        return quickPickupItemCount != null && quickPickupItemCount > 0;
    }

    public boolean isAllItemsCompleted()
    {
        for (PickupItem pickupItem : pickupItemList)
        {
            if (!pickupItem.isCompleted())
                return false;
        }

        return true;
    }

    public boolean hasLocation()
    {
        return this.latitude != null && this.longitude != null && this.latitude != 0 && this.longitude != 0;
    }

    public boolean isDraft()
    {
        return this.status != null && this.status.equals(STATUS_DRAFTED);
    }

    public boolean isRequested()
    {
        return this.status != null && this.status.equals(STATUS_REQUESTED);
    }

    public boolean isAssigned()
    {
        return this.status != null && this.status.equals(STATUS_ASSIGNED);
    }

    public boolean isTripCancelled()
    {
        return this.status != null && this.status.equals(STATUS_TRIP_CANCELLED);
    }

    public boolean isTripStarted()
    {
        return this.status != null && this.status.equals(STATUS_TRIP_STARTED);
    }

    public boolean hasArrived()
    {
        return this.status != null && this.status.equals(STATUS_ARRIVED);
    }

    public boolean isCancelled()
    {
        return this.status != null && this.status.equals(STATUS_CANCELLED);
    }

    public boolean isActive()
    {
        return isDraft() || isRequested() || isAssigned() || isTripStarted() || isTripCancelled() || hasArrived();
    }

    public boolean isHistory()
    {
        return !isActive();
    }

    public Boolean isBike()
    {
        return this.vehicleCode != null && this.vehicleCode.equals(VEHICLE_BIKE);
    }

    public Boolean isCar()
    {
        return this.vehicleCode != null && this.vehicleCode.equals(VEHICLE_CAR);
    }

    public void update(Pickup pickup)
    {
        this.id = pickup.getId();
        this.code = pickup.getCode();
        this.createdDate = pickup.getCreatedDate();
        this.jetIDCode = pickup.getJetIDCode();
        this.name = pickup.getName();
        this.address = pickup.getAddress();
        this.addressDetail = pickup.getAddressDetail();
        this.phone = pickup.getPhone();
        this.email = pickup.getEmail();
        this.pic = pickup.getPic();
        this.position = pickup.getPosition();
        this.vehicleCode = pickup.getVehicleCode();
        this.vehicleName = pickup.getVehicleName();
        this.city = pickup.getCity();
        this.province = pickup.getProvince();
        this.branchCode = pickup.getBranchCode();
        this.branchName = pickup.getBranchName();
        this.description = pickup.getDescription();
        this.status = pickup.getStatus();
        this.paymentMethodCode = pickup.getPaymentMethodCode();
        this.paymentMethodName = pickup.getPaymentMethodName();
        this.latitude = pickup.getLatitude();
        this.longitude = pickup.getLongitude();
        this.totalFee = pickup.getTotalFee();
        this.pickupItemList = pickup.getPickupItemList();
        this.imageBase64 = pickup.getImageBase64();
        this.courier = pickup.getCourier();
        this.rating = pickup.getRating();
        this.totalItem = pickup.totalItem;
        this.quickPickupItemCount = pickup.getQuickPickupItemCount();
        this.actualPickupItemCount = pickup.getActualPickupItemCount();
        this.waybillCreatedCount = pickup.getWaybillCreatedCount();
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
        dest.writeString(createdDate);
        dest.writeString(code);
        dest.writeString(jetIDCode);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(addressDetail);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(pic);
        dest.writeString(position);
        dest.writeString(vehicleCode);
        dest.writeString(vehicleName);
        dest.writeString(city);
        dest.writeString(province);
        dest.writeString(branchCode);
        dest.writeString(branchName);
        dest.writeString(description);
        dest.writeString(status);
        dest.writeString(paymentMethodCode);
        dest.writeString(paymentMethodName);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeDouble(totalFee != null ? totalFee : 0);
        dest.writeString(imageBase64);
        dest.writeFloat(rating);
        dest.writeLong(totalItem != null ? totalItem : 0);
        dest.writeLong(quickPickupItemCount != null ? quickPickupItemCount : 0);
        dest.writeLong(actualPickupItemCount != null ? actualPickupItemCount : 0);
        dest.writeLong(waybillCreatedCount != null ? waybillCreatedCount : 0);
        dest.writeParcelable(courier, flags);
        dest.writeTypedList(pickupItemList != null ? pickupItemList : new ArrayList<PickupItem>());
    }

    private Pickup(Parcel in)
    {
        id = in.readLong();
        createdDate =in.readString();
        code = in.readString();
        jetIDCode = in.readString();
        name = in.readString();
        address = in.readString();
        addressDetail = in.readString();
        phone = in.readString();
        email = in.readString();
        pic = in.readString();
        position = in.readString();
        vehicleCode = in.readString();
        vehicleName = in.readString();
        city = in.readString();
        province = in.readString();
        branchCode = in.readString();
        branchName = in.readString();
        description = in.readString();
        status = in.readString();
        paymentMethodCode = in.readString();
        paymentMethodName = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        totalFee = in.readDouble();
        imageBase64 = in.readString();
        rating = in.readFloat();
        totalItem = in.readLong();
        quickPickupItemCount = in.readLong();
        actualPickupItemCount = in.readLong();
        waybillCreatedCount = in.readLong();
        courier = in.readParcelable(Courier.class.getClassLoader());
        in.readTypedList(pickupItemList, PickupItem.CREATOR);
    }

    public static final Parcelable.Creator<Pickup> CREATOR = new Parcelable.Creator<Pickup>()
    {
        public Pickup createFromParcel(Parcel in) {
            return new Pickup(in);
        }
        public Pickup[] newArray(int size) {
            return new Pickup[size];
        }
    };
}