package com.example.victor.latrans.view.ui.login;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
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
import com.example.victor.latrans.dependency.AppFactory;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.model.NewUser;
import com.example.victor.latrans.view.ui.App;
import com.example.victor.latrans.view.ui.profile.ProfileActivity;
import com.example.victor.latrans.view.ui.signup.SignupActivity;
import com.example.victor.latrans.view.ui.trip.TripActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    LottieAnimationView animationView;
    private LoginViewModel mLoginViewModel;

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       //((App) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        App app = (App) this.getApplication();
        setUpCLickListener();
        initLoadingAnim();
        initViewModel(app);
    }


    private void initViewModel(App app) {
        mLoginViewModel = ViewModelProviders.of(this, new AppFactory(app)).get(LoginViewModel.class);
      //subscribeDataStreams(mLoginViewModel);
    }


    /**
     * Observes token
     * @param loginViewModel
     */
    private void subscribeDataStreams(final LoginViewModel loginViewModel){
        loginViewModel.getToken().observe(this, this::handleResponse);}

   void enableRetry(){
       stopAnim();
       mLoginButton.setEnabled(true);
    }

    void handleResponse(@Nullable Resource<NewUser> response){
       switch (response.status){
           case SUCCESS:
               enableRetry();
               Toast.makeText(LoginActivity.this, response.data.getToken(), Toast.LENGTH_SHORT).show();
               Intent intent = ProfileActivity.Companion.newImtent(this);
               startActivity(intent);
               overridePendingTransition(R.anim.enter, R.anim.exit);
               break;
           case MESSAGE:
               enableRetry();
               Toast.makeText(LoginActivity.this, response.message, Toast.LENGTH_SHORT).show();
               break;
           case ERROR:
               enableRetry();
               Toast.makeText(LoginActivity.this, response.error.toString(), Toast.LENGTH_SHORT).show();
               break;

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
        String email = mLoginViewModel.mEmail;
        String password = mLoginViewModel.mPassword;
        //|| !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        if (TextUtils.isEmpty(email)|| email == null) {
            mEmailText.setError(getString(R.string.invalid_email));
            valid = false;
        } else {
            mEmailText.setError(null);
        }
        if (TextUtils.isEmpty(password) || password == null) {
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
                mLoginViewModel.mEmail = editable.toString();
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
                mLoginViewModel.mPassword =  editable.toString();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = TripActivity.newIntent(this);
        startActivity(intent);
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}
