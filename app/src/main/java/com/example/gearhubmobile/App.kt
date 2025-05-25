package com.example.gearhubmobile

import android.app.Application
import com.example.gearhubmobile.data.apirest.RetrofitInstance
import com.example.gearhubmobile.utils.SessionManager
/**
 * @author Rodrigo
 * @date 25 mayo, 2025
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val sessionManager = SessionManager(applicationContext)
        RetrofitInstance.init(sessionManager)
    }
}