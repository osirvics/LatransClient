package com.example.victor.latrans.view.ui.profile;

import android.Manifest;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.bumptech.glide.Glide;
import com.example.victor.latrans.R;
import com.example.victor.latrans.amazon.AmazonUtility;
import com.example.victor.latrans.dependency.AppFactory;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.db.entity.User;
import com.example.victor.latrans.repocitory.local.model.UploadResponse;
import com.example.victor.latrans.view.ui.App;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class EditProfileActivity extends AppCompatActivity implements LifecycleRegistryOwner {
    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @BindView(R.id.profile_image)
    ImageView mProfileImage;
    @BindView(R.id.btn_upload_image)
    AppCompatButton mBtnUploadImage;
    @BindView(R.id.input_number)
    TextInputEditText mInputNumber;
    @BindView(R.id.input_name)
    TextInputEditText mInputName;
    EditProfileViewModel mEditProfileViewModel;
    @BindView(R.id.upload_progress)ProgressBar mProgressBar;
    FloatingActionButton fab;
    public static int REQUEST_CODE_CHOOSE = 34;

    public static Intent newINtent(Context context) {
        Intent mIntent = new Intent(context, EditProfileActivity.class);
        return mIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        ((App) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        App app = (App) this.getApplication();
        initView();
        setEditListners();
        initViewModel(app);
    }

    private void initViewModel(App app){
        mEditProfileViewModel = ViewModelProviders.of(this, new AppFactory(app)).get(EditProfileViewModel.class);
       // mInputName.setText(mEditProfileViewModel.name);
       // mInputNumber.setText(mEditProfileViewModel.phoneNo);
        initUserData();
    }


    private void initView(){
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {

            if(validate()){
                if(mEditProfileViewModel.file != null){
                    fab.setEnabled(false);
                    mEditProfileViewModel.startUpload().observe(this, responseResource -> handleResponse(responseResource));
                }
                else {
                    fab.setEnabled(false);
                    uploadUserData();
                }
            }


        });
    }

    @OnClick(R.id.btn_upload_image)
    public void chooseImage(){
        EditProfileActivityPermissionsDispatcher.selectImageWithCheck(this);
    }
    private void initUserData(){
        mEditProfileViewModel.getUserData().observe(this, this::populateUserData);
    }
    private void populateUserData(Resource<User> user){
        switch (user.status){
            case SUCCESS:
                if (user.data != null){
                    mEditProfileViewModel.name = user.data.name;
                    mEditProfileViewModel.phoneNo = user.data.phone_no;
                    mInputName.setText(user.data.name);
                    mInputNumber.setText(user.data.phone_no);
                    Glide.with(this).load(user.data.picture).centerCrop()
                            .placeholder(R.drawable.ic_person_grey_600_24dp).error(R.drawable.ic_person_grey_600_24dp)
                            .into(mProfileImage);
                }
                break;
        }
    }

    private void handleResponse(Resource<UploadResponse> responseResource){

        if(responseResource!= null){
            if(null != responseResource.data.getLong()){
                Integer progress =  (int) (long) responseResource.data.getLong();
                showProgressbar(true);
                mProgressBar.setProgress(progress);
            }
            if(null != responseResource.data.mState){
                TransferState state = responseResource.data.mState;
                if(state == TransferState.COMPLETED){
                    mProgressBar.setIndeterminate(true);
                    uploadUserData();
                }
            }
            if(null != responseResource.data.mException){
                fab.setEnabled(true);
                showProgressbar(false);
                Toast.makeText(this,getString(R.string.profile_updated_error), Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void uploadUserData(){
        mEditProfileViewModel.getResponse().observe(this, this::handleUpLoadResponse);
    }

    private void handleUpLoadResponse(Resource<User> resource){
        switch (resource.status){
            case SUCCESS:
                if(resource.data!= null)
                fab.setEnabled(true);
                AmazonUtility.deleteFile(mEditProfileViewModel.file, this);
                finish();
                Toast.makeText(this,getString(R.string.profile_update_success), Toast.LENGTH_SHORT).show();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                break;
            case MESSAGE:
                Toast.makeText(this,getString(R.string.profile_updated_error), Toast.LENGTH_SHORT).show();
                break;

        }


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
            mEditProfileViewModel.setSelected(Matisse.obtainResult(data));
            processUrlToFile();
        }
    }
    private void showProgressbar(Boolean flag){
        if(flag)
            mProgressBar.setVisibility(View.VISIBLE);
        else mProgressBar.setVisibility(View.GONE);
    }

    void processUrlToFile(){
        try {
            mEditProfileViewModel.file = AmazonUtility.copyContentUriToFile(this, mEditProfileViewModel.mSelected.get(0));
            File file =   mEditProfileViewModel.file;
            String imagePath = file.getAbsolutePath();
            Glide.with(this).load(imagePath).centerCrop().into(mProfileImage);
        } catch (IOException e) {
            Toast.makeText(this, getString(R.string.error_unknown), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    void setEditListners(){
        mInputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                        mEditProfileViewModel.name = editable.toString();
            }
        });

        mInputNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    mEditProfileViewModel.phoneNo = String.valueOf(editable.toString());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        EditProfileActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
    private boolean validate(){
        String name = mEditProfileViewModel.name;
        String phone_no = mEditProfileViewModel.phoneNo;

        if(name.length() <=2 ){
            mInputName.setError(getString(R.string.error_name));
            return false;
        }
        String total =  phone_no.toLowerCase();
        char first = total.charAt(0);
        char second  = total.charAt(1);
        char third = total.charAt(2);
        char zero =  '0';
        if(phone_no.length() < 11 || phone_no.length()> 11 || first != zero || second == zero || third != zero ){
            mInputNumber.setError(getString(R.string.error_invalid_number));
            return false;
        }
        return true;
    }
}
