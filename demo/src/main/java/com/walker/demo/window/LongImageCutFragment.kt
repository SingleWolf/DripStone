package com.walker.demo.window

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.webkit.WebView
import android.widget.ScrollView
import android.widget.Toast
import com.davemorrissey.labs.subscaleview.ImageSource
import com.walker.core.base.mvc.BaseFragment
import com.walker.demo.R
import kotlinx.android.synthetic.main.fragment_demo_long_image_cut.*
import kotlinx.android.synthetic.main.fragment_demo_prepage_bitmap.tvAction
import java.io.FileOutputStream


class LongImageCutFragment : BaseFragment() {

    companion object {
        const val KEY_ID = "key_demo_long_image_cut_fragment"
        const val TAG = "LongImageCutFragment"
        fun instance() = LongImageCutFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (Build.VERSION.SDK_INT > +Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw()
        }
    }

    override fun buildView(baseView: View?, savedInstanceState: Bundle?) {

        webView.loadUrl("http://www.w3school.com.cn/html/html5_intro.asp")

        tvAction.setOnClickListener {
            webViewShot(webView)
        }
    }

    override fun getLayoutId() = R.layout.fragment_demo_long_image_cut

    fun scrollershot(scrollView: ScrollView) {
        // 获取当前屏幕的视图
        val screenView = scrollView

        var totalHeight = 0
        for (i in 0 until scrollView.childCount) {
            totalHeight += scrollView.getChildAt(i).height
        }

        // 创建一个Bitmap对象，并设置大小为当前屏幕的宽高
        val bitmap =
            Bitmap.createBitmap(screenView.width, totalHeight, Bitmap.Config.ARGB_8888)

        // 创建一个Canvas对象，并将位图绘制到其中
        val canvas = Canvas(bitmap)
        screenView.draw(canvas)

        // 保存位图为图片文件
        val filePath = "${Environment.getExternalStorageDirectory()}/Walker/long_screenshot.png"
        try {
            val fos = FileOutputStream(filePath)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
            Toast.makeText(activity, "截屏成功，保存路径：$filePath", Toast.LENGTH_SHORT).show()

            tvContent.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(activity, "截屏失败", Toast.LENGTH_SHORT).show()
        } finally {
            ivShowImage.visibility = View.VISIBLE
            ivShowImage.setImage(ImageSource.bitmap(bitmap))
        }
    }


    private fun webViewShot(webView: WebView) {
        webView.measure(
            View.MeasureSpec.makeMeasureSpec(
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED
            ), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        webView.layout(0, 0, webView.measuredWidth, webView.measuredHeight)
        webView.isDrawingCacheEnabled = true
        webView.buildDrawingCache()

        val bitmap =
            Bitmap.createBitmap(
                webView.measuredWidth,
                webView.measuredHeight,
                Bitmap.Config.ARGB_8888
            )
        val canvas = Canvas(bitmap)
        webView.draw(canvas)

        // 保存位图为图片文件
        val filePath = "${Environment.getExternalStorageDirectory()}/Walker/long_web_screenshot.png"
        try {
            val fos = FileOutputStream(filePath)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.flush()
            fos.close()
            Toast.makeText(activity, "截屏成功，保存路径：$filePath", Toast.LENGTH_SHORT).show()

            webView.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(activity, "截屏失败", Toast.LENGTH_SHORT).show()
        } finally {
            ivShowImage.visibility = View.VISIBLE
            ivShowImage.setImage(ImageSource.uri(filePath))
        }
    }
}