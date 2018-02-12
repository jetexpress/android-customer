package coid.customer.pickupondemand.jet.model.map;

import com.google.gson.Gson;

import java.util.List;

public class Direction
{
    private List<Route> routes;

    public List<Route> getRoutes()
    {
        return routes;
    }

    public void setRoutes(List<Route> routes)
    {
        this.routes = routes;
    }

    @Override
    public String toString()
    {
        return new Gson().toJson(this);
    }
}
