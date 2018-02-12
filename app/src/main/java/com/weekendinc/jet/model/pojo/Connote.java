package com.weekendinc.jet.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Connote implements Parcelable {

    public static final Creator<Connote> CREATOR = new Creator<Connote>() {
        @Override
        public Connote createFromParcel(Parcel in) {
            return new Connote(in);
        }

        @Override
        public Connote[] newArray(int size) {
            return new Connote[size];
        }
    };
    @JsonProperty("connoteCode")
    private String connoteCode;
    @JsonProperty("itemDescription")
    private String itemDescription;
    @JsonProperty("currentLocationName")
    private String currentLocationName;
    @JsonProperty("destinationLocationName")
    private String destinationLocationName;
    @JsonProperty("shipperName")
    private String shipperName;
    @JsonProperty("consigneeName")
    private String consigneeName;
    @JsonProperty("totalPart")
    private Integer totalPart;
    @JsonProperty("part")
    private Integer part;
    @JsonProperty("weight")
    private Integer weight;
    @JsonProperty("volume")
    private Integer volume;
    @JsonProperty("latestStatus")
    private String latestStatus;
    @JsonProperty("latestUpdate")
    private String latestUpdate;


    public Connote() {

    }

    public Connote(Parcel in) {
        connoteCode = in.readString();
        itemDescription = in.readString();
        currentLocationName = in.readString();
        destinationLocationName = in.readString();
        shipperName = in.readString();
        consigneeName = in.readString();
        totalPart = in.readInt();
        part = in.readInt();
        weight = in.readInt();
        volume = in.readInt();
        latestStatus = in.readString();
        latestUpdate = in.readString();
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(connoteCode);
        dest.writeString(itemDescription);
        dest.writeString(currentLocationName);
        dest.writeString(destinationLocationName);
        dest.writeString(shipperName);
        dest.writeString(consigneeName);
        dest.writeInt(totalPart);
        dest.writeInt(part);
        dest.writeInt(weight);
        dest.writeInt(volume);
        dest.writeString(latestStatus);
        dest.writeString(latestUpdate);
    }

    @JsonProperty("latestStatus")
    public String getLatestStatus() {
        return latestStatus;
    }

    @JsonProperty("latestStatus")
    public void setLatestStatus(String latestStatus) {
        this.latestStatus = latestStatus;
    }

    @JsonProperty("latestUpdate")
    public String getLatestUpdate() {
        return latestUpdate;
    }

    @JsonProperty("latestUpdate")
    public void setLatestUpdate(String latestUpdate) {
        this.latestUpdate = latestUpdate;
    }

    /**
     * @return The connoteCode
     */
    @JsonProperty("connoteCode")
    public String getConnoteCode() {
        return connoteCode;
    }

    /**
     * @param connoteCode The connoteCode
     */
    @JsonProperty("connoteCode")
    public void setConnoteCode(String connoteCode) {
        this.connoteCode = connoteCode;
    }

    /**
     * @return The itemDescription
     */
    @JsonProperty("itemDescription")
    public String getItemDescription() {
        return itemDescription;
    }

    /**
     * @param itemDescription The itemDescription
     */
    @JsonProperty("itemDescription")
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    /**
     * @return The currentLocationName
     */
    @JsonProperty("currentLocationName")
    public String getCurrentLocationName() {
        return currentLocationName;
    }

    /**
     * @param currentLocationName The currentLocationName
     */
    @JsonProperty("currentLocationName")
    public void setCurrentLocationName(String currentLocationName) {
        this.currentLocationName = currentLocationName;
    }

    /**
     * @return The destinationLocationName
     */
    @JsonProperty("destinationLocationName")
    public String getDestinationLocationName() {
        return destinationLocationName;
    }

    /**
     * @param destinationLocationName The destinationLocationName
     */
    @JsonProperty("destinationLocationName")
    public void setDestinationLocationName(String destinationLocationName) {
        this.destinationLocationName = destinationLocationName;
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
     * @return The totalPart
     */
    @JsonProperty("totalPart")
    public Integer getTotalPart() {
        return totalPart;
    }

    /**
     * @param totalPart The totalPart
     */
    @JsonProperty("totalPart")
    public void setTotalPart(Integer totalPart) {
        this.totalPart = totalPart;
    }

    /**
     * @return The part
     */
    @JsonProperty("part")
    public Integer getPart() {
        return part;
    }

    /**
     * @param part The part
     */
    @JsonProperty("part")
    public void setPart(Integer part) {
        this.part = part;
    }

    /**
     * @return The weight
     */
    @JsonProperty("weight")
    public Integer getWeight() {
        return weight;
    }

    /**
     * @param weight The weight
     */
    @JsonProperty("weight")
    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    /**
     * @return The volume
     */
    @JsonProperty("volume")
    public Integer getVolume() {
        return volume;
    }

    /**
     * @param volume The volume
     */
    @JsonProperty("volume")
    public void setVolume(Integer volume) {
        this.volume = volume;
    }

}
