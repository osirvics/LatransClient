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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.victor.latrans.R;
import com.example.victor.latrans.dependency.AppFactory;
import com.example.victor.latrans.google.Resource;
import com.example.victor.latrans.repocitory.local.db.entity.Conversation;
import com.example.victor.latrans.repocitory.local.db.entity.Message;
import com.example.victor.latrans.repocitory.local.db.entity.User;
import com.example.victor.latrans.util.SharedPrefsHelper;
import com.example.victor.latrans.view.adapter.MessageAdapter;
import com.example.victor.latrans.view.ui.App;
import com.example.victor.latrans.view.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MessageActivity extends AppCompatActivity implements LifecycleRegistryOwner {
    public static final String DIALOGUE_KEY = "key";
    public static final String RECIPIENT_ID = "recipient_id";

    public static Intent newIntent(Context packageContext, long conversationId, long senderId) {
        Intent i = new Intent(packageContext, MessageActivity.class);
        i.putExtra(DIALOGUE_KEY, conversationId);
        i.putExtra(RECIPIENT_ID, senderId);
        return i;
    }

    public static Intent newMessageIntent(Context packageContext, long recipientId) {
        Intent i = new Intent(packageContext, MessageActivity.class);
        i.putExtra(RECIPIENT_ID, recipientId);
        return i;
    }

    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    @BindView(R.id.reyclerview_message_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.edittext_chatbox)
    EditText mEdittextChatbox;
    @BindView(R.id.button_chatbox_send)
    ImageButton mButtonChatboxSend;
    MessageViewModel mMessageViewModel;
    MessageAdapter mMessageAdapter;
    LottieAnimationView animationView;
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar mToolbar;
    long mConversationId = -1; //new message initialisation
    long mRecipeintId = -1; // existing conversation initialisation
    @Inject
    SharedPrefsHelper mSharedPrefsHelper;
    LiveData<Resource<Conversation>> observer;

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ((App) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        App app = (App) this.getApplication();
        initViewModel(app);
        Intent intent = getIntent();
        processIntent(intent);
        initLoadingAnim();
        setUpView();
        getMessagesInConversation();


    }
    void processIntent(Intent intent){
        if(intent.hasExtra(RECIPIENT_ID) && !intent.hasExtra(DIALOGUE_KEY)){
            mRecipeintId = getIntent().getLongExtra(RECIPIENT_ID, 0);
            mMessageViewModel.recipientId = mRecipeintId;
            Timber.e("Called Called");
            // Required to check and get conversation if it exist between users
            observer = mMessageViewModel.getConversation();
            observer.observe(this, this::handleConversationResponse);
        }
        if (intent.hasExtra(DIALOGUE_KEY) && intent.hasExtra(RECIPIENT_ID)){
            mConversationId = intent.getLongExtra(DIALOGUE_KEY, 0);
            mRecipeintId = intent.getLongExtra(RECIPIENT_ID,0);
            mMessageViewModel.recipientId = mRecipeintId;
        }
    }



    private void initViewModel(App app) {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mMessageViewModel = ViewModelProviders.of(this, new AppFactory(app)).get(MessageViewModel.class);
    }

    private void handleConversationResponse(Resource<Conversation> conversationResource){
        Timber.e("Handling Response");
        if(conversationResource.data != null){
            mConversationId = conversationResource.data.getId();
           // getMessagesInConversation();
            mMessageViewModel.setDialogueId(mConversationId);

        }

    }

    private void getMessagesInConversation(){
        mMessageViewModel.setDialogueId(mConversationId);
        mMessageViewModel.recipientId = mRecipeintId;
        getUserStreams(mMessageViewModel);
        subscribeToDataStreams(mMessageViewModel);
    }

    private void getUserStreams(MessageViewModel messageViewModel){
        mMessageViewModel.recipientId = mRecipeintId;
        messageViewModel.getUserData().observe(this, this::handleUserResponse);
    }

    private void handleUserResponse(Resource<User> user){
        switch (user.status){
            case SUCCESS:
                if (user.data != null){
                    mMessageViewModel.senderId = user.data.getId();
                }
                else  {
                    openLoginActivity();
                }
                break;
            case MESSAGE:
                openLoginActivity();
                break;
        }

    }

    private void setUpView() {
        setListeners();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mMessageAdapter = new MessageAdapter(new ArrayList<>(), this, mSharedPrefsHelper.getUserId());
        mRecyclerView.setAdapter(mMessageAdapter);
        startAnim();
    }

    private void subscribeToDataStreams(MessageViewModel messageViewModel) {
        LiveData<Resource<List<Message>>> resource = messageViewModel.getResponse();
        resource.observe(this, this::handleResponse);
    }

    private void handleResponse(Resource<List<Message>> listResource) {
//        switch (listResource.status) {
//            case SUCCESS:
                stopAnim();
                if (listResource.data != null) {
                    mMessageAdapter.addMessages(listResource.data);
                  //  mRecyclerView.smoothScrollToPosition(mMessageAdapter.getItemCount()-1);
                    mMessageAdapter.notifyDataSetChanged();

                 if (mMessageAdapter.getItemCount() > 1) {
                      mRecyclerView.smoothScrollToPosition(mMessageAdapter.getItemCount()-1);
                    // recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
                  }
                  if(observer!=null) observer.removeObservers(this);

                } else {
                    //Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
                }
//                break;
//            case MESSAGE:
//                stopAnim();
//                Toast.makeText(this, listResource.message, Toast.LENGTH_SHORT).show();
//                break;
//        }
    }


    @OnClick(R.id.button_chatbox_send)
    void sendMessage(){
        addMessageToUI();
        mMessageViewModel.sendMessage().observe(this, this::handleMessageResponse);
        mMessageViewModel.mMessage = "";
        mEdittextChatbox.setText("");
    }

    private void addMessageToUI(){
        hideKeypad();
        Message message = mMessageViewModel.buildMessage();
        mMessageAdapter.addAMassage(message);
        mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, mMessageAdapter.getItemCount() - 1);

    }


    void openLoginActivity() {
        Intent intent = LoginActivity.newIntent(this);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private void handleMessageResponse(Resource<Message> messageResource){
        switch (messageResource.status){
            case SUCCESS:
                //Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE:
                Toast.makeText(this, "Error, failed to send message", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void setListeners(){
        mEdittextChatbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mMessageViewModel.mMessage = editable.toString();
            }
        });
    }

    void initLoadingAnim() {
        animationView = (LottieAnimationView) findViewById(R.id.animation_view);
        animationView.setAnimation("preloader.json");
        animationView.loop(true);
        animationView.setVisibility(View.GONE);
        animationView.setScale(0.3f);

    }

    void startAnim() {
        animationView.setVisibility(View.VISIBLE);
        animationView.playAnimation();
    }

    void stopAnim() {
        animationView.cancelAnimation();
        animationView.setVisibility(View.GONE);
    }

    private void hideKeypad(){
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
