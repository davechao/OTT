package com.isuncloud.ott.ui.main

import android.app.Activity
import android.os.Bundle
import com.isuncloud.isuntvmall.ui.base.BaseActivity
import com.isuncloud.ott.R
import com.isuncloud.ott.ui.MainActivityViewModel

class MainActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

//    override fun getLayoutId(): Int {
//        return R.layout.activity_main
//    }
//
//    override fun getViewModelClass(): Class<MainActivityViewModel>? {
//        return MainActivityViewModel::class.java
//    }

}