package com.application.intercom.user.home

import android.R
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.application.intercom.data.model.remote.UserAdvertimentNewResponse
import com.application.intercom.databinding.UserAdViewpagerItemBinding
import com.application.intercom.utils.ExoPlayerUtils.Companion.playVideo
import com.application.intercom.utils.loadImagesWithGlideExt
import com.google.android.exoplayer2.*


class UserTopAdvitAdapter(
    val con: Context,
    val list: ArrayList<UserAdvertimentNewResponse.Data.TopUser>,
    val handle: () -> Unit = {}
) :
    PagerAdapter() {
    private val MIN_BUFFER_DURATION = 2000

    //Max Video you want to buffer during PlayBack
    private val MAX_BUFFER_DURATION = 5000

    //Min Video you want to buffer before start Playing it
    private val MIN_PLAYBACK_START_BUFFER = 1500

    //Min video You want to buffer when user resumes video
    private val MIN_PLAYBACK_RESUME_BUFFER = 2000
    var simpleExoPlayer: ExoPlayer? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding: UserAdViewpagerItemBinding = UserAdViewpagerItemBinding.inflate(
            LayoutInflater.from(
                con
            ), container, false
        )
       /* val aaManager = con.getSystemService(AUDIO_SERVICE) as AudioManager
        aaManager.setStreamMute(AudioManager.STREAM_MUSIC, false)*/
        simpleExoPlayer?.volume = 0f
        val old = list[position].advertisementData[0].image/*replace(" ", "%20")*/
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
        //binding.imageView52.loadImagesWithGlideExt(old)
        binding.videoView
        container.addView(binding.root, 0)
        println("---advertisementData${list[position]}")
        return binding.root
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    private fun initVideoPlayer(url: String, binding: UserAdViewpagerItemBinding) {
        simpleExoPlayer = binding.videoView.playVideo(url, Player.REPEAT_MODE_ONE) {
            if (it == Player.STATE_BUFFERING) {
                binding.pro.visibility = View.VISIBLE
            } else {
                binding.pro.visibility = View.GONE
            }
            if (it == Player.STATE_ENDED) {
                handle.invoke()
            }
        }
        /*val loadControl: LoadControl

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

            }
        })
        simpleExoPlayer?.repeatMode = Player.REPEAT_MODE_ONE
        simpleExoPlayer?.volume = 0f
        binding.videoView.player = simpleExoPlayer
//       var mpl = MediaPlayer.create(con,url)
//       var si:Int = mpl.getDuration()*/

    }
   /* fun File.getMediaDuration(context: Context): Long {
        if (!exists()) return 0

        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(context, Uri.parse(absolutePath))
        val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        retriever.release()

        return duration!!.toLongOrNull() ?: 0
    }*/

}