package com.example.victor.latrans.view.ui.login;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.victor.latrans.R;
import com.example.victor.latrans.di.component.ActivityComponent;
import com.example.victor.latrans.di.component.DaggerActivityComponent;
import com.example.victor.latrans.di.module.ActivityModule;
import com.example.victor.latrans.repocitory.remote.api.ApiResponse;
import com.example.victor.latrans.view.ui.App;
import com.example.victor.latrans.view.ui.signup.SignupActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

@SuppressWarnings("all")
public class LoginActivity extends AppCompatActivity implements LifecycleRegistryOwner {


    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, LoginActivity.class);
        return i;
    }

    @BindView(R.id.input_email)
    EditText mEmailText;
    @BindView(R.id.input_password) EditText mPasswordText;
    @BindView(R.id.btn_login)
    Button mLoginButton;
    @BindView(R.id.link_signup)
    Button mSignupButton;
    @BindView(R.id.empty)ProgressBar mProgressBar;
    @BindView(R.id.tem_logout)Button mTempLogout;
    LottieAnimationView animationView;
    private LoginViewModel mLoginViewModel;
    private ActivityComponent activityComponent;

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(App.get(this).getComponent())
                    .build();
        }
        return activityComponent;
    }

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        setUpCLickListener();
        initLoadingAnim();
        initViewModel();
    }


    private void initViewModel() {
        mLoginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
    }


    /**
     * Observes token
     * @param loginViewModel
     */
    private void subscribeDataStreams(final LoginViewModel loginViewModel){
        loginViewModel.getToken().observe(this, new Observer<ApiResponse>() {
            @Override
            public void onChanged(@Nullable ApiResponse response) {

            }
        });
    }



   void enableRetry(){
       stopAnim();
       mLoginButton.setEnabled(true);
    }

    void handleResponse(@Nullable ApiResponse response){
        if(null != response.getToken()){
            enableRetry();
            Timber.e("Returned token: " + response.getToken().getToken());
        }
        else if (null != response.getMessage()) {
            enableRetry();
            Toast.makeText(LoginActivity.this, response.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        else {
            enableRetry();
            Toast.makeText(LoginActivity.this, response.getError().toString(), Toast.LENGTH_SHORT).show();

        }
    }

    private void showProgressbar(Boolean flag){
        if(flag)
            mProgressBar.setVisibility(View.VISIBLE);
        else mProgressBar.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_login)
    public void login(){
        if(validate()){
            mLoginButton.setEnabled(false);
            hideKeypad();
            mLoginViewModel.login();
            startAnim();
            subscribeDataStreams(mLoginViewModel);
        }
    }


    @OnClick(R.id.link_signup)
    public void LaunchSignUp(){
        Intent intent = SignupActivity.newIntent(this);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
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




    public boolean validate() {
        boolean valid = true;
        String email = mLoginViewModel.getEmailOrPassword();
        String password = mLoginViewModel.getPassword();
        //|| !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if (email ==null || email.isEmpty() ) {
            mEmailText.setError(getString(R.string.invalid_email));
            valid = false;
        } else {
            mEmailText.setError(null);
        }
        if (password == null || TextUtils.isEmpty(password)) {
            mPasswordText.setError(getString(R.string.empty_password));
            valid = false;
        } else {
            mPasswordText.setError(null);
        }
        return valid;
    }

    private void hideKeypad(){
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpCLickListener(){
        mEmailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mLoginViewModel.setEmailOrPassword(editable.toString());
            }
        });

        mPasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mLoginViewModel.setPassword(editable.toString());
            }
        });
    }


}
