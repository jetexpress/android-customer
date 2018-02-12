package coid.customer.pickupondemand.jet.model;

public class PaymentMethod
{
    private Long id;
    private String code;
    private String name;
    private String description;
    private String proxy;
    private Boolean availableOnJetOutlet;
    private Boolean availableOnFranchise;
    private Boolean availableOnPartner;

    public PaymentMethod()
    {

    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getProxy()
    {
        return proxy;
    }

    public void setProxy(String proxy)
    {
        this.proxy = proxy;
    }

    public Boolean getAvailableOnJetOutlet()
    {
        return availableOnJetOutlet;
    }

    public void setAvailableOnJetOutlet(Boolean availableOnJetOutlet)
    {
        this.availableOnJetOutlet = availableOnJetOutlet;
    }

    public Boolean getAvailableOnFranchise()
    {
        return availableOnFranchise;
    }

    public void setAvailableOnFranchise(Boolean availableOnFranchise)
    {
        this.availableOnFranchise = availableOnFranchise;
    }

    public Boolean getAvailableOnPartner()
    {
        return availableOnPartner;
    }

    public void setAvailableOnPartner(Boolean availableOnPartner)
    {
        this.availableOnPartner = availableOnPartner;
    }
}
