package com.weekendinc.jet.model.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PricingOption {

    @JsonProperty("minKG")
    private Integer minKG;
    @JsonProperty("minPrice")
    private Integer minPrice;
    @JsonProperty("nKG")
    private Integer nKG;
    @JsonProperty("nPrice")
    private Integer nPrice;
    @JsonProperty("productCode")
    private String productCode;
    @JsonProperty("productName")
    private String productName;
    @JsonProperty("pricingSetCode")
    private String pricingSetCode;
    @JsonProperty("redoc")
    private Boolean redoc;
    @JsonProperty("dueDay")
    private String dueDay;
    @JsonProperty("volumeDivider")
    private Integer volumeDivider;
    @JsonProperty("calculationResult")
    private Integer calculationResult;

    /**
     * @return The minKG
     */
    @JsonProperty("minKG")
    public Integer getMinKG() {
        return minKG;
    }

    /**
     * @param minKG The minKG
     */
    @JsonProperty("minKG")
    public void setMinKG(Integer minKG) {
        this.minKG = minKG;
    }

    /**
     * @return The minPrice
     */
    @JsonProperty("minPrice")
    public Integer getMinPrice() {
        return minPrice;
    }

    /**
     * @param minPrice The minPrice
     */
    @JsonProperty("minPrice")
    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    /**
     * @return The nKG
     */
    @JsonProperty("nKG")
    public Integer getNKG() {
        return nKG;
    }

    /**
     * @param nKG The nKG
     */
    @JsonProperty("nKG")
    public void setNKG(Integer nKG) {
        this.nKG = nKG;
    }

    /**
     * @return The nPrice
     */
    @JsonProperty("nPrice")
    public Integer getNPrice() {
        return nPrice;
    }

    /**
     * @param nPrice The nPrice
     */
    @JsonProperty("nPrice")
    public void setNPrice(Integer nPrice) {
        this.nPrice = nPrice;
    }

    /**
     * @return The productCode
     */
    @JsonProperty("productCode")
    public String getProductCode() {
        return productCode;
    }

    /**
     * @param productCode The productCode
     */
    @JsonProperty("productCode")
    public void setProductCode(String productCode) {
        this.productCode = productCode;
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
     * @return The pricingSetCode
     */
    @JsonProperty("pricingSetCode")
    public String getPricingSetCode() {
        return pricingSetCode;
    }

    /**
     * @param pricingSetCode The pricingSetCode
     */
    @JsonProperty("pricingSetCode")
    public void setPricingSetCode(String pricingSetCode) {
        this.pricingSetCode = pricingSetCode;
    }

    /**
     * @return The redoc
     */
    @JsonProperty("redoc")
    public Boolean getRedoc() {
        return redoc;
    }

    /**
     * @param redoc The redoc
     */
    @JsonProperty("redoc")
    public void setRedoc(Boolean redoc) {
        this.redoc = redoc;
    }

    /**
     * @return The dueDay
     */
    @JsonProperty("dueDay")
    public String getDueDay() {
        return dueDay;
    }

    /**
     * @param dueDay The dueDay
     */
    @JsonProperty("dueDay")
    public void setDueDay(String dueDay) {
        this.dueDay = dueDay;
    }

    /**
     * @return The volumeDivider
     */
    @JsonProperty("volumeDivider")
    public Integer getVolumeDivider() {
        return volumeDivider;
    }

    /**
     * @param volumeDivider The volumeDivider
     */
    @JsonProperty("volumeDivider")
    public void setVolumeDivider(Integer volumeDivider) {
        this.volumeDivider = volumeDivider;
    }

    /**
     * @return The calculationResult
     */
    @JsonProperty("calculationResult")
    public Integer getCalculationResult() {
        return calculationResult;
    }

    /**
     * @param calculationResult The calculationResult
     */
    @JsonProperty("calculationResult")
    public void setCalculationResult(Integer calculationResult) {
        this.calculationResult = calculationResult;
    }

}