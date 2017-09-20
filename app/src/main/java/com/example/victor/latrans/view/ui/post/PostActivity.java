package com.example.victor.latrans.view.ui.post;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;

import com.example.victor.latrans.BaseActivity;
import com.example.victor.latrans.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostActivity extends BaseActivity {
    @BindView(R.id.trip_button)
    AppCompatButton mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        //setContentView(R.layout.activity_post);

    }
    @OnClick(R.id.trip_button)
    void openTripActivity(){
        Intent intent = AddTripActivity.newIntent(this);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
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
