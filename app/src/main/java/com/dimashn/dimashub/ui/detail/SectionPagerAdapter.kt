package com.dimashn.dimashub.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dimashn.dimashub.ui.detail.followers.FollowersFragment
import com.dimashn.dimashub.ui.detail.following.FollowingFragment

class SectionPagerAdapter(activity: AppCompatActivity, private val fragmentBundle: Bundle) :
    FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FollowersFragment()
            1 -> FollowingFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }.apply {
            arguments = fragmentBundle
        }
    }

    override fun getItemCount(): Int = 2
}
