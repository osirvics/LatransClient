package com.example.victor.latrans.view.ui;

import android.app.Application;

import com.example.victor.latrans.dependency.AppComponent;
import com.example.victor.latrans.dependency.AppModule;
import com.example.victor.latrans.dependency.DaggerAppComponent;

import timber.log.Timber;

public class App extends Application {


	AppComponent mAppComponent;

	@Override
	public void onCreate() {
		super.onCreate();
		mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this))
				.build();

		Timber.plant(new Timber.DebugTree(){
			@Override
			protected String createStackElementTag(StackTraceElement element) {
				return super.createStackElementTag(element) + ":" + element.getLineNumber();
			}
		});
	}

	public AppComponent getAppComponent() {
		return mAppComponent;
	}


}
