package com.andresdiaz.finalapp.util

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject


object RxBus{
    private val publisher=PublishSubject.create<Any>()
    fun publish(event: Any){
        publisher.onNext(event)//todos los que esten subscritos o escuchando eventos resivira este valor
    }
    fun <T>listen(eventType: Class<T>):Observable<T> = publisher.ofType(eventType)
}