package com.example.victor.latrans.view.ui.post;

import android.os.Bundle;

import com.example.victor.latrans.BaseActivity;
import com.example.victor.latrans.R;

public class PostActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_post);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_post;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_post;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}
