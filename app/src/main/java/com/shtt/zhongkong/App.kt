package com.shtt.zhongkong

import android.app.Application
import com.shtt.zhongkong.net.NetWorkManager

class App  : Application(){

    override fun onCreate() {
        super.onCreate()
        NetWorkManager.getInstance().init(this)
    }

}