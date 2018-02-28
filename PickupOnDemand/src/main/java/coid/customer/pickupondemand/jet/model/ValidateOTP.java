package coid.customer.pickupondemand.jet.model;

import com.activeandroid.Model;

/**
 * Created by jordan.leonardi on 2/22/2018.
 */

public class ValidateOTP extends Model {

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public boolean isValid;



}
