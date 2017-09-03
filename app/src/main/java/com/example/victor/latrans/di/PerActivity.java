package com.example.victor.latrans.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by janisharali on 08/12/16.
 */

@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {
}

