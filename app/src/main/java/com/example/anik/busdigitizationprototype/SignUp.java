package com.example.anik.busdigitizationprototype;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.anik.busdigitizationprototype.Utility.ConnectionManager;
import com.example.anik.busdigitizationprototype.Utility.ConnectionStrings;

import java.io.IOException;

public class SignUp extends Activity {
    private static Button button_finish;
    private static Button button_signup;
    private static EditText txtUser, txtPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtUser = (EditText)findViewById(R.id.editText3);
        txtPass = (EditText)findViewById(R.id.editText4);

        OnClickButtonListener();
    }

    public class ClientListener implements Runnable{
        @Override
        public void run() {
            String line;

            try {
                while(true)
                {
                    if (ConnectionManager.in == null) continue;
                    line = ConnectionManager.in.readLine();
                    if (line.equals(ConnectionStrings.SIGNUP_SQ))
                    {
                        line = ConnectionManager.in.readLine();
                        if (line.equals("1"))
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "New Account Created.", Toast.LENGTH_LONG).show();
                                    button_signup.setEnabled(true);
                                }
                            });
                        }
                        else
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Sorry, Account name already exists.", Toast.LENGTH_LONG).show();
                                    button_signup.setEnabled(true);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }
    public void OnClickButtonListener()
    {
        button_finish  = (Button) findViewById(R.id.button4);
        button_signup = (Button) findViewById(R.id.button3);
        button_signup.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                if (txtUser.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Username cannot be empty", Toast.LENGTH_SHORT).show();
                    txtUser.requestFocus();
                }
                else if (txtPass.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
                    txtPass.requestFocus();
                }
                else {
                    if (ConnectionManager.out == null)
                        Toast.makeText(getApplicationContext(), "Network connection is unavailable.", Toast.LENGTH_SHORT).show();
                    else {
                        button_signup.setEnabled(false);
                        ConnectionManager.out.println(ConnectionStrings.SIGNUP_SQ);
                        ConnectionManager.out.println(txtUser.getText());
                        ConnectionManager.out.println(txtPass.getText());
                        new Thread(new ClientListener()).start();
                        txtPass.setText("");
                        txtUser.setText("");
                        txtUser.requestFocus();
                    }
                }
            }
        });
        /*txtUser.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                    txtPass.requestFocus();
                    return true;
                }
                return false;
            }
        });*/

        {

        }
        button_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent("com.example.anik.busdigitizationprototype.DuringJourney");
                finish();
                //startActivity(intent);
            }

        });
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
