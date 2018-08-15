package com.isuncloud.ott.ui.main

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.isuncloud.ott.R

class MainActivity: FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
    }

    override fun onBackPressed() {

    }

    private fun setupView() {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, MainFragment())
                .commit()
    }

}