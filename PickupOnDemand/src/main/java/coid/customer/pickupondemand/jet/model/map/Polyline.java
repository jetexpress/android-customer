package coid.customer.pickupondemand.jet.model.map;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Polyline
{
    private String points;

    public String getPoints()
    {
        return points;
    }

    public void setPoints(String points)
    {
        this.points = points;
    }

    public ArrayList<LatLng> getDecodedPolyline()
    {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = points.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = points.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = points.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }
}
