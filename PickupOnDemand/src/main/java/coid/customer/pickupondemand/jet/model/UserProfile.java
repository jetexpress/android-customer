package coid.customer.pickupondemand.jet.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import coid.customer.pickupondemand.jet.model.db.DBContract;

@Table(name = DBContract.UserProfileEntry.TABLE_NAME)
public class UserProfile extends Model
{
    @Column(name = DBContract.UserProfileEntry.COLUMN_USERNAME)
    private String username;
    @SerializedName("fullname")
    @Column(name = DBContract.UserProfileEntry.COLUMN_FULL_NAME)
    private String fullName;
    @SerializedName("dateOfBirth")
    @Column(name = DBContract.UserProfileEntry.COLUMN_DATE_OF_BIRTH)
    private String dateOfBirth;
    @Column(name = DBContract.UserProfileEntry.COLUMN_DATE_OF_BIRTH_IN_MILLIS)
    private Long dateOfBirthInMillis;
    @Column(name = DBContract.UserProfileEntry.COLUMN_PHONE_NUMBER)
    private String phoneNumber;
    @Column(name = DBContract.UserProfileEntry.COLUMN_ADDRESS)
    private String address;
    @Column(name = DBContract.UserProfileEntry.COLUMN_EMAIL)
    private String email;
    private Wallet wallet;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public String getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public Long getDateOfBirthInMillis()
    {
        return dateOfBirthInMillis;
    }

    public void setDateOfBirthInMillis(Long dateOfBirthInMillis)
    {
        this.dateOfBirthInMillis = dateOfBirthInMillis;
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

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Wallet getWallet()
    {
        try
        {
            List<Wallet> walletList = getMany(Wallet.class, DBContract.WalletEntry.FK_USER_PROFILE_ID);
            if (walletList != null && walletList.size() > 0)
                return walletList.get(0);
        }
        catch (Exception ex)
        {
            return wallet;
        }

        return wallet;
    }

    public void setWallet(Wallet wallet)
    {
        this.wallet = wallet;
    }

    public void saveComplete()
    {
        this.save();
        this.wallet.setUserProfile(this);
        this.wallet.save();
    }

    @Override
    public String toString()
    {
        return DBContract.UserProfileEntry.TABLE_NAME + "@" + getId()
                + ", Username : " + this.username
                + ", FullName : " + this.fullName
                + ", Email : " + this.email;
    }
}
