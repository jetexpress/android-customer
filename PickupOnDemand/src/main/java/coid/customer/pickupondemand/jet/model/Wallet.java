package coid.customer.pickupondemand.jet.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import coid.customer.pickupondemand.jet.model.db.DBContract;

@Table(name = DBContract.WalletEntry.TABLE_NAME)
public class Wallet extends Model
{
    @Column(name = DBContract.WalletEntry.COLUMN_BONUS_CREDIT)
    private Integer bonusCredit;
    @Column(name = DBContract.WalletEntry.COLUMN_TOP_UP_CREDIT)
    private Integer topupCredit;
    @Column(name = DBContract.WalletEntry.FK_USER_PROFILE_ID, onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private UserProfile userProfile;

    public Integer getBonusCredit()
    {
        return bonusCredit;
    }

    public void setBonusCredit(Integer bonusCredit)
    {
        this.bonusCredit = bonusCredit;
    }

    public Integer getTopupCredit()
    {
        return topupCredit;
    }

    public void setTopupCredit(Integer topupCredit)
    {
        this.topupCredit = topupCredit;
    }

    public UserProfile getUserProfile()
    {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile)
    {
        this.userProfile = userProfile;
    }
}
