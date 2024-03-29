package com.walker.dripstone.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.accessibility.AccessibilityEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.walker.common.router.ICollectRouter
import com.walker.common.router.IDemoRouter
import com.walker.common.share.ShareOption
import com.walker.common.share.SharePlatform
import com.walker.common.util.DrawableHelper
import com.walker.core.log.LogHelper
import com.walker.core.router.RouterLoader
import com.walker.core.util.SteepStatusBarUtils
import com.walker.core.util.ToastUtils
import com.walker.dripstone.NetworkState
import com.walker.dripstone.R
import com.walker.dripstone.databinding.ActivityMainBinding
import com.walker.dripstone.fragment.CollectFragment
import com.walker.dripstone.fragment.DemoFragment
import com.walker.dripstone.home.headline.HomeFragment
import com.walker.dripstone.links.LinkHelper
import com.walker.dripstone.setting.AccountFragment
import com.walker.dripstone.share.ShareActionMgr
import q.rorbin.badgeview.QBadgeView

class MainActivity : AppCompatActivity() {
    private lateinit var viewDataBinding: ActivityMainBinding
    private var homeFragment = HomeFragment()
    private var demoFragment: Fragment = getDemoFragment()
    private var collectFragment: Fragment = getCollectFragment()
    private var accountFragment = AccountFragment()
    private var fromFragment: Fragment = homeFragment
    private var backPressTime = 0L

    private val networkState: NetworkState by lazy {
        NetworkState(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initSteepStatusBar()
        initToolbar()
        // Disable shift method require for to prevent shifting icon . When you select any icon then remain all icon shift
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            disableShiftMode(viewDataBinding.bottomView)
        }

        viewDataBinding.bottomView.setOnNavigationItemSelectedListener { item ->
            var fragCategory: Fragment? = null
            // init corresponding fragment
            when (item.itemId) {
                R.id.menu_home -> fragCategory = homeFragment
                R.id.menu_demo -> fragCategory = demoFragment
                R.id.menu_collect -> fragCategory = collectFragment
                R.id.menu_account -> fragCategory = accountFragment
            }
            //Set bottom menu selected item text in toolbar
            val actionBar = supportActionBar
            actionBar?.title = item.title
            if (item.itemId == R.id.menu_account || item.itemId == R.id.menu_collect) {
                actionBar?.hide()
                viewDataBinding.viewSpace.visibility = View.GONE
            } else {
                actionBar?.show()
                viewDataBinding.viewSpace.visibility = View.VISIBLE
            }
            switchFragment(fromFragment, fragCategory)
            fromFragment = fragCategory!!
            true
        }
        viewDataBinding.bottomView.labelVisibilityMode =
            LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, homeFragment, homeFragment.javaClass.simpleName)
        transaction.commit()
        showBadgeView(3, 5)
        networkState.observe(this, Observer<Boolean> {
            if (!it) {
                ToastUtils.show(getString(R.string.tip_net_connect_failed))
            }
        })

        LinkHelper.getInstance().transactLink(this)
        Log.i("MainActivity", "onCreate()");
        interceptWindowCallback()
    }

    private fun initSteepStatusBar() {
        SteepStatusBarUtils.setTranslucentTheme(this)
    }

    private fun initToolbar() {
        //Set Toolbar
        setSupportActionBar(viewDataBinding.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(false)
            title = getString(R.string.menu_home)
        }
    }

    private fun switchFragment(from: Fragment?, to: Fragment?) {
        if (from !== to) {
            val manger = supportFragmentManager
            val transaction = manger.beginTransaction()
            if (!to!!.isAdded) {
                if (from != null) {
                    transaction.hide(from)
                }
                if (to != null) {
                    transaction.add(R.id.container, to, to.javaClass.name).commit()
                }

            } else {
                if (from != null) {
                    transaction.hide(from)
                }
                if (to != null) {
                    transaction.show(to).commit()
                }

            }
        }
    }

    private fun getDemoFragment(): Fragment {
        var fragment: Fragment?
        val summaryProvider = RouterLoader.load(IDemoRouter::class.java)
        fragment = summaryProvider?.getSummaryFragment()
        fragment ?: let { fragment = DemoFragment() }
        return fragment!!
    }

    private fun getCollectFragment(): Fragment {
        var fragment: Fragment?
        val summaryProvider = RouterLoader.load(ICollectRouter::class.java)
        fragment = summaryProvider?.getHomeFragment()
        fragment ?: let { fragment = CollectFragment() }
        return fragment!!
    }

    @SuppressLint("RestrictedApi")
    private fun disableShiftMode(view: BottomNavigationView) {
        val menuView = view.getChildAt(0) as BottomNavigationMenuView
        try {
            val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
            shiftingMode.isAccessible = true
            shiftingMode.setBoolean(menuView, false)
            shiftingMode.isAccessible = false
            for (i in 0 until menuView.childCount) {
                val item = menuView.getChildAt(i) as BottomNavigationItemView
                // item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.itemData.isChecked)
            }
        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalAccessException) {
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.action_share -> {
                execShare()
            }
            R.id.action_about -> {
            }
        }// case blocks for other MenuItems (if any)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    /**
     * BottomNavigationView显示角标
     *
     * @param viewIndex  tab索引
     * @param showNumber 显示的数字，小于等于0是将不显示
     */
    private fun showBadgeView(viewIndex: Int, showNumber: Int) {
        // 具体child的查找和view的嵌套结构请在源码中查看
        // 从bottomNavigationView中获得BottomNavigationMenuView
        val menuView = viewDataBinding.bottomView.getChildAt(0) as BottomNavigationMenuView
        // 从BottomNavigationMenuView中获得childview, BottomNavigationItemView
        if (viewIndex < menuView.childCount) {
            // 获得viewIndex对应子tab
            val view = menuView.getChildAt(viewIndex)
            // 从子tab中获得其中显示图片的ImageView
            val icon = view.findViewById<View>(com.google.android.material.R.id.icon)
            // 获得图标的宽度
            val iconWidth = icon.width
            // 获得tab的宽度/2
            val tabWidth = view.width / 2
            // 计算badge要距离右边的距离
            val spaceWidth = tabWidth - iconWidth

            // 显示badegeview
            QBadgeView(this).bindTarget(view)
                .setGravityOffset((spaceWidth + 50).toFloat(), 13f, false).badgeNumber = showNumber
        }
    }

    override fun onBackPressed() {
        val now = System.currentTimeMillis()
        if (now - backPressTime > 2000) {
            ToastUtils.showCenter(
                String.format(
                    getString(R.string.tip_press_again_to_exit),
                    getString(R.string.app_name)
                )
            )
            backPressTime = now
        } else {
            super.onBackPressed()
            LogHelper.get().close()
        }
    }

    private fun execShare() {
        val option = ShareOption<Bitmap, Bitmap>().apply {
            image = DrawableHelper.createBitmapFromView(viewDataBinding.productClMainView)
            thumb = DrawableHelper.createBitmapFromView(viewDataBinding.productClMainView, 0.3f)
            platform = SharePlatform.MEDIA_WEIXIN
        }
        ShareActionMgr.get().shareImage(this, option, null)
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
}
