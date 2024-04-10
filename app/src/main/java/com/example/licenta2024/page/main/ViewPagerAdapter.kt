package com.example.licenta2024.page.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.licenta2024.page.main.fragments.HomeFragment
import com.example.licenta2024.page.main.fragments.ProfileFragment
import com.example.licenta2024.page.main.fragments.ExploreFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3 // Number of fragments
    }

    override fun createFragment(position: Int): Fragment {
        // Return the fragment for each position
        return when (position) {
            0 -> HomeFragment()
            1 -> ExploreFragment()
            2 -> ProfileFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
