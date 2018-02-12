package coid.customer.pickupondemand.jet.model;

import android.os.Parcel;
import android.os.Parcelable;

import coid.customer.pickupondemand.jet.base.IBaseItemSelectModel;

public class Product implements IBaseItemSelectModel, Parcelable
{
    public static final String CODE_PRIORITY = "PRI";
    public static final String CODE_REGULAR = "REG";

    private Long id;
    private String code;
    private String name;
    private String description;
    private Long dueDayMin;
    private Long dueDay;
    private Long volumeDivider;
    private Boolean isWeekdays;
    private String label;

    public Product()
    {

    }

    public Product(String code, String description)
    {
        this.code = code;
        this.description = description;
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

    public Long getDueDayMin()
    {
        return dueDayMin;
    }

    public void setDueDayMin(Long dueDayMin)
    {
        this.dueDayMin = dueDayMin;
    }

    public Long getDueDay()
    {
        return dueDay;
    }

    public void setDueDay(Long dueDay)
    {
        this.dueDay = dueDay;
    }

    public Long getVolumeDivider()
    {
        return volumeDivider;
    }

    public void setVolumeDivider(Long volumeDivider)
    {
        this.volumeDivider = volumeDivider;
    }

    public Boolean getWeekdays()
    {
        return isWeekdays;
    }

    public void setWeekdays(Boolean weekdays)
    {
        isWeekdays = weekdays;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    @Override
    public CharSequence getItemSelectCode()
    {
        return this.code;
    }

    @Override
    public CharSequence getItemSelectDescription()
    {
        if (this.code == null)
            return "";
        return this.code + " - " + this.description;
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
        dest.writeLong(dueDayMin);
        dest.writeLong(dueDay);
        dest.writeLong(volumeDivider);
        dest.writeInt(isWeekdays ? 1 : 0);
        dest.writeString(label);
    }

    private Product(Parcel in)
    {
        id = in.readLong();
        code = in.readString();
        name = in.readString();
        description = in.readString();
        dueDayMin = in.readLong();
        dueDay = in.readLong();
        volumeDivider = in.readLong();
        isWeekdays = in.readInt() == 1;
        label = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>()
    {
        @Override
        public Product createFromParcel(Parcel source)
        {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size)
        {
            return new Product[size];
        }
    };
}
