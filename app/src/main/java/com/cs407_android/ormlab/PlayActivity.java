package com.cs407_android.ormlab;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Objects;
import java.util.Random;

public class PlayActivity extends AppCompatActivity{
  //  public static String detailsText;
    //  public static PlayActivity activity;
    private Button confirm;
    String date;
    EditText time;
    EditText details;
    String detailsText;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        //    activity = this;
        confirm = (Button)findViewById(R.id.confirmButton);
        date = Objects.toString(MainActivity.calendar.getDate());
        MainActivity.calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                date = month + "/" + dayOfMonth + "/" + year;
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
      /*  if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    public void confirmClicked(View view){
        details = (EditText) findViewById(R.id.detailsEditText);
        time = (EditText) findViewById(R.id.timeEditText);
        detailsText = time.getText().toString() + " " + details.getText().toString();
        Random rand = new Random();
        Event newEvent = new Event(rand.nextLong(), date,
                detailsText, true);
    //    MainActivity.eventList.add(detailsText);
        MainActivity.saveEvent(newEvent);
        finish();
    }
}