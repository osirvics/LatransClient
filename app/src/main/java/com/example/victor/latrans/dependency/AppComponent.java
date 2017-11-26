package com.example.victor.latrans.dependency;

import com.example.victor.latrans.fcm.LatransFirebaseMessagingService;
import com.example.victor.latrans.view.ui.addorder.AddOrderActivity;
import com.example.victor.latrans.view.ui.addorder.AddOrderViewModel;
import com.example.victor.latrans.view.ui.addtrip.AddTripActivity;
import com.example.victor.latrans.view.ui.addtrip.AddTripViewModel;
import com.example.victor.latrans.view.ui.login.LoginActivity;
import com.example.victor.latrans.view.ui.login.LoginViewModel;
import com.example.victor.latrans.view.ui.message.ConversationActivity;
import com.example.victor.latrans.view.ui.message.ConversationViewModel;
import com.example.victor.latrans.view.ui.message.MessageActivity;
import com.example.victor.latrans.view.ui.message.MessageViewModel;
import com.example.victor.latrans.view.ui.order.OrderActivity;
import com.example.victor.latrans.view.ui.order.OrderViewModel;
import com.example.victor.latrans.view.ui.profile.EditProfileActivity;
import com.example.victor.latrans.view.ui.profile.EditProfileViewModel;
import com.example.victor.latrans.view.ui.profile.ProfileActivity;
import com.example.victor.latrans.view.ui.profile.ProfileViewModel;
import com.example.victor.latrans.view.ui.profile.fragment.ProfileFragment;
import com.example.victor.latrans.view.ui.profile.fragment.ProfileOrderFragment;
import com.example.victor.latrans.view.ui.profile.fragment.ProfileOrderViewModel;
import com.example.victor.latrans.view.ui.profile.fragment.ProfileTripFragment;
import com.example.victor.latrans.view.ui.profile.fragment.ProfileTripViewModel;
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
    void inject(AddTripActivity addTripActivity);
    void inject (OrderActivity orderActivity);
    void inject (AddOrderActivity addOrderActivity);

    void inject (AddOrderViewModel addOrderViewModel);
    void inject (OrderViewModel orderViewModel);
    void inject(SignUpViewModel signUpViewModel);
    void inject(LoginViewModel loginViewModel);
    void inject(TripViewModel tripViewModel);
    void inject(ConversationViewModel conversationViewModel);
    void inject(MessageViewModel messageViewModel);
    void inject(ProfileViewModel profileViewModel);
    void inject(EditProfileViewModel editProfileViewModel);
    void inject(AddTripViewModel addTripViewModel);
    void inject(ProfileTripViewModel profileTripViewModel);
    void inject(ProfileOrderViewModel profileOrderViewModel);



    void inject (LatransFirebaseMessagingService messagingService);


    void inject (ProfileFragment fragment);
    void inject (ProfileTripFragment profileTripFragment);
    void inject (ProfileOrderFragment profileOrderFragment);



    interface Injectable {
        void inject(AppComponent appComponent);
    }

}