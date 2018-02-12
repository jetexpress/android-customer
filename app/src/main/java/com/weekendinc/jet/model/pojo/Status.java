package com.weekendinc.jet.model.pojo;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
                           "err",
                           "err_msg",
                           "elapsetime",
                           "memoryusage"
                   })
public class Status {

    @JsonProperty("err")
    private String err;
    @JsonProperty("err_msg")
    private String errMsg;
    @JsonProperty("elapsetime")
    private String elapsetime;
    @JsonProperty("memoryusage")
    private String memoryusage;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The err
     */
    @JsonProperty("err")
    public String getErr() {
        return err;
    }

    /**
     * @param err The err
     */
    @JsonProperty("err")
    public void setErr(String err) {
        this.err = err;
    }

    public Status withErr(String err) {
        this.err = err;
        return this;
    }

    /**
     * @return The errMsg
     */
    @JsonProperty("err_msg")
    public String getErrMsg() {
        return errMsg;
    }

    /**
     * @param errMsg The err_msg
     */
    @JsonProperty("err_msg")
    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Status withErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }

    /**
     * @return The elapsetime
     */
    @JsonProperty("elapsetime")
    public String getElapsetime() {
        return elapsetime;
    }

    /**
     * @param elapsetime The elapsetime
     */
    @JsonProperty("elapsetime")
    public void setElapsetime(String elapsetime) {
        this.elapsetime = elapsetime;
    }

    public Status withElapsetime(String elapsetime) {
        this.elapsetime = elapsetime;
        return this;
    }

    /**
     * @return The memoryusage
     */
    @JsonProperty("memoryusage")
    public String getMemoryusage() {
        return memoryusage;
    }

    /**
     * @param memoryusage The memoryusage
     */
    @JsonProperty("memoryusage")
    public void setMemoryusage(String memoryusage) {
        this.memoryusage = memoryusage;
    }

    public Status withMemoryusage(String memoryusage) {
        this.memoryusage = memoryusage;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Status withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(err).append(errMsg).append(elapsetime).append(memoryusage).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Status) == false) {
            return false;
        }
        Status rhs = ((Status) other);
        return new EqualsBuilder().append(err, rhs.err).append(errMsg, rhs.errMsg).append(elapsetime, rhs.elapsetime).append(memoryusage, rhs.memoryusage).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
