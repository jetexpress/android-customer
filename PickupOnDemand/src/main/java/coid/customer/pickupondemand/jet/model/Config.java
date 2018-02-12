package coid.customer.pickupondemand.jet.model;

import com.google.gson.annotations.SerializedName;

public class Config
{
    @SerializedName("id")
    private long configId;
    private long waitingTime;

    public long getConfigId()
    {
        return configId;
    }

    public void setConfigId(long configId)
    {
        this.configId = configId;
    }

    public long getWaitingTime()
    {
        return waitingTime;
    }

    public void setWaitingTime(long waitingTime)
    {
        this.waitingTime = waitingTime;
    }
}
