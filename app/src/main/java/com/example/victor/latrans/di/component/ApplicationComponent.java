package com.example.victor.latrans.di.component;

import android.app.Application;
import android.content.Context;

import com.example.victor.latrans.di.ApplicationContext;
import com.example.victor.latrans.di.module.ApplicationModule;
import com.example.victor.latrans.view.ui.App;

import javax.inject.Singleton;

import dagger.Component;


/**
 * Created by janisharali on 08/12/16.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(App demoApplication);

    @ApplicationContext
    Context getContext();

    Application getApplication();
//
//    DatabaseManager getDataManager();
//
//    SharedPrefsHelper getPreferenceHelper();
//
//    AppDatabase getDbHelper();

}
