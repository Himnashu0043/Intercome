package com.application.intercom.utils


import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.*
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ui.PlayerView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class ExoPlayerUtils {
    companion object {

        private val MIN_BUFFER_DURATION = 2000
        private val MAX_BUFFER_DURATION = 5000
        private val MIN_PLAYBACK_START_BUFFER = 1500
        private val MIN_PLAYBACK_RESUME_BUFFER = 2000


        fun PlayerView.playVideo(
            url: String, repeatMode: Int = Player.REPEAT_MODE_ONE, isBuffering: (Int) -> Unit
        ): ExoPlayer {

//            player?.stop()
//            player?.release()
//            player = null
            val isFileInCache = isFileInCache(context, url)
            if (!isFileInCache) {
                val inputData =
                    Data.Builder().putString(DownloadVideoWorker.KEY_VIDEO_URL, url).build()
                val downloadWorkRequest =
                    OneTimeWorkRequest.Builder(DownloadVideoWorker::class.java)
                        .setInputData(inputData).build()
                WorkManager.getInstance(context).enqueue(downloadWorkRequest)
            }

            val videoUri: Uri = if (isFileInCache) Uri.fromFile(
                getVideoFileFromCache(
                    context,
                    url
                )
            ) else Uri.parse(url)
            Log.d("PlayingVideoExoPlayer", "videoUri: ${videoUri.toString()}")
            val loadControl: LoadControl

            loadControl = DefaultLoadControl.Builder()
                .setBufferDurationsMs(
                    MIN_BUFFER_DURATION,
                    MAX_BUFFER_DURATION,
                    MIN_PLAYBACK_START_BUFFER,
                    MIN_PLAYBACK_RESUME_BUFFER
                )
                .setPrioritizeTimeOverSizeThresholds(true)
                .createDefaultLoadControl()

            val simpleExoPlayer = ExoPlayer.Builder(context).setLoadControl(loadControl).build()

            val mediaItem: MediaItem =
                MediaItem.fromUri(videoUri)

            simpleExoPlayer.setMediaItem(mediaItem)
            simpleExoPlayer.prepare()
            simpleExoPlayer.playWhenReady = true
            simpleExoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    isBuffering.invoke(state)
                }
            })
            simpleExoPlayer.repeatMode = repeatMode
            simpleExoPlayer.volume = 0f
            player = simpleExoPlayer
            player?.play()
            return simpleExoPlayer
        }


        fun isFileInCache(context: Context, fileName: String): Boolean {
            val cacheDir = context.cacheDir
            val file = File(cacheDir, fileName)
            return file.exists()
        }

        fun clearCache(context: Context) {
            context.cacheDir.deleteRecursively()
        }

        fun getVideoFileFromCache(context: Context, fileName: String): File? {
            val cacheDir = context.cacheDir
            val file = File(cacheDir, fileName)
            return if (file.exists()) {
                Log.d("PlayingVideoExoPlayer", "videoUri: getting file from cache ${file.name}")
                file
            } else {
                Log.d("PlayingVideoExoPlayer", "videoUri: not getting file from cache")
                null
            }
        }
    }
}

class DownloadVideoWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val videoUrl = inputData.getString(KEY_VIDEO_URL) ?: return Result.failure()

        Log.d("SAVE_FILE_CACHE", "saveVideoToCache: $videoUrl")
        val file = File(applicationContext.cacheDir, videoUrl)
        try {
            val url = URL(videoUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            try {
                connection.connect()
                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    throw IOException("HTTP error code: " + connection.responseCode)
                }
                if (file.parentFile?.exists() != true) {
                    file.parentFile?.mkdirs()
                }
                FileOutputStream(file).use { output ->
                    connection.inputStream.use { input ->
                        val buffer = ByteArray(8 * 1024)
                        var bytesRead: Int
                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            Log.d("SAVE_FILE_CACHE", " writingObjects: $bytesRead")
                            output.write(buffer, 0, bytesRead)
                        }
                    }
                }
                Log.d("SAVE_FILE_CACHE", " file downlaoded")
            } catch (e: Exception) {
                connection.disconnect()
                return Result.failure()
            } finally {
                connection.disconnect()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return Result.failure()
        }

        return Result.success()
    }

    companion object {
        const val KEY_VIDEO_URL = "VIDEO_URL"
    }
}
