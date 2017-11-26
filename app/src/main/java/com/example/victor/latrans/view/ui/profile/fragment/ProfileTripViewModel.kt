package com.example.victor.latrans.view.ui.profile.fragment

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.victor.latrans.dependency.AppComponent
import com.example.victor.latrans.google.Resource
import com.example.victor.latrans.repocitory.SignupRepository
import com.example.victor.latrans.repocitory.local.db.entity.Trip
import com.example.victor.latrans.util.SharedPrefsHelper
import javax.inject.Inject

class ProfileTripViewModel : ViewModel(), AppComponent.Injectable {
//    @get:Bindable
//    var title by bindable<CharSequence>("")
//        private set
//
//    @Bindable
//    var title : CharSequence = ""
//        private set(value) {
//            if (field != value) {
//                field = value
//                notifyPropertyChanged(BR.title)
//            }
//        }
    var userId: Long? = null
    @Inject
    lateinit var mSignupRepository: SignupRepository
    @Inject
    lateinit var mSharedPrefsHelper: SharedPrefsHelper

    lateinit var mLiveData: LiveData<Resource<List<Trip>>>

    val response: LiveData<Resource<List<Trip>>>
        get() {
            mLiveData = MutableLiveData()
            queryResponse()
            return mLiveData as MutableLiveData<Resource<List<Trip>>>

        }


    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    private fun queryResponse() {
        mLiveData = mSignupRepository.getTripsForUser(mSharedPrefsHelper.userId)
    }
}
