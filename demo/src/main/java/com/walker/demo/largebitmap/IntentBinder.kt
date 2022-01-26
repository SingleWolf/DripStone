package com.walker.demo.largebitmap

import android.graphics.Bitmap
import android.os.Binder

/**
 * Author  : walker
 * Date    : 2022/1/26  11:01 上午
 * Email   : feitianwumu@163.com
 * Summary : 同一进程下传递大图
 */
class IntentBinder(val imageBmp: Bitmap? = null) : Binder()