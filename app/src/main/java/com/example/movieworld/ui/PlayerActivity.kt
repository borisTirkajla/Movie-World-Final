package com.example.movieworld.ui

import android.os.Bundle
import android.util.Log
import com.example.movieworld.BuildConfig
import com.example.movieworld.databinding.ActivityPlayerBinding
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer

class PlayerActivity : YouTubeBaseActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var youtubePlayerInit: YouTubePlayer.OnInitializedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val trailerUrl = intent.extras?.getString("trailerUrl")
        if (!trailerUrl.isNullOrEmpty()) {
            playYouTubeTrailer(trailerUrl)
        }
    }

    private fun playYouTubeTrailer(videoUrl: String) {
        val stringListDelimited = videoUrl.split("?v=")
        val mVideoUrl = stringListDelimited[1]
        youtubePlayerInit = object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                p0: YouTubePlayer.Provider?,
                p1: YouTubePlayer?,
                p2: Boolean
            ) {
                p1?.setFullscreen(true)
                p1?.loadVideo(mVideoUrl)
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.d("MovieItemActivity", "Youtube API fail.")
            }

        }
        binding.youtubePlayer.initialize(BuildConfig.GOOGLE_API_KEY, youtubePlayerInit)
    }

}