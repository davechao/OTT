package com.isuncloud.isuntvmall.ui.base

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

open class BaseActivity<B: ViewDataBinding, V: BaseAndroidViewModel>: DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var binding: B
    lateinit var viewModel: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(getViewModelClass()!!)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    open fun getLayoutId(): Int {
        return -1
    }

    open fun getViewModelClass(): Class<V>? {
        return null
    }

}

