package com.example.peodemo.DashBoard.courseGropie.ModulesPage

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peodemo.DashBoard.courseGropie.CoursePage.coursePageMainDashboardActivity
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
import kotlinx.android.synthetic.main.activity_course_modules_page.back_button
import kotlinx.android.synthetic.main.activity_course_modules_page.playVideo_button
import kotlinx.android.synthetic.main.activity_course_modules_page.seeAll_button
import kotlinx.android.synthetic.main.activity_course_page_main_dashboard.*
import kotlinx.android.synthetic.main.activity_main_dash_board2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class courseModulesPageActivity : AppCompatActivity() {

    private lateinit var courseInfoFromDashBoard: String
    private var courseURLInfoFromDashBoard = ""
    private var courseModulesFromDashBoard = 0
    private var courseLessonsFromDashBoard = 0
    private var courseVideosFromDashBoard = 0

    private lateinit var moduleInfoFromModuleScreen: String
    private lateinit var moduleInfoFromModuleScreenPlusOne: String

    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val fireStoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val currentUserDocRef: DocumentReference
        get() = fireStoreInstance.document("Users/${mAuth.currentUser?.uid.toString()}")
    private var modulesItems = mutableListOf<Item>()
    private var moduleName = ArrayList<String>()
    private lateinit var courseItemSection: Section


    private lateinit var timer: CountDownTimer
    private lateinit var checkEnabledTimer: CountDownTimer

    private var dashBoardpassCheck = false
    private lateinit var constraintLayoutRoot:ConstraintLayout
    private lateinit var exoPlayerView: PlayerView
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var mediaSource: MediaSource
    private var statusShown = 0
    private lateinit var urlType : URLTypeCourse

    @RequiresApi(Build.VERSION_CODES.M)
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
        dashBoardpassCheck = intent.getBooleanExtra("dashboardPassBoarding",false)
        if (dashBoardpassCheck){
            courseInfoFromDashBoard = intent.getStringExtra("courseInfo") as String
            courseURLInfoFromDashBoard = intent.getStringExtra("courseInfourl") as String
            courseModulesFromDashBoard = intent.getIntExtra("courseModule",0)
            courseLessonsFromDashBoard = intent.getIntExtra("courseLessons",0)
            courseVideosFromDashBoard = intent.getIntExtra("courseVideos",0)
            courseName.text = "$courseInfoFromDashBoard Course"
            course_module_count.text = "$courseModulesFromDashBoard Modules"
            course_Lesson_count.text = "$courseLessonsFromDashBoard Lessons"
            course_Videos_count.text = "$courseVideosFromDashBoard Videos"
        }

        findView()


        GlobalScope.launch(Dispatchers.IO) {
            getModulesName()
            delay(1000L)
            changeModulesInfo()
            getModulesInformation(::initRecycleView)
        }





        playVideo_button.setOnClickListener {
            intiPlayer()
            playVideo_button.visibility = View.GONE
            statusShown = 1
        }
        back_button.setOnClickListener {
            finish()
        }
        timer =  object : CountDownTimer(5000, 1000) {

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

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            hideSystemUI()
            window.statusBarColor = this.resources.getColor(R.color.black)
            timer.start()
        }else{
            timer.cancel()
            showSystemUI()
            window.statusBarColor = this.resources.getColor(R.color.white)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }




    }

    private fun getModulesName(){
        currentUserDocRef.collection("My Courses").document(courseInfoFromDashBoard).collection("Modules").addSnapshotListener { value, error ->
            if (error != null){
                return@addSnapshotListener
            }
            value!!.documents.forEach {
                moduleName.add(it.id)
            }
        }
    }
    private fun changeModulesInfo(){
        currentUserDocRef.collection("My Courses").document(courseInfoFromDashBoard).collection("Modules").get().addOnSuccessListener{
            if (it.isEmpty){
                return@addOnSuccessListener
            }
            var index = 0
            for (items in it.documents){
                var anotherIndex = index + 1
                if (anotherIndex <= moduleName.size - 1){
                    checkEnabledInCourse(moduleName[index],moduleName[anotherIndex],anotherIndex)
                    //Toast.makeText(this,"${moduleName[index]} :: ${moduleName[anotherIndex]}",Toast.LENGTH_LONG).show()
                }
                index++
            }
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
        if (Item is modulesOfCourseGropie){
            if (Item.moduleInfo.enabled!!){
                val intent = Intent(this,coursePageMainDashboardActivity::class.java)
                intent.putExtra("courseName",courseInfoFromDashBoard)
                intent.putExtra("courseModuleName",Item.moduleInfo.name)
                intent.putExtra("courseModuleNamePlusOne",moduleName[Item.index])
                startActivity(intent)
                this.overridePendingTransition(R.anim.slide_in_left_introduction_activity,R.anim.silde_out_right_introduction_activity)
            }
        }
    }


    private fun findView(){
        //constraintLayoutRoot = findViewById(R.id.course_modules_linear_layout)
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
        urlType.url = courseURLInfoFromDashBoard

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
        if (statusShown == 1){
            simpleExoPlayer.playWhenReady = true
            simpleExoPlayer.play()
        }
    }
    override fun onPause() {
        super.onPause()
        if (statusShown == 1){
            simpleExoPlayer.pause()
            simpleExoPlayer.playWhenReady = false
        }

    }

    override fun onStop() {
        super.onStop()
        if (statusShown == 1){
            simpleExoPlayer.pause()
            simpleExoPlayer.playWhenReady = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
       if (statusShown == 1){
           simpleExoPlayer.removeListener(playerListener)
           simpleExoPlayer.stop()
           simpleExoPlayer.clearMediaItems()
           window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
       }
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
                        playVideo_button.visibility = View.GONE
                    }
                    Player.STATE_BUFFERING -> {
                        exoPlayerView.useController = true
                        playVideo_button.visibility = View.GONE
                    }
                    Player.STATE_READY -> {
                        exoPlayerView.useController = true
                        playVideo_button.visibility = View.GONE
                    }
                    Player.STATE_ENDED -> {
                        playVideo_button.visibility = View.VISIBLE
                        exoPlayerView.useController = false
                        statusShown = 0
                    }
                }

            }
        })
    }

    private fun checkEnabledInCourse(moduleInfoFromModuleScreenFun:String,moduleInfoFromModuleScreenPlusOneFun:String,indexPlusOne:Int){
        moduleInfoFromModuleScreen = moduleInfoFromModuleScreenFun
        moduleInfoFromModuleScreenPlusOne = moduleInfoFromModuleScreenPlusOneFun
        getUserModuleInformation {
            val moduleDataByHashMap = mutableMapOf<String, Any?>()
            moduleDataByHashMap["description"] = it.description
            moduleDataByHashMap["finished"] = it.Finished
            moduleDataByHashMap["finishedLessons"] = it.finishedLessons
            moduleDataByHashMap["id"] = it.id
            moduleDataByHashMap["image"] = it.image
            moduleDataByHashMap["lessonDetails"] = it.lessonDetails
            moduleDataByHashMap["lessonsCount"] = it.lessonsCount
            moduleDataByHashMap["name"] = it.name
            moduleDataByHashMap["process"] = it.Process
            moduleDataByHashMap["tasksCount"] = it.tasksCount
            moduleDataByHashMap["title"] = it.title
            moduleDataByHashMap["videosCount"] = it.videosCount
            //currentUserDocRef.collection("My Courses").document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).update(moduleDataByHashMap)

            if (it.Finished!! && it.Process!!){
                moduleInfoFromModuleScreenPlusOne = moduleName[indexPlusOne]
                getUserModuleInformationPlusOne {
                    val moduleDataByHashMap = mutableMapOf<String, Any?>()
                    moduleDataByHashMap["description"] = it.description
                    moduleDataByHashMap["enabled"] = true
                    moduleDataByHashMap["finished"] = it.Finished
                    moduleDataByHashMap["finishedLessons"] = it.finishedLessons
                    moduleDataByHashMap["id"] = it.id
                    moduleDataByHashMap["image"] = it.image
                    moduleDataByHashMap["lessonDetails"] = it.lessonDetails
                    moduleDataByHashMap["lessonsCount"] = it.lessonsCount
                    moduleDataByHashMap["name"] = it.name
                    moduleDataByHashMap["process"] = it.Process
                    moduleDataByHashMap["tasksCount"] = it.tasksCount
                    moduleDataByHashMap["title"] = it.title
                    moduleDataByHashMap["videosCount"] = it.videosCount
                    currentUserDocRef.collection("My Courses").document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreenPlusOneFun).update(moduleDataByHashMap)
                }
            }else if (!it.Finished!! && !it.Process!!){
                moduleInfoFromModuleScreenPlusOne = moduleName[indexPlusOne]
                getUserModuleInformationPlusOne{
                    val moduleDataByHashMap = mutableMapOf<String, Any?>()
                    moduleDataByHashMap["description"] = it.description
                    moduleDataByHashMap["enabled"] = false
                    moduleDataByHashMap["finished"] = it.Finished
                    moduleDataByHashMap["finishedLessons"] = it.finishedLessons
                    moduleDataByHashMap["id"] = it.id
                    moduleDataByHashMap["image"] = it.image
                    moduleDataByHashMap["lessonDetails"] = it.lessonDetails
                    moduleDataByHashMap["lessonsCount"] = it.lessonsCount
                    moduleDataByHashMap["name"] = it.name
                    moduleDataByHashMap["process"] = it.Process
                    moduleDataByHashMap["tasksCount"] = it.tasksCount
                    moduleDataByHashMap["title"] = it.title
                    moduleDataByHashMap["videosCount"] = it.videosCount
                    currentUserDocRef.collection("My Courses").document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreenPlusOneFun).update(moduleDataByHashMap)
                }
            }
        }
    }

    private fun getUserModuleInformation(onComplete:(courseModulesModel) -> Unit){
        currentUserDocRef.collection("My Courses").document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).get().addOnSuccessListener {
            onComplete(it.toObject(courseModulesModel::class.java)!!)
        }
    }

    private fun getUserModuleInformationPlusOne(onComplete:(courseModulesModel) -> Unit){
        currentUserDocRef.collection("My Courses").document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreenPlusOne).get().addOnSuccessListener {
            onComplete(it.toObject(courseModulesModel::class.java)!!)
        }
    }

}

enum class URLTypeCourse(var url: String) {
    MP4(""),HLS("")

}
