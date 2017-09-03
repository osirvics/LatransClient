package com.example.victor.latrans.view.ui.trip;

import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.victor.latrans.BaseActivity;
import com.example.victor.latrans.R;

public class TripActivity extends BaseActivity  implements LifecycleRegistryOwner {

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, TripActivity.class);
        return i;
    }


    private final LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

   // private TextView mTextMessage;

//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
//                    return true;
//                case R.id.navigation_order:
//                    mTextMessage.setText(R.string.title_order);
//                    return true;
//                case R.id.navigation_post:
//                    mTextMessage.setText(R.string.title_post);
//                    return true;
//                case R.id.navigation_message:
//                    mTextMessage.setText(R.string.title_message);
//                    return true;
//                case R.id.navigation_profile:
//                    mTextMessage.setText(R.string.title_profile);
//                    return true;
//            }
//            return false;
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_trip);

       // mTextMessage = (TextView) findViewById(R.id.message);
       // BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
       // navigation.setItemIconTintList(null);
        //Util.removeShiftMode(navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
