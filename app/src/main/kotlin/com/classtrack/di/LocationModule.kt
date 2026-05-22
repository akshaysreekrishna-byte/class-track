package com.classtrack.di

import android.content.Context
import com.classtrack.core.data.location.AospLocationProvider
import com.classtrack.core.domain.location.LocationProvider
import com.classtrack.notification.NotificationHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that wires location and notification infrastructure.
 * Binds the AOSP implementation to the Domain's LocationProvider interface.
 */
@Module
@InstallIn(SingletonComponent::class)
object LocationModule {

    @Provides
    @Singleton
    fun provideLocationProvider(
        @ApplicationContext context: Context
    ): LocationProvider = AospLocationProvider(context)

    @Provides
    @Singleton
    fun provideNotificationHelper(
        @ApplicationContext context: Context
    ): NotificationHelper = NotificationHelper(context)
}
