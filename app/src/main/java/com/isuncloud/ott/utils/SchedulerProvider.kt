package com.isuncloud.ott.utils

import io.reactivex.*

class SchedulerProvider(val backgroundScheduler: Scheduler, val foregroundScheduler: Scheduler) {

    fun <T> getSchedulersForObservable(): (Observable<T>) -> Observable<T> {
        return { observable: Observable<T> ->
            observable.subscribeOn(backgroundScheduler)
                    .observeOn(foregroundScheduler)
        }
    }

    fun <T> getSchedulersForSingle(): (Single<T>) -> Single<T> {
        return { single: Single<T> ->
            single.subscribeOn(backgroundScheduler)
                    .observeOn(foregroundScheduler)
        }
    }

    fun getSchedulersForCompletable(): (Completable) -> Completable {
        return { completable: Completable ->
            completable.subscribeOn(backgroundScheduler)
                    .observeOn(foregroundScheduler)
        }
    }

    fun <T> getSchedulersForFlowable(): (Flowable<T>) -> Flowable<T> {
        return { flowable: Flowable<T> ->
            flowable.subscribeOn(backgroundScheduler)
                    .observeOn(foregroundScheduler)
        }
    }

    fun <T> getSchedulersForMaybe(): (Maybe<T>) -> Maybe<T> {
        return { maybe: Maybe<T> ->
            maybe.subscribeOn(backgroundScheduler)
                    .observeOn(foregroundScheduler)
        }
    }
}