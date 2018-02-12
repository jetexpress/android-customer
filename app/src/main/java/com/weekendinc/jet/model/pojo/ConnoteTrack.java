package com.weekendinc.jet.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Calendar;
import java.util.TimeZone;

import coid.customer.pickupondemand.jet.Utility;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConnoteTrack {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("connoteId")
    private Integer connoteId;
    @JsonProperty("connoteCode")
    private String connoteCode;
    @JsonProperty("trackDate")
    private String trackDate;
    @JsonProperty("branchId")
    private Integer branchId;
    @JsonProperty("branchCode")
    private String branchCode;
    @JsonProperty("branchName")
    private String branchName;
    @JsonProperty("branchAddress")
    private String branchAddress;
    @JsonProperty("branchIsMitra")
    private String branchIsMitra;
    @JsonProperty("locationId")
    private Integer locationId;
    @JsonProperty("locationCode")
    private String locationCode;
    @JsonProperty("locationName")
    private String locationName;
    @JsonProperty("status")
    private String status;
    @JsonProperty("statusCode")
    private Integer statusCode;
    @JsonProperty("geoPoint")
    private String geoPoint;
    @JsonProperty("note")
    private String note;
    @JsonProperty("operationStatusId")
    private Integer operationStatusId;
    @JsonProperty("operationStatusCode")
    private String operationStatusCode;
    @JsonProperty("operationStatusName")
    private String operationStatusName;
    @JsonProperty("operationStatus")
    private OperationStatus operationStatus;
    @JsonProperty("displayLocation")
    private String displayLocation;
    @JsonProperty("displayedStatus")
    private String displayedStatus;
    @JsonProperty("kioskName")
    private String kioskName;
    @JsonProperty("location")
    private String location;
    @JsonProperty("deliveryDate")
    private String deliveryDate;

    @JsonProperty("branchAddress")
    public String getBranchAddress() {
        return branchAddress;
    }

    @JsonProperty("branchAddress")
    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
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
     * @return The connoteId
     */
    @JsonProperty("connoteId")
    public Integer getConnoteId() {
        return connoteId;
    }

    /**
     * @param connoteId The connoteId
     */
    @JsonProperty("connoteId")
    public void setConnoteId(Integer connoteId) {
        this.connoteId = connoteId;
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
     * @return The trackDate
     */
    @JsonProperty("trackDate")
    public String getTrackDate() {
        return trackDate;
    }

    /**
     * @param trackDate The trackDate
     */
    @JsonProperty("trackDate")
    public void setTrackDate(String trackDate) {
        this.trackDate = trackDate;
    }

    /**
     * @return The branchId
     */
    @JsonProperty("branchId")
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId The branchId
     */
    @JsonProperty("branchId")
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    /**
     * @return The branchCode
     */
    @JsonProperty("branchCode")
    public String getBranchCode() {
        return branchCode;
    }

    /**
     * @param branchCode The branchCode
     */
    @JsonProperty("branchCode")
    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    /**
     * @return The branchName
     */
    @JsonProperty("branchName")
    public String getBranchName() {
        return branchName;
    }

    /**
     * @param branchName The branchName
     */
    @JsonProperty("branchName")
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    /**
     * @return The locationId
     */
    @JsonProperty("locationId")
    public Integer getLocationId() {
        return locationId;
    }

    /**
     * @param locationId The locationId
     */
    @JsonProperty("locationId")
    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    /**
     * @return The locationCode
     */
    @JsonProperty("locationCode")
    public String getLocationCode() {
        return locationCode;
    }

    /**
     * @param locationCode The locationCode
     */
    @JsonProperty("locationCode")
    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    /**
     * @return The locationName
     */
    @JsonProperty("locationName")
    public String getLocationName() {
        return locationName;
    }

    /**
     * @param locationName The locationName
     */
    @JsonProperty("locationName")
    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    /**
     * @return The status
     */
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The statusCode
     */
    @JsonProperty("statusCode")
    public Integer getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode The statusCode
     */
    @JsonProperty("statusCode")
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return The geoPoint
     */
    @JsonProperty("geoPoint")
    public String getGeoPoint() {
        return geoPoint;
    }

    /**
     * @param geoPoint The geoPoint
     */
    @JsonProperty("geoPoint")
    public void setGeoPoint(String geoPoint) {
        this.geoPoint = geoPoint;
    }

    /**
     * @return The note
     */
    @JsonProperty("note")
    public String getNote() {
        return note;
    }

    /**
     * @param note The note
     */
    @JsonProperty("note")
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return The operationStatusId
     */
    @JsonProperty("operationStatusId")
    public Integer getOperationStatusId() {
        return operationStatusId;
    }

    /**
     * @param operationStatusId The operationStatusId
     */
    @JsonProperty("operationStatusId")
    public void setOperationStatusId(Integer operationStatusId) {
        this.operationStatusId = operationStatusId;
    }

    /**
     * @return The operationStatusCode
     */
    @JsonProperty("operationStatusCode")
    public String getOperationStatusCode() {
        return operationStatusCode;
    }

    /**
     * @param operationStatusCode The operationStatusCode
     */
    @JsonProperty("operationStatusCode")
    public void setOperationStatusCode(String operationStatusCode) {
        this.operationStatusCode = operationStatusCode;
    }

    /**
     * @return The operationStatusName
     */
    @JsonProperty("operationStatusName")
    public String getOperationStatusName() {
        return operationStatusName;
    }

    /**
     * @param operationStatusName The operationStatusName
     */
    @JsonProperty("operationStatusName")
    public void setOperationStatusName(String operationStatusName) {
        this.operationStatusName = operationStatusName;
    }

    /**
     * @return The operationStatus
     */
    @JsonProperty("operationStatus")
    public OperationStatus getOperationStatus() {
        return operationStatus;
    }

    /**
     * @param operationStatus The operationStatus
     */
    @JsonProperty("operationStatus")
    public void setOperationStatus(OperationStatus operationStatus) {
        this.operationStatus = operationStatus;
    }

    /**
     * @return The displayLocation
     */
    @JsonProperty("displayLocation")
    public String getDisplayLocation() {
        return displayLocation;
    }

    /**
     * @param displayLocation The displayLocation
     */
    @JsonProperty("displayLocation")
    public void setDisplayLocation(String displayLocation) {
        this.displayLocation = displayLocation;
    }

    /**
     * @return The displayedStatus
     */
    @JsonProperty("displayedStatus")
    public String getDisplayedStatus() {
        return displayedStatus;
    }

    /**
     * @param displayedStatus The displayedStatus
     */
    @JsonProperty("displayedStatus")
    public void setDisplayedStatus(String displayedStatus) {
        this.displayedStatus = displayedStatus;
    }

    /**
     * @return The kioskName
     */
    @JsonProperty("kioskName")
    public String getKioskName() {
        return kioskName;
    }

    /**
     * @param kioskName The kioskName
     */
    @JsonProperty("kioskName")
    public void setKioskName(String kioskName) {
        this.kioskName = kioskName;
    }

    /**
     * @return The location
     */
    @JsonProperty("location")
    public String getLocation() {
        return location;
    }

    /**
     * @param location The location
     */
    @JsonProperty("location")
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return The deliveryDate
     */
    @JsonProperty("deliveryDate")
    public String getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * @param deliveryDate The deliveryDate
     */
    @JsonProperty("deliveryDate")
    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Long getTrackDateTimeInMillisUTC()
    {
        return Utility.DateFormat.getMillisFromDateString(this.trackDate, "yyyy-MM-dd'T'HH:mm:ss", true);
    }

    public String getTrackDayOfMonthString()
    {
        return Utility.DateFormat.getDateStringFromMillis(getTrackDateTimeInMillisUTC(), "dd", false);
    }

    public String getTrackMonthString()
    {
        return Utility.DateFormat.getDateStringFromMillis(getTrackDateTimeInMillisUTC(), "MMM", false);
    }

    public String getTrackYearString()
    {
        return Utility.DateFormat.getDateStringFromMillis(getTrackDateTimeInMillisUTC(), "yyyy", false);
    }

    public String getTrackTimeString()
    {
        return Utility.DateFormat.getDateStringFromMillis(getTrackDateTimeInMillisUTC(), "HH:mm", false);
    }

    public boolean isTrackSameDay(ConnoteTrack connoteTrack)
    {
        return this.getTrackDayOfMonthString().equals(connoteTrack.getTrackDayOfMonthString())
                && getTrackMonthString().equals(connoteTrack.getTrackMonthString())
                && getTrackYearString().equals(connoteTrack.getTrackYearString());
    }
}
