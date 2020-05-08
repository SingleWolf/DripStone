package com.walker.dripstone.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.walker.common.arouter.RouteServiceManager
import com.walker.common.arouter.collect.ICollectProvider
import com.walker.common.arouter.demo.IDemoProvider
import com.walker.core.log.LogHelper
import com.walker.core.util.ToastUtils
import com.walker.dripstone.NetworkState
import com.walker.dripstone.R
import com.walker.dripstone.test.TraceMethodTest
import com.walker.dripstone.databinding.ActivityMainBinding
import com.walker.dripstone.fragment.AccountFragment
import com.walker.dripstone.fragment.CollectFragment
import com.walker.dripstone.fragment.DemoFragment
import com.walker.dripstone.home.headline.HomeFragment
import com.walker.dripstone.links.LinkHelper
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

        TraceMethodTest().tryTest()
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
        var fragment: Fragment
        val summaryProvider = RouteServiceManager.provide(
            IDemoProvider::class.java,
            IDemoProvider.DEMO_SUMMARY_SERVICE
        )
        if (summaryProvider == null) {
            LogHelper.get().e("getDemoFragment", "summaryProvider is null", true)
            fragment = DemoFragment()
        } else {
            fragment = summaryProvider.getSummaryFragment()
        }
        return fragment
    }

    private fun getCollectFragment(): Fragment {
        var fragment: Fragment
        val summaryProvider = RouteServiceManager.provide(
            ICollectProvider::class.java,
            ICollectProvider.COLLECT_SUMMARY_SERVICE
        )
        if (summaryProvider == null) {
            LogHelper.get().e("getCollectFragment", "summaryProvider is null", true)
            fragment = CollectFragment()
        } else {
            fragment = summaryProvider.getHomeFragment()
        }
        return fragment
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
        }
    }
}
