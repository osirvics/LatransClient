package com.example.victor.latrans.view.ui.order;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.example.victor.latrans.BaseActivity;
import com.example.victor.latrans.R;
import com.example.victor.latrans.dependency.AppFactory;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.db.entity.Request;
import com.example.victor.latrans.util.DividerItemDecoration;
import com.example.victor.latrans.util.OnItemClick;
import com.example.victor.latrans.util.SharedPrefsHelper;
import com.example.victor.latrans.view.adapter.OrderAdapter;
import com.example.victor.latrans.view.ui.App;
import com.example.victor.latrans.view.ui.message.MessageActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderActivity extends BaseActivity implements OnItemClick {

    public static Intent newIntent(Context context) {
        Intent mIntent = new Intent(context, OrderActivity.class);
        return mIntent;
    }


    private OrderViewModel mOrderViewModel;
    private LottieAnimationView animationView;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerOrderView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private OrderAdapter mOrderAdapter;
    @Inject
    SharedPrefsHelper mSharedPrefsHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        ((App) getApplication()).getAppComponent().inject(this);
        App app = (App) this.getApplication();
        initLoadingAnim();
        setUpView();
        initViewModel(app);
    }

    private void setUpView(){
        mToolbar.setTitle(getString(R.string.order_activity_name));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerOrderView.setLayoutManager(mLayoutManager);
        mRecyclerOrderView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mOrderAdapter  = new OrderAdapter(new ArrayList<>(), this, mSharedPrefsHelper.getUserId(), this);
        mRecyclerOrderView.setAdapter(mOrderAdapter);

    }

    void initViewModel(App app){
        startAnim();
        mOrderViewModel = ViewModelProviders.of(this, new AppFactory(app)).get(OrderViewModel.class);
        subscribeToOrderStreams();
    }
    private void subscribeToOrderStreams(){
        mOrderViewModel.getOrders().observe(this, this::handleOrderResponse);
    }

    private void handleOrderResponse(Resource<List<Request>> resource){
        stopAnim();
        if(resource.data != null){
            mOrderAdapter.addOrder(resource.data);
        }
        else {
           // Toast.makeText(this, "No orders found this time", Toast.LENGTH_SHORT).show();
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
        return R.layout.activity_order;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_order;
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
}
