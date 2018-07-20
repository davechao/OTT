package com.isuncloud.isuntvmall.ui.base

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.DaggerFragment
import javax.inject.Inject

open class BaseFragment<B: ViewDataBinding, V: BaseAndroidViewModel>: DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var binding: B
    lateinit var viewModel: V

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(getViewModelClass()!!)
        return binding.root
    }

    open fun getLayoutId(): Int {
        return -1
    }

    open fun getViewModelClass(): Class<V>? {
        return null
    }

}