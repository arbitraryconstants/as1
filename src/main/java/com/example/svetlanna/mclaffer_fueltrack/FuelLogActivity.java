package com.example.svetlanna.mclaffer_fueltrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Purpose: Display Fuel log and allow user to interact with the log
 * through pressing buttons/log entry.
 *
 * Design rational: Implements Serializable so that information
 * can be passed between activities using the intent method .putExtra
 * ListView of log is updated here as log is updated.
 *
 * Outstanding Issues: Would ideally create new FuelLog, rather
 * than new ArraryList<FuelLogEntry>, for log. That way custom
 * methods could be implemented for log.
 */
public class FuelLogActivity extends Activity implements Serializable {

    private static final String FILENAME = "file.sav";  // Name of file used for saving fuel log
    private ListView oldFuelLog; // New ListView for log
    private ArrayList<FuelLogEntry> log = new ArrayList<FuelLogEntry>(); // Create fuel log
    //private FuelLog log= new FuelLog(); // More OO solution -- had issues implementing
    private ArrayAdapter<FuelLogEntry> adapter; // Adapter used for displaying the ListView items
    TextView addResult; // Text displayed when user presses "Calculate Total Fuel Cost" button
    FuelLogEntry entry; // Used to save old entry so that it can be edited
    private int CODE = 1; // Code for intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_fuel_log);

        addResult = (TextView)findViewById(R.id.txtResult); // Load total fuel cost if result already exists

        // "Calculate Total Fuel Cost" and "Clear" buttons
        Button sumButton = (Button) findViewById(R.id.sum);
        Button clearButton = (Button) findViewById(R.id.clear);

        oldFuelLog = (ListView) findViewById(R.id.oldFuelLog);

        setupListViewListener(); // For fuel log entry in list view

        // Called when user clicks clear button
        clearButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                setResult(RESULT_OK);
                log.clear(); // Remove all fuel log entries from log
                //log.clearLog(); // // More OO solution
                adapter.notifyDataSetChanged(); // Update adapter
                saveInFile(); // Update file.save

            }
        });

        // Called when user clicks "Calculate Total Fuel Cost" button
        // addResult.setText() idea from: http://codehandbook.org/a-simple-android-application-for-adding-two-numbers/
        sumButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                double sum_dbl = 0.00; // Initialize
                setResult(RESULT_OK);

                for (FuelLogEntry entry : log) {
                    String fuel_cost_str = entry.fuel_cost;
                    Double fuel_cost_dbl = Double.parseDouble(fuel_cost_str);
                    sum_dbl += fuel_cost_dbl ;
                }

                String sum_str = Double.toString(sum_dbl);
                BigDecimal sum = new BigDecimal(sum_str);
                // Convert BigDecimal to string: https://stackoverflow.com/questions/13900204/bigdecimal-to-string
                addResult.setText("Total fuel cost: " + sum.toString() + " dollars");
                adapter.notifyDataSetChanged(); // Update adapter
                saveInFile(); // Update file.save

            }
        });


    }

    // Called by ListView listener when user long clicks a log entry
    public void editLogEntry(View view )  {

        Intent intentEdit = new Intent(this, FuelLogEntryActivity.class);
        // Pass information about entry to edit
        intentEdit.putExtra("date", entry.getDate());
        intentEdit.putExtra("station", entry.getStation());
        intentEdit.putExtra("odometer_reading", entry.getOdometer_reading());
        intentEdit.putExtra("fuel_grade", entry.getFuel_grade());
        intentEdit.putExtra("fuel_amount", entry.getFuel_amount());
        intentEdit.putExtra("fuel_unit_cost", entry.getFuel_unit_cost());
        startActivityForResult(intentEdit, CODE);

    }

    // Called when user clicks "Create New Log Entry" button
    public void createLogEntry(View view) {

        Intent intent = new Intent(this, FuelLogEntryActivity.class);
        startActivityForResult(intent, CODE);
    }

    // Result returned from starting intent for editing/creating new log entry
    // Idea for onActivityResult from :https://stackoverflow.com/questions/920306/sending-data-back-to-the-main-activity-in-android
    // And from: http://www.101apps.co.za/index.php/articles/passing-objects-between-activities.html
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // If the request went well (OK) and the request was CODE
        if (resultCode == Activity.RESULT_OK && requestCode == CODE) {
            FuelLogEntry newestEntry = (FuelLogEntry) data.getSerializableExtra("newestEntry");
            log.add(newestEntry); // Append new entry to log
            //log.addEntry(newestEntry) // More OO solution
            saveInFile(); // Update file.save
            adapter.notifyDataSetChanged(); // Update adapter

        }
    }

    // Called When a fuel log entry is long clicked ie. user wants to edit this entry
    // Code inspired by: https://guides.codepath.com/android/Basic-Todo-App-Tutorial
    private void setupListViewListener() {
        oldFuelLog.setOnItemLongClickListener(
            new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {

                    entry = log.get(pos); // Get current entry to use later for editing
                    log.remove(pos); // Remove current entry from list
                    //log.removeEntry // More OO solution
                    saveInFile(); // Update file.save
                    editLogEntry(item); // Call to edit entry
                    return true;
                }
            });
    }

    // Code from https://github.com/joshua2ua/lonelyTwitter
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        loadFromFile();
        adapter = new ArrayAdapter<FuelLogEntry>(FuelLogActivity.this,
                R.layout.log_item, log);
        oldFuelLog.setAdapter(adapter);
    }

    // Code from https://github.com/joshua2ua/lonelyTwitter
    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            // took from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.htmlon Jan-20-2016
            Type listType = new TypeToken<ArrayList<FuelLogEntry>>() {
            }.getType();
            log = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            log = new ArrayList<FuelLogEntry>();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    // Code from https://github.com/joshua2ua/lonelyTwitter
    private void saveInFile() {
        try {
            adapter.notifyDataSetChanged();
            FileOutputStream fos = openFileOutput(FILENAME,
                    0); // This file can be accessed by this application only, file will be filled with new stuff
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(log, out);
            out.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }
}