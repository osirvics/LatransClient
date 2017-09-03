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
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.victor.latrans.R;
import com.example.victor.latrans.repocitory.remote.api.response.NewUserResponse;
import com.example.victor.latrans.util.Util;
import com.example.victor.latrans.view.ui.login.LoginActivity;

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
    @BindView(R.id.input_name)
    EditText mEditTextUsername;
    @BindView(R.id.input_email) EditText mEditTextEmail;
    @BindView(R.id.input_password) EditText mEditTextPassowrd;
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
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setClickListeners();
        initLoadingAnim();
        initViewModel();

    }

    private void initViewModel(){
        mSignUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
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
        signUpViewModel.getResponse().observe(this, response -> handleStreams(response));}

    private void handleStreams(NewUserResponse response){

        if(null != response.getNewUser()){
            stopAnim();
        }
        else if (null != response.getMessage()){
            stopAnim();
            Toast.makeText(getApplicationContext(),  response.getMessage(), Toast.LENGTH_SHORT).show();
        }
        else if (null != response.getError()){
            stopAnim();
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
    InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (Character.isWhitespace(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }

    };


    public boolean validate() {
        boolean valid = true;

        String username = mSignUpViewModel.getUsername();
        String email = mSignUpViewModel.getEmail();
        String password = mSignUpViewModel.getPassword();


        if (TextUtils.isEmpty(username) || username.length() < 4 || username.length() > 32) {
            mEditTextUsername.setError(getString(R.string.error_invalid_username));
            valid = false;
        } else {
            mEditTextUsername.setError(null);
        }

        if (TextUtils.isEmpty(email) || !Util.isEmailValid(email)) {
            mEditTextEmail.setError(getString(R.string.error_invalid_email));
            valid = false;
        } else {
            mEditTextEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 16) {
            mEditTextPassowrd.setError(getString(R.string.error_invalid_password));
            valid = false;
        } else {
            mEditTextPassowrd.setError(null);
        }

        return valid;
    }

    private void setClickListeners(){
        mEditTextPassowrd.setFilters(new InputFilter[] { filter });
        mEditTextUsername.setFilters(new InputFilter[] { filter });
        mEditTextEmail.setFilters(new InputFilter[] { filter });

        mEditTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mSignUpViewModel.setUsername(editable.toString());
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
                mSignUpViewModel.setEmail(editable.toString());
            }
        });
        mEditTextPassowrd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mSignUpViewModel.setPassword(editable.toString());
            }
        });
    }


}