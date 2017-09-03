package com.example.victor.latrans.view.ui;

import android.app.Application;
import android.content.Context;

import com.example.victor.latrans.di.component.ApplicationComponent;
import com.example.victor.latrans.di.component.DaggerApplicationComponent;
import com.example.victor.latrans.di.module.ApplicationModule;

import timber.log.Timber;

public class App extends Application {

	//private static App sInstance;

	protected ApplicationComponent applicationComponent;

	public static App get(Context context) {
		return (App) context.getApplicationContext();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		//sInstance = this;
		Timber.plant(new Timber.DebugTree(){
			@Override
			protected String createStackElementTag(StackTraceElement element) {
				return super.createStackElementTag(element) + ":" + element.getLineNumber();
			}
		});




		applicationComponent = DaggerApplicationComponent
				.builder()
				.applicationModule(new ApplicationModule(this))
				.build();
		applicationComponent.inject(this);
	}


	public ApplicationComponent getComponent(){
		return applicationComponent;
	}

//	    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }



//	public static App getInstance() {
//		if (sInstance == null) {
//			sInstance = new App();
//		}
//		return sInstance;
//	}

//	public static Context getContext() {
//		return getInstance().getApplicationContext();
//	}
}
