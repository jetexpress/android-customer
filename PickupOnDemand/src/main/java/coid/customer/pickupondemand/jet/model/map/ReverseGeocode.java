package coid.customer.pickupondemand.jet.model.map;

import java.util.List;

public class ReverseGeocode
{
    private List<ReverseGeocodeResult> results;
    private String status;

    public List<ReverseGeocodeResult> getResults()
    {
        return results;
    }

    public void setResults(List<ReverseGeocodeResult> results)
    {
        this.results = results;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
