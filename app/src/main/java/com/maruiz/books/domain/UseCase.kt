package com.maruiz.books.domain

import arrow.fx.IO
import arrow.fx.extensions.fx
import kotlin.coroutines.CoroutineContext

abstract class UseCase<out Type, in Params> where Type : Any {

    abstract fun run(params: Params): IO<Type>

    operator fun invoke(params: Params, clientContext: CoroutineContext): IO<Type> =
        IO.fx {
            val value = !run(params)
            continueOn(clientContext)
            value
        }

    fun cancel() {
        //TODO CANCEL THE REQUEST
    }
}