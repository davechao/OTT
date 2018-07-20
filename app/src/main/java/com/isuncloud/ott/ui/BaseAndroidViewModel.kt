package com.isuncloud.ott.ui

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

open class BaseAndroidViewModel @Inject constructor(
        application: Application)
    : AndroidViewModel(application), Observable {

    @Transient
    private var mCallbacks: PropertyChangeRegistry? = null

    @SuppressLint("StaticFieldLeak")
    private val applicationContext = getApplication<Application>().applicationContext // To avoid leaks, only use Application Context.

    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        synchronized(this) {
            if (mCallbacks == null) {
                mCallbacks = PropertyChangeRegistry()
            }
        }
        mCallbacks?.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        synchronized(this) {
            if (mCallbacks == null) {
                return
            }
        }
        mCallbacks?.remove(callback)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }

    /**
     * Notifies listeners that all properties of this instance have changed.
     */
    fun notifyChange() {
        synchronized(this) {
            if (mCallbacks == null) {
                return
            }
        }
        mCallbacks?.notifyCallbacks(this, 0, null)
    }

    /**
     * Notifies listeners that a specific property has changed. The getter for the property
     * that changes should be marked with [Bindable] to generate a field in
     * `BR` to be used as `fieldId`.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    fun notifyPropertyChanged(fieldId: Int) {
        synchronized(this) {
            if (mCallbacks == null) {
                return
            }
        }
        mCallbacks?.notifyCallbacks(this, fieldId, null)
    }

}