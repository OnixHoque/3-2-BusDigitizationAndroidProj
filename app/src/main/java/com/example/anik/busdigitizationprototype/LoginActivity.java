package com.example.anik.busdigitizationprototype;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Looper;

import com.example.anik.busdigitizationprototype.Utility.ConnectionManager;
import com.example.anik.busdigitizationprototype.Utility.ConnectionStrings;
import com.example.anik.busdigitizationprototype.Utility.Utility;

import org.w3c.dom.Text;

import java.io.IOException;

public class LoginActivity extends Activity {
    private static Button button_signup;
    private static Button button_login;
    private static TextView txt;
    private static EditText txtUser, txtPass;
    public static Context mContext;
    //public static Handler UIHandler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;

        txtUser = (EditText)findViewById(R.id.editText);
        txtPass = (EditText)findViewById(R.id.editText2);
        /*
        Set the default ip and check the connection. If working, very good.
            If not working, prompt the user to enter new ip address. If working, very good.
                If not working, exit.
         */
        ConnectionManager.ip_address = getResources().getString(R.string.default_ip);
        //ConnectionManager.ip_address = "10.1.1.39";
        Thread t = new Thread(new ConnectionManager());
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (ConnectionManager.connEstablished == false)
        {
            get_new_ip();
        }

        OnClickButtonListener();
    }
    public class ClientListener implements Runnable{
        @Override
        public void run() {
            String line;

            try {
                while(true)
                {
                    if (ConnectionManager.in == null || ConnectionManager.echoSocket.isConnected() == false)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //txt.setText("Machine Says Hi");
                                Toast.makeText(getApplicationContext(), "Connection does not exist", Toast.LENGTH_SHORT).show();
                            }
                        });
                        continue;
                    }
                    line = ConnectionManager.in.readLine();
                    if (line.equals(ConnectionStrings.TEST_CONN))
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //txt.setText("Machine Says Hi");
                                Toast.makeText(getApplicationContext(), "Machine Says Hi", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    }
                    else if (line.equals(ConnectionStrings.LOGIN_SQ))
                    {
                        line = ConnectionManager.in.readLine();
                        if (line.equals("1"))
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Toast.makeText(getApplicationContext(), "Correct Infromation", Toast.LENGTH_LONG).show();
                                    Utility.UserData.user_name = txtUser.getText().toString();
                                    Intent intent = new Intent("com.example.anik.busdigitizationprototype.StartJourney");
                                    finish();
                                    startActivity(intent);
                                }
                            });
                        }
                        else
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Invalid Infromation", Toast.LENGTH_LONG).show();
                                    txtPass.setText("");
                                    txtUser.setText("");
                                    txtUser.requestFocus();

                                }
                            });
                        }
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (Exception e1)
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ConnectionManager.connEstablished = false;
                        Toast.makeText(getApplicationContext(), "Connection could not be established", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
    public void OnClickButtonListener()
    {
        button_signup  = (Button) findViewById(R.id.button2);
        button_login  = (Button) findViewById(R.id.button);

        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.anik.busdigitizationprototype.SignUp");
                startActivity(intent);
            }

        });
        txtPass.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    ConnectionManager.out.println(ConnectionStrings.LOGIN_SQ);
                    ConnectionManager.out.println(txtUser.getText());
                    ConnectionManager.out.println(txtPass.getText());
                    new Thread(new ClientListener()).start();
                    return true;
                }
                return false;
            }
        });
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionManager.out.println(ConnectionStrings.LOGIN_SQ);
                ConnectionManager.out.println(txtUser.getText());
                ConnectionManager.out.println(txtPass.getText());
                new Thread(new ClientListener()).start();

                //ConnectionManager.connectToServer cn = new ConnectionManager.connectToServer();
                //cn.execute();
                //boolean i = ConnectionManager.connnectToServer();
                //if (i==true)
                //    Toast.makeText(getApplicationContext(), "Connection Established", Toast.LENGTH_SHORT).show();
                //else
                //    Toast.makeText(getApplicationContext(), "Connection Could not be Established", Toast.LENGTH_SHORT).show();
                /*Intent intent = new Intent("com.example.anik.busdigitizationprototype.StartJourney");
                finish();
                startActivity(intent);*/
            }

        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void get_new_ip()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Default IP Address could not be resolved. Enter another Server IP:");
        // Set up the input
        final EditText input = new EditText(mContext);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(mContext, "Connection could not be established, Enter new IP or Press Cancel to exit", Toast.LENGTH_SHORT).show();
                ConnectionManager.ip_address = input.getText().toString();
                Log.d("GOT IP ADDRESS:", ConnectionManager.ip_address);
                Thread t = new Thread(new ConnectionManager());
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("Login Debug", "Conn man returned with status " + ConnectionManager.connEstablished);
                if (ConnectionManager.connEstablished == false)
                {
                    System.exit(-1);
                }
                dialog.cancel();
                Toast.makeText(mContext, "Connection established", Toast.LENGTH_SHORT).show();


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(mContext, "Connection could not be established", Toast.LENGTH_SHORT).show();
                dialog.cancel();
                System.exit(-1);
            }
        });
        builder.show();
    }

}
