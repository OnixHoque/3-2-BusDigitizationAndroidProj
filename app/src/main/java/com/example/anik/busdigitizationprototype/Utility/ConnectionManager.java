package com.example.anik.busdigitizationprototype.Utility;

import android.os.AsyncTask;

import java.io.*;
import java.net.*;

/**
 * Class that defines all  materials for establishing connection with the server
 */

public class ConnectionManager implements Runnable
{
    public static String LOGIN_SQ = "LOGIN";        //followed by 'usr' and 'password'. Send 0 : Unsuccessful login; 	1 : Successful login
    public static String SIGNUP_SQ = "SIGNUP";        //followed by 'usr' and 'password'. Send 0 : Unsuccessful Signup; 	1 : Successful Signup
    public static String TEST_CONN = "PING";        //say hi

    public static Socket echoSocket;
    public static PrintWriter out;
    public static BufferedReader in;
    static boolean connEstablished = false;
    public void run()
    {
        try
        {
            InetAddress serverAddr = InetAddress.getByName("10.0.2.2");
            echoSocket = new Socket(serverAddr, 8088);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        }
        catch (UnknownHostException e1)
        {
            e1.printStackTrace();
        } catch (IOException e1)
        {
            e1.printStackTrace();
        }
    }
}
/*
public class ConnectionManager {
    public static String LOGIN_SQ = "LOGIN";        //followed by 'usr' and 'password'. Send 0 : Unsuccessful login; 	1 : Successful login
    public static String SIGNUP_SQ = "SIGNUP";        //followed by 'usr' and 'password'. Send 0 : Unsuccessful Signup; 	1 : Successful Signup
    public static String TEST_CONN = "PING";        //say hi

    static Socket echoSocket;
    static PrintWriter out;
    static BufferedReader in;
    static boolean connEstablished = false;

    public class connectToServer extends AsyncTask<Void, Void, Void>
    {
        protected Void doInBackground(Void... params)
        {
            String hostName = "10.0.2.2";
            int portNumber = 8088;
            try
            {
                echoSocket = new Socket(hostName, portNumber);
                connEstablished = true;
                out = new PrintWriter(echoSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                //return true;
            }
            catch (UnknownHostException e) {

                System.err.println("Don't know about host " + hostName);
                //return false;
                //System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to " +
                        hostName);
                //return false;
                //System.exit(1);
            }
            return null;
        }
    }
    public static boolean _connnectToServer() {
        //boolean success_id = false;

        if (connEstablished) return false;
        String hostName = "10.0.2.2";
        int portNumber = 8088;

        try
        //BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        {
            echoSocket = new Socket(hostName, portNumber);
            connEstablished = true;
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            return true;
        } catch (UnknownHostException e) {

            System.err.println("Don't know about host " + hostName);
            return false;
            //System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            return false;
            //System.exit(1);
        }
        //return true;
    }
}
*/