package com.example.peodemo.home.introduction.fragments.ourCourses.CourseDetails


import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.example.peodemo.R
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class IntroductionVideo : AppCompatActivity() {
    private lateinit var constraintLayoutRoot:ConstraintLayout
    private lateinit var exoPlayerView: PlayerView
    private  var URLFromCourseDetails = ""
    private lateinit var simpleExoPlayer:SimpleExoPlayer
    private lateinit var mediaSource:MediaSource
    private var statusShown = 1
    private lateinit var urlType : URLType
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction_video)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window?.statusBarColor = this.resources.getColor(R.color.black)
        window?.navigationBarColor = this.resources.getColor(R.color.black)
        URLFromCourseDetails = intent.getStringExtra("url") as String
        findView()
        intiPlayer()
        timer =  object : CountDownTimer(6000, 1000) {

            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {

            }

            // Callback function, fired
            // when the time is up
            override fun onFinish() {
                hideSystemUI()
                if (statusShown == 1){
                    exoPlayerView.hideController()
                }
                start()
            }

        }
        timer.start()
    }
    private fun findView(){
        constraintLayoutRoot = findViewById(R.id.constraintLayoutRoot)
        exoPlayerView = findViewById(R.id.exoPlayerView)
    }
    private fun intiPlayer(){
        simpleExoPlayer = SimpleExoPlayer.Builder(this).build()
        simpleExoPlayer.addListener(playerListener)

        exoPlayerView.player = simpleExoPlayer

        createMediaSource()

        simpleExoPlayer.setMediaSource(mediaSource)
        simpleExoPlayer.prepare()
    }

    private fun createMediaSource() {

        //urlType = URLType.MP4
        //urlType.url = "https://www.dropbox.com/home/EPO%20educating%20programming%20online/project%20explanation/QR%20section?preview=QR+Section.mp4"

        urlType = URLType.MP4
        urlType.url = URLFromCourseDetails

        simpleExoPlayer.seekTo(0)
        when(urlType){
            URLType.MP4 -> {
                val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(

                    this,
                    Util.getUserAgent(this,applicationInfo.name)
                )
                mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
                    MediaItem.fromUri(Uri.parse(urlType.url))
                )
            }
            URLType.HLS -> {
                val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                    this,
                    com.google.android.exoplayer2.util.Util.getUserAgent(this,applicationInfo.name)
                )
                mediaSource = HlsMediaSource.Factory(dataSourceFactory).createMediaSource(
                    MediaItem.fromUri(Uri.parse(urlType.url))

                )
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val  constraintSet = ConstraintSet()
        constraintSet.connect(exoPlayerView.id,ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,0)
        constraintSet.connect(exoPlayerView.id,ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM,0)
        constraintSet.connect(exoPlayerView.id,ConstraintSet.START,ConstraintSet.PARENT_ID,ConstraintSet.START,0)
        constraintSet.connect(exoPlayerView.id,ConstraintSet.END,ConstraintSet.PARENT_ID,ConstraintSet.END,0)

        constraintSet.applyTo(constraintLayoutRoot)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            statusShown = 1
            showSystemUI()
            exoPlayerView.layoutParams.width= ViewGroup.LayoutParams.MATCH_PARENT
            exoPlayerView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        else {
            statusShown = 1
            showSystemUI()
            val layoutParams = exoPlayerView.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.dimensionRatio = "16:9"
        }

        window.decorView.requestLayout()
    }

    private fun hideSystemUI(){
        actionBar?.hide()

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
    }

    private fun showSystemUI(){
        actionBar?.hide()

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
    }

    override fun onResume() {
        super.onResume()
        simpleExoPlayer.playWhenReady = true
        simpleExoPlayer.play()
    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer.pause()
        simpleExoPlayer.playWhenReady = false
    }

    override fun onStop() {
        super.onStop()
        simpleExoPlayer.pause()
        simpleExoPlayer.playWhenReady = false
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer.removeListener(playerListener)
        simpleExoPlayer.stop()
        simpleExoPlayer.clearMediaItems()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private var playerListener = object  : Player.Listener {
        override fun onRenderedFirstFrame() {
            super.onRenderedFirstFrame()
            if (urlType == URLType.HLS){
                exoPlayerView.useController = false
            }
            if (urlType == URLType.MP4){
                exoPlayerView.useController = true
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            Toast.makeText(this@IntroductionVideo, "Check your internet connection and try again!", Toast.LENGTH_SHORT).show()
        }
    }
}
enum class URLType(var url: String) {
    MP4(""),HLS("")

}


