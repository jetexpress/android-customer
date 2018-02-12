package coid.customer.pickupondemand.jet.utility;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatter
{
    public static String doubleToString(Double value, Integer decimalPlace)
    {
        String decimalPlaceStringFormat = String.valueOf(decimalPlace);
        return String.format(Locale.getDefault(), "%,." + decimalPlaceStringFormat + "f", value);
    }

    public static Double stringToDouble(String valueString)
    {
        NumberFormat numberFormat = NumberFormat.getInstance();
        try
        {
            return numberFormat.parse(valueString).doubleValue();
        }
        catch (Exception ex1)
        {
            if (valueString.isEmpty())
                return 0D;
            try
            {
                NumberFormat indonesianNumberFormat = NumberFormat.getInstance(new Locale("in"));
                return indonesianNumberFormat.parse(valueString).doubleValue();
            }
            catch (Exception ex2)
            {
                try
                {
                    NumberFormat USNumberFormat = NumberFormat.getInstance(Locale.US);
                    return USNumberFormat.parse(valueString).doubleValue();
                }
                catch (Exception ex3)
                {
                    return null;
                }
            }
        }
    }
}
