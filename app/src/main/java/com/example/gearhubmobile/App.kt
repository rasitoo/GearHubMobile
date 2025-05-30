package com.example.gearhubmobile

import android.app.Application
import com.example.gearhubmobile.data.apirest.RetrofitInstance
import com.example.gearhubmobile.utils.SessionManager
import dagger.hilt.android.HiltAndroidApp

/**
 * @author Rodrigo
 * @date 25 mayo, 2025
 */
@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}