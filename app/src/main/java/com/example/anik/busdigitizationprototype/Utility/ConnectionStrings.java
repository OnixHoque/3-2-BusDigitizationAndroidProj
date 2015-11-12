package com.example.anik.busdigitizationprototype.Utility;

/**
 * Contains String constants for communicating with the server
 */
public class ConnectionStrings {
    public static String LOGIN_SQ = "LOGIN";        //followed by 'usr' and 'password'. Send 0 : Unsuccessful login; 	1 : Successful login
    public static String SIGNUP_SQ = "SIGNUP";        //followed by 'usr' and 'password'. Send 0 : Unsuccessful Signup; 	1 : Successful Signup
    public static String TEST_CONN = "PING";        //say hi
    public static String LOCATION_LIST = "LOCATIONS";
}
