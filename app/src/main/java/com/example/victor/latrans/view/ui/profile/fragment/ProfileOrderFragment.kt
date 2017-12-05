package com.example.victor.latrans.view.ui.profile.fragment

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.example.victor.latrans.R
import com.example.victor.latrans.dependency.AppFactory
import com.example.victor.latrans.google.Resource
import com.example.victor.latrans.repocitory.local.db.entity.Request
import com.example.victor.latrans.util.DividerItemDecoration
import com.example.victor.latrans.util.OnItemClick
import com.example.victor.latrans.util.SharedPrefsHelper
import com.example.victor.latrans.view.adapter.OrderAdapter
import com.example.victor.latrans.view.ui.App
import kotlinx.android.synthetic.main.profile_fragment_main.*
import java.util.*
import javax.inject.Inject



class ProfileOrderFragment : Fragment(),OnItemClick {
    override fun onClick(conversationId: Long, recipientId: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var mOrderViewModel: ProfileOrderViewModel
    private lateinit var animationView: LottieAnimationView
    private lateinit var mOrderAdapter: OrderAdapter
    @Inject
    lateinit var mSharedPrefsHelper : SharedPrefsHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.profile_fragment_main, container, false)
        (activity.application as App).appComponent.inject(this)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val app = activity.applicationContext as App
        initLoadingAnim(view)
        setUpView()
        initViewModel(app)
    }

    private fun setUpView() {
        val mLayoutManager = LinearLayoutManager(activity)
        recycler_view.layoutManager = mLayoutManager
        recycler_view.addItemDecoration(DividerItemDecoration(activity, LinearLayoutManager.VERTICAL))
        mOrderAdapter = OrderAdapter(ArrayList(), activity, mSharedPrefsHelper.getUserId(), this)
        recycler_view.adapter = mOrderAdapter

    }

    internal fun initViewModel(app: App) {
        startAnim()
        mOrderViewModel = ViewModelProviders.of(activity, AppFactory(app)).get(ProfileOrderViewModel::class.java)
        subscribeToOrderStreams()
    }

    private fun subscribeToOrderStreams() {
        mOrderViewModel.orders.observe(activity as LifecycleOwner, Observer<Resource<List<Request>>> { this.handleOrderResponse(it) })
    }

    private fun handleOrderResponse(resource: Resource<List<Request>>?) {
        stopAnim()
        if (resource!!.data != null) {
            mOrderAdapter.addOrder(resource.data)
        } else {
            //Toast.makeText(activity, "No orders found this time", Toast.LENGTH_SHORT).show()
        }
    }

    internal fun initLoadingAnim(view : View) {
        animationView = view.findViewById<LottieAnimationView>(R.id.animation_view)
        animationView.setAnimation("preloader.json")
        animationView.loop(true)
        animationView.setVisibility(View.GONE)
        animationView.setScale(0.3f)

    }

    internal fun startAnim() {
        animationView.setVisibility(View.VISIBLE)
        animationView.playAnimation()
    }

    internal fun stopAnim() {
        animationView.cancelAnimation()
        animationView.setVisibility(View.GONE)
    }
}