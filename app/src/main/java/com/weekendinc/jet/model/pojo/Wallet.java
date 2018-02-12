package com.weekendinc.jet.model.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Wallet {

    @JsonProperty("bonusCredit")
    private Integer bonusCredit;
    @JsonProperty("topupCredit")
    private Integer topupCredit;

    /**
     * @return The bonusCredit
     */
    @JsonProperty("bonusCredit")
    public Integer getBonusCredit() {
        return bonusCredit;
    }

    /**
     * @param bonusCredit The bonusCredit
     */
    @JsonProperty("bonusCredit")
    public void setBonusCredit(Integer bonusCredit) {
        this.bonusCredit = bonusCredit;
    }

    /**
     * @return The topupCredit
     */
    @JsonProperty("topupCredit")
    public Integer getTopupCredit() {
        return topupCredit;
    }

    /**
     * @param topupCredit The topupCredit
     */
    @JsonProperty("topupCredit")
    public void setTopupCredit(Integer topupCredit) {
        this.topupCredit = topupCredit;
    }

}