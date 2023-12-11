package com.android.chatsapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.ArrayList

class ViewPagerAdapter(
    private val items: ArrayList<Fragment>, private val fragmentTitles: List<String>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle)  {
    override fun getItemCount(): Int {
       return items.size
    }

    override fun createFragment(position: Int): Fragment {
       return items[position]
    }

    override fun getItemId(position: Int): Long {
        return items[position].hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return items.any { it.hashCode().toLong() == itemId }
    }

    fun getTitle(position: Int): String {
        return fragmentTitles[position]
    }
}
