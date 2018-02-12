package coid.customer.pickupondemand.jet.model;

import android.support.annotation.Nullable;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.List;

import coid.customer.pickupondemand.jet.Utility;
import coid.customer.pickupondemand.jet.config.AppConfig;
import coid.customer.pickupondemand.jet.model.db.DBContract;
import coid.customer.pickupondemand.jet.model.db.DBQuery;

@Table(name = DBContract.NotificationPayloadEntry.TABLE_NAME)
public class NotificationPayload extends Model
{
    public static final String TYPE_PICKUP = "Pickup Request";
    public static final String ROLE_CUSTOMER = "Customer";

    @Expose
    @Column(name = DBContract.NotificationPayloadEntry.COLUMN_CODE)
    private String code;
    @Expose
    @Column(name = DBContract.NotificationPayloadEntry.COLUMN_ROLE)
    private String role;
    @Expose
    @Column(name = DBContract.NotificationPayloadEntry.COLUMN_NOTIFICATION_TIME)
    private String notificationTime;
    @Expose
    @Column(name = DBContract.NotificationPayloadEntry.COLUMN_RATE)
    private String rate;
    @Expose
    @Column(name = DBContract.NotificationPayloadEntry.COLUMN_TYPE)
    private String type;
    @Expose
    @Column(name = DBContract.NotificationPayloadEntry.COLUMN_STATUS)
    private String status;
    @Expose
    @Column(name = DBContract.NotificationPayloadEntry.COLUMN_IS_UNREAD)
    private boolean isUnread;
    @Expose
    @Column(name = DBContract.NotificationPayloadEntry.COLUMN_IS_NEW)
    private boolean isNew;

    public NotificationPayload()
    {
//        this.code = "";
//        this.role = "";
//        this.notificationTime = "";
//        this.rate = "";
//        this.type = "";
//        this.status = "";
//        this.isUnread = true;
//        this.isNew = true;
    }
//
//    public NotificationPayload(String code, String role, String notificationTime, String rate, String type, String status, boolean isUnread, boolean isNew)
//    {
//        this.code = code;
//        this.role = role;
//        this.notificationTime = notificationTime;
//        this.rate = rate;
//        this.type = type;
//        this.status = status;
//        this.isUnread = isUnread;
//        this.isNew = isNew;
//    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public String getNotificationTime()
    {
        return notificationTime;
    }

    public void setNotificationTime(String notificationTime)
    {
        this.notificationTime = notificationTime;
    }

    public String getRate()
    {
        return rate;
    }

    public void setRate(String rate)
    {
        this.rate = rate;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public boolean isUnread()
    {
        return isUnread;
    }

    public void setUnread(boolean unread)
    {
        isUnread = unread;
    }

    public boolean isNew()
    {
        return isNew;
    }

    public void setNew(boolean aNew)
    {
        isNew = aNew;
    }

    public boolean isPickup()
    {
        return this.type.equals(TYPE_PICKUP);
    }

    public boolean isCustomer()
    {
        return this.role.equals(ROLE_CUSTOMER);
    }

    public String getReadableStatus()
    {
        Pickup pickup = new Pickup();
        pickup.setStatus(this.status);
        return pickup.getReadableStatus();
    }

    public String toJson()
    {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        return gson.toJson(this);
    }

    public void update(NotificationPayload notificationPayload)
    {
        setRole(notificationPayload.getRole());
        setNotificationTime(notificationPayload.getNotificationTime());
        setRate(notificationPayload.getRate());
        setType(notificationPayload.getType());
        setStatus(notificationPayload.getStatus());
    }

    public static NotificationPayload getByCode(String code)
    {
        return new Select().from(NotificationPayload.class).where(DBContract.NotificationPayloadEntry.COLUMN_CODE + "=?", code).executeSingle();
    }

    public static List<NotificationPayload> getPagedNotificationPayloadList(@Nullable Long lastItemId)
    {
        int limit = (int) AppConfig.DEFAULT_PAGING_SIZE;

        /** Null value of lastItemId will get 1st page of notification list */
        if (lastItemId == null)
        {
            /** Get 1st page */
            return new Select()
                    .from(NotificationPayload.class)
                    .orderBy("ID DESC")
                    .offset(0)
                    .limit(limit)
                    .execute();
        }
        else
        {
            return new Select()
                    .from(NotificationPayload.class)
                    .where("ID < ?", lastItemId)
                    .orderBy("ID DESC")
                    .limit(limit)
                    .execute();
        }
    }

    public static void deleteByCode(String code)
    {
        From deleteByCode = new Delete().from(NotificationPayload.class).where(DBContract.NotificationPayloadEntry.COLUMN_CODE + "=?", code);
        deleteByCode.executeSingle();
    }

    public static void setAllRead()
    {
        List<NotificationPayload> notificationPayloadList = DBQuery.getAll(NotificationPayload.class);
        ActiveAndroid.beginTransaction();
        try
        {
            for (NotificationPayload notificationPayload : notificationPayloadList)
            {
                notificationPayload.setUnread(false);
                notificationPayload.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally
        {
            ActiveAndroid.endTransaction();
        }
    }

    public static void setAllOld()
    {
        List<NotificationPayload> notificationPayloadList = DBQuery.getAll(NotificationPayload.class);
        ActiveAndroid.beginTransaction();
        try
        {
            for (NotificationPayload notificationPayload : notificationPayloadList)
            {
                notificationPayload.setNew(false);
                notificationPayload.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally
        {
            ActiveAndroid.endTransaction();
        }
    }

    public static void setAllOldAndRead()
    {
        List<NotificationPayload> notificationPayloadList = DBQuery.getAll(NotificationPayload.class);
        ActiveAndroid.beginTransaction();
        try
        {
            for (NotificationPayload notificationPayload : notificationPayloadList)
            {
                notificationPayload.setNew(false);
                notificationPayload.setUnread(false);
                notificationPayload.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally
        {
            ActiveAndroid.endTransaction();
        }
    }

    public static NotificationPayload createFromString(String dataString)
    {
        try
        {
            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC);
            builder.excludeFieldsWithoutExposeAnnotation();
            Gson gson = builder.create();
            return gson.fromJson(dataString, NotificationPayload.class);
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}
