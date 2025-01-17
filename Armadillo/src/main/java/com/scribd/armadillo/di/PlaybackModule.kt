package com.scribd.armadillo.di

import android.app.Application
import android.content.Context
import com.scribd.armadillo.broadcast.ArmadilloNoisyReceiver
import com.scribd.armadillo.broadcast.ArmadilloNoisySpeakerReceiver
import com.scribd.armadillo.broadcast.ArmadilloNotificationDeleteReceiver
import com.scribd.armadillo.broadcast.NotificationDeleteReceiver
import com.scribd.armadillo.mediaitems.ArmadilloMediaBrowse
import com.scribd.armadillo.mediaitems.MediaContentSharer
import com.scribd.armadillo.playback.ArmadilloAudioAttributes
import com.scribd.armadillo.playback.AudioAttributesBuilderImpl
import com.scribd.armadillo.playback.MediaMetadataCompatBuilder
import com.scribd.armadillo.playback.MediaMetadataCompatBuilderImpl
import com.scribd.armadillo.playback.PlaybackEngineFactoryHolder
import com.scribd.armadillo.playback.PlaybackStateBuilderImpl
import com.scribd.armadillo.playback.PlaybackStateCompatBuilder
import com.scribd.armadillo.playback.mediasource.MediaSourceRetriever
import com.scribd.armadillo.playback.mediasource.MediaSourceRetrieverImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class PlaybackModule {
    @Singleton
    @Provides
    fun playbackEngineFactory() = PlaybackEngineFactoryHolder.factory

    @Singleton
    @Provides
    fun noisySpeakerReceiver(context: Context): ArmadilloNoisySpeakerReceiver =
        ArmadilloNoisyReceiver(context.applicationContext as Application)

    @Provides
    fun playbackStateBuilder(): PlaybackStateCompatBuilder = PlaybackStateBuilderImpl()

    @Provides
    fun mediaMetadataBuilder(): MediaMetadataCompatBuilder = MediaMetadataCompatBuilderImpl()

    @Provides
    fun audioAttributes(): ArmadilloAudioAttributes = AudioAttributesBuilderImpl()

    @Provides
    fun notificationListener(context: Context): NotificationDeleteReceiver =
        ArmadilloNotificationDeleteReceiver(context.applicationContext as Application)

    @Provides
    @Singleton
    fun mediaContentSharer(contentSharer: MediaContentSharer): ArmadilloMediaBrowse.ContentSharer = contentSharer

    @Provides
    @Singleton
    fun mediaBrowser(contentSharer: MediaContentSharer): ArmadilloMediaBrowse.Browser = contentSharer

    @Provides
    @Singleton
    fun mediaSourceRetriever(mediaSourceRetrieverImpl: MediaSourceRetrieverImpl): MediaSourceRetriever = mediaSourceRetrieverImpl
}