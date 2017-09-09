package com.example.victor.latrans.dependency;

import com.example.victor.latrans.fcm.LatransFirebaseMessagingService;
import com.example.victor.latrans.view.ui.login.LoginActivity;
import com.example.victor.latrans.view.ui.login.LoginViewModel;
import com.example.victor.latrans.view.ui.message.ConversationActivity;
import com.example.victor.latrans.view.ui.message.ConversationViewModel;
import com.example.victor.latrans.view.ui.signup.SignUpViewModel;
import com.example.victor.latrans.view.ui.signup.SignupActivity;
import com.example.victor.latrans.view.ui.trip.TripActivity;
import com.example.victor.latrans.view.ui.trip.TripViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by James on 6/7/2017.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(SignupActivity signupActivity);
    void inject(LoginActivity loginActivity);
    void inject(TripActivity tripActivity);
    void inject(ConversationActivity conversationActivity);

    void inject(SignUpViewModel signUpViewModel);
    void inject(LoginViewModel loginViewModel);
    void inject(TripViewModel tripViewModel);
    void inject(ConversationViewModel conversationViewModel);

    void inject (LatransFirebaseMessagingService messagingService);




//    String getUsername();
//    String getPassword();

    interface Injectable {
        void inject(AppComponent appComponent);
    }

}