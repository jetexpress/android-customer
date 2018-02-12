package com.weekendinc.jet.model.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status_code",
        "message",
        "errors"
})
public class ResultDefault {

    @JsonProperty("error")
    private String error;
    @JsonProperty("status_code")
    private Integer statusCode;
    @JsonProperty("message")
    private String message;
    @JsonProperty("errors")
    private List<Object> errors = new ArrayList<Object>();

    /**
     * @return The statusCode
     */
    @JsonProperty("status_code")
    public Integer getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode The status_code
     */
    @JsonProperty("status_code")
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return The message
     */
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return The errors
     */
    @JsonProperty("errors")
    public List<Object> getErrors() {
        return errors;
    }

    /**
     * @param errors The errors
     */
    @JsonProperty("errors")
    public void setErrors(List<Object> errors) {
        this.errors = errors;
    }

    @JsonProperty("error")
    public String getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(String error) {
        this.error = error;
    }
}
