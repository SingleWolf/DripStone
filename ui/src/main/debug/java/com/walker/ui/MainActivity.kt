package com.walker.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.walker.ui.summary.SummaryFragment
import kotlinx.android.synthetic.main.activity_ui_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ui_main)
        initToolbar()
        initFragment()
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
}