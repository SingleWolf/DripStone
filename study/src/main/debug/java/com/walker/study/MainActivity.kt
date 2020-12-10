package com.walker.study

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.walker.study.R
import com.walker.study.summary.SummaryFragment
import kotlinx.android.synthetic.main.activity_study_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_study_main)
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