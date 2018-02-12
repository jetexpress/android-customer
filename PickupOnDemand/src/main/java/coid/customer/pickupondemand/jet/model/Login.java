package coid.customer.pickupondemand.jet.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import coid.customer.pickupondemand.jet.config.ApiConfig;
import coid.customer.pickupondemand.jet.model.db.DBContract;
import coid.customer.pickupondemand.jet.model.db.DBQuery;

@Table(name = DBContract.LoginEntry.TABLE_NAME)
public class Login extends Model
{
    @SerializedName("access_token")
    @Column(name = DBContract.LoginEntry.COLUMN_ACCESS_TOKEN)
    private String accessToken;
    @SerializedName("token_type")
    @Column(name = DBContract.LoginEntry.COLUMN_TOKEN_TYPE)
    private String tokenType;
    @SerializedName("expires_in")
    @Column(name = DBContract.LoginEntry.COLUMN_EXPIRES_IN)
    private Long expiresIn;
    @SerializedName("refresh_token")
    @Column(name = DBContract.LoginEntry.COLUMN_REFRESH_TOKEN)
    private String refreshToken;

    @SerializedName("error")
    private String error;
    @SerializedName("error_description")
    private String errorDescription;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn()
    {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn)
    {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken()
    {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken)
    {
        this.refreshToken = refreshToken;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription + "(" + error + ")";
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getFailedMessage()
    {
        String errorMessage;
        if (this.errorDescription != null && !this.errorDescription.isEmpty())
            errorMessage = this.errorDescription;
        else
            errorMessage = this.error;

        return errorMessage;
    }

    public Boolean isFailed()
    {
        return error != null;
    }

    public static Boolean isValid(Login login)
    {
        return login != null;
    }

    public static String getHeaderAuthorization()
    {
        Login login = DBQuery.getSingle(Login.class);
        if (isValid(login))
            return login.getTokenType() + " " + login.getAccessToken();
        else
            return ApiConfig.UNAUTHORIZED;
    }

    @Override
    public String toString()
    {
        return DBContract.LoginEntry.TABLE_NAME + "@" + getId()
                + ", TokenType : " + tokenType
                + ", AccessToken : " + accessToken;
    }

    public String toStringComplete()
    {
        return DBContract.LoginEntry.TABLE_NAME + "@" + getId()
                + ", TokenType : " + this.tokenType
                + ", AccessToken : " + this.accessToken
                + ", ExpiresIn : " + this.expiresIn
                + ", RefreshToken : " + this.refreshToken;
    }
}
