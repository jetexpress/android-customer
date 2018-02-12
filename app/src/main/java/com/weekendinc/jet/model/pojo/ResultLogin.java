package com.weekendinc.jet.model.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultLogin extends ResultDefault {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_typee")
    private String tokenType;
    @JsonProperty("expires_in")
    private Integer expiresIn;
    @JsonProperty("refresh_token")
    private String refreshToken;

    public ResultLogin()
    {

    }

    public ResultLogin(String accessToken, String tokenType, String refreshToken)
    {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.expiresIn = 0;
    }

    /**
     * @return The accessToken
     */
    @JsonProperty("access_token")
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * @param accessToken The access_token
     */
    @JsonProperty("access_token")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * @return The tokenType
     */
    @JsonProperty("token_type")
    public String getTokenType() {
        return tokenType;
    }

    /**
     * @param tokenType The token_type
     */
    @JsonProperty("token_type")
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    /**
     * @return The expiresIn
     */
    @JsonProperty("expires_in")
    public Integer getExpiresIn() {
        return expiresIn;
    }

    /**
     * @param expiresIn The expires_in
     */
    @JsonProperty("expires_in")
    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    /**
     * @return The refreshToken
     */
    @JsonProperty("refresh_token")
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * @param refreshToken The refresh_token
     */
    @JsonProperty("refresh_token")
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /** FIX SKIP BEHAVIOUR ~ uletbe */
    public Boolean isDummy()
    {
        return accessToken.equals(DUMMY_ACCESS_TOKEN) &&
                tokenType.equals(DUMMY_TOKEN_TYPE) &&
                refreshToken.equals(DUMMY_REFRESH_TOKEN);
    }

    public static String DUMMY_ACCESS_TOKEN = "dummyAccessToken";
    public static String DUMMY_TOKEN_TYPE = "dummyTokenType";
    public static String DUMMY_REFRESH_TOKEN = "dummyRefreshToken";

    public static ResultLogin getDummyResultLogin()
    {
        ResultLogin resultLogin = new ResultLogin();
        resultLogin.setAccessToken(DUMMY_ACCESS_TOKEN);
        resultLogin.setTokenType(DUMMY_TOKEN_TYPE);
        resultLogin.setExpiresIn(0);
        resultLogin.setRefreshToken(DUMMY_REFRESH_TOKEN);
        return resultLogin;
    }
    /** FIX SKIP BEHAVIOUR ~ uletbe ~ END*/
}
