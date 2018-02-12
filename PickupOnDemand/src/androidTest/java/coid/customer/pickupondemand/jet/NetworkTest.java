package coid.customer.pickupondemand.jet;

import android.test.AndroidTestCase;

import com.activeandroid.ActiveAndroid;

import coid.customer.pickupondemand.jet.model.Pickup;
import coid.customer.pickupondemand.jet.request.PickupDraftCreateRequest;
import retrofit2.Response;

public class NetworkTest extends AndroidTestCase
{
    public void testAAAB()
    {
        assertTrue(false);
    }

    public void testAAA() throws Exception
    {
        ActiveAndroid.initialize(JETApplication.getContext());

        double lat = -6.224971666666666;
        double lng = 106.80968499999999;
        PickupDraftCreateRequest pickupDraftCreateRequest = new PickupDraftCreateRequest(null, lat, lng);
        Response<Pickup> response = pickupDraftCreateRequest.getCall().execute();
        assertTrue(response.body() != null);
    }
}
