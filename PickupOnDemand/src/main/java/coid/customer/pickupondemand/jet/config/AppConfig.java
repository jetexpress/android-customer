package coid.customer.pickupondemand.jet.config;

public class AppConfig
{
    public static final String MAIN_ACTIVITY_EXTRA_PARAM = "MainActivityExtraParam";
    public static final String JET_SHARED_PREFERENCES = "JETPref";
    public static final String FIRST_TIME_OPENED_PARAM_KEY = "FirstTimeKey";
    public static final String ONE_SIGNAL_ROLE_TAG = "customer";
    public static final int COURIER_ORDER_LAST_ATTEMPT = 3;
    public static final long COURIER_ORDER_ATTEMPT_DELAY_IN_MILLIS = 40000;
    public static final int IMAGE_PICKER_MIN_WIDTH = 200;
    public static final int IMAGE_PICKER_MIN_HEIGHT = 200;
    public static final long LOCATION_LOG_INTERVAL_IN_MILLIS = 30000L;
    public static final long DEFAULT_PAGING_SIZE = 10;
    public static int PICKUP_ACTIVITY_REQUEST_CODE = 1001;
    public static int LOGIN_ACTIVITY_REQUEST_CODE = 1002;

    public static class LoaderId
    {
        public static final int USER_PROFILE_LOADER_ID = 3;
        public static final int NOTIFICATION_PAYLOAD_LOADER_ID = 4;
    }
}