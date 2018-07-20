package com.isuncloud.ott.ui

import android.app.Application
import com.isuncloud.isuntvmall.ui.base.BaseAndroidViewModel
import com.isuncloud.isuntvmall.utils.SchedulerProvider
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
        app: Application, schedulerProvider: SchedulerProvider)
    : BaseAndroidViewModel(app, schedulerProvider) {

}