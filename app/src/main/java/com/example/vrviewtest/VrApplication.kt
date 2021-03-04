package com.example.vrviewtest

import android.app.Application
import android.content.Context

class VrApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        context=this
    }

    companion object {
        private var context : Context? = null

        @JvmStatic
        fun getContext(): Context? {
            return context
        }
    }
}
