package com.application.intercom.owner.activity.playVideo

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.databinding.ActivityPlayVideoBinding
import com.application.intercom.utils.CommonUtil
import com.application.intercom.utils.EmpCustomLoader

class PlayVideoActivity : BaseActivity<ActivityPlayVideoBinding>() {
    override fun getLayout(): ActivityPlayVideoBinding {
        return ActivityPlayVideoBinding.inflate(layoutInflater)
    }

    private var video_url: String = ""
    private var key: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        video_url = intent.getStringExtra("video_url").toString()
        key = intent.getStringExtra("key").toString()
        println("----urlvideo$video_url")
        /*  var thum = CommonUtil.generateThumbnail(this, video_url)
          println("----thum$thum")*/
        initView()
        listener()
    }

    private fun initView() {
        if (key.equals("community")) {
            binding.imageView71.visibility = View.VISIBLE
        } else if (key.equals("chat")) {
            binding.imageView71.visibility = View.VISIBLE
        } else {
            binding.imageView71.visibility = View.INVISIBLE
        }

    }

    private fun listener() {
        binding.imageView85.setOnClickListener {
            finish()
        }
        binding.play.setOnClickListener {
            binding.play.visibility = View.INVISIBLE
            initVideoPlayer()
        }
        binding.imageView71.setOnClickListener {
            CommonUtil.downloadVideo(this, video_url, "video.mp4")
            Toast.makeText(this, "Download Video!!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun initVideoPlayer() {
        EmpCustomLoader.showLoader(this)
        val videoUri = Uri.parse(video_url)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(binding.simpleExoPlayerView2)
        binding.simpleExoPlayerView2.setMediaController(mediaController)
        binding.simpleExoPlayerView2.setVideoURI(videoUri)
        binding.simpleExoPlayerView2.setOnPreparedListener { mediaPlayer ->
            binding.ivthum.visibility = View.INVISIBLE
            EmpCustomLoader.hideLoader()
            binding.simpleExoPlayerView2.visibility = View.VISIBLE
            // mediaPlayer.isLooping = true // Set looping to true for autoplay
            mediaPlayer.start() // Start the video playback
        }


        binding.simpleExoPlayerView2.setOnErrorListener { mediaPlayer, what, extra ->
            // Log or display the error details
            Log.d("tag", "erroeeeeee" + what.toString() + extra.toString())
            return@setOnErrorListener true
        }

    }
}