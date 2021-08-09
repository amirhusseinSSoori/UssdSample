package com.arad.forwardcall.data.di

import android.content.Context
import com.arad.ussdlibrary.USSDApi
import com.arad.ussdlibrary.USSDController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


import java.util.*


@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    fun provideUSSDApi(@ApplicationContext context: Context): USSDApi {
        return USSDController.getInstance(context)
    }
    @Provides
    fun provideHashMap(): HashMap<String, HashSet<String>> {
        val map = HashMap<String, HashSet<String>>()
        map["KEY_LOGIN"] = HashSet(listOf("espere", "waiting", "loading", "esperando"))
        map["KEY_ERROR"] = HashSet(listOf("problema", "problem", "error", "null"))
        return map
    }
}