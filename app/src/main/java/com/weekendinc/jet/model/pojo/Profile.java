package com.weekendinc.jet.model.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Profile {

    @JsonProperty("username")
    private String username;
    @JsonProperty("fullname")
    private String fullname;
    @JsonProperty("dateOfBirth")
    private String dateOfBirth;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("address")
    private String address;
    @JsonProperty("email")
    private String email;
    @JsonProperty("wallet")
    private Wallet wallet;

    /**
     * @return The username
     */
    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    /**
     * @param username The username
     */
    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return The fullname
     */
    @JsonProperty("fullname")
    public String getFullname() {
        return fullname;
    }

    /**
     * @param fullname The fullname
     */
    @JsonProperty("fullname")
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * @return The dateOfBirth
     */
    @JsonProperty("dateOfBirth")
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @param dateOfBirth The dateOfBirth
     */
    @JsonProperty("dateOfBirth")
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * @return The phoneNumber
     */
    @JsonProperty("phoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber The phoneNumber
     */
    @JsonProperty("phoneNumber")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return The address
     */
    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    /**
     * @param address The address
     */
    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return The email
     */
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The wallet
     */
    @JsonProperty("wallet")
    public Wallet getWallet() {
        return wallet;
    }

    /**
     * @param wallet The wallet
     */
    @JsonProperty("wallet")
    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

}
