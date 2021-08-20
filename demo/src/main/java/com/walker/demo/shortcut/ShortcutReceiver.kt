package com.walker.demo.shortcut

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.walker.core.log.LogHelper

class ShortcutReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        LogHelper.get().i(ShortcutHelper.TAG, "MyShortcutReceiver onReceive()")
    }
}