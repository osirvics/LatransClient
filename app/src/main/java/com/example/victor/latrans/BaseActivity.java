package com.example.victor.latrans;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.victor.latrans.util.Util;
import com.example.victor.latrans.view.ui.message.ConversationActivity;
import com.example.victor.latrans.view.ui.order.OrderActivity;
import com.example.victor.latrans.view.ui.post.PostActivity;
import com.example.victor.latrans.view.ui.profile.ProfileActivity;
import com.example.victor.latrans.view.ui.trip.TripActivity;

public abstract class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    protected BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
       //navigationView.setItemIconTintList(null);
        Util.removeShiftMode( navigationView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }


    // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
    @Override
    public void onPause() {
        super.onPause();
       overridePendingTransition(0, 0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
       // navigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this,TripActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
            } else if (itemId == R.id.navigation_order) {
                startActivity(new Intent(this, OrderActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
            } else if (itemId == R.id.navigation_post) {
                startActivity(new Intent(this, PostActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
            else if(itemId == R.id.navigation_message){
                startActivity(new Intent(this, ConversationActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
            else if(itemId == R.id.navigation_profile){
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
            //finish();
       // }, 3);
        return true;
    }

    private void updateNavigationBarState(){
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        Menu menu = navigationView.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            boolean shouldBeChecked = item.getItemId() == itemId;
            if (shouldBeChecked) {
                item.setChecked(true);
                break;
            }
        }
    }

    public abstract int getContentViewId();

    public abstract int getNavigationMenuItemId();

}