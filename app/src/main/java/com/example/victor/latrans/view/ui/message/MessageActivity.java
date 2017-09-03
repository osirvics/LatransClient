package com.example.victor.latrans.view.ui.message;

import android.os.Bundle;

import com.example.victor.latrans.BaseActivity;
import com.example.victor.latrans.R;

public class MessageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_message);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_message;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.navigation_message;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}
