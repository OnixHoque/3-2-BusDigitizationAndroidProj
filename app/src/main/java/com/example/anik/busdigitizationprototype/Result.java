package com.example.anik.busdigitizationprototype;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anik.busdigitizationprototype.Utility.ConnectionManager;
import com.example.anik.busdigitizationprototype.Utility.ConnectionStrings;
import com.example.anik.busdigitizationprototype.Utility.ResultItems;
import com.example.anik.busdigitizationprototype.Utility.Utility;

import java.util.ArrayList;
import java.util.List;

public class Result extends AppCompatActivity {
    private static Button button_StartJourney, button_Back;
    private static Spinner spinnerSort;
    private static TextView lblD;

    public ResultListAdaptor rla;
    public List<ResultItems> lst = new ArrayList<ResultItems>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Utility.pd = new ProgressDialog(Result.this);
        Utility.pd.setMessage("Please wait, loading results...");
        Utility.pd.setCancelable(false);
        Utility.pd.setInverseBackgroundForced(false);
        Utility.pd.show();

        OnClickButtonListener();
        lblD = (TextView) findViewById(R.id.textView9);
        lblD.setText("Available Bus Services for: " + Utility.UserData.destination);

        rla = new ResultListAdaptor();
        ListView lv = (ListView)findViewById(R.id.listViewResult);
        lv.setAdapter(rla);

        ConnectionManager.out.println(ConnectionStrings.ROUTE_RESULT);
        ConnectionManager.out.println(Utility.UserData.source);
        ConnectionManager.out.println(Utility.UserData.destination);
        Thread t = new Thread(new ClientListener());
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //lst.add(new ResultItems("Falgun", 20, 19, 4.0, 5, 0));
        //lst.add(new ResultItems("Torongo", 15, 5, 3.0, 6, 1));
        //lst.add(new ResultItems("Bolaka", 5, 25, 4.4, 7, 2));
        //rla.notifyDataSetChanged();
        Collections.sort(lst, ResultItems.sortByTime);
        rla.notifyDataSetChanged();
        OnSpinnerChangeListener();
        Utility.pd.dismiss();

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
                    else if (line.equals(ConnectionStrings.ROUTE_RESULT))
                    {

                        int len = 0;
                        try {
                            len = Integer.valueOf(ConnectionManager.in.readLine());
                            for (int i = 0; i<len; i++)
                            {
                                String busName = ConnectionManager.in.readLine();
                                String rating = ConnectionManager.in.readLine();
                                String time = ConnectionManager.in.readLine();
                                String distance = ConnectionManager.in.readLine();
                                String price = ConnectionManager.in.readLine();
                                String hazard = ConnectionManager.in.readLine();
                                lst.add(new ResultItems(busName, Integer.parseInt(price), Double.parseDouble(time), Double.parseDouble(rating), Double.parseDouble(distance), Integer.parseInt(hazard)));
                            }
                            //*****TEST ONLY****
                            lst.add(new ResultItems("Falgun (Dummy)", 20, 19, 4.0, 5, 0));
                            lst.add(new ResultItems("Torongo (Dummy)", 15, 5, 3.0, 6, 1));
                            lst.add(new ResultItems("Bolaka (Dummy)", 5, 25, 4.4, 7, 2));
                            rla.notifyDataSetChanged();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        /*
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });*/
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class ResultListAdaptor extends BaseAdapter{

        @Override
        public int getCount() {
            return lst.size();
        }

        @Override
        public Object getItem(int position) {
            return lst.get(position) ;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null)
            {
                LayoutInflater inflater = (LayoutInflater) Result.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.result_list_item, parent,false);
            }

            TextView txtbusCompany = (TextView)convertView.findViewById(R.id.lblBusCompany);
            RatingBar rb = (RatingBar) convertView.findViewById(R.id.ratingBus);
            TextView txtTime = (TextView)convertView.findViewById(R.id.lblTime);
            TextView txtDist = (TextView)convertView.findViewById(R.id.lblDist);
            TextView txtPrice = (TextView)convertView.findViewById(R.id.lblPrice);
            TextView txtHaz = (TextView)convertView.findViewById(R.id.lblHazard);

            ResultItems result_item = lst.get(position);
            txtbusCompany.setText(result_item.getBusName());
            txtTime.setText(String.valueOf("ETA " + result_item.getTime())+ " min");
            rb.setRating(((float) (result_item.getRating())));
            txtDist.setText(String.valueOf(result_item.getDistance()) + " km");
            txtPrice.setText(String.valueOf(result_item.getPrice())+ " taka");
            txtHaz.setText(String.valueOf(result_item.getHazardCount()) + " Hazard(s) on the way");
            return convertView;
        }
    }
    //Toast.makeText(getApplicationContext(), "Sort selected -> " + String.valueOf(position), Toast.LENGTH_LONG).show();
    public void OnSpinnerChangeListener()
    {
        spinnerSort = (Spinner) findViewById(R.id.spinner3);
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "Sort selected -> " + String.valueOf(position), Toast.LENGTH_LONG).show();
                switch (position)
                {
                    case 0:
                    {
                        //Shortest time
                        Collections.sort(lst, ResultItems.sortByTime);
                        rla.notifyDataSetChanged();
                        break;
                    }
                    case 1:
                    {
                        //lowest price
                        Collections.sort(lst, ResultItems.sortByPrice);
                        rla.notifyDataSetChanged();
                        break;
                    }
                    case 2:
                    {
                        //Shortest Distance
                        Collections.sort(lst, ResultItems.sortByDistance);
                        rla.notifyDataSetChanged();
                        break;
                    }
                    case 3:
                    {
                        //Best bus rating
                        Collections.sort(lst, ResultItems.sortByRating);
                        rla.notifyDataSetChanged();
                        break;
                    }
                    case 4:
                    {
                        //Least hazard count
                        Collections.sort(lst, ResultItems.sortByHazard);
                        rla.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void OnClickButtonListener()
    {
        button_StartJourney  = (Button) findViewById(R.id.button6);
        button_Back  = (Button) findViewById(R.id.button7);
        button_StartJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent("com.example.anik.busdigitizationprototype.DuringJourney");
                //finish();
                //startActivity(intent);
            }

        });
        button_Back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
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
