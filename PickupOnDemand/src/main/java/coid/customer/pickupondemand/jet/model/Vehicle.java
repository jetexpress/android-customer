package coid.customer.pickupondemand.jet.model;

public class Vehicle
{
    private Long id;
    private String code;
    private String name;
    private String description;
    private Integer wheels;
    private Double weightCapacity;
    private Double volumeCapacity;
    private String iconImageBase64;

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

    public Integer getWheels()
    {
        return wheels;
    }

    public void setWheels(Integer wheels)
    {
        this.wheels = wheels;
    }

    public Double getWeightCapacity()
    {
        return weightCapacity;
    }

    public void setWeightCapacity(Double weightCapacity)
    {
        this.weightCapacity = weightCapacity;
    }

    public Double getVolumeCapacity()
    {
        return volumeCapacity;
    }

    public void setVolumeCapacity(Double volumeCapacity)
    {
        this.volumeCapacity = volumeCapacity;
    }

    public String getIconImageBase64()
    {
        return iconImageBase64;
    }

    public void setIconImageBase64(String iconImageBase64)
    {
        this.iconImageBase64 = iconImageBase64;
    }

    public static Vehicle CAR = getCar();
    public static Vehicle BIKE = getBike();

    private static Vehicle getCar()
    {
        Vehicle car = new Vehicle();
        car.setId(2L);
        car.setCode("CAR");
        car.setName("Car");
        car.setDescription("-");
        car.setWheels(4);
        car.setWeightCapacity(1000D);
        car.setVolumeCapacity(1000D);
        car.setIconImageBase64("-");
        return car;
    }

    private static Vehicle getBike()
    {
        Vehicle bike = new Vehicle();
        bike.setId(3L);
        bike.setCode("BIKE");
        bike.setName("Bike");
        bike.setDescription("motor");
        bike.setWheels(4);
        bike.setWeightCapacity(50D);
        bike.setVolumeCapacity(50D);
        bike.setIconImageBase64("motor");
        return bike;
    }
}
