package com.walker.demo.shortcut

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import com.walker.common.media.image.ImageLoadHelper
import com.walker.demo.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


object ShortcutHelper {

    private const val ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT"
    const val TAG = "ShortcutHelper"

    fun add(context: Context, data: ShortcutBean<*>) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            addShortCut(context, data)
        } else {
            addShortcutBelowAndroidN(context, data)
        }
    }

    private fun addShortcutBelowAndroidN(context: Context, data: ShortcutBean<*>) {
        val addShortcutIntent = Intent(ACTION_ADD_SHORTCUT)
        // 不允许重复创建，不是根据快捷方式的名字判断重复的
        addShortcutIntent.putExtra("duplicate", false)
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, data.name)
        //图标
        addShortcutIntent.putExtra(
            Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
            Intent.ShortcutIconResource.fromContext(context, R.drawable.gaoguai)
        )

        // 设置关联程序
        val launcherIntent =  Intent(context, ShortcutActivity::class.java)
        launcherIntent.putExtra("data", data.extraData)
        launcherIntent.action = Intent.ACTION_VIEW
        addShortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent)
        // 发送广播
        context.sendBroadcast(addShortcutIntent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addShortCut(context: Context, data: ShortcutBean<*>) {
        val shortcutManager: android.content.pm.ShortcutManager =
            context.getSystemService(Context.SHORTCUT_SERVICE) as android.content.pm.ShortcutManager;
        if (shortcutManager.isRequestPinShortcutSupported) {
            var shortcutInfoIntent = Intent(context, ShortcutActivity::class.java);
            shortcutInfoIntent.action = Intent.ACTION_VIEW
            shortcutInfoIntent.putExtra("data", data.extraData)
            GlobalScope.launch(Dispatchers.Main) {
                val icon = getShortcutIcon(context, data.icon)
                var info = ShortcutInfo.Builder(context, data.id)
                    .setIcon(icon)
                    .setShortLabel(data.name)
                    .setIntent(shortcutInfoIntent)
                    .build()
                //当添加快捷方式的确认弹框弹出来时，将被回调
                val shortcutCallbackIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    Intent(context, ShortcutReceiver::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT
                );
                shortcutManager.requestPinShortcut(info, shortcutCallbackIntent.intentSender);
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun getShortcutIcon(context: Context, icon: Any?): Icon {
        return when (icon) {
            null -> {
                Icon.createWithResource(context, R.drawable.gaoguai)
            }
            is String -> {
                val bitmap = withContext(Dispatchers.IO) {
                    var data = ImageLoadHelper.downloadBitmap(context, icon, 400, 400)
                    data ?: let {
                        data = BitmapFactory.decodeResource(context.resources, R.drawable.luoli)
                    }
                    data
                }
                Icon.createWithBitmap(bitmap)
            }
            is Int -> {
                Icon.createWithResource(context, icon)
            }
            else -> {
                Icon.createWithResource(context, R.drawable.gaoguai)
            }
        }
    }
}