package com.example.anik.busdigitizationprototype;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anik.busdigitizationprototype.Utility.ConnectionManager;
import com.example.anik.busdigitizationprototype.Utility.ConnectionStrings;
import com.example.anik.busdigitizationprototype.Utility.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartJourney extends Activity {
    private static Button button_showresult;
    private static TextView txtGreetings;
    private Spinner from, to;
    public List<String> list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_journey);
        from = (Spinner) findViewById(R.id.cboStartJourneyFrom);
        to = (Spinner) findViewById(R.id.cboStartJourneyTo);

        txtGreetings = (TextView) findViewById(R.id.lblGreetings);
        txtGreetings.setText("Hello " + Utility.UserData.user_name);
        //Log.d("STARTJ_Error", "Sending code");
        ConnectionManager.out.println(ConnectionStrings.LOCATION_LIST);
        //Log.d("STARTJ_Error", "Starting Thread");
        new Thread(new ClientListener()).start();
        OnClickButtonListener();


    }

    private class ClientListener implements Runnable{
        @Override
        public void run() {
            String line;

            try {
                while(true)
                {
                    if (ConnectionManager.in == null) continue;
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
                    else if (line.equals(ConnectionStrings.LOCATION_LIST))
                    {
                        //Log.d("STARTJ_Error", "Waiting for reply");
                        int len = 0;

                        //Log.d("STARTJ_Error", "Arraylist declared");
                        try {
                            len = Integer.valueOf(ConnectionManager.in.readLine());
                            for (int i = 0; i<len; i++)
                            {
                                //Log.d("STARTJ_Error", "Line read");
                                list.add(ConnectionManager.in.readLine());
                            }
                        }
                        catch (NullPointerException e1) {
                            //Log.d("STARTJ_Error", "Connection Lost");
                            e1.printStackTrace();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //txt.setText("Machine Says Hi");
                                //Log.d("STARTJ_Error", "Data Adaptor set, total locations " + list.size() );
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(StartJourney.this, android.R.layout.simple_spinner_item, list);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                from.setAdapter(dataAdapter);
                                to.setAdapter(dataAdapter);
                                //Toast.makeText(getApplicationContext(), "Data Adaptor set, total locations " + len , Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    }
                }

            }
            catch (NullPointerException e1) {
                Log.d("STARTJ_Error", "Connection Lost ");
                e1.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void OnClickButtonListener()
    {
        button_showresult  = (Button) findViewById(R.id.button5);

        button_showresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strFrom, strTo;
                strFrom = from.getSelectedItem().toString();
                strTo = to.getSelectedItem().toString();
                if (strFrom.equals(strTo))
                    Toast.makeText(getApplicationContext(), "Current Location and Destination is same.", Toast.LENGTH_SHORT).show();
                else
                {
                    //Toast.makeText(getApplicationContext(), "You want to go from " + strFrom + " to " + strTo, Toast.LENGTH_SHORT).show();
                    Utility.pd = new ProgressDialog(StartJourney.this);
                    Utility.pd.setMessage("Please wait, loading results...");
                    Utility.pd.setCancelable(false);
                    Utility.pd.setInverseBackgroundForced(false);
                    Utility.pd.show();

                    Utility.UserData.source = strFrom;
                    Utility.UserData.destination = strTo;
                    Intent intent = new Intent("com.example.anik.busdigitizationprototype.Result");
                    startActivity(intent);
                    Utility.pd.dismiss();
                }
            }

        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_journey, menu);
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
