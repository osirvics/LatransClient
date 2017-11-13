package com.example.victor.latrans.dependency;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.victor.latrans.google.AppExecutors;
import com.example.victor.latrans.repocitory.MessageRepository;
import com.example.victor.latrans.repocitory.MessageRepositoryImpl;
import com.example.victor.latrans.repocitory.OrderRepository;
import com.example.victor.latrans.repocitory.OrderRepositoryImpl;
import com.example.victor.latrans.repocitory.PostRepository;
import com.example.victor.latrans.repocitory.PostRepositoryImpl;
import com.example.victor.latrans.repocitory.SignupRepository;
import com.example.victor.latrans.repocitory.SignupRepositoryImpl;
import com.example.victor.latrans.repocitory.local.db.AppDatabase;
import com.example.victor.latrans.repocitory.remote.api.APIService;
import com.example.victor.latrans.repocitory.remote.api.ServiceGenerator;
import com.example.victor.latrans.util.SharedPrefsHelper;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;


@Module
public class AppModule {

    private final Application mApplication;



    public AppModule(Application app) {
        this.mApplication = app;

    }

    @Provides
    Context applicationContext() {
        return mApplication;
    }


    @Provides
    @Singleton
    APIService provideAPIService( @Named("username") String username, @Named("password")String password) {
        Timber.e("username: " + username + " password: "+ password);
        return ServiceGenerator.createService(APIService.class,username, password);
    }

//    @Provides
//    @Singleton
//    SignupRepository provideSignupRepository(SignupRepositoryImpl repository) {
//        return repository;
//    }

    @Provides
    @Singleton
    SignupRepository providesSignupRepository(AppDatabase appDatabase, SharedPrefsHelper sharedPrefsHelper, Context context, AppExecutors executors) {

        return new SignupRepositoryImpl(appDatabase, sharedPrefsHelper, context, executors );
    }

    @Provides
    @Singleton
    MessageRepository providesMessageRepository(AppDatabase appDatabase, SharedPrefsHelper sharedPrefsHelper, Context context, AppExecutors executors) {

        return new MessageRepositoryImpl(appDatabase, sharedPrefsHelper, context, executors );
    }

    @Provides
    @Singleton
    PostRepository providesPostRepository(AppDatabase appDatabase, SharedPrefsHelper sharedPrefsHelper, Context context, AppExecutors executors) {

        return new PostRepositoryImpl(appDatabase, sharedPrefsHelper, context, executors );
    }

    @Provides
    @Singleton
    OrderRepository providesOrderRepository(AppDatabase appDatabase, SharedPrefsHelper sharedPrefsHelper, Context context, AppExecutors executors) {

        return new OrderRepositoryImpl(appDatabase, sharedPrefsHelper, context, executors );
    }


    @Provides
    @Singleton
    AppExecutors providesAppExecutors(){
        return new AppExecutors();
    }


//    @Provides
//    ViewModel provideSignupViewModel(SignUpViewModel viewModel) {
//        return viewModel;
//    }
    @Provides
    @Singleton
    SharedPreferences providesSharedPrefsHelper(Context context){
            return  context.getApplicationContext().getSharedPreferences("latrans-prefs", Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    AppDatabase providesAppDatabase(Context context) {
        return  Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, AppDatabase.DATABASE_NAME)
                .addMigrations(AppDatabase.MIGRATION_1_2).build();
    }

//    @Provides
//    ViewModelProvider.Factory provideSignUpViewModelFactory(
//            ViewModelFactory factory
//    ) {
//        return factory;
//    }

}
