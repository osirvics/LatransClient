package com.example.victor.latrans.di.component;


import com.example.victor.latrans.di.PerActivity;
import com.example.victor.latrans.di.module.ActivityModule;
import com.example.victor.latrans.view.ui.login.LoginActivity;

import dagger.Component;

/**
 * Created by janisharali on 08/12/16.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(LoginActivity mainActivity);

}
