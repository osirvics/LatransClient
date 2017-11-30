package com.example.victor.latrans.view.ui.profile.fragment

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.example.victor.latrans.R
import com.example.victor.latrans.dependency.AppFactory
import com.example.victor.latrans.google.Resource
import com.example.victor.latrans.repocitory.local.db.entity.Trip
import com.example.victor.latrans.util.ActionDial
import com.example.victor.latrans.util.DividerItemDecoration
import com.example.victor.latrans.util.OnItemClick
import com.example.victor.latrans.util.SharedPrefsHelper
import com.example.victor.latrans.view.adapter.TripAdapter
import com.example.victor.latrans.view.ui.App
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class ProfileTripFragment : Fragment(), OnItemClick , ActionDial{
    override fun dial(number: String, name: String) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClick(conversationId: Long, recipientId: Long) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var mTripAdapter: TripAdapter
    private lateinit var mProfileTripViewModel : ProfileTripViewModel

    @Inject
    lateinit var mSharedPrefsHelper: SharedPrefsHelper
    private lateinit var  animationView : LottieAnimationView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.profile_fragment_main, container, false)
        (activity.application as App).appComponent.inject(this)
        val app = activity.application as App
        initLoadingAnim(view) 
        setUpView(view)
        initViewModel(app)
        return view
    }

    private fun setUpView(view : View) {
        val mLayoutManager = LinearLayoutManager(activity.applicationContext)
        val mRecyclerView =  view.findViewById<RecyclerView>(R.id.recycler_view)
        mRecyclerView.layoutManager = mLayoutManager
        //mRecyclerTripView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        mTripAdapter = TripAdapter(ArrayList(), activity,this, this,mSharedPrefsHelper.userId)
        mRecyclerView.adapter = mTripAdapter
        startAnim()
    }

    private fun initViewModel(application: App) {
        mProfileTripViewModel = ViewModelProviders.of(activity, AppFactory(application)).get(ProfileTripViewModel::class.java)
        subscribeToTripsStreams(mProfileTripViewModel)
    }

    private fun subscribeToTripsStreams(viewModel: ProfileTripViewModel) {
        viewModel.response.observe(activity as LifecycleOwner, Observer<Resource<List<Trip>>> { this.handleResponse(it) })
    }

    private fun handleResponse(listResource: Resource<List<Trip>>?) {
        //        switch (listResource.status){
        //            case SUCCESS:
        stopAnim()
        if (listResource?.data != null) {
            mTripAdapter.addTrips(listResource.data)
            for ((id) in listResource.data) {
                Timber.e("Trip id: " + id!!)
            }
        } else
            Toast.makeText(activity, "No data found", Toast.LENGTH_SHORT).show()
        //                break;
        //            case MESSAGE:
        ////                stopAnim();
        ////                if (listResource.data != null){
        ////                    mTripAdapter.addTrips(listResource.data);
        ////                }
        //              Toast.makeText(this, listResource.message, Toast.LENGTH_SHORT).show();
        //                break;
        //        }

    }

    internal fun initLoadingAnim(view : View) {
        animationView = view.findViewById<LottieAnimationView>(R.id.animation_view) as LottieAnimationView
        animationView.setAnimation("preloader.json")
        animationView.loop(true)
        animationView.setVisibility(View.GONE)
        animationView.setScale(0.3f)

    }

    fun startAnim() {
        animationView.setVisibility(View.VISIBLE)
        animationView.playAnimation()
    }

    internal fun stopAnim() {
        animationView.cancelAnimation()
        animationView.setVisibility(View.GONE)
    }

}