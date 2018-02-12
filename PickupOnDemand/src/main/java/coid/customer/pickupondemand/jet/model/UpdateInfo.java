package coid.customer.pickupondemand.jet.model;

public class UpdateInfo
{
    private boolean isForceUpdate;
    private String message;

    public boolean isForceUpdate()
    {
        return isForceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate)
    {
        isForceUpdate = forceUpdate;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
