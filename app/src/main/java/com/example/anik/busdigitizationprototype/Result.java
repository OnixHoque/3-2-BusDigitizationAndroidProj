package com.example.anik.busdigitizationprototype;

import android.content.Context;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import android.widget.RatingBar;
import android.widget.TextView;

import com.example.anik.busdigitizationprototype.Utility.ResultItems;

import java.util.ArrayList;
import java.util.List;

public class Result extends AppCompatActivity {
    private static Button button_StartJourney;
    public List<ResultItems> lst = new ArrayList<ResultItems>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        OnClickButtonListener();

        lst.add(new ResultItems("Falgun", 20, 19, 4.0, 5, 0));

        ResultListAdaptor rla = new ResultListAdaptor();
        ListView lv = (ListView)findViewById(R.id.listViewResult);
        lv.setAdapter(rla);
        lst.add(new ResultItems("Torongo", 15, 5, 3.0, 6, 1));
        lst.add(new ResultItems("Bolaka", 5, 25, 4.4, 7, 2));
        rla.notifyDataSetChanged();
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
            txtTime.setText(String.valueOf(result_item.getTime())+ " min");
            rb.setRating(((float)(result_item.getRating())));
            txtDist.setText(String.valueOf(result_item.getDistance())+ " km");
            txtPrice.setText(String.valueOf(result_item.getPrice())+ " taka");
            txtHaz.setText(String.valueOf(result_item.getHazardCount()) + " Hazard(s) on the way");
            return convertView;
        }
    }

    public void OnClickButtonListener()
    {
        button_StartJourney  = (Button) findViewById(R.id.button6);

        button_StartJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.anik.busdigitizationprototype.DuringJourney");
                finish();
                startActivity(intent);
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
