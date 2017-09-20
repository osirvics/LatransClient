package com.example.victor.latrans.dependency;

import com.example.victor.latrans.fcm.LatransFirebaseMessagingService;
import com.example.victor.latrans.view.ui.login.LoginActivity;
import com.example.victor.latrans.view.ui.login.LoginViewModel;
import com.example.victor.latrans.view.ui.message.ConversationActivity;
import com.example.victor.latrans.view.ui.message.ConversationViewModel;
import com.example.victor.latrans.view.ui.message.MessageActivity;
import com.example.victor.latrans.view.ui.message.MessageViewModel;
import com.example.victor.latrans.view.ui.profile.EditProfileActivity;
import com.example.victor.latrans.view.ui.profile.EditProfileViewModel;
import com.example.victor.latrans.view.ui.profile.ProfileActivity;
import com.example.victor.latrans.view.ui.profile.ProfileViewModel;
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
    void inject(MessageActivity messageActivity);
    void inject(ProfileActivity profileActivity);
    void inject(EditProfileActivity editProfileActivity);

    void inject(SignUpViewModel signUpViewModel);
    void inject(LoginViewModel loginViewModel);
    void inject(TripViewModel tripViewModel);
    void inject(ConversationViewModel conversationViewModel);
    void inject(MessageViewModel messageViewModel);
    void inject(ProfileViewModel profileViewModel);
    void inject(EditProfileViewModel editProfileViewModel);

    void inject (LatransFirebaseMessagingService messagingService);




//    String getUsername();
//    String getPassword();

    interface Injectable {
        void inject(AppComponent appComponent);
    }

}