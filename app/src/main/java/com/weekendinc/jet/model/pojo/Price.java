package com.weekendinc.jet.model.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Price extends ResultDefault {

    @JsonProperty("value")
    private String value;
    @JsonProperty("label")
    private String label;
    @JsonProperty("branchCode")
    private String branchCode;
    @JsonProperty("locationCode")
    private String locationCode;
    @JsonProperty("locationName")
    private String locationName;
    @JsonProperty("locationType")
    private String locationType;
    @JsonProperty("display")
    private String display;
    @JsonProperty("pricingOptions")
    private List<PricingOption> pricingOptions = new ArrayList<PricingOption>();

    /**
     * @return The value
     */
    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    /**
     * @param value The value
     */
    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return The label
     */
    @JsonProperty("label")
    public String getLabel() {
        return label;
    }

    /**
     * @param label The label
     */
    @JsonProperty("label")
    public void setLabel(String label) {
        this.label = label;
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
     * @return The locationType
     */
    @JsonProperty("locationType")
    public String getLocationType() {
        return locationType;
    }

    /**
     * @param locationType The locationType
     */
    @JsonProperty("locationType")
    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    /**
     * @return The display
     */
    @JsonProperty("display")
    public String getDisplay() {
        return display;
    }

    /**
     * @param display The display
     */
    @JsonProperty("display")
    public void setDisplay(String display) {
        this.display = display;
    }

    /**
     * @return The pricingOptions
     */
    @JsonProperty("pricingOptions")
    public List<PricingOption> getPricingOptions() {
        return pricingOptions;
    }

    /**
     * @param pricingOptions The pricingOptions
     */
    @JsonProperty("pricingOptions")
    public void setPricingOptions(List<PricingOption> pricingOptions) {
        this.pricingOptions = pricingOptions;
    }

}
