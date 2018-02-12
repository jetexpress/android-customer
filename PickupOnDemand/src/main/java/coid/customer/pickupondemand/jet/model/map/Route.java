package coid.customer.pickupondemand.jet.model.map;

import java.util.List;

public class Route
{
    private List<Leg> legs;

    public List<Leg> getLegs()
    {
        return legs;
    }

    public void setLegs(List<Leg> legs)
    {
        this.legs = legs;
    }
}
