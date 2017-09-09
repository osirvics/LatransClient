package com.example.victor.latrans.dependency;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.victor.latrans.view.ui.App;


/**
 * @author rebeccafranks
 * @since 2017/05/10.
 */

public class AppFactory extends ViewModelProvider.NewInstanceFactory {

    private App application;

    public AppFactory(App application) {
        this.application = application;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        T t = super.create(modelClass);
        if (t instanceof AppComponent.Injectable) {
            ((AppComponent.Injectable) t).inject(application.getAppComponent());
        }
        return t;
    }
}
