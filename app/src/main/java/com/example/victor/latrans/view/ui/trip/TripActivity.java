package com.example.victor.latrans.view.ui.trip;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.example.victor.latrans.BaseActivity;
import com.example.victor.latrans.R;
import com.example.victor.latrans.dependency.AppFactory;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.db.entity.Trip;
import com.example.victor.latrans.util.ActionDial;
import com.example.victor.latrans.util.DividerItemDecoration;
import com.example.victor.latrans.util.OnItemClick;
import com.example.victor.latrans.util.SharedPrefsHelper;
import com.example.victor.latrans.util.Util;
import com.example.victor.latrans.view.adapter.TripAdapter;
import com.example.victor.latrans.view.ui.App;
import com.example.victor.latrans.view.ui.message.MessageActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import timber.log.Timber;


@RuntimePermissions
public class TripActivity extends BaseActivity  implements  OnItemClick, ActionDial {

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, TripActivity.class);
        return i;
    }

    private TripViewModel mTripViewModel;
    private LottieAnimationView animationView;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerTripView;
    private TripAdapter mTripAdapter;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @Inject
    SharedPrefsHelper mSharedPrefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Timber.e("Refreshed token: " + refreshedToken);
        ButterKnife.bind(this);
        initLoadingAnim();
        setUpView();
        App app = (App) this.getApplication();
        initViewModel(app);

    }

    private void setUpView(){
        mToolbar.setTitle(getString(R.string.app_name));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerTripView.setLayoutManager(mLayoutManager);
        //mRecyclerTripView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerTripView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mTripAdapter  = new TripAdapter(new ArrayList<>(), this,this,this, mSharedPrefsHelper.getUserId());
        mRecyclerTripView.setAdapter(mTripAdapter);
        startAnim();
    }

    private void initViewModel(App application){
        mTripViewModel = ViewModelProviders.of(this, new AppFactory(application)).get(TripViewModel.class);
        subscribeToTripsStreams(mTripViewModel);
    }

    private void subscribeToTripsStreams(TripViewModel viewModel){
        viewModel.getResponse().observe(this, this::handleResponse);
    }

    private void handleResponse(Resource<List<Trip>> listResource){
//        switch (listResource.status){
//            case SUCCESS:
                stopAnim();
                if (listResource.data != null){
                 mTripAdapter.addTrips(listResource.data);
                }
                else  Timber.e("No data found");
                    //Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
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


    @Override
    public void onClick(long conversationId, long recipientId) {
        Intent intent = MessageActivity.newMessageIntent(this, recipientId);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }


    @Override
    public void dial(@NotNull String number, @NotNull String name) {
       // Toast.makeText(this, "Calling " + name, Toast.LENGTH_SHORT).show();
        TripActivityPermissionsDispatcher.MakePhoneCallWithCheck(this, number, name);
    }
    @NeedsPermission(Manifest.permission.CALL_PHONE)
    public void MakePhoneCall(@NotNull String number, @NotNull String name){
        Util.showCallDialogue(this, name, number);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        TripActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

}
