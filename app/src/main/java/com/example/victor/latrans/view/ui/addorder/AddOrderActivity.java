package com.example.victor.latrans.view.ui.addorder;

import android.Manifest;
import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.airbnb.lottie.LottieAnimationView;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.bumptech.glide.Glide;
import com.example.victor.latrans.R;
import com.example.victor.latrans.amazon.AmazonUtility;
import com.example.victor.latrans.dependency.AppFactory;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.db.entity.Request;
import com.example.victor.latrans.repocitory.local.db.entity.User;
import com.example.victor.latrans.repocitory.local.model.Local;
import com.example.victor.latrans.repocitory.local.model.Region;
import com.example.victor.latrans.repocitory.local.model.State;
import com.example.victor.latrans.repocitory.local.model.UploadResponse;
import com.example.victor.latrans.view.ui.App;
import com.example.victor.latrans.view.ui.order.OrderActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.ganfra.materialspinner.MaterialSpinner;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import timber.log.Timber;

@RuntimePermissions
public class AddOrderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener{
    public static Intent newIntent(Context context) {
        Intent mIntent = new Intent(context, AddOrderActivity.class);
        return mIntent;
    }


    MaterialSpinner mSpinnerDeliveryState;
    MaterialSpinner mSpinnerDeliveryCity;
    MaterialSpinner mSpinnerItemState;
    MaterialSpinner mSpinnerItemCity;
    @BindView(R.id.photo_thumbnail)
    ImageView mPhotoThumbnail;
    @BindView(R.id.order_add_photo)
    ImageView mOrderAddPhoto;
    @BindView(R.id.order_item_name)
    EditText mOrderItemName;
    @BindView(R.id.order_date_text)
    TextInputEditText mOrderDateText;
    @BindView(R.id.order_starting_price)
    EditText mOrderStartingPrice;
    @BindView(R.id.phone_number)
    TextInputEditText mPhoneNumber;
    @BindView(R.id.add_order_button)
    AppCompatButton mPostButton;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    public static int REQUEST_CODE_CHOOSE = 35;
    private ArrayAdapter<String> adapter;
    AddOrderViewModel mAddOrderViewModel;
    private List<Local> mLocalList;
    private List<State> mStateList;
    private LottieAnimationView animationView;
    MaterialDialog dialog;
    MaterialDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        ButterKnife.bind(this);
        App app = (App) this.getApplication();
        initSetup();
        initLoadingAnim();
        initProgressDialog();
        initViewModel(app);
        initListner();

    }


    private void initViewModel(App app){
         mAddOrderViewModel = ViewModelProviders.of(this, new AppFactory(app)).get(AddOrderViewModel.class);
        initUserData();
        initLocationData();
    }


    @OnClick(R.id.order_add_photo)
    public void chooseImage(){
        AddOrderActivityPermissionsDispatcher.selectImageWithCheck(this);
    }

    @OnClick(R.id.order_starting_price)
    void setPrice(){
        showAmountInvalidation();
    }

    @OnClick(R.id.add_order_button)
    public void postRequest(){
        if(validate()){
            mPostButton.setEnabled(false);
            mAddOrderViewModel.startUpload().observe(this, this::handleUploadResponse);
        }

    }

    private void handleUploadResponse(Resource<UploadResponse> responseResource){
        if(responseResource!= null){
            if(null != responseResource.data.getLong()){
                Integer progress =  (int) (long) responseResource.data.getLong();
                startAnim();
            }
            if(null != responseResource.data.mState){
                TransferState state = responseResource.data.mState;
              switch (state){
                  case COMPLETED:
                      postOrder();
                      break;
                  case FAILED:
                      stopAnim();
                      Toast.makeText(this,getString(R.string.profile_updated_error), Toast.LENGTH_SHORT).show();
                      break;


              }
            }
            if(null != responseResource.data.mException){
              mPostButton.setEnabled(true);
              stopAnim();
                Toast.makeText(this,getString(R.string.profile_updated_error), Toast.LENGTH_SHORT).show();
            }

        }

    }
    private void postOrder(){
        mAddOrderViewModel.postOrder().observe(this, this::handlePostResponse);
    }

    private void handlePostResponse(Resource<Request> resource) {
        stopAnim();
        mPostButton.setEnabled(true);
        switch (resource.status) {
            case SUCCESS:
                AmazonUtility.deleteFile(mAddOrderViewModel.mItemImagefile, this);
                Toast.makeText(this, getString(R.string.profile_update_success), Toast.LENGTH_SHORT).show();
                Intent intent = OrderActivity.newIntent(this);
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                break;
            case MESSAGE:
                Toast.makeText(this, getString(R.string.profile_updated_error), Toast.LENGTH_SHORT).show();
                break;

        }
    }
    private void initUserData(){
        mAddOrderViewModel.getUserData().observe(this, this::populateUserData);
    }
    private void initLocationData(){
        mAddOrderViewModel.getRegions().observe(this, this::handleRegionResponse);
    }
    private void populateUserData(Resource<User> user){
        switch (user.status){
            case SUCCESS:
                if (user.data != null){
                   mAddOrderViewModel.userId = user.data.getId();
                    mAddOrderViewModel.mContactNumber = user.data.getPhone_no();
                    mPhoneNumber.setText(mAddOrderViewModel.mContactNumber);
                }
                break;
        }
    }

    private void handleRegionResponse(Resource<Region> regionResource){
        if(regionResource.data == null )
            return;
        mStateList = new ArrayList<>();
        mLocalList = new ArrayList<>();
        mAddOrderViewModel.mStates = regionResource.data.getState();
        mStateList = regionResource.data.getState();
        List<String> mStrings = new ArrayList<>();
        for(State state: mStateList){
            mStrings.add(state.getName());
        }

        setUpSpinnerAdapter(mSpinnerDeliveryState, mStrings);
        setUpSpinnerAdapter(mSpinnerItemState, mStrings);
    }

    private void setUpSpinnerAdapter(MaterialSpinner spinner, List<String> mStrings){
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mStrings );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setPaddingSafe(0, 0, 0, 0);
    }



    private void initSetup(){
        mToolbar.setTitle(getString(R.string.add_order_activity_name));
        mSpinnerDeliveryState = findViewById(R.id.spinner_delivery_state);
        mSpinnerDeliveryCity = findViewById(R.id.spinner_delivery_city);
        mSpinnerItemState = findViewById(R.id.spinner_item_location_state);
        mSpinnerItemCity = findViewById(R.id.spinner_item_location_city);

    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void selectImage(){
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .theme(R.style.Matisse_Dracula)
                .countable(false)
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, "com.example.victor.latrans.fileprovider"))
                .maxSelectable(1)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mAddOrderViewModel.mSelected = Matisse.obtainResult(data);
            processUrlToFile();
        }
    }

    void processUrlToFile(){
        try {
            mAddOrderViewModel.mItemImagefile = AmazonUtility.copyContentUriToFile(this, mAddOrderViewModel.mSelected.get(0));
            File file =   mAddOrderViewModel.mItemImagefile;
            String imagePath = file.getAbsolutePath();
            Glide.with(this).load(imagePath).centerCrop().into(mPhotoThumbnail);
            mAddOrderViewModel.mItemPictureUrl = AmazonUtility.getAmazonLink(mAddOrderViewModel.mItemImagefile);
        } catch (IOException e) {
            Toast.makeText(this, getString(R.string.error_unknown), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
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
                AddOrderActivity.this, AddOrderActivity.this, year, month, day);
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
        mAddOrderViewModel.mExpectedDelivryDate = sdf.format(myCalendar.getTime());
        mOrderDateText.setText(mAddOrderViewModel.mExpectedDelivryDate);
    }

    private void initListner(){
        mOrderDateText.setMovementMethod(null);
        mOrderDateText.setKeyListener(null);
        mPhoneNumber.addTextChangedListener(new GenericTextWatcher(mPhoneNumber, mAddOrderViewModel));
        mOrderItemName.addTextChangedListener(new GenericTextWatcher(mOrderItemName, mAddOrderViewModel));


        mSpinnerItemState.setOnItemSelectedListener(this);
        mSpinnerItemCity.setOnItemSelectedListener(this);
        mSpinnerDeliveryState.setOnItemSelectedListener(this);
        mSpinnerDeliveryCity.setOnItemSelectedListener(this);

    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        AddOrderActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == -1)
            return;
        switch (adapterView.getId()){
            case R.id.spinner_delivery_state:
                mAddOrderViewModel.mDeliveryState = mStateList.get(i).getName();
                mLocalList = mAddOrderViewModel.mStates.get(i).getLocals();
                setUpSpinnerAdapter(mSpinnerDeliveryCity, setStateData(mLocalList));
                mSpinnerDeliveryCity.performClick();
                break;
            case R.id.spinner_delivery_city:
                mAddOrderViewModel.mDeliveryCity = mLocalList.get(i).getName();
                break;
            case R.id.spinner_item_location_state:
                mAddOrderViewModel.mItemLocationState = mStateList.get(i).getName();
                mLocalList = mAddOrderViewModel.mStates.get(i).getLocals();
                setUpSpinnerAdapter(mSpinnerItemCity, setStateData(mLocalList));
                mSpinnerItemCity.performClick();
                break;
            case R.id.spinner_item_location_city:
                mAddOrderViewModel.mItemLocationCity = mLocalList.get(i).getName();
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //Gets names of local
    private List<String> setStateData(List<Local> mLocals){
        List<String> mdata = new ArrayList<>();
        for (Local local: mLocals){
            mdata.add(local.getName());
        }
        return mdata;
    }


    private static class GenericTextWatcher implements TextWatcher {
        private EditText mEditText;
        private AddOrderViewModel mViewModel;
        public GenericTextWatcher(EditText editText, AddOrderViewModel viewModel){
            this.mEditText = editText;
            this.mViewModel = viewModel;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            int id = mEditText.getId();
            switch (id){
                case R.id.phone_number:
                    mViewModel.mContactNumber = editable.toString();
                    break;
                case R.id.order_item_name:
                    mViewModel.mItemName = editable.toString();
                    break;

            }

        }
    }

    private void initProgressDialog(){
         builder = new MaterialDialog.Builder(this)
                .title("Uploading content")
                .content("Please wait")
                .progress(false, 100, false);
        dialog = builder.build();

    }

    public void showAmountInvalidation() {
        new MaterialDialog.Builder(this)
                .title("Enter starting reward")
                .content("Amount (₦)")
                .inputRange(1,9)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .negativeText("Cancel")
                .positiveText("Save")
                .alwaysCallInputCallback() // this forces the callback to be invoked with every input change
                .input(
                        R.string.input_hint,
                        0,
                        false,
                        this::validateAmountInput)
                .onAny((dialog, which) -> {
                    switch (which){
                        case NEGATIVE:
                            mOrderStartingPrice.setText("");
                            mAddOrderViewModel.mItemStartingRewardAmount = "";
                            break;
                    }
                })
                .show();
    }

    public static String removeLeadingZeroes(String value) {
        return Integer.valueOf(value).toString();
    }

    private void validateAmountInput(MaterialDialog dialog, CharSequence input){
        String amount = input.toString();
        if (!TextUtils.isEmpty(amount)){
            amount =  removeLeadingZeroes(amount);
        }
        mAddOrderViewModel.mItemStartingRewardAmount = amount;
        mOrderStartingPrice.setText("₦" + mAddOrderViewModel.mItemStartingRewardAmount);
        if (amount.equals("0")) {
            dialog.setContent("Please enter a valid amount");
            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
        } else {
            dialog.setContent("Amount (N)");
            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
        }
    }


    private boolean validate(){
        String itemName = mAddOrderViewModel.mItemName;
        Timber.e("Itemname: " + itemName);
        File file = mAddOrderViewModel.mItemImagefile;
        //String itemPicture = mAddOrderViewModel.mItemPictureUrl;
        String deliverDate = mAddOrderViewModel.mExpectedDelivryDate;
        String contactNumber = mAddOrderViewModel.mContactNumber;
        String rewardAmount = mAddOrderViewModel.mItemStartingRewardAmount;

        if(itemName == null || TextUtils.isEmpty(itemName)){
            mOrderItemName.setError("Please enter an item name");
            return false;
        }
        if (file == null){
          Toast.makeText(this, "Please provide a picture of the item", Toast.LENGTH_SHORT).show();
          return false;
        }
        if(deliverDate == null || TextUtils.isEmpty(deliverDate)){
            mOrderDateText.setError("Please provide expected delivery date of item");
            return false;
        }
        if(contactNumber == null || TextUtils.isEmpty(contactNumber)){
            mPhoneNumber.setError("Please provide a contact number");
            return false;
        }
        if(rewardAmount == null || TextUtils.isEmpty(rewardAmount)){
            mOrderStartingPrice.setError("Enter a reward amount in ₦");
            return false;
        }

        return true;
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
