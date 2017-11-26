package com.example.victor.latrans.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import com.example.victor.latrans.view.ui.profile.fragment.ProfileFragment
import com.example.victor.latrans.view.ui.profile.fragment.ProfileTripFragment
import com.example.victor.latrans.view.ui.profile.fragment.ProfileOrderFragment


class PagerAdapter(fm: FragmentManager, private var mNumOfTabs: Int) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return ProfileTripFragment()
            1 -> return ProfileOrderFragment()
            2 -> return ProfileFragment()

            else -> return null
        }
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }
}
