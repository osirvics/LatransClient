package com.example.victor.latrans.view.ui.signup;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.victor.latrans.repocitory.local.db.DatabaseManager;
import com.example.victor.latrans.repocitory.local.model.Registration;
import com.example.victor.latrans.repocitory.remote.api.APIService;
import com.example.victor.latrans.repocitory.remote.api.ServiceGenerator;
import com.example.victor.latrans.repocitory.SignupRepository;
import com.example.victor.latrans.repocitory.remote.api.response.NewUserResponse;
import com.example.victor.latrans.util.SharedPrefsHelper;

/**
 * Created by Victor on 31/08/2017.
 */

public class SignUpViewModel extends AndroidViewModel {
    public SignUpViewModel(Application application) {
        super(application);
        init();
    }

    private String username;
    private String password;
    private String email;

    private SignupRepository mSignupRepository;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private LiveData<NewUserResponse> mLiveData;

     LiveData<NewUserResponse> getResponse(){
        mLiveData = new MutableLiveData<>();
        queryResponse();
        return  mLiveData;

    }

    private void queryResponse(){
        mLiveData = mSignupRepository.getUserCredential(buildCredentials());
    }

    private void init(){
        if (mSignupRepository == null){
            mSignupRepository = SignupRepository.getInstance(ServiceGenerator.createService(APIService.class,"", ""),
                    SharedPrefsHelper.getInstance(this.getApplication()), DatabaseManager.getInstance(this.getApplication())
            .getDatabase().userDao());
        }
    }

    private Registration buildCredentials(){
        Registration registration = new Registration();
        registration.setUsername(getUsername());
        registration.setPassword(getPassword());
        registration.setEmail(getEmail());
        return registration;
    }

}
