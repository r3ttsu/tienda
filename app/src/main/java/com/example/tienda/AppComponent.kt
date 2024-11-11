package com.example.tienda

import com.example.tienda.ui.dashboard.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface AppComponent {
    fun inject(activity: MainActivity)
}