package com.application.intercom.user.home

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.UserAdvertimentNewResponse
import com.application.intercom.databinding.UserAdViewpagerItemBinding
import com.application.intercom.utils.loadImagesWithGlideExt
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.LoadControl
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player

class UserTopAdvitAdapter2(
    val con: Context,
    val list: ArrayList<UserAdvertimentNewResponse.Data.TopUser>,
    private val handle: () -> Unit = {}
) :
    RecyclerView.Adapter<UserTopAdvitAdapter2.VideoViewHolder>() {


    private val MIN_BUFFER_DURATION = 2000
    private val MAX_BUFFER_DURATION = 5000
    private val MIN_PLAYBACK_START_BUFFER = 1500
    private val MIN_PLAYBACK_RESUME_BUFFER = 2000
    var simpleExoPlayer: ExoPlayer? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(
            UserAdViewpagerItemBinding.inflate(
                LayoutInflater.from(
                    con
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.apply {
            simpleExoPlayer?.volume = 0f
            val old = list[position].advertisementData[0].image
            if (old.contains("mp4")) {
                binding.videoView.visibility = View.VISIBLE
                binding.imageView52.visibility = View.INVISIBLE
                initVideoPlayer(old, binding)
            } else {
                binding.pro.visibility = View.GONE
                binding.videoView.visibility = View.INVISIBLE
                binding.imageView52.visibility = View.VISIBLE
                binding.imageView52.loadImagesWithGlideExt(old)
            }
            binding.ivMute.setOnClickListener {
                if (binding.ivMute.visibility == View.VISIBLE) {
                    binding.ivMute.visibility = View.INVISIBLE
                    binding.ivunMute.visibility = View.VISIBLE
                    simpleExoPlayer?.volume = 1f
                }
            }
            binding.ivunMute.setOnClickListener {
                if (binding.ivunMute.visibility == View.VISIBLE) {
                    binding.ivunMute.visibility = View.INVISIBLE
                    binding.ivMute.visibility = View.VISIBLE
                    simpleExoPlayer?.volume = 0f
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class VideoViewHolder(val binding: UserAdViewpagerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }
    }

    fun initVideoPlayer(url: String, binding: UserAdViewpagerItemBinding) {
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
        val videoUri = Uri.parse(url)
        simpleExoPlayer =
            ExoPlayer.Builder(binding.root.context).setLoadControl(loadControl).build()
        binding.videoView.player = simpleExoPlayer
        val mediaItem: MediaItem =
            MediaItem.fromUri(videoUri)
        simpleExoPlayer?.addMediaItem(mediaItem)
        simpleExoPlayer?.prepare()
        simpleExoPlayer?.playWhenReady = true
        simpleExoPlayer?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {

                // to change progress bar's visibility
                if (state == Player.STATE_BUFFERING) {
                    binding.pro.visibility = View.VISIBLE
                } else {
                    binding.pro.visibility = View.GONE
                }

                // to move to the next page when video completed
                if (state == Player.STATE_ENDED) {
                    handle()
                }
            }
        })

        simpleExoPlayer?.repeatMode = Player.REPEAT_MODE_OFF
        simpleExoPlayer?.volume = 0f
        binding.videoView.player = simpleExoPlayer
//       var mpl = MediaPlayer.create(con,url)
//       var si:Int = mpl.getDuration()

    }


}