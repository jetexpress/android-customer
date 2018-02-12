package com.weekendinc.jet.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import coid.customer.pickupondemand.jet.Utility;
import lb.library.StringArrayAlphabetIndexer;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Waybill implements Parcelable {

    public static final Creator<Waybill> CREATOR = new Creator<Waybill>() {
        @Override
        public Waybill createFromParcel(Parcel in) {
            return new Waybill(in);
        }

        @Override
        public Waybill[] newArray(int size) {
            return new Waybill[size];
        }
    };

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("awbNumber")
    private String awbNumber;
    @JsonProperty("productName")
    private String productName;
    @JsonProperty("totalWeight")
    private Integer totalWeight;
    @JsonProperty("displayOriginCity")
    private String displayOriginCity;
    @JsonProperty("displayDestinationCity")
    private String displayDestinationCity;
    @JsonProperty("shipperName")
    private String shipperName;
    @JsonProperty("consigneeName")
    private String consigneeName;
    @JsonProperty("pinned")
    private Boolean pinned;
    @JsonProperty("isFinished")
    private Boolean isFinished;
    @JsonProperty("connotes")
    private List<Connote> connotes = new ArrayList<Connote>();
    @JsonProperty("isInternational")
    private Boolean isInternational;

    public Waybill() {

    }

    public Waybill(String awbNumber) {
        this.awbNumber = awbNumber;
    }

    public Waybill(Parcel in) {
        id = in.readInt();
        awbNumber = in.readString();
        productName = in.readString();
        totalWeight = in.readInt();
        displayOriginCity = in.readString();
        displayDestinationCity = in.readString();
        shipperName = in.readString();
        consigneeName = in.readString();
        pinned = in.readInt() != 0;
        isFinished = in.readInt() != 0;
        in.readTypedList(connotes, Connote.CREATOR);
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(awbNumber);
        dest.writeString(productName);
        dest.writeInt(totalWeight);
        dest.writeString(displayOriginCity);
        dest.writeString(displayDestinationCity);
        dest.writeString(shipperName);
        dest.writeString(consigneeName);
        dest.writeInt(pinned ? 1 : 0);
        dest.writeInt(isFinished ? 1 : 0);
        dest.writeTypedList(connotes);
    }

    /**
     * @return The id
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The awbNumber
     */
    @JsonProperty("awbNumber")
    public String getAwbNumber() {
        return awbNumber;
    }

    /**
     * @param awbNumber The awbNumber
     */
    @JsonProperty("awbNumber")
    public void setAwbNumber(String awbNumber) {
        this.awbNumber = awbNumber;
    }

    /**
     * @return The productName
     */
    @JsonProperty("productName")
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName The productName
     */
    @JsonProperty("productName")
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return The totalWeight
     */
    @JsonProperty("totalWeight")
    public Integer getTotalWeight() {
        return totalWeight;
    }

    /**
     * @param totalWeight The totalWeight
     */
    @JsonProperty("totalWeight")
    public void setTotalWeight(Integer totalWeight) {
        this.totalWeight = totalWeight;
    }

    /**
     * @return The displayOriginCity
     */
    @JsonProperty("displayOriginCity")
    public String getDisplayOriginCity() {
        return displayOriginCity;
    }

    /**
     * @param displayOriginCity The displayOriginCity
     */
    @JsonProperty("displayOriginCity")
    public void setDisplayOriginCity(String displayOriginCity) {
        this.displayOriginCity = displayOriginCity;
    }

    /**
     * @return The displayDestinationCity
     */
    @JsonProperty("displayDestinationCity")
    public String getDisplayDestinationCity() {
        return displayDestinationCity;
    }

    /**
     * @param displayDestinationCity The displayDestinationCity
     */
    @JsonProperty("displayDestinationCity")
    public void setDisplayDestinationCity(String displayDestinationCity) {
        this.displayDestinationCity = displayDestinationCity;
    }

    /**
     * @return The shipperName
     */
    @JsonProperty("shipperName")
    public String getShipperName() {
        return shipperName;
    }

    /**
     * @param shipperName The shipperName
     */
    @JsonProperty("shipperName")
    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    /**
     * @return The consigneeName
     */
    @JsonProperty("consigneeName")
    public String getConsigneeName() {
        return consigneeName;
    }

    /**
     * @param consigneeName The consigneeName
     */
    @JsonProperty("consigneeName")
    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    /**
     * @return The pinned
     */
    @JsonProperty("pinned")
    public Boolean getPinned() {
        return pinned;
    }

    /**
     * @param pinned The pinned
     */
    @JsonProperty("pinned")
    public void setPinned(Boolean pinned) {
        this.pinned = pinned;
    }

    /**
     * @return The isFinished
     */
    @JsonProperty("isFinished")
    public Boolean getIsFinished() {
        return isFinished;
    }

    /**
     * @param isFinished The isFinished
     */
    @JsonProperty("isFinished")
    public void setIsFinished(Boolean isFinished) {
        this.isFinished = isFinished;
    }

    /**
     * @return The connotes
     */
    @JsonProperty("connotes")
    public List<Connote> getConnotes() {
        return connotes;
    }

    /**
     * @param connotes The connotes
     */
    @JsonProperty("connotes")
    public void setConnotes(List<Connote> connotes) {
        this.connotes = connotes;
    }

    public String getFirstConnoteStatus()
    {
        if (getConnotes() != null && getConnotes().size() > 0)
        {
            return getConnotes().get(0).getLatestStatus();
        }

        return "";
    }

    public String getFirstConnoteDateString()
    {
        if (getConnotes() != null && getConnotes().size() > 0)
        {
            Long millis = Utility.DateFormat.getMillisFromDateString(getConnotes().get(0).getLatestUpdate(), "yyyy-MM-dd'T'HH:mm", true);

            if (millis != null)
                return Utility.DateFormat.getDateStringFromMillis(millis, "yyyy/MM/dd HH:mm", false);
        }

        return "";
    }

    public int getTotalVolume()
    {
        int totalVolume = 0;
        for (Connote connote : this.connotes)
        {
            totalVolume += connote.getVolume();
        }

        return totalVolume;
    }
}
