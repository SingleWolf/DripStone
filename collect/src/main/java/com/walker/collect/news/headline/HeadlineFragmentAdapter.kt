package com.walker.collect.news.headline

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.walker.collect.news.newslist.NewsListFragment
import java.util.*

class HeadlineFragmentAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(
    fm,
    lifecycle
) {

    private var channels: ObservableList<Channel> = ObservableArrayList()
    private val fragmentHashMap = HashMap<String, Fragment>()

    override fun getItemCount() = channels.size

    override fun createFragment(pos: Int): Fragment {
        val key = "${channels[pos].channelId}_$${channels[pos].channelName}"
        var fragment: Fragment? = fragmentHashMap[key]
        fragment ?: let {
            fragment =
                NewsListFragment.newInstance(channels[pos].channelId, channels[pos].channelName)
            fragmentHashMap.put(key, fragment!!)
        }
        return fragment!!
    }

    fun setChannels(channels: ObservableList<Channel>) {
        this.channels = ObservableArrayList()
        this.channels.addAll(channels)
        notifyDataSetChanged()
    }

    fun getChannelName(position: Int): String? {
        return if (position >= channels.size) {
            ""
        } else channels[position].channelName
    }
}