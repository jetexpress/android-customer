package com.weekendinc.jet.model.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import coid.customer.pickupondemand.jet.base.IBaseItemSelectModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Location implements IBaseItemSelectModel{

    @JsonProperty("code")
    private String code;
    @JsonProperty("display")
    private String display;
    @JsonProperty("branchCode")
    private String branchCode;

    /**
     * @return The code
     */
    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    /**
     * @param code The code
     */
    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
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

    @Override
    public CharSequence getItemSelectCode()
    {
        return this.code;
    }

    @Override
    public CharSequence getItemSelectDescription()
    {
        return this.display;
    }
}
