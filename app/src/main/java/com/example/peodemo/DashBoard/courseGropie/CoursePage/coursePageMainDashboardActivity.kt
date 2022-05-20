package com.example.peodemo.DashBoard.courseGropie.CoursePage

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peodemo.DashBoard.courseGropie.ModulesPage.URLTypeCourse
import com.example.peodemo.DashBoard.courseGropie.ModulesPage.modulesOfCourseGropie
import com.example.peodemo.DashBoard.courseGropie.lessonDetailsPage.lessonDetailsPageActivity
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.CourseLessonsModel
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseModulesModel
import com.example.peodemo.logPages.model.User
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
import com.google.firebase.firestore.CollectionReference
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
import kotlinx.android.synthetic.main.activity_course_page_main_dashboard.*
import kotlinx.android.synthetic.main.course_modules_card_view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class coursePageMainDashboardActivity : AppCompatActivity() {


    private lateinit var courseInfoFromDashBoard: String
    private lateinit var moduleInfoFromModuleScreen: String
    private lateinit var moduleInfoFromModuleScreenPlusOne: String
    private lateinit var lessonInfoFromLessonScreen: String
    private lateinit var lessonInfoFromLessonScreenPlusOne: String
    private var lessonImageInfo = ArrayList<String>()


    private lateinit var exoPlayerView: PlayerView
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var mediaSource: MediaSource
    private var statusShown = 0
    private lateinit var urlType : URLTypeCourse
    private lateinit var timer: CountDownTimer


    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val fireStoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val currentUserCourseDocRef: CollectionReference
        get() = fireStoreInstance.collection("Users/${mAuth.currentUser?.uid.toString()}/My Courses")

    private var coursesItems = mutableListOf<Item>()
    private var coursesLessonsName = ArrayList<String>()
    private lateinit var courseItemSection: Section

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_page_main_dashboard)

        //assigning this property to context the activity on it
        val window = this.window
        //this line to change the state bar by using statusBarColor
        window?.statusBarColor = this.resources.getColor(R.color.white)
        window?.decorView!!.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        courseInfoFromDashBoard = intent.getStringExtra("courseName") as String
        moduleInfoFromModuleScreen = intent.getStringExtra("courseModuleName") as String
        moduleInfoFromModuleScreenPlusOne = intent.getStringExtra("courseModuleNamePlusOne") as String

        lessonPageSeeAll.setOnClickListener {
            lessonRecycleView.smoothScrollToPosition(coursesItems.size)
        }

        GlobalScope.launch(Dispatchers.IO){
            getDataFromServer()
        }

        back_button.setOnClickListener {
            finish()
        }

        GlobalScope.launch(Dispatchers.IO) {
            getLessonsName()
            delay(200L)
            changeLessonsInfo()
            getCourseInformation(::initRecycleView)
        }
        findView()
        lessonPlayVideo.setOnClickListener {
            intiPlayer()
            lessonPlayVideo.visibility = View.GONE
            statusShown = 1
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

    private fun getCourseInformation(onListen : (List<Item>) -> Unit): ListenerRegistration {
        return currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).collection("Lessons").addSnapshotListener { value, error ->
            if (error != null)
                return@addSnapshotListener
            coursesItems.clear()
            var index = 0
            value!!.documents.forEach {
                coursesItems.add(coursesOfModuleGropie(it.toObject(CourseLessonsModel::class.java)!!,this@coursePageMainDashboardActivity,index + 1))
                index++
            }
            onListen(coursesItems)
        }
    }

    @SuppressLint("WrongConstant")
    private fun initRecycleView(item: List<Item>){
        lessonRecycleView.apply {
            layoutManager = LinearLayoutManager(this@coursePageMainDashboardActivity, LinearLayout.VERTICAL,false)
            adapter = GroupAdapter<ViewHolder>().apply {
                courseItemSection =  Section(item)
                add(courseItemSection)
                setOnItemClickListener(onItemClick)
            }
        }
    }
    val onItemClick = OnItemClickListener {Item,View ->
        if (Item is coursesOfModuleGropie){
            if (Item.lessonInfo.enabled!!){
                val intent = Intent(this,lessonDetailsPageActivity::class.java)
                intent.putExtra("courseName",courseInfoFromDashBoard)
                intent.putExtra("courseModuleName",moduleInfoFromModuleScreen)
                intent.putExtra("courseLessonName",coursesLessonsName[Item.index - 1])
                startActivity(intent)
                this.overridePendingTransition(R.anim.slide_in_left_introduction_activity,R.anim.silde_out_right_introduction_activity)
            }
        }
    }

    private fun getLessonsName(){
        currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).collection("Lessons").addSnapshotListener { value, error ->
            if (error != null){
                return@addSnapshotListener
            }
            value!!.documents.forEach {
                coursesLessonsName.add(it.id)
            }
        }
    }

    private fun changeLessonsInfo(){
        currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).collection("Lessons").get().addOnSuccessListener{
            if (it.isEmpty){
                return@addOnSuccessListener
            }
            var index = 0
            for (items in it.documents){
                var anotherIndex = index + 1
                if (anotherIndex <= coursesLessonsName.size - 1){
                    checkEnabledInCourse(coursesLessonsName[index],coursesLessonsName[anotherIndex],anotherIndex)
                    //Toast.makeText(this,"${moduleName[index]} :: ${moduleName[anotherIndex]}",Toast.LENGTH_LONG).show()
                }
                index++
            }
        }
    }















    @SuppressLint("SetTextI18n")
    private fun getDataFromServer(){
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
            moduleDataByHashMap["process"] = true
            moduleDataByHashMap["tasksCount"] = it.tasksCount
            moduleDataByHashMap["title"] = it.title
            moduleDataByHashMap["videosCount"] = it.videosCount
            ModuleName.text = moduleDataByHashMap["title"].toString()
            lessonCount.text = "${moduleDataByHashMap["lessonsCount"]} Lessons"
            videoCount.text = "${moduleDataByHashMap["videosCount"]} Videos"
            taskCount.text = "${moduleDataByHashMap["tasksCount"]} Tasks"


            currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).update(moduleDataByHashMap)
            if (it.Finished!!){
                getUserCourseModulePlusOne {
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
                    currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreenPlusOne).update(moduleDataByHashMap)
                }
            }else{
                getUserCourseModulePlusOne {
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
                    moduleDataByHashMap["process"] = false
                    moduleDataByHashMap["tasksCount"] = it.tasksCount
                    moduleDataByHashMap["title"] = it.title
                    moduleDataByHashMap["videosCount"] = it.videosCount
                    currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreenPlusOne).update(moduleDataByHashMap)
                }
            }
        }
    }

    private fun getUserModuleInformation(onComplete:(courseModulesModel) -> Unit){
        currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).get().addOnSuccessListener {
            onComplete(it.toObject(courseModulesModel::class.java)!!)
        }
    }

    private fun getUserCourseModulePlusOne(onComplete:(courseModulesModel) -> Unit){
        currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreenPlusOne).get().addOnSuccessListener {
            onComplete(it.toObject(courseModulesModel::class.java)!!)
        }
    }

    private fun checkEnabledInCourse(LessonInfoFromModuleScreenFun:String,LessonInfoFromModuleScreenPlusOneFun:String,indexPlusOne:Int){
        lessonInfoFromLessonScreen = LessonInfoFromModuleScreenFun
        lessonInfoFromLessonScreenPlusOne = LessonInfoFromModuleScreenPlusOneFun
        getUserLessonInformation {
            if (it.finishedCount!! == it.finishCount!!){
                val lessonDataByHashMap = mutableMapOf<String, Any?>()
                lessonDataByHashMap["assignmentCount"] = it.assignmentCount
                lessonDataByHashMap["challengeFinishedCount"] = it.challengeFinishedCount
                lessonDataByHashMap["description"] = it.description
                lessonDataByHashMap["enabled"] = it.enabled
                lessonDataByHashMap["finishCount"] = it.finishCount
                lessonDataByHashMap["finished"] = true
                lessonDataByHashMap["finishedCount"] = it.finishedCount
                lessonDataByHashMap["id"] = it.id
                lessonDataByHashMap["lessonChallengeDetails"] = it.lessonChallengeDetails
                lessonDataByHashMap["lessonDetails"] = it.lessonDetails
                lessonDataByHashMap["lessonQUIZDetails"] = it.lessonQUIZDetails
                lessonDataByHashMap["lessonResourceDetails"] = it.lessonResourceDetails
                lessonDataByHashMap["minutes"] = it.minutes
                lessonDataByHashMap["name"] = it.name
                lessonDataByHashMap["number"] = it.number
                lessonDataByHashMap["process"] = it.Process
                lessonDataByHashMap["quizCount"] = it.quizCount
                lessonDataByHashMap["quizFinishedCount"] = it.quizFinishedCount
                lessonDataByHashMap["resourceFinishedCount"] = it.resourceFinishedCount
                lessonDataByHashMap["videoFinishedCount"] = it.videoFinishedCount
                lessonDataByHashMap["videosCount"] = it.videosCount
                //Toast.makeText(this,LessonInfoFromModuleScreenFun,Toast.LENGTH_LONG).show()
                currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).collection("Lessons").document(LessonInfoFromModuleScreenFun).update(lessonDataByHashMap)
                lessonInfoFromLessonScreenPlusOne = coursesLessonsName[indexPlusOne]
                getUserLessonInformationPlusOne {
                    val lessonDataByHashMap = mutableMapOf<String, Any?>()
                    lessonDataByHashMap["assignmentCount"] = it.assignmentCount
                    lessonDataByHashMap["challengeFinishedCount"] = it.challengeFinishedCount
                    lessonDataByHashMap["description"] = it.description
                    lessonDataByHashMap["enabled"] = true
                    lessonDataByHashMap["finishCount"] = it.finishCount
                    lessonDataByHashMap["finished"] = it.finished
                    lessonDataByHashMap["finishedCount"] = it.finishedCount
                    lessonDataByHashMap["id"] = it.id
                    lessonDataByHashMap["lessonChallengeDetails"] = it.lessonChallengeDetails
                    lessonDataByHashMap["lessonDetails"] = it.lessonDetails
                    lessonDataByHashMap["lessonQUIZDetails"] = it.lessonQUIZDetails
                    lessonDataByHashMap["lessonResourceDetails"] = it.lessonResourceDetails
                    lessonDataByHashMap["minutes"] = it.minutes
                    lessonDataByHashMap["name"] = it.name
                    lessonDataByHashMap["number"] = it.number
                    lessonDataByHashMap["process"] = it.Process
                    lessonDataByHashMap["quizCount"] = it.quizCount
                    lessonDataByHashMap["quizFinishedCount"] = it.quizFinishedCount
                    lessonDataByHashMap["resourceFinishedCount"] = it.resourceFinishedCount
                    lessonDataByHashMap["videoFinishedCount"] = it.videoFinishedCount
                    lessonDataByHashMap["videosCount"] = it.videosCount
                    currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).collection("Lessons").document(LessonInfoFromModuleScreenPlusOneFun).update(lessonDataByHashMap)
                }
            }
        }
    }

    private fun getUserLessonInformation(onComplete:(CourseLessonsModel) -> Unit){
        currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).collection("Lessons").document(lessonInfoFromLessonScreen).get().addOnSuccessListener {
            onComplete(it.toObject(CourseLessonsModel::class.java)!!)
        }
    }

    private fun getUserLessonInformationPlusOne(onComplete:(CourseLessonsModel) -> Unit){
        currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).collection("Lessons").document(lessonInfoFromLessonScreenPlusOne).get().addOnSuccessListener {
            onComplete(it.toObject(CourseLessonsModel::class.java)!!)
        }
    }

    override fun onResume() {
        super.onResume()
        changeLessonsInfo()
        if (statusShown == 1){
            simpleExoPlayer.playWhenReady = true
            simpleExoPlayer.play()
        }
    }
     private fun findView(){
        //constraintLayoutRoot = findViewById(R.id.course_modules_linear_layout)
        exoPlayerView = findViewById(R.id.courseLessonVideoIntroduction)
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
        urlType.url = "https://firebasestorage.googleapis.com/v0/b/epo-d0e54.appspot.com/o/courses%2Ffoundation%2Fmodule%201%2Flesson%202%2Flesson%2Fintroduction%20to%20xcode.mp4?alt=media&token=4aa6c239-fb69-4d8f-839a-fd54914ac796"

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
            Toast.makeText(this@coursePageMainDashboardActivity , "Check your internet connection and try again!", Toast.LENGTH_SHORT).show()
        }
    }
    private fun addListener() {
        simpleExoPlayer.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_IDLE -> {
                        exoPlayerView.useController = true
                        lessonPlayVideo.visibility = View.GONE
                    }
                    Player.STATE_BUFFERING -> {
                        exoPlayerView.useController = true
                        lessonPlayVideo.visibility = View.GONE
                    }
                    Player.STATE_READY -> {
                        exoPlayerView.useController = true
                        lessonPlayVideo.visibility = View.GONE
                    }
                    Player.STATE_ENDED -> {
                        lessonPlayVideo.visibility = View.VISIBLE
                        exoPlayerView.useController = false
                        statusShown = 0
                    }
                }

            }
        })
    }

}