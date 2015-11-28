package com.example.anik.busdigitizationprototype;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.example.anik.busdigitizationprototype.Utility.ConnectionManager;
import com.example.anik.busdigitizationprototype.Utility.ConnectionStrings;
import com.example.anik.busdigitizationprototype.Utility.Utility;

public class Feedback extends Activity {
    private static Button button_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        OnClickButtonListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
        return true;
    }
    public void OnClickButtonListener()
    {
        button_submit  = (Button) findViewById(R.id.button10);

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeekBar sb1, sb2, sb3, sb4, sb5;
                sb1 = (SeekBar)findViewById(R.id.sbComf);
                sb2 = (SeekBar)findViewById(R.id.sbTicket);
                sb3 = (SeekBar)findViewById(R.id.sbNoS);
                sb4 = (SeekBar)findViewById(R.id.sbSeat);
                sb5 = (SeekBar)findViewById(R.id.sbQos);

                ConnectionManager.out.println(ConnectionStrings.FEEDBACK);
                ConnectionManager.out.println(Utility.UserData.user_name);
                ConnectionManager.out.println(Utility.UserData.bus_taken);
                ConnectionManager.out.println(String.valueOf(sb1.getProgress()+1));
                ConnectionManager.out.println(String.valueOf(sb2.getProgress()+1));
                ConnectionManager.out.println(String.valueOf(sb3.getProgress()+1));
                ConnectionManager.out.println(String.valueOf(sb4.getProgress()+1));
                ConnectionManager.out.println(String.valueOf(sb5.getProgress()+1));
                finish();

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
