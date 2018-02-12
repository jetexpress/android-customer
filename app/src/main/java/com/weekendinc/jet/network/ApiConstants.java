package com.weekendinc.jet.network;

import coid.customer.pickupondemand.jet.config.ApiConfig;

public class ApiConstants {

    public static final int retry = 3;
    public static final int HTTP_CONNECT_TIMEOUT = 60*20*1000; // milliseconds
    public static final int HTTP_READ_TIMEOUT = 60*20*1000; // milliseconds
    public static String RESPONSE_VALUE_SUCCESS = "success";
    public static String PARAM_VALUE_OFF = "0";
    public static String PARAM_VALUE_ON = "1";

    /** DEV */
//    public static final String AUTH_BASE_URL = "http://11.11.1.42:30001";
//    public static final String RESOURCE_BASE_URL = "http://11.11.1.42:30002";

    /** PRD */
//    public static final String AUTH_BASE_URL = "http://jet-api-auth.azurewebsites.net";
//    public static final String RESOURCE_BASE_URL = "http://jet-api-resource.azurewebsites.net";

    public static final String AUTH_BASE_URL = ApiConfig.AUTH_URL;
    public static final String RESOURCE_BASE_URL = ApiConfig.RESOURCE_URL;
}
