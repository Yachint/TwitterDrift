package com.yachint.twitterdrift.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class NavigationService: Service() {

    var mBinder: IBinder = Binder()
    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }
}