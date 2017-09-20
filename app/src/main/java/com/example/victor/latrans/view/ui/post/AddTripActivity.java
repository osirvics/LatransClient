package com.example.victor.latrans.view.ui.post;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.victor.latrans.R;
import com.example.victor.latrans.repocitory.local.model.Local;
import com.example.victor.latrans.repocitory.local.model.Region;
import com.example.victor.latrans.repocitory.local.model.State;
import com.example.victor.latrans.view.adapter.CustomSpinnerAdapter;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class AddTripActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static Intent newIntent(Context context) {
        Intent mIntent = new Intent(context, AddTripActivity.class);
        return mIntent;
    }
    @BindView(R.id.country_spinner)
    Spinner mSpinner;
    @BindView(R.id.local_spinner)
    Spinner mSpinnerLocal;
    @BindView(R.id.country_spinner_from)
    Spinner mSpinnerFrom;
    @BindView(R.id.local_spinner_from)
    Spinner mSpinnerLocalFrom;
    @BindView(R.id.date_linearlayout)
    LinearLayout mLinearLayoutDate;
    @BindView(R.id.date_text)EditText mEditTextDate;
    @BindView(R.id.add_trip_button)
    Button mButtonPost;
    CustomSpinnerAdapter mCustomSpinnerAdapter;
    CustomSpinnerAdapter mLocalAdapter;
    CustomSpinnerAdapter mStateSpinnerAdapterFrom;
    CustomSpinnerAdapter mLocalAdapterFrom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        ButterKnife.bind(this);

        StringBuilder builder = new StringBuilder();
        InputStream in = getResources().openRawResource(R.raw.regions);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            Timber.e("Failed to read line");
            e.printStackTrace();
        }
        //Parse resource into key/values
        final String rawJson = builder.toString();

        Gson gson = new Gson();
        Region regions = gson.fromJson(rawJson, Region.class);
        List<State> states = regions.getState();
        List<String> mStrings = new ArrayList<>();
        for(State state: states){
            mStrings.add(state.getName());
        }
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(this,
                 android.R.layout.simple_spinner_item, mStrings);

        mCustomSpinnerAdapter = new CustomSpinnerAdapter(this, mStrings);


       //adapter.setDropDownViewResource(R.drawable.ic_arrow_drop_down_circle_grey_600_24dp);

        mSpinner.setAdapter(mCustomSpinnerAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                List<Local> mLocals = states.get(i).getLocals();
                List<String> mdata = new ArrayList<>();
                for (Local local: mLocals){
                    mdata.add(local.getName());
                }

              //  ArrayAdapter<String> adapter =  new ArrayAdapter<String>(AddTripActivity.this,
              //          android.R.layout.simple_spinner_item, mdata);

               // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mLocalAdapter = new CustomSpinnerAdapter(AddTripActivity.this, mdata);
                mSpinnerLocal.setAdapter(mLocalAdapter);
               // mSpinnerLocal.performClick();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mStateSpinnerAdapterFrom = new CustomSpinnerAdapter(this, mStrings);
        mSpinnerFrom.setAdapter( mStateSpinnerAdapterFrom);
        mSpinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                List<Local> mLocals = states.get(i).getLocals();
                List<String> mdata = new ArrayList<>();
                for (Local local: mLocals){
                    mdata.add(local.getName());
                }

                //  ArrayAdapter<String> adapter =  new ArrayAdapter<String>(AddTripActivity.this,
                //          android.R.layout.simple_spinner_item, mdata);

                // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mLocalAdapterFrom = new CustomSpinnerAdapter(AddTripActivity.this, mdata);
                mSpinnerLocalFrom.setAdapter(mLocalAdapterFrom);
                //mSpinnerLocal.performClick();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    @OnClick(R.id.date_linearlayout)
    void showDatePicker(){
        showPicker();
    }

    private void showPicker(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddTripActivity.this, AddTripActivity.this, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        //String format = "EEEE, d MMMM yyyy";
        String format = "dd/ MM/ yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        mEditTextDate.setText(sdf.format(myCalendar.getTime()));
    }
}
