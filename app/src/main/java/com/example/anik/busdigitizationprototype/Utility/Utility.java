package com.example.anik.busdigitizationprototype.Utility;
import android.app.ProgressDialog;
import android.widget.Toast;
import android.app.Activity;

/**
 * This class contains the state of the user and a progressdialog box shown while loading the search results
 */
public class Utility {

    public static ProgressDialog pd;
    public static class UserData
    {
        public static String user_name="";
        public static int state = 0;          //0 = idle; 1 = running
        public static String source = "";
        public static String destination = "";
        public static String bus_taken = "";

        public static String start_time = "";
        public static String end_time = "";
    }
}
