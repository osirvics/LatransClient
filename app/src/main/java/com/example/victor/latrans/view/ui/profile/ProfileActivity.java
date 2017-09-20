package com.example.victor.latrans.view.ui.profile;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.victor.latrans.BaseActivity;
import com.example.victor.latrans.R;
import com.example.victor.latrans.dependency.AppFactory;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.db.entity.User;
import com.example.victor.latrans.util.SharedPrefsHelper;
import com.example.victor.latrans.view.ui.App;
import com.example.victor.latrans.view.ui.login.LoginActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends BaseActivity  implements LifecycleRegistryOwner {



    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    public static Intent newImtent(Context context) {
        Intent mIntent = new Intent(context, ProfileActivity.class);
        return mIntent;
    }


    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.profile_image)
    ImageView mProfileImage;
    @BindView(R.id.profile_name)
    TextView mProfileName;
    @BindView(R.id.mobile_no)
    TextView mMobileNo;
    @BindView(R.id.profile_email)
    TextView mProfileEmail;
    ProfileViewModel mProfileViewModel;

    //TODO remove this here!!!
    @Inject
    SharedPrefsHelper mSharedPrefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_profile);
        ((App) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        App app = (App) this.getApplication();
        initView();
        initViewModel(app);


    }
    private void initViewModel(App app){
        mProfileViewModel = ViewModelProviders.of(this, new AppFactory(app)).get(ProfileViewModel.class);
       subscribeToDataStreams(mProfileViewModel);
    }

    void subscribeToDataStreams(ProfileViewModel profileViewModel){
        profileViewModel.getResponse().observe(this, this::handleResponse);
    }

    private void handleResponse(Resource<User> user){
        switch (user.status){
            case SUCCESS:
                if (user.data != null){
                    //mSharedPrefsHelper.setUserId(user.data.id);
                    String url = user.data.picture;
                    mMobileNo.setText(user.data.phone_no);
                    mProfileName.setText(user.data.name);
                    Glide.with(this).load(url).centerCrop().placeholder(R.drawable.ic_person_grey_600_24dp).error(R.drawable.ic_person_grey_600_24dp).into(mProfileImage);
                    mProfileEmail.setText(user.data.email);
                }
                else  {
                    openLoginActivity();
                }
                break;
            case MESSAGE:
                openLoginActivity();
                break;
        }

    }

    void initView() {
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(view -> openEditActivity());
    }

    void openEditActivity() {
        Intent intent = EditProfileActivity.newINtent(this);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    void openLoginActivity(){
        Intent intent = LoginActivity.newIntent(this);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_profile;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_profile;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}
