package com.example.victor.latrans.view.ui.profile.fragment

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.victor.latrans.R
import com.example.victor.latrans.dependency.AppFactory
import com.example.victor.latrans.google.AppExecutors
import com.example.victor.latrans.google.Resource
import com.example.victor.latrans.google.Status
import com.example.victor.latrans.repocitory.SignupRepositoryImpl
import com.example.victor.latrans.repocitory.local.db.AppDatabase
import com.example.victor.latrans.repocitory.local.db.entity.User
import com.example.victor.latrans.util.SharedPrefsHelper
import com.example.victor.latrans.view.ui.App
import com.example.victor.latrans.view.ui.login.LoginActivity
import com.example.victor.latrans.view.ui.profile.ProfileViewModel
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.content_profile.*
import javax.inject.Inject



class ProfileFragment : Fragment() {
    internal lateinit var mProfileViewModel: ProfileViewModel
    @Inject
    lateinit var mSharedPrefsHelper: SharedPrefsHelper
    @Inject
    lateinit var mAppDatabase: AppDatabase
    @Inject
    lateinit var mAppExecutors: AppExecutors





    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.content_profile, container, false)
        (activity.application as App).appComponent.inject(this)
        initView(rootView)
        val app = activity.application as App
        initViewModel(app)
        return rootView
    }


    private fun initViewModel(app: App) {
        mProfileViewModel = ViewModelProviders.of(activity, AppFactory(app)).get(ProfileViewModel::class.java)
        subscribeToDataStreams(mProfileViewModel)
    }

    internal fun subscribeToDataStreams(profileViewModel: ProfileViewModel) {
        profileViewModel.response.observe(activity as LifecycleOwner, Observer<Resource<User>> {
            if (it != null) {
                this.handleResponse(it)
            }
        })
    }

    private fun handleResponse(user: Resource<User>) {
        when (user.status) {
            Status.SUCCESS -> if (user.data != null) {
                val url = user.data.picture
                mobile_no.text = user.data.phone_no
                profile_first_name.text = user.data.first_name
                profile_last_name.text = user.data.last_name
               // Glide.with(this).load(url).centerCrop().placeholder(R.drawable.ic_person_grey_600_24dp)
                // .error(R.drawable.ic_person_grey_600_24dp).crossFade().into(profile_image)
                profile_email.text = user.data.email
            } else {
                openLoginActivity()
            }
            Status.MESSAGE -> openLoginActivity()
//            else -> {
//                openLoginActivity()
//            }
        }
    }

    fun initView(view: View) {
       val  logout = view.findViewById<Button>(R.id.logout_button) as Button
       logout.setOnClickListener { _ -> logout() }
    }



    fun logout() {

        val id = mSharedPrefsHelper.userId
        val myTopic = SignupRepositoryImpl.getUser() + id.toString()
        FirebaseMessaging.getInstance().unsubscribeFromTopic(myTopic)
        FirebaseMessaging.getInstance().unsubscribeFromTopic(SignupRepositoryImpl.TRIP_TOPIC)
        FirebaseMessaging.getInstance().unsubscribeFromTopic(SignupRepositoryImpl.ORDER_TOPIC)
        mAppExecutors.diskIO().execute {
            deleteData()
            mAppExecutors.mainThread().execute { this.openLoginActivity() }
        }

    }

    fun deleteData() {
        mAppDatabase.tripDao().deleteAll()
        mAppDatabase.messageDao().deleteAll()
        mAppDatabase.userDao().deleteAll()
        mAppDatabase.dialogueDao().deleteAll()
        mAppDatabase.orderDao().deleteAll()
        mAppDatabase.conversationDao().deleteAll()
        mSharedPrefsHelper.deleteSavedData(SharedPrefsHelper.PREF_KEY_CURRENT_USER_ID)
        mSharedPrefsHelper.deleteSavedData(SharedPrefsHelper.PREF_KEY_USER_PROFILE_URL)
    }

    fun openLoginActivity() {
        val intent = LoginActivity.newIntent(activity)
        startActivity(intent)
        activity.overridePendingTransition(R.anim.enter, R.anim.exit)
    }
}


