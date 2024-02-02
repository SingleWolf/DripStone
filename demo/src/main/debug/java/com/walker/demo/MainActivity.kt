package com.walker.demo

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.*
import android.view.accessibility.AccessibilityEvent
import androidx.appcompat.app.AppCompatActivity
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.ExplainReasonCallback
import com.permissionx.guolindev.callback.RequestCallback
import com.walker.common.feedback.FeedbackHelper
import com.walker.core.log.LogHelper
import com.walker.demo.summary.SummaryFragment
import com.walker.demo.screenshot.ScreenshotObserver
import kotlinx.android.synthetic.main.activity_demo_main.*

class MainActivity : AppCompatActivity() {

    private var screenshotObserver: ScreenshotObserver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_main)
        initToolbar()

        PermissionX.init(this)
            .permissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .onExplainRequestReason(ExplainReasonCallback { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "应用运行需要以下权限",
                    "允许",
                    "拒绝"
                )
            })
            .request(
                RequestCallback { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        initFragment()
                    } else {
                        finish()
                    }
                }
            )

        interceptWindowCallback()
        initScreenShot()
    }

    private fun initScreenShot() {
        if (screenshotObserver == null) {
            screenshotObserver = ScreenshotObserver(
                Handler(Looper.getMainLooper()),
                this
            )
        }
        screenshotObserver?.apply {
            contentResolver?.registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                true,
                this
            )
        }
    }

    private fun initToolbar() {
        //Set Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(false)
            title = getString(R.string.app_name)
        }
    }

    private fun initFragment() {
        val fragment = SummaryFragment()
        val manger = supportFragmentManager
        val transaction = manger.beginTransaction()
        transaction.add(com.walker.common.R.id.container, fragment!!, fragment!!.javaClass.name)
            .commit()
    }

    fun interceptWindowCallback() {
        window.callback = object : Window.Callback {
            override fun dispatchKeyEvent(event: KeyEvent): Boolean {
                return super@MainActivity.dispatchKeyEvent(event)
            }

            @SuppressLint("RestrictedApi")
            override fun dispatchKeyShortcutEvent(event: KeyEvent): Boolean {
                return super@MainActivity.dispatchKeyShortcutEvent(event)
            }

            override fun dispatchTouchEvent(event: MotionEvent): Boolean {
                LogHelper.get().i("MainActivity", "dispatchTouchEvent : $event")
                if (event.action == MotionEvent.ACTION_UP) {
                    FeedbackHelper.dispatchTouchEventFromScreen(event)
                }
                return window.superDispatchTouchEvent(event)
            }

            override fun dispatchTrackballEvent(event: MotionEvent): Boolean {
                return super@MainActivity.dispatchTrackballEvent(event)
            }

            override fun dispatchGenericMotionEvent(event: MotionEvent): Boolean {
                return super@MainActivity.dispatchGenericMotionEvent(event)
            }

            override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent): Boolean {
                return super@MainActivity.dispatchPopulateAccessibilityEvent(event)
            }

            override fun onCreatePanelView(featureId: Int): View? {
                return super@MainActivity.onCreatePanelView(featureId)
            }

            override fun onCreatePanelMenu(featureId: Int, menu: Menu): Boolean {
                return super@MainActivity.onCreatePanelMenu(featureId, menu)
            }

            override fun onPreparePanel(featureId: Int, view: View?, menu: Menu): Boolean {
                return super@MainActivity.onPreparePanel(featureId, view, menu)
            }

            override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
                return super@MainActivity.onMenuOpened(featureId, menu)
            }

            override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean {
                return super@MainActivity.onMenuItemSelected(featureId, item)
            }

            override fun onWindowAttributesChanged(attrs: WindowManager.LayoutParams) {
                super@MainActivity.onWindowAttributesChanged(attrs)
            }

            override fun onContentChanged() {
                super@MainActivity.onContentChanged()
            }

            override fun onWindowFocusChanged(hasFocus: Boolean) {
                super@MainActivity.onWindowFocusChanged(hasFocus)
            }

            override fun onAttachedToWindow() {
                super@MainActivity.onAttachedToWindow()
            }

            override fun onDetachedFromWindow() {
                super@MainActivity.onDetachedFromWindow()
            }

            override fun onPanelClosed(featureId: Int, menu: Menu) {
                super@MainActivity.onPanelClosed(featureId, menu)
            }

            override fun onSearchRequested(): Boolean {
                return super@MainActivity.onSearchRequested()
            }

            override fun onSearchRequested(searchEvent: SearchEvent): Boolean {
                return super@MainActivity.onSearchRequested(searchEvent)
            }

            override fun onWindowStartingActionMode(callback: ActionMode.Callback): ActionMode? {
                return super@MainActivity.onWindowStartingActionMode(callback)
            }

            override fun onWindowStartingActionMode(
                callback: ActionMode.Callback,
                type: Int
            ): ActionMode? {
                return super@MainActivity.onWindowStartingActionMode(callback, type)
            }

            override fun onActionModeStarted(mode: ActionMode) {
                super@MainActivity.onActionModeStarted(mode)
            }

            override fun onActionModeFinished(mode: ActionMode) {
                super@MainActivity.onActionModeFinished(mode)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        screenshotObserver?.apply {
            contentResolver?.unregisterContentObserver(this)
        }
    }
}