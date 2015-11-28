package com.example.anik.busdigitizationprototype;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anik.busdigitizationprototype.Utility.ConnectionManager;
import com.example.anik.busdigitizationprototype.Utility.ConnectionStrings;
import com.example.anik.busdigitizationprototype.Utility.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CheckedInputStream;

public class DuringJourney extends Activity {
    private static Button button_Terminate, button_Refresh, button_SubmitHazard;
    private static TextView lblStat, lblHazCount;

    private Spinner cboLocation, cboHazType, cboTime;
    private ListView lstHazrd;
    private static CheckBox chkFeedback;
    public List<String> loc_list = new ArrayList<String>();
    public List<String> haz_list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.d("Act", "JOURNEY STARTED");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_during_journey);
        lblStat = (TextView) findViewById(R.id.labelJourneyStatus);
        lblHazCount = (TextView) findViewById(R.id.HazardCountLabel);
        chkFeedback = (CheckBox) findViewById(R.id.FeedBackCheckbox);
        cboLocation = (Spinner) findViewById(R.id.spinLocation);
        cboHazType = (Spinner) findViewById(R.id.spinHazType);
        cboTime = (Spinner) findViewById(R.id.spinExpireTime);
        lstHazrd = (ListView)findViewById(R.id.listViewHazList);

        ConnectionManager.out.println(ConnectionStrings.WAY_POINTS);
        ConnectionManager.out.println(Utility.UserData.bus_taken);
        ConnectionManager.out.println(Utility.UserData.source);
        ConnectionManager.out.println(Utility.UserData.destination);
        ConnectionManager.out.flush();
        new Thread(new ClientListener()).start();
        //Log.d("JUserData", "[" + Utility.UserData.source + " to " + Utility.UserData.destination + " in " + Utility.UserData.bus_taken + "]");
        lblStat.setText("[" + Utility.UserData.source + " to " + Utility.UserData.destination + " in " + Utility.UserData.bus_taken + "]");
        OnClickButtonListener();
        UpdateHazardList();
    }

    void UpdateHazardList()
    {
        ConnectionManager.out.println(ConnectionStrings.RETRIVE_HAZARD);
        ConnectionManager.out.println(Utility.UserData.bus_taken);
        ConnectionManager.out.println(Utility.UserData.source);
        ConnectionManager.out.println(Utility.UserData.destination);
        ConnectionManager.out.flush();
        new Thread(new ClientListener()).start();
    }
    private class ClientListener implements Runnable{
        @Override
        public void run() {
            String line;

            try {
                while(true)
                {
                    if (ConnectionManager.in == null)
                    {
                        Log.d("Connection", "Connection Error");
                        break;
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
                    if (line.equals(ConnectionStrings.INSERT_HAZARD))
                    {
                        String success = ConnectionManager.in.readLine();
                        if (success.equals("1"))
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //txt.setText("Machine Says Hi");
                                    Toast.makeText(getApplicationContext(), "Hazard successfully added", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //txt.setText("Machine Says Hi");
                                    Toast.makeText(getApplicationContext(), "Unexpired Hazard entry already present", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        break;
                    }
                    else if (line.equals(ConnectionStrings.WAY_POINTS))
                    {
                        //Log.d("STARTJ_Error", "Waiting for reply");
                        int len = 0;

                        //Log.d("STARTJ_Error", "Arraylist declared");
                        try {
                            len = Integer.valueOf(ConnectionManager.in.readLine());
                            //Log.d("Waypoint", "len " + String.valueOf(len));
                            for (int i = 0; i<len; i++)
                            {
                                //Log.d("STARTJ_Error", "Line read");
                                loc_list.add(ConnectionManager.in.readLine());
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
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(DuringJourney.this, android.R.layout.simple_spinner_item, loc_list);
                                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                cboLocation.setAdapter(dataAdapter);
                                //Toast.makeText(getApplicationContext(), "Data Adaptor set, total locations " + len , Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    }
                    else if (line.equals(ConnectionStrings.RETRIVE_HAZARD))
                    {
                        //Log.d("STARTJ_Error", "Waiting for reply");
                        int len = 0;
                        haz_list.clear();
                        //Log.d("STARTJ_Error", "Arraylist declared");
                        try {
                            len = Integer.valueOf(ConnectionManager.in.readLine());
                            Log.d("Hazard Count", "len " + String.valueOf(len));
                            for (int i = 0; i<len; i++)
                            {
                                //Log.d("STARTJ_Error", "Line read");
                                haz_list.add(ConnectionManager.in.readLine());
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
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(DuringJourney.this, android.R.layout.simple_list_item_1, haz_list);
                                lstHazrd.setAdapter(dataAdapter);
                                if (haz_list.isEmpty())
                                    lblHazCount.setText("No significant Hazard in your path");
                                else
                                    lblHazCount.setText("");
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
        button_Terminate  = (Button) findViewById(R.id.button8);
        button_Refresh = (Button) findViewById(R.id.RefreshButton);
        button_SubmitHazard = (Button) findViewById(R.id.SubmitButton);

        button_Terminate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionManager.out.println(ConnectionStrings.LOG_END);
                ConnectionManager.out.println(Utility.UserData.user_name);

                boolean feedback = chkFeedback.isChecked();
                if (feedback == true)
                {
                    Intent intent = new Intent("com.example.anik.busdigitizationprototype.Feedback");
                    finish();
                    startActivity(intent);
                }
                else
                {
                    finish();
                }

            }
        });

        button_Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateHazardList();
            }
        });

        button_SubmitHazard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = cboLocation.getSelectedItem().toString();
                String hazard_type = cboHazType.getSelectedItem().toString();
                String exp_time1 = cboTime.getSelectedItem().toString();
                String exp_time = exp_time1.substring(0, exp_time1.length()-4);     //without ' min'

                ConnectionManager.out.println(ConnectionStrings.INSERT_HAZARD);
                ConnectionManager.out.println(Utility.UserData.user_name);
                ConnectionManager.out.println(location);
                ConnectionManager.out.println(hazard_type);
                ConnectionManager.out.println(exp_time);
                new Thread(new ClientListener()).start();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_during_journey, menu);
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
