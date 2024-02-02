package com.walker.demo.screenshot

import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.lzf.easyfloat.EasyFloat
import com.walker.common.media.image.ImageLoadHelper
import com.walker.common.media.photo.PhotoGetterHelper
import com.walker.common.view.floatview.FloatViewAdapter
import com.walker.demo.R

class ScreenShotFloatAdapter : FloatViewAdapter<String>() {

    private var datas: String = ""

    fun setData(data: String) {
        datas = data
    }

    override fun getData(): String {
        return datas
    }

    override fun getLayoutId() = R.layout.layout_demo_float_screen_shot

    override fun bindView(view: View, data: String) {
        view.findViewById<ImageView>(R.id.ivShowScreenShot)?.apply {
            ImageLoadHelper.loadFile(this, data, null)
            Log.i("ScreenshotObserver", "render imageView , filePath=$data")
        }
        view.findViewById<ImageView>(R.id.ivFloatClose).setOnClickListener {
            EasyFloat.dismiss("ScreenShotFloatView", false)
        }
    }

    override fun touchEvent(view: View, event: MotionEvent) {

    }

    override fun dragIng(view: View, event: MotionEvent) {
    }

    override fun dragEnd(view: View) {
    }

}