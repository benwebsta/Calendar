package com.cs407_android.ormlab;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button add, delete;
    ListView listView;
    ArrayAdapter adapter;
    Context context;
    public static boolean newDate = false;
    public static boolean adapterSetup = false;
    public static ArrayList<String> eventList;
    public static CalendarView calendar;


    //Uncomment once ready
    public static DaoMaster.DevOpenHelper eventBookDBHelper;
    public static  SQLiteDatabase eventBookDB;
    public static DaoMaster daoMaster;
    public static DaoSession daoSession;
    public static EventDao eventDao;
    public static List<Event> eventListFromDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiate objects
        context = this;
        eventList = new ArrayList<>();

        add = (Button) findViewById(R.id.addButton);
        delete = (Button) findViewById(R.id.deleteButton);
        calendar = (CalendarView) findViewById(R.id.calendarView);
        listView = (ListView) findViewById(R.id.listView);


        //set up ListView
     //   adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventListFromDB);
       // listView.setAdapter(adapter);

        initDatabase();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                final Event listItem = (Event) (eventListFromDB.get(myItemInt));
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                eventDao.delete(listItem);
                                eventListFromDB = eventDao.queryBuilder().where(
                                        EventDao.Properties.Display.eq(true)).list();
                                adapter.notifyDataSetChanged();
                                listView.invalidate();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                PlayActivity.date = month + "/" + dayOfMonth + "/" + year;
                newDate = true;
            }
        });

        adapter.notifyDataSetChanged();
        adapterSetup = true;

    }

     private void initDatabase()
    {
        eventBookDBHelper = new DaoMaster.DevOpenHelper(this, "ORM.sqlite", null);
        eventBookDB = eventBookDBHelper.getWritableDatabase();

        //Get DaoMaster
        daoMaster = new DaoMaster(eventBookDB);

        //Create database and tables
        daoMaster.createAllTables(eventBookDB, true);

        //Create DaoSession
        daoSession = daoMaster.newSession();

        //Create customer addition/removal instances
        eventDao = daoSession.getEventDao();


        if (eventDao.queryBuilder().where(
            EventDao.Properties.Display.eq(true)).list() == null)
        {
            closeReopenDatabase();
        }
        eventListFromDB = eventDao.queryBuilder().where(
                EventDao.Properties.Display.eq(true)).list();

        if (eventListFromDB != null) {

            for (Event event : eventListFromDB)
            {
                if (event == null)
                {
                    return;
                }
            }
            //set up ListView
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventListFromDB);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public static void saveEvent(Event event)
    {
        eventDao.insert(event);
    }

    private void closeDatabase()
    {
        daoSession.clear();
        eventBookDB.close();
        eventBookDBHelper.close();
    }

    private void closeReopenDatabase()
    {

        closeDatabase();

        eventBookDBHelper = new DaoMaster.DevOpenHelper(this, "ORM.sqlite", null);
        eventBookDB = eventBookDBHelper.getWritableDatabase();

        //Get DaoMaster
        daoMaster = new DaoMaster(eventBookDB);

        //Create database and tables
        daoMaster.createAllTables(eventBookDB, true);

        //Create DaoSession
        daoSession = daoMaster.newSession();

        //Create customer addition/removal instances
        eventDao = daoSession.getEventDao();
    }
    public void addPressed(View view){
        Intent playActivity = new Intent(this, PlayActivity.class);
        startActivity(playActivity);
    }
    /*public void listSelected(View view){
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                eventListFromDB = eventDao.queryBuilder().where(
                        EventDao.Properties.Date.eq(month + "/" + dayOfMonth + "/" + year)).list();
                        adapter.notifyDataSetChanged();
            }
        });
    }*/
    public void listSelected(ListView view){
        if(adapterSetup)
            adapter.notifyDataSetChanged();
    }



}
