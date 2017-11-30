package com.example.victor.latrans.view.ui.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.victor.latrans.BaseActivity
import com.example.victor.latrans.R
import com.example.victor.latrans.util.SharedPrefsHelper
import com.example.victor.latrans.view.adapter.PagerAdapter
import com.example.victor.latrans.view.ui.App
import com.example.victor.latrans.view.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_profile.*
import timber.log.Timber
import javax.inject.Inject

class ProfileActivity : BaseActivity(){
@Inject
lateinit var mSharedPrefsHelper : SharedPrefsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_profile);
       (this.application as App).appComponent.inject(this)

        initView()
    }

    fun initView(): Unit{
        if(mSharedPrefsHelper.userId == ((-1).toLong())){
            val intent = LoginActivity.newIntent(this)
            startActivity(intent)
        }
        else{
            initTabedViewpager()
            loadImage()
        }

    }

    fun openEditActivity() {
        val intent = EditProfileActivity.newINtent(this)
        startActivity(intent)
        overridePendingTransition(R.anim.enter, R.anim.exit)
    }
    private fun loadImage() : Unit{
        Glide.with(this).load(mSharedPrefsHelper.userProfileUrl).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_person_grey_600_24dp)
                .error(R.drawable.ic_person_grey_600_24dp).crossFade().into(profile_image)
        fab.setOnClickListener { _ -> openEditActivity() }

    }


    override fun getContentViewId(): Int {
        return R.layout.activity_profile
    }

    override fun getNavigationMenuItemId(): Int {
        return R.id.navigation_profile
    }

    override fun onBackPressed() {
        super.onBackPressed()

        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left)
    }

    companion object {

        fun newImtent(context: Context): Intent {
            return Intent(context, ProfileActivity::class.java)
        }
    }

    private fun initTabedViewpager() {
        tab_layout.addTab(tab_layout.newTab().setText(getString(R.string.profile_order_tab)))
        tab_layout.addTab(tab_layout.newTab().setText(getString(R.string.profile_request_tab)))
        tab_layout.addTab(tab_layout.newTab().setText(getString(R.string.profile_tab)))
        tab_layout.tabGravity = TabLayout.GRAVITY_CENTER
        val adapter = PagerAdapter(supportFragmentManager, tab_layout.tabCount)
        pager.adapter = adapter
        pager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
                when(tab.position){
                    0 -> fab.visibility = View.INVISIBLE
                    1 -> fab.visibility = View.INVISIBLE
                    2 -> fab.show()
                    else -> fab.visibility = View.INVISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                        if(tab.position == 2 ) fab.visibility = View.INVISIBLE
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        pager.offscreenPageLimit = 3


    }

    fun dialPhoneNumber(phoneNumber: String ) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + phoneNumber)
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}
