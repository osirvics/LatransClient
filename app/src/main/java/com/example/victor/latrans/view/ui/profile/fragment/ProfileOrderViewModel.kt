package com.example.victor.latrans.view.ui.profile.fragment

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.victor.latrans.dependency.AppComponent
import com.example.victor.latrans.google.Resource
import com.example.victor.latrans.repocitory.OrderRepository
import com.example.victor.latrans.repocitory.local.db.entity.Request
import com.example.victor.latrans.util.SharedPrefsHelper
import javax.inject.Inject




class ProfileOrderViewModel : ViewModel(), AppComponent.Injectable {

    @Inject
    lateinit var mOrderRepository: OrderRepository
    @Inject
    lateinit var mSharedPrefsHelper : SharedPrefsHelper

    private var mLiveData: LiveData<Resource<List<Request>>>? = null

    val orders: LiveData<Resource<List<Request>>>
        get() {
            if (mLiveData == null) {
                mLiveData = MutableLiveData()
                mLiveData = mOrderRepository.getRequestForUser(mSharedPrefsHelper.userId)
            }
            return mLiveData as MutableLiveData<Resource<List<Request>>>
        }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }
}

