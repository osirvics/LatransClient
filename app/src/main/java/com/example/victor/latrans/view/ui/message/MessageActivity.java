package com.example.victor.latrans.view.ui.message;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.victor.latrans.R;
import com.example.victor.latrans.dependency.AppFactory;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.db.entity.Message;
import com.example.victor.latrans.view.adapter.MessageAdapter;
import com.example.victor.latrans.view.ui.App;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MessageActivity extends AppCompatActivity implements LifecycleRegistryOwner{
    public static final String DIALOGUE_KEY = "key";
    public static Intent newIntent(Context packageContext, int conversaionId) {
        Intent i = new Intent(packageContext, MessageActivity.class);
        i.putExtra(DIALOGUE_KEY, conversaionId);
        return i;
    }

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    @BindView(R.id.reyclerview_message_list)
    RecyclerView mRecyclerView;
    MessageViewModel mMessageViewModel;
    MessageAdapter mMessageAdapter;
    LottieAnimationView animationView;



    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ((App) getApplication()).getAppComponent().inject(this);
        int id = getIntent().getIntExtra(DIALOGUE_KEY,0);
        ButterKnife.bind(this);
        initLoadingAnim();
        App app = (App) this.getApplication();
        setUpView();
        initViewModel(app, id);
    }

    private void initViewModel(App app, int id){
        mMessageViewModel = ViewModelProviders.of(this, new AppFactory(app)).get(MessageViewModel.class);
        mMessageViewModel.setDialogueId(id);
        subscribeToDataStreams(mMessageViewModel);
    }

    private void setUpView(){
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mLayoutManager.setStackFromEnd(true);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mMessageAdapter  = new MessageAdapter(new ArrayList<>(), this, 1);
        mRecyclerView.setAdapter(mMessageAdapter);
        startAnim();
    }

    private void subscribeToDataStreams(MessageViewModel messageViewModel){
        LiveData<Resource<List<Message>>> resource = messageViewModel.getResponse();
        resource.observe(this, this::handleResponse);

    }

    private void handleResponse(Resource<List<Message>> listResource){
        switch (listResource.status){
            case SUCCESS:
                stopAnim();
                if (listResource != null && listResource.data != null){
                    Timber.e("Size of conversation: "+ listResource.data.size());
                    mMessageAdapter.addMessages(listResource.data);
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }



}
