package com.maruiz.books.presentation.app

import android.app.Application
import com.maruiz.books.presentation.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.core.context.startKoin

class BookApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BookApp)
            androidFileProperties()
            modules(appModule)
        }
    }
}