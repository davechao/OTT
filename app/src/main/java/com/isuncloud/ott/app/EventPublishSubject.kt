package com.isuncloud.isuntvmall.app

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

class EventPublishSubject<T> {

    private val subject: PublishRelay<T>

    init {
        this.subject = PublishRelay.create()
    }

    fun getSubject(vararg classes: Class<*>): Observable<T> {
        return subject.filter { event ->
            for (clazz in classes) {
                if (clazz.isInstance(event)) {
                    true
                }
            }
            false
        }.observeOn(AndroidSchedulers.mainThread())
    }

    fun <E : T> getSubject(clazz: Class<E>): Observable<E> {
        return subject.ofType(clazz)
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun <E : T> onNext(event: E) {
        subject.accept(event)
    }

    fun hasObservers(): Boolean {
        return subject.hasObservers()
    }
}
