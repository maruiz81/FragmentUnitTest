package com.maruiz.arrowexample.data.extensions

import arrow.fx.IO
import arrow.fx.extensions.io.async.async
import arrow.fx.extensions.io.monad.map
import arrow.fx.fix
import arrow.integrations.retrofit.adapter.CallK

fun <R> CallK<R>.makeCall(default: R): IO<R> =
    this.async(IO.async())
        .map { it.body() ?: default }
        .fix()