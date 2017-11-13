package com.example.victor.latrans.view.ui.addtrip;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.victor.latrans.R;
import com.example.victor.latrans.dependency.AppFactory;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.db.entity.Trip;
import com.example.victor.latrans.repocitory.local.db.entity.User;
import com.example.victor.latrans.repocitory.local.model.Local;
import com.example.victor.latrans.repocitory.local.model.Region;
import com.example.victor.latrans.repocitory.local.model.State;
import com.example.victor.latrans.view.adapter.CustomSpinnerAdapter;
import com.example.victor.latrans.view.ui.App;
import com.example.victor.latrans.view.ui.trip.TripActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTripActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    public static Intent newIntent(Context context) {
        Intent mIntent = new Intent(context, AddTripActivity.class);
        return mIntent;
    }

    @BindView(R.id.country_spinner)
    Spinner mSpinnerStateTo;
    @BindView(R.id.local_spinner)
    Spinner mSpinnerLocalTo;
    @BindView(R.id.country_spinner_from)
    Spinner mSpinnerStateFrom;
    @BindView(R.id.local_spinner_from)
    Spinner mSpinnerLocalFrom;
    @BindView(R.id.date_linearlayout)
    LinearLayout mLinearLayoutDate;
    @BindView(R.id.date_text)EditText mEditTextDate;
    @BindView(R.id.add_trip_button)
    Button mButtonPost;
    @BindView(R.id.phone_number) EditText mEditTextPhoneNumber;
    AddTripViewModel mAddTripViewModel;
    CustomSpinnerAdapter mCustomSpinnerAdapter;
    CustomSpinnerAdapter mLocalAdapter;
    CustomSpinnerAdapter mStateSpinnerAdapterFrom;
    CustomSpinnerAdapter mLocalAdapterFrom;
    private LottieAnimationView animationView;
    private List<State> states;
    private List<Local> mLocals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        ButterKnife.bind(this);
        setListener();
        initLoadingAnim();
        App app = (App) this.getApplication();
        initViewModel(app);
    }

    private void initViewModel(App app){
        mAddTripViewModel = ViewModelProviders.of(this, new AppFactory(app)).get(AddTripViewModel.class);
        subscribeToRegionStreams(mAddTripViewModel);
        initUserData(mAddTripViewModel);
    }

    private void initUserData(AddTripViewModel addTripViewModel){
        addTripViewModel.getUserData().observe(this, this::populateUserData);
    }
    private void populateUserData(Resource<User> user){
        switch (user.status){
            case SUCCESS:
                if (user.data != null){
                    mAddTripViewModel.mPhoneNo = user.data.phone_no;
                    mAddTripViewModel.mUserId = user.data.id;
                    mEditTextPhoneNumber.setText(mAddTripViewModel.mPhoneNo);
                }
                break;
        }
    }

    private void subscribeToRegionStreams(AddTripViewModel addTripViewModel){
        addTripViewModel.getRegions().observe(this, this::handleRegionResponse);
    }

    private void subscribeToTripResponse(){
        mAddTripViewModel.getResponse().observe(this, this::handelTripResponse);
    }

    private void handelTripResponse(Resource<Trip> tripResource){
        stopAnim();
        switch (tripResource.status){
            case SUCCESS:
                //if(tripResource.data!= null){
                    navigateToTripActivity();
                    Toast.makeText(this, getString(R.string.post_success), Toast.LENGTH_SHORT).show();
               // }
                break;
            case MESSAGE:
                break;

        }
    }

    @OnClick(R.id.add_trip_button)
    void postTrip(){
        startAnim();
        subscribeToTripResponse();
    }

    @OnClick(R.id.date_linearlayout)
    void showDatePicker(){
        showPicker();
    }

    void navigateToTripActivity(){
        Intent intent = TripActivity.newIntent(this);
        startActivity(intent);
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
        String format = "MMMM d";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        mAddTripViewModel.mTravelDate = sdf.format(myCalendar.getTime());
        mEditTextDate.setText(mAddTripViewModel.mTravelDate);
    }

    private void handleRegionResponse(Resource<Region> regionResource){
        if(regionResource.data == null )
            return;
        states = new ArrayList<>();
        mLocals = new ArrayList<>();
        states = regionResource.data.getState();
        List<String> mStrings = new ArrayList<>();
        for(State state: states){
            mStrings.add(state.getName());
        }
        mCustomSpinnerAdapter = new CustomSpinnerAdapter(this, mStrings);
        mSpinnerStateTo.setAdapter(mCustomSpinnerAdapter);
        mStateSpinnerAdapterFrom = new CustomSpinnerAdapter(this, mStrings);
        mSpinnerStateFrom.setAdapter( mStateSpinnerAdapterFrom);
    }
    private void setListener(){
        mSpinnerStateTo.setOnItemSelectedListener(this);
        mSpinnerLocalTo.setOnItemSelectedListener(this);
        mSpinnerStateFrom.setOnItemSelectedListener(this);
        mSpinnerLocalFrom.setOnItemSelectedListener(this);
        mEditTextDate.setMovementMethod(null);
        mEditTextDate.setKeyListener(null);
        mEditTextPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mAddTripViewModel.mPhoneNo = String.valueOf(editable.toString());
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()){
            case R.id.country_spinner:
                mAddTripViewModel.mToState = states.get(i).getName();
                mLocals = states.get(i).getLocals();
                List<String> mdata = new ArrayList<>();
                for (Local local: mLocals){
                    mdata.add(local.getName());
                }
                mLocalAdapter = new CustomSpinnerAdapter(AddTripActivity.this, mdata);
                mSpinnerLocalTo.setAdapter(mLocalAdapter);
                break;
            case R.id.local_spinner:
                mAddTripViewModel.mToLocal = mLocals.get(i).getName();
                break;
            case R.id.country_spinner_from:
                mAddTripViewModel.mFromState = states.get(i).getName();
                mLocals = states.get(i).getLocals();
                List<String> mdataFrom = new ArrayList<>();
                for (Local local: mLocals){
                    mdataFrom.add(local.getName());
                }
                mLocalAdapterFrom = new CustomSpinnerAdapter(AddTripActivity.this, mdataFrom);
                mSpinnerLocalFrom.setAdapter(mLocalAdapterFrom);
                break;
            case R.id.local_spinner_from:
                mAddTripViewModel.mFromLocal =mLocals.get(i).getName();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    void initLoadingAnim(){
        animationView = (LottieAnimationView) findViewById(R.id.animation_view);
        animationView.setAnimation("preloader.json");
        animationView.loop(true);
        animationView.setVisibility(View.GONE);
        animationView.setScale(0.3f);

    }

    void startAnim(){
        animationView.setVisibility(View.VISIBLE);
        animationView.playAnimation();
    }

    void stopAnim(){
        animationView.cancelAnimation();
        animationView.setVisibility(View.GONE);
    }
}
