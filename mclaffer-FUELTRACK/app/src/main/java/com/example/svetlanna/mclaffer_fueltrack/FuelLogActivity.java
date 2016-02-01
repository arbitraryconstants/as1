package com.example.svetlanna.mclaffer_fueltrack;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

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
import java.math.MathContext;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FuelLogActivity extends Activity implements Serializable {

    public static final String FILENAME = "file.sav";
    public ListView oldFuelLog; // Cannot show tweets directly so we need a method to convert to strings

    public ArrayList<FuelLogEntry> log = new ArrayList<FuelLogEntry>();
    public ArrayAdapter<FuelLogEntry> adapter;

    TextView addResult;

    FuelLogEntry entry;

    @Override //onCreate only called once during the life of the activity
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_fuel_log);

        // Add up total fuel cost
        addResult = (TextView)findViewById(R.id.txtResult);
        Button sumButton = (Button) findViewById(R.id.sum);


        Button clearButton = (Button) findViewById(R.id.clear);
        oldFuelLog = (ListView) findViewById(R.id.oldFuelLog);
        setupListViewListener();

        // Called when user clicks clear button
        clearButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                setResult(RESULT_OK);

                log.clear(); // remove all tweets from the list of tweets
                adapter.notifyDataSetChanged();
                saveInFile(); // Save the empty list to FILENAME

            }
        });

        // Called when user clicks sum button
        sumButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                double sum_dbl = 0.00;
                setResult(RESULT_OK);

                for (FuelLogEntry entry : log) {
                    String fuel_cost_str = entry.fuel_cost;
                    Double fuel_cost_dbl = Double.parseDouble(fuel_cost_str);
                    sum_dbl += fuel_cost_dbl ;
                }

                String sum_str = Double.toString(sum_dbl);
                BigDecimal sum = new BigDecimal(sum_str);
                addResult.setText("Total fuel cost: " + sum.toString() + " dollars");

                adapter.notifyDataSetChanged();
                saveInFile(); // Save the empty list to FILENAME

            }
        });


    }
    public int CODE = 1;

    public void editLogEntry(View view )  {


        Intent intentEdit = new Intent(this, FuelLogEntryActivity.class);
        intentEdit.putExtra("date", entry.getDate());
        intentEdit.putExtra("station", entry.getStation());
        intentEdit.putExtra("odometer_reading", entry.getOdometer_reading());
        intentEdit.putExtra("fuel_grade", entry.getFuel_grade());
        intentEdit.putExtra("fuel_amount", entry.getFuel_amount());
        intentEdit.putExtra("fuel_unit_cost", entry.getFuel_unit_cost());
        //startActivity(intentEdit);
        startActivityForResult(intentEdit, CODE);

    }

    /**
     * Called when the user clicks the Create Log Entry button
     */



    public void createLogEntry(View view) {
        //System.out.println(view);
        Intent intent = new Intent(this, FuelLogEntryActivity.class);
        startActivityForResult(intent, CODE);
    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the request went well (OK) and the request was PICK_CONTACT_REQUEST
        if (resultCode == Activity.RESULT_OK && requestCode == CODE) {
            FuelLogEntry newestEntry = (FuelLogEntry) data.getSerializableExtra("newestEntry");
            log.add(newestEntry);

            //System.out.println(newestEntry);
            saveInFile();
            adapter.notifyDataSetChanged();

        }
    }


    // Attaches a long click listener to the listview
    // Edit fuel log entry
    // When a fuel log entry is long clicked
    private void setupListViewListener() {
        oldFuelLog.setOnItemLongClickListener(
            new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                    // Remove the item within array at position
                    System.out.println("hello removing");

                    entry = log.get(pos);
                    log.remove(pos);
                    // Refresh the adapter
                    saveInFile(); // this calls adapter.notifyDataSetChanged();

                    editLogEntry(item);

                    // Return true consumes the long click event (marks it handled)
                    return true;
                }

            });

    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        loadFromFile();

        adapter = new ArrayAdapter<FuelLogEntry>(FuelLogActivity.this,
                R.layout.log_item, log);

        oldFuelLog.setAdapter(adapter);

        ///adapter.notifyDataSetChanged();

    }

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