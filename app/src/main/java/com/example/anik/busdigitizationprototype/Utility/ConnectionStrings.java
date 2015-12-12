package com.example.anik.busdigitizationprototype.Utility;

/**
 * Contains String constants for communicating with the server
 */
public class ConnectionStrings {
    public static String LOGIN_SQ = "LOGIN";        //followed by 'usr' and 'password'. Send 0 : Unsuccessful login; 	1 : Successful login
    public static String SIGNUP_SQ = "SIGNUP";        //followed by 'usr' and 'password'. Send 0 : Unsuccessful Signup; 	1 : Successful Signup
    public static String TEST_CONN = "PING";        //for testing connections
    public static String LOCATION_LIST = "LOCATIONS";
    public static String ROUTE_RESULT = "ROUTE_RESULTS";
    public static String WAY_POINTS = "WAYPOINTS";
    public static String INSERT_HAZARD = "INSERT_HAZARD";
    public static String RETRIVE_HAZARD = "RETRIVE_HAZARD";
    public static String FEEDBACK = "FEEDBACK";
    public static String LOG_START = "LOG_START";
    public static String LOG_END = "LOG_END";
}
