package com.example.peodemo.DashBoard.courseGropie.ModulesPage

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peodemo.DashBoard.courseGropie.courseItems
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.ourCourses.CourseDetails.URLType
import com.example.peodemo.home.introduction.fragments.ourCourses.CourseDetails.courseDetailModel
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.CoursesModel
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseModulesModel
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_course_modules_page.*
import kotlinx.android.synthetic.main.activity_main_dash_board2.*

class courseModulesPageActivity : AppCompatActivity() {

    private lateinit var courseInfoFromDashBoard: String
    private var courseModulesFromDashBoard = 0
    private var courseLessonsFromDashBoard = 0
    private var courseVideosFromDashBoard = 0
    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val fireStoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val currentUserDocRef: DocumentReference
        get() = fireStoreInstance.document("Users/${mAuth.currentUser?.uid.toString()}")
    private var modulesItems = mutableListOf<Item>()
    private lateinit var courseItemSection: Section



    private lateinit var constraintLayoutRoot:ConstraintLayout
    private lateinit var linearLayoutRoot:LinearLayout
    private lateinit var exoPlayerView: PlayerView
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var mediaSource: MediaSource
    private var statusShown = 1
    private lateinit var urlType : URLTypeCourse

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_modules_page)
        //assigning this property to context the activity on it
        val window = this.window
        //this line to change the state bar by using statusBarColor
        window?.statusBarColor = this.resources.getColor(R.color.white)
        window?.decorView!!.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        seeAll_button.setOnClickListener {
            modules_RecyclerView.smoothScrollToPosition(modulesItems.size)
        }
        back_button.setOnClickListener {
            finish()
        }

        courseInfoFromDashBoard = intent.getStringExtra("courseInfo") as String
        courseModulesFromDashBoard = intent.getIntExtra("courseModule",0)
        courseLessonsFromDashBoard = intent.getIntExtra("courseLessons",0)
        courseVideosFromDashBoard = intent.getIntExtra("courseVideos",0)
        courseName.text = "$courseInfoFromDashBoard Course"
        course_module_count.text = "$courseModulesFromDashBoard Modules"
        course_Lesson_count.text = "$courseLessonsFromDashBoard Lessons"
        course_Videos_count.text = "$courseVideosFromDashBoard Videos"
        getModulesInformation(::initRecycleView)



        playVideo_button.setOnClickListener {
            findView()
            intiPlayer()
            playVideo_button.visibility = View.GONE
        }





    }




    private fun getModulesInformation(onListen : (List<Item>) -> Unit): ListenerRegistration {
        return currentUserDocRef.collection("My Courses").document(courseInfoFromDashBoard).collection("Modules").addSnapshotListener { value, error ->
            if (error != null)
                return@addSnapshotListener
            modulesItems.clear()
            var index = 0
            value!!.documents.forEach {
                modulesItems.add(modulesOfCourseGropie(it.toObject(courseModulesModel::class.java)!!,this,index + 1))
                index++
            }
            onListen(modulesItems)
        }
    }

    @SuppressLint("WrongConstant")
    private fun initRecycleView(item: List<Item>){
        modules_RecyclerView.apply {
            layoutManager = LinearLayoutManager(this@courseModulesPageActivity, LinearLayout.VERTICAL,false)
            adapter = GroupAdapter<ViewHolder>().apply {
                courseItemSection =  Section(item)
                add(courseItemSection)
                setOnItemClickListener(onItemClick)
            }
        }
    }
    val onItemClick = OnItemClickListener {Item,View ->
        if (Item is courseItems){
            if (Item.courseInfo.id == null){

            }else{

            }
        }
    }



    private fun findView(){
        constraintLayoutRoot = findViewById(R.id.course_modules_linear_layout)
        exoPlayerView = findViewById(R.id.courseModulesVideoIntroduction)
        //linearLayoutRoot = findViewById(R.id.CourseModelLinearLayout)
    }
    private fun intiPlayer(){
        simpleExoPlayer = SimpleExoPlayer.Builder(this).build()
        simpleExoPlayer.addListener(playerListener)

        exoPlayerView.player = simpleExoPlayer

        createMediaSource()

        simpleExoPlayer.setMediaSource(mediaSource)
        simpleExoPlayer.prepare()
        addListener()
        simpleExoPlayer.play()


    }

    private fun createMediaSource() {

        //urlType = URLType.MP4
        //urlType.url = "https://www.dropbox.com/home/EPO%20educating%20programming%20online/project%20explanation/QR%20section?preview=QR+Section.mp4"

        urlType = URLTypeCourse.MP4
        urlType.url = "https://firebasestorage.googleapis.com/v0/b/messrenger.appspot.com/o/Courses%20Videos%2FTHE%20introduction%20of%20courses%2FIOS%20development%2FiOS%20Foundations%20(SwiftUI).mp4?alt=media&token=ee3c2084-b743-4a75-a433-e78c996cf0ea"

        simpleExoPlayer.seekTo(0)
        when(urlType){
            URLTypeCourse.MP4 -> {
                val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(

                    this,
                    Util.getUserAgent(this,applicationInfo.name)
                )
                mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(
                    MediaItem.fromUri(Uri.parse(urlType.url))
                )
            }
            URLTypeCourse.HLS -> {
                val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
                    this,
                    Util.getUserAgent(this,applicationInfo.name)
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

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){

            hideSystemUI()

        }
        else {
            showSystemUI()

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
            if (urlType == URLTypeCourse.HLS){
                exoPlayerView.useController = false
            }
            if (urlType == URLTypeCourse.MP4){
                exoPlayerView.useController = true
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            Toast.makeText(this@courseModulesPageActivity, "Check your internet connection and try again!", Toast.LENGTH_SHORT).show()
        }
    }
    private fun addListener() {
        simpleExoPlayer.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_IDLE -> {
                        exoPlayerView.useController = true
                    }
                    Player.STATE_BUFFERING -> {
                        exoPlayerView.useController = true
                    }
                    Player.STATE_READY -> {
                        exoPlayerView.useController = true
                    }
                    Player.STATE_ENDED -> {
                        playVideo_button.visibility = View.VISIBLE
                        exoPlayerView.useController = false
                    }
                }

            }
        })
    }
}
enum class URLTypeCourse(var url: String) {
    MP4(""),HLS("")

}
