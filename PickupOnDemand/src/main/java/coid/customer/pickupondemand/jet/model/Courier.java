package coid.customer.pickupondemand.jet.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Courier implements Parcelable
{
    private Long id;
    private String userId;
    private String username;
    private String fullname;
    private String dateOfBirth;
    private String phoneNumber;
    private String address;
    private String label;
    private Double currentCreditBalance;
    private Boolean courierAvailability;
    private Boolean isOwnedByJet;
    private String ownedByKioskCode;
    private String profilePictureUrl;
    private String managedBy;
    private String referencedBy;
    private String imei;
    private Float rating;
    private Integer ratingCount;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getFullname()
    {
        return fullname;
    }

    public void setFullname(String fullname)
    {
        this.fullname = fullname;
    }

    public String getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public Double getCurrentCreditBalance()
    {
        return currentCreditBalance;
    }

    public void setCurrentCreditBalance(Double currentCreditBalance)
    {
        this.currentCreditBalance = currentCreditBalance;
    }

    public Boolean getCourierAvailability()
    {
        return courierAvailability;
    }

    public void setCourierAvailability(Boolean courierAvailability)
    {
        this.courierAvailability = courierAvailability;
    }

    public Boolean getOwnedByJet()
    {
        return isOwnedByJet;
    }

    public void setOwnedByJet(Boolean ownedByJet)
    {
        isOwnedByJet = ownedByJet;
    }

    public String getOwnedByKioskCode()
    {
        return ownedByKioskCode;
    }

    public void setOwnedByKioskCode(String ownedByKioskCode)
    {
        this.ownedByKioskCode = ownedByKioskCode;
    }

    public String getProfilePictureUrl()
    {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl)
    {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getManagedBy()
    {
        return managedBy;
    }

    public void setManagedBy(String managedBy)
    {
        this.managedBy = managedBy;
    }

    public String getReferencedBy()
    {
        return referencedBy;
    }

    public void setReferencedBy(String referencedBy)
    {
        this.referencedBy = referencedBy;
    }

    public String getImei()
    {
        return imei;
    }

    public void setImei(String imei)
    {
        this.imei = imei;
    }

    public Float getRating()
    {
        return rating;
    }

    public void setRating(Float rating)
    {
        this.rating = rating;
    }

    public Integer getRatingCount()
    {
        return ratingCount;
    }

    public void setRatingCount(Integer ratingCount)
    {
        this.ratingCount = ratingCount;
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
        dest.writeString(userId);
        dest.writeString(username);
        dest.writeString(fullname);
        dest.writeString(dateOfBirth);
        dest.writeString(phoneNumber);
        dest.writeString(address);
        dest.writeString(label);
        dest.writeDouble(currentCreditBalance != null ? currentCreditBalance : 0);
        dest.writeInt(courierAvailability ? 1 : 0);
        dest.writeInt(isOwnedByJet ? 1 : 0);
        dest.writeString(ownedByKioskCode);
        dest.writeString(profilePictureUrl);
        dest.writeString(managedBy);
        dest.writeString(referencedBy);
        dest.writeString(imei);
        dest.writeFloat(rating != null ? rating : 0);
        dest.writeInt(ratingCount != null ? ratingCount : 0);
    }

    private Courier(Parcel in)
    {
        id = in.readLong();
        userId = in.readString();
        username = in.readString();
        fullname = in.readString();
        dateOfBirth = in.readString();
        phoneNumber = in.readString();
        address = in.readString();
        label = in.readString();
        currentCreditBalance = in.readDouble();
        courierAvailability = in.readInt() > 0;
        isOwnedByJet = in.readInt() > 0;
        ownedByKioskCode = in.readString();
        profilePictureUrl = in.readString();
        managedBy = in.readString();
        referencedBy = in.readString();
        imei = in.readString();
        rating = in.readFloat();
        ratingCount = in.readInt();
    }

    public static final Parcelable.Creator<Courier> CREATOR = new Parcelable.Creator<Courier>()
    {
        @Override
        public Courier createFromParcel(Parcel in)
        {
            return new Courier(in);
        }

        @Override
        public Courier[] newArray(int size)
        {
            return new Courier[size];
        }
    };
}
