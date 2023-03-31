package com.walker.dripstone.home.headline

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.walker.dripstone.home.Channel

class HeadlineFragmentAdapter(
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(
    fm, lifecycle
) {

    private var channels: ObservableList<Channel> = ObservableArrayList()

    override fun getItemCount() = channels.size

    override fun createFragment(pos: Int): Fragment {
        return HomeChannelModel.createFragment(channels[pos].channelId)
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