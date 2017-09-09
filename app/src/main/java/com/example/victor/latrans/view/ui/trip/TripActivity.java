package com.example.victor.latrans.view.ui.trip;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.victor.latrans.BaseActivity;
import com.example.victor.latrans.R;
import com.example.victor.latrans.dependency.AppFactory;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.db.entity.Trip;
import com.example.victor.latrans.util.DividerItemDecoration;
import com.example.victor.latrans.view.adapter.TripAdapter;
import com.example.victor.latrans.view.ui.App;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class TripActivity extends BaseActivity  implements LifecycleRegistryOwner {

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, TripActivity.class);
        return i;
    }

    private TripViewModel mTripViewModel;
    LottieAnimationView animationView;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerTripView;
    private TripAdapter mTripAdapter;
    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Timber.e("Refreshed token: " + refreshedToken);
        FirebaseMessaging.getInstance().subscribeToTopic("user1");
       // setContentView(R.layout.activity_trip);
        ButterKnife.bind(this);
        initLoadingAnim();
        setUpView();
        App app = (App) this.getApplication();
        initViewModel(app);

    }

    private void setUpView(){
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerTripView.setLayoutManager(mLayoutManager);
        //mRecyclerTripView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerTripView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mTripAdapter  = new TripAdapter(new ArrayList<>(), this);
        mRecyclerTripView.setAdapter(mTripAdapter);
        startAnim();
    }

    private void initViewModel(App application){
        mTripViewModel = ViewModelProviders.of(this, new AppFactory(application)).get(TripViewModel.class);
        subscribstoTripsStreams(mTripViewModel);
    }

    private void subscribstoTripsStreams(TripViewModel viewModel){
        viewModel.getResponse().observe(this, this::handleResponse);
    }

    private void handleResponse(Resource<List<Trip>> listResource){
        switch (listResource.status){
            case SUCCESS:
                stopAnim();
                if (listResource != null && listResource.data != null){
                    Timber.e("Size of trips: "+ listResource.data.size());
                 mTripAdapter.addTrips(listResource.data);
                }
                else   Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE:
                stopAnim();
                Toast.makeText(this, listResource.message, Toast.LENGTH_SHORT).show();
                break;
        }

    }

    void initLoadingAnim(){
        animationView = (LottieAnimationView) findViewById(R.id.animation_view);
        animationView.setAnimation("preloader.json");
        animationView.loop(true);
        animationView.setVisibility(View.GONE);
        animationView.setScale(0.3f);

    }

    void startAnim(){
        animationView.setVisibility(View.VISIBLE);
        animationView.playAnimation();
    }

    void stopAnim(){
        animationView.cancelAnimation();
        animationView.setVisibility(View.GONE);
    }



    @Override
    public int getContentViewId() {
        return R.layout.activity_trip;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_home;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

}
