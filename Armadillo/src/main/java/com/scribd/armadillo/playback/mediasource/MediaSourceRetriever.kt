package com.scribd.armadillo.playback.mediasource

import android.content.Context
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.util.Util
import com.scribd.armadillo.di.Injector
import com.scribd.armadillo.extensions.toUri
import com.scribd.armadillo.models.AudioPlayable
import javax.inject.Inject

/** Creates a MediaSource for starting playback in Exoplayer based on what type
 * of audio content is passed into it. */
interface MediaSourceRetriever {
    fun generateMediaSource(request: AudioPlayable.MediaRequest,
                            context: Context): MediaSource

    fun updateMediaSourceHeaders(request: AudioPlayable.MediaRequest)
}

class MediaSourceRetrieverImpl @Inject constructor(): MediaSourceRetriever {
    @Inject
    internal lateinit var hlsGenerator: HlsMediaSourceGenerator

    @Inject
    internal lateinit var progressiveMediaSourceGenerator: ProgressiveMediaSourceGenerator

    init {
        Injector.mainComponent.inject(this)
    }

    override fun generateMediaSource(request: AudioPlayable.MediaRequest,
                                     context: Context): MediaSource {

        return buildMediaGenerator(request).generateMediaSource(context, request)
    }

    override fun updateMediaSourceHeaders(request: AudioPlayable.MediaRequest) {
        buildMediaGenerator(request).updateMediaSourceHeaders(request)
    }

    private fun buildMediaGenerator(request: AudioPlayable.MediaRequest): MediaSourceGenerator = buildMediaGenerator(request, null)

    private fun buildMediaGenerator(request: AudioPlayable.MediaRequest, overrideExtension: String?): MediaSourceGenerator {
        val uri = request.url.toUri()

        return when (@C.ContentType val type = Util.inferContentType(uri, overrideExtension)) {
            C.TYPE_HLS -> hlsGenerator
            C.TYPE_OTHER -> progressiveMediaSourceGenerator
            else -> throw IllegalStateException("Unsupported type: $type")
        }
    }
}