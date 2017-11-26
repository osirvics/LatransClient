package com.example.victor.latrans.view.ui.signup;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.victor.latrans.R;
import com.example.victor.latrans.dependency.AppFactory;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.model.NewUser;
import com.example.victor.latrans.util.Util;
import com.example.victor.latrans.view.ui.App;
import com.example.victor.latrans.view.ui.login.LoginActivity;
import com.example.victor.latrans.view.ui.profile.ProfileActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity implements LifecycleRegistryOwner{

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, SignupActivity.class);
        return i;
    }

    @BindView(R.id.btn_signup)
    Button mButtonSignUp;
    @BindView(R.id.input_first_name)
    EditText mEditTextFirstName;
    @BindView(R.id.input_last_name)
    EditText mEditTextLastName;
    @BindView(R.id.input_email) EditText mEditTextEmail;
    @BindView(R.id.input_password) EditText mEditTextPassword;
    @BindView(R.id.login) Button mButtonLogin;
    LottieAnimationView animationView;
    SignUpViewModel mSignUpViewModel;

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
       ((App) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        App app = (App) this.getApplication();

        setClickListeners();
        initLoadingAnim();
        initViewModel(app);

    }

    private void initViewModel(App application){
        mSignUpViewModel = ViewModelProviders.of(this, new AppFactory(application)).get(SignUpViewModel.class);
    }


    @OnClick(R.id.login)
    public void login(){
        Intent intent = LoginActivity.newIntent(this);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
    @OnClick(R.id.btn_signup)
    public void CreateNewUserWithCredentials(){
        if(validate()){
            startAnim();
            subscribeToDataStreams(mSignUpViewModel);
        }

    }

    private void subscribeToDataStreams(final  SignUpViewModel signUpViewModel){
        signUpViewModel.getResponse().observe(this, this::handleStreams);}

    private void handleStreams(Resource<NewUser> response){
        switch (response.status){
            case SUCCESS:
                stopAnim();
                Toast.makeText(this, "Token: "+ response.data.getToken(), Toast.LENGTH_SHORT).show();
                Intent intent = ProfileActivity.Companion.newImtent(this);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case MESSAGE:
                stopAnim();
                Toast.makeText(this, "Username: "+ response.message, Toast.LENGTH_SHORT).show();
                break;
            case ERROR:
                stopAnim();
                Toast.makeText(this, "Username: "+ response.error, Toast.LENGTH_SHORT).show();
                break;

        }

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
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

    /* To restrict Space Bar in Keyboard */
    InputFilter filter = (source, start, end, dest, dstart, dend) -> {
        for (int i = start; i < end; i++) {
            if (Character.isWhitespace(source.charAt(i))) {
                return "";
            }
        }
        return null;
    };

    public boolean validate() {
        boolean valid = true;

        String userFirstName = mSignUpViewModel.firstName;
        String userLastName = mSignUpViewModel.lastName;
        String email = mSignUpViewModel.email;
        String password = mSignUpViewModel.password;


        if (TextUtils.isEmpty(userFirstName)) {
            mEditTextFirstName.setError(getString(R.string.error_invalid_name));
            valid = false;
        } else {
            mEditTextLastName.setError(null);
        }

        if (TextUtils.isEmpty(userLastName)) {
            mEditTextLastName.setError(getString(R.string.error_invalid_name));
            valid = false;
        } else {
            mEditTextLastName.setError(null);
        }


        if (TextUtils.isEmpty(email) || !Util.isEmailValid(email)) {
            mEditTextEmail.setError(getString(R.string.error_invalid_email));
            valid = false;
        } else {
            mEditTextEmail.setError(null);
        }

        if (TextUtils.isEmpty(password) || password.length() < 6 || password.length() > 16) {
            mEditTextPassword.setError(getString(R.string.error_invalid_password));
            valid = false;
        } else {
            mEditTextPassword.setError(null);
        }

        return valid;
    }

    private void setClickListeners(){
        mEditTextPassword.setFilters(new InputFilter[] { filter });
        mEditTextEmail.setFilters(new InputFilter[] { filter });

        mEditTextFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mSignUpViewModel.firstName = editable.toString();
            }
        });
        mEditTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mSignUpViewModel.email = editable.toString();
            }
        });
        mEditTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mSignUpViewModel.password = editable.toString();
            }
        });
        mEditTextLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mSignUpViewModel.lastName = editable.toString();
            }
        });
    }


}
