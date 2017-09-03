package com.example.victor.latrans.view.ui.profile;

import android.os.Bundle;

import com.example.victor.latrans.BaseActivity;
import com.example.victor.latrans.R;

public class ProfileActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_profile);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_profile;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_profile;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}
