package com.walker.dripstone.home.headline

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.walker.dripstone.home.Channel
import java.util.*

class HeadlineFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(
    fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    private var channels: ObservableList<Channel> = ObservableArrayList()
    private val fragmentHashMap = HashMap<String, Fragment>()

    override fun getItem(pos: Int): Fragment {
        val key = "${channels[pos].channelId}_$${channels[pos].channelName}"
        var fragment: Fragment? = fragmentHashMap[key]
        fragment ?: let {
            fragment = HomeChannelModel.createFragment(channels[pos].channelId)
            fragmentHashMap.put(key, fragment!!)
        }
        return fragment!!
    }

    override fun getCount() = if (channels != null) {
        channels.size
    } else {
        0
    }

    override fun getPageTitle(pos: Int): CharSequence? {
        return channels[pos].channelName
    }

    fun setChannels(channels: ObservableList<Channel>) {
        this.channels = ObservableArrayList()
        this.channels.addAll(channels)
        notifyDataSetChanged()
    }
}