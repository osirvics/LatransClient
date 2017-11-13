package com.example.victor.latrans.view.ui.message;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
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
import com.example.victor.latrans.repocitory.local.db.entity.ConversationAndMessage;
import com.example.victor.latrans.util.DividerItemDecoration;
import com.example.victor.latrans.util.OnItemClick;
import com.example.victor.latrans.util.SharedPrefsHelper;
import com.example.victor.latrans.view.adapter.ConversationAdapter;
import com.example.victor.latrans.view.ui.App;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ConversationActivity extends BaseActivity implements LifecycleRegistryOwner, OnItemClick{

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    @BindView(R.id.recycler_view_conversation)
    RecyclerView mRecyclerViewConverstaion;
    ConversationViewModel mConversationViewModel;
    ConversationAdapter mConversationAdapter;
    LottieAnimationView animationView;
    @Inject
    SharedPrefsHelper mSharedPrefsHelper;


    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_conversation);
        ((App) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        initLoadingAnim();
        App app = (App) this.getApplication();
        setUpView();
        initViewModel(app);
    }

    private void initViewModel(App app){
        mConversationViewModel = ViewModelProviders.of(this, new AppFactory(app)).get(ConversationViewModel.class);
        subscribeToDataStreams(mConversationViewModel);
    }

    private void setUpView(){
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerViewConverstaion.setLayoutManager(mLayoutManager);
        mRecyclerViewConverstaion.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mConversationAdapter  = new ConversationAdapter(new ArrayList<>(), this, mSharedPrefsHelper.getUserId());
        mRecyclerViewConverstaion.setAdapter(mConversationAdapter);
        startAnim();
    }

    private void subscribeToDataStreams(ConversationViewModel conversationViewModel){
       LiveData<Resource<List<ConversationAndMessage>>> resource = conversationViewModel.getResponse();
       resource.observe(this, this::handleResponse);

    }

    private void handleResponse(Resource<List<ConversationAndMessage>> listResource){
//        switch (listResource.status){
//            case SUCCESS:
                stopAnim();
                if (listResource.data != null){
                    Timber.e("Size of conversation: "+ listResource.data.size());
                    mConversationAdapter.addConversation(listResource.data);

                }
                else   Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
//                break;
//            case MESSAGE:
//                stopAnim();
//                Toast.makeText(this, listResource.message, Toast.LENGTH_SHORT).show();
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
        return R.layout.activity_conversation;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_message;
    }

    @Override
    public void onClick(long conversationId, long recipientId) {
        Intent intent = MessageActivity.newIntent(this, conversationId, recipientId);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }
}
