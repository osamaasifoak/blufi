package com.example.freepowersocket.di

import android.content.Context
import com.example.freepowersocket.data.BleController
import com.example.freepowersocket.domain.BleControllerInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideBleController(@ApplicationContext context: Context): BleControllerInterface {
        return BleController(context)
    }
}