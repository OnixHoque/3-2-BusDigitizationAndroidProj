package com.example.anik.busdigitizationprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anik.busdigitizationprototype.Utility.ConnectionManager;
import com.example.anik.busdigitizationprototype.Utility.ConnectionStrings;

import java.io.IOException;
import java.net.Socket;

public class LoginActivity extends AppCompatActivity {
    private static Button button_signup;
    private static Button button_login;
    private static TextView txt;
    private static EditText txtUser, txtPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        OnClickButtonListener();
        //txt = (TextView)findViewById(R.id.textView);
        txtUser = (EditText)findViewById(R.id.editText);
        txtPass = (EditText)findViewById(R.id.editText2);
        new Thread(new ConnectionManager()).start();
    }
    public class ClientListener implements Runnable{
        @Override
        public void run() {
            String line;

            try {
                while(true)
                {
                    if (ConnectionManager.in == null)
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
                                    Toast.makeText(getApplicationContext(), "Correct Infromation", Toast.LENGTH_LONG).show();
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
                                }
                            });
                        }
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
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

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionManager.out.println(ConnectionStrings.LOGIN_SQ);
                ConnectionManager.out.println(txtUser.getText());
                ConnectionManager.out.println(txtPass.getText());
                new Thread(new ClientListener()).start();
                txtPass.setText("");
                txtUser.setText("");
                txtUser.requestFocus();

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
}
