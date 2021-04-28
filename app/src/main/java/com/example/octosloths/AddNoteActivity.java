package com.example.octosloths;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

// BASED ON TUTORIAL: https://www.youtube.com/playlist?list=PLrnPJCHvNZuDihTpkRs6SpZhqgBqPU118

public class AddNoteActivity extends AppCompatActivity { // for the adding note page

    // keys for intent, communication with main activity when saving note
    public static final String EXTRA_ID =
            "com.example.octosloths.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.example.octosloths.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "com.example.octosloths.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY  =
            "com.example.octosloths.EXTRA_PRIORITY";
    public static final String EXTRA_HOURS  =
            "com.example.octosloths.EXTRA_HOURS";
    public static final String EXTRA_START_DATE  =
            "com.example.octosloths.EXTRA_START_DATE";
    public static final String EXTRA_END_DATE  =
            "com.example.octosloths.EXTRA_END_DATE";

    // for which type of entry they're entering
    public static final String EXTRA_BASIC =
            "com.example.octosloths.EXTRA_BASIC";
    public static final String EXTRA_VOLUNTEERING =
            "com.example.octosloths.EXTRA_VOLUNTEERING";


    // ui inputs
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextHours;
    private NumberPicker numberPickerPriority;

    private TextView mDisplayDate;
    private TextView mDisplayDate2;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

       mDisplayDate = (TextView) findViewById(R.id.textView7);
       mDisplayDate.setOnClickListener(new View.OnClickListener() {
           @RequiresApi(api = Build.VERSION_CODES.N)
           @Override
           public void onClick(View v) {
               Calendar cal = Calendar.getInstance();
               int year = cal.get(Calendar.YEAR);
               int month = cal.get(Calendar.MONTH); 
               int day = cal.get(Calendar.DAY_OF_MONTH);

               DatePickerDialog dialog = new DatePickerDialog(AddNoteActivity.this,
                       android.R.style.Theme_Holo_Dialog_MinWidth,mDateSetListener,year,month,day);
               dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
               dialog.show();
           }
       });

       mDateSetListener = new DatePickerDialog.OnDateSetListener() {

           @Override
           public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
               month = month + 1;
               String date = month + "/" + dayOfMonth + "/" + year;
               mDisplayDate.setText(date);


           }
       };

        mDisplayDate2 = (TextView) findViewById(R.id.textView8);
        mDisplayDate2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddNoteActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,mDateSetListener,year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = month + "/" + dayOfMonth + "/" + year;
                mDisplayDate2.setText(date);


            }
        };





        // similar to what we've been doing in the past
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextHours = findViewById(R.id.edit_text_hours);
        numberPickerPriority = findViewById(R.id.number_picker_priority);

        Log.v("AddNoteActivity: ", "onCreate entered");

        // setting min and max values of numberpicker
        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        // getting menu bar and setting icons
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);



        Intent intent = getIntent(); // getting intent that started this activity


        if(intent.hasExtra(EXTRA_ID)) { // we only sent id for editing
            setTitle("Edit Entry");

            // setting data
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            editTextHours.setText(intent.getStringExtra(EXTRA_HOURS)); // for hours, is value necessary?
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
        }
        else {
            setTitle("Add Entry");
        }


    }

    // method to save note
    private void saveNote() {
        // getting value of ui fields
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        String hrs = editTextHours.getText().toString(); // cannot parse empty string
        if(hrs.isEmpty())
            hrs = "0";

        // int hours = Integer.parseInt(hrs); // parsing to int for hours
        int priority = numberPickerPriority.getValue();

        // checking fields are not empty
        if(title.trim().isEmpty() || description.trim().isEmpty()) {
            // show toast message
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }


        // sending data back to main activity, not matter add or update
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);
        data.putExtra(EXTRA_HOURS, hrs);


        // for update situation
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1) {
            data.putExtra(EXTRA_ID, id); // only put the id in result intent if it is an update situation
        }

        setResult(RESULT_OK, data); // can read later in main to check if everything went as planned
        finish(); // closes this activity, when saved this page will disappear
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu); // tells system to use add note menu as the menu of this activity

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { // param item is the item that is clicked

        switch(item.getItemId()) { // switch for one case
            case R.id.save_note:
                saveNote();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}