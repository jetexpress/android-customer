package coid.customer.pickupondemand.jet.model;

public class BroadcastRecipient
{
    private String id;
    private Long recipients;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public Long getRecipients()
    {
        if (recipients == null)
            return  0L;
        return recipients;
    }

    public void setRecipients(Long recipients)
    {
        this.recipients = recipients;
    }
}
