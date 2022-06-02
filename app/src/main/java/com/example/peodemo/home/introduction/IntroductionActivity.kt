package com.example.peodemo.home.introduction

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peodemo.DashBoard.courseGropie.ModulesPage.URLTypeCourse
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.HomeFragment
import com.example.peodemo.home.introduction.fragments.InformationFragment
import com.example.peodemo.home.introduction.fragments.ourCourses.ourCoursesFragment
import com.example.peodemo.home.introduction.fragments.teachOnEPOFragment
import com.example.peodemo.logPages.signInActivity
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.android.synthetic.main.activity_introduction.*


import com.example.peodemo.home.introduction.teacher.teacherModel
import com.example.peodemo.home.introduction.teacher.teacherModelView
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


import kotlin.collections.ArrayList


open class introductionActivity : AppCompatActivity(), tapViewModel.OnItemClickListener,
    teacherModelView.OnItemClickListener {

    private var youTubePlayerView:YouTubePlayerView? = null
    private val videoId = "HMys6oWaUio"

    private val fireStoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val epoStatisticsDocRef: CollectionReference
        get() = fireStoreInstance.collection("epoStatistics")

    private var codeCountS = 0
    private var currentCount = 0

    //assigning a tapList array property to store the recycle view in it
    private val  tapList = ArrayList<tapModel>()
    //assigning a adapter property to store the tapView instance in ti
    private val adapter = tapViewModel(tapList,this)
    //assigning a mHomeFragment to get a HomeFragment instance to access this fragment
    private val mHomeFragment = HomeFragment()
    //assigning a mInformationFragment to get a InformationFragment instance to access this fragment
    private val mInformationFragment = InformationFragment()
    //assigning a mTeachOnEPOFragment to get a TeachOnEPOFragment instance to access this fragment
    private val mTeachOnEPOFragment = teachOnEPOFragment()
    //assigning a mOurCoursesFragment to get a OurCoursesFragment instance to access this fragment
    private val mOurCoursesFragment = ourCoursesFragment()


    private val  teachers = ArrayList<teacherModel>()
    //assigning a adapter property to store the tapView instance in ti
    private val teacherAdapter = teacherModelView(teachers,this)
    private var listenr:AbstractYouTubePlayerListener? = null


    private lateinit var exoPlayerView: PlayerView
    private lateinit var simpleExoPlayer: SimpleExoPlayer
    private lateinit var mediaSource: MediaSource
    private var statusShown = 0
    private lateinit var urlType : URLTypeCourse
    private lateinit var timer: CountDownTimer


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)




        getStatisticOfCodeFromServer {
            codeCountS = it.count!!
        }

        cwcImage.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://codewithchris.com/?_ga=2.43673443.752191784.1654040368-433373843.1649806717")
            startActivity(intent)
        }
        moshImage.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://codewithmosh.com")
            startActivity(intent)
        }
        jomaImage.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.jomaclass.com")
            startActivity(intent)
        }


        timer =  object : CountDownTimer(8000, 1000) {

            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {
                if (currentCount <= codeCountS ){
                    codeCount.setText("${currentCount}k")
                    currentCount++
                }
            }

            // Callback function, fired
            // when the time is up
            override fun onFinish() {

            }


        }.start()





        getStatisticOfUsersFromServer {
            usesCount.setText("${currentCount}k")
        }
        getStatisticOfVideosFromServer {
            videoCount.setText("${currentCount}k")
        }


        //make a condition if the phone sdk above 15 or not
        //if matches
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.window.navigationBarColor = getColor(R.color.theSubIOSFoundationCourse)


            //assigning this property to context the activity on it
            val window = this.window
            //this line to change the state bar by using statusBarColor
            window?.statusBarColor = this.resources.getColor(R.color.white)
            window?.decorView!!.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            //addition the Home item to tapList array to access it in adapter class
            tapList.add(tapModel("Epo",R.drawable.epoproject,this.getColor(R.color.epoBackground),this.getColor(R.color.epo),1))
            //addition the Information item to tapList array to access it in adapter class
            tapList.add(tapModel("Teacher",R.drawable.teacher,this.getColor(R.color.teacherBackground),this.getColor(R.color.teacher),0))
            //addition the Teach on EPO item to tapList array to access it in adapter class
            tapList.add(tapModel("Collaboration",R.drawable.trust,this.getColor(R.color.collaborationBackground),this.getColor(R.color.collaboration),0))
            //addition the Our Courses item to tapList array to access it in adapter class
            tapList.add(tapModel("Courses",R.drawable.cappressed,this.getColor(R.color.courseBackground),this.getColor(R.color.course),0))

            teachers.add(teacherModel("Chris","After building apps for enterprise clients for years, I found a passion for helping non-coders discover a love for programming, become professional developers and change their careers. I would love to help you reach your goals!"
                ,"iOS Developer",this.getColor(R.color.chrisImageBackground),R.drawable.chrisimage))
            teachers.add(teacherModel("Jonathan Ma","I'm a Canadian YouTuber with a channel of the same name. MY real name is Jonathan Ma. Mostly known for making videos related to Software engineering, data science, Silicon Valley, and tech companies, etc"
            ,"Engineer of Computer Science",this.getColor(R.color.JomaImageBackground),R.drawable.jomaimage))
            teachers.add(
                teacherModel("Mosh Hamedani","I'm a passionate and pragmatic software engineer specializing in web application development with ASP.NET MVC, Web API, Entity Framework, Angular, Backbone, HTML5, and CSS.",
            "software engineer",this.getColor(R.color.moshImageBackground),R.drawable.moshimage))
        }

        //set a home fragment as a init fragment if the activity is created
        setFragment(mOurCoursesFragment)
        //control the recycle view to by horizontal
        fragmentMarksButton.layoutManager = LinearLayoutManager(this,LinearLayout.HORIZONTAL,false)
        //set an adapter to this recycle view
        fragmentMarksButton.adapter = adapter

        teachersRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayout.HORIZONTAL,false)
        teachersRecyclerView.adapter = teacherAdapter


        //make an action if the user tap on signIn button take they to SignIn Activity
        signInIntroductionButton.setOnClickListener {
            val intent = Intent(this,signInActivity::class.java)
            intent.putExtra("CourseID","")
            startActivity(intent)
            this.overridePendingTransition(R.anim.slide_in_left_introduction_activity,R.anim.silde_out_right_introduction_activity)
        }

        startNow.setOnClickListener {

            //introductionScroll.smoothScrollTo(0,videoIntroductionLayout.top)
            var x = ObjectAnimator.ofInt(introductionScroll,"scrollY",fragmentMarksButton.top)
            x.duration = 3000
            x.start()
        }

    }

    //declare a private SetFragment method to control in the fragments when the user press on the items in recycle view
    private fun setFragment(fragment: Fragment) {
        //declare an instance from beginTransaction class to call the method in it and store it in fr property
        val fr = supportFragmentManager.beginTransaction()
        //calling the replace method to trans the fragments
        fr.replace(R.id.introductionCoordinatorLayoutNestedScrollView,fragment)
        //and commit it
        fr.commit()

    }

    //calling a onItemClick that we crated in tapViewModel class to make actions if the user tap on any item
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onItemClick(position: Int) {
        //use a conditional loop to access each item by index
        when (position){
            // if the user tap on the first item
            0 ->{
                youTubePlayerView!!.addYouTubePlayerListener(object :
                    AbstractYouTubePlayerListener() {
                    override fun onError(
                        youTubePlayer: YouTubePlayer,
                        error: PlayerConstants.PlayerError
                    ) {
                        super.onError(youTubePlayer, error)
                        youTubePlayer.loadVideo(videoId,0f)
                    }

                })



                //call this item and put it in tap property
                var taps = tapList[position]
                //and set a last var in this item to 1 if the user tap on this item
                taps.pressed = 1
                //And set 0 to other items
                tapList[1].pressed = 0
                tapList[2].pressed = 0
                tapList[3].pressed = 0
                //And change the fragment
                //setFragment(mHomeFragment)
                var x = ObjectAnimator.ofInt(introductionScroll,"scrollY",videoIntroductionLayout.top)
                x.duration = 2000
                x.start()
                //And change the item in the tap array
                adapter.notifyItemChanged(position)
                adapter.notifyItemChanged(1)
                adapter.notifyItemChanged(2)
                adapter.notifyItemChanged(3)
                this.window.navigationBarColor = getColor(R.color.theSubIOSFoundationCourse)

            }
            // if the user tap on the second item
            1 ->{
                //call this item and put it in tap property
                var taps = tapList[position]
                //and set a last var in this item to 1, if the user tap on this item
                taps.pressed = 1
                //And set 0 to other items
                tapList[0].pressed = 0
                tapList[2].pressed = 0
                tapList[3].pressed = 0

                var x = ObjectAnimator.ofInt(introductionScroll,"scrollY",teacherLinearLayout.top)
                x.duration = 3000
                x.start()
                //And change the fragment
                //setFragment(mInformationFragment)
                //And change the item in the tap array
                adapter.notifyItemChanged(position)
                adapter.notifyItemChanged(0)
                adapter.notifyItemChanged(2)
                adapter.notifyItemChanged(3)
                this.window.navigationBarColor = getColor(R.color.theSubIOSFoundationCourse)
            }
            // if the user tap on the third item
            2 ->{
                //call this item and put it in tap property
                var taps = tapList[position]
                //and set a last var in this item to 1, if the user tap on this item
                taps.pressed = 1
                //And set 0 to other items
                tapList[0].pressed = 0
                tapList[1].pressed = 0
                tapList[3].pressed = 0
                var x = ObjectAnimator.ofInt(introductionScroll,"scrollY",collaborationLayout.top)
                x.duration = 4000
                x.start()
                //And change the fragment
                //setFragment(mTeachOnEPOFragment)
                //And change the item in the tap array
                adapter.notifyItemChanged(position)
                adapter.notifyItemChanged(0)
                adapter.notifyItemChanged(1)
                adapter.notifyItemChanged(3)
                this.window.navigationBarColor = getColor(R.color.theSubIOSFoundationCourse)
            }
            // if the user tap on the last item
            3 ->{
                //call this item and put it in tap property
                var taps = tapList[position]
                //and set a last var in this item to 1, if the user tap on this item
                taps.pressed = 1
                //And set 0 to other items
                tapList[0].pressed = 0
                tapList[1].pressed = 0
                tapList[2].pressed = 0
                //And change the fragment
                //setFragment(mOurCoursesFragment)
                var x = ObjectAnimator.ofInt(introductionScroll,"scrollY",introductionCoordinatorLayoutNestedScrollView.top)
                x.duration = 5000
                x.start()
                //And change the item in the tap array
                adapter.notifyItemChanged(position)
                adapter.notifyItemChanged(0)
                adapter.notifyItemChanged(1)
                adapter.notifyItemChanged(2)
                
                this.window.navigationBarColor = getColor(R.color.transpernt)


            }
        }



    }



    private fun getStatisticOfCodeFromServer(onComplete:(statisticsModel) -> Unit){
        epoStatisticsDocRef.document("code").get().addOnSuccessListener {
            onComplete(it.toObject(statisticsModel::class.java)!!)
        }
    }

    private fun getStatisticOfUsersFromServer(onComplete:(statisticsModel) -> Unit){
        epoStatisticsDocRef.document("users").get().addOnSuccessListener {
            onComplete(it.toObject(statisticsModel::class.java)!!)
        }
    }

    private fun getStatisticOfVideosFromServer(onComplete:(statisticsModel) -> Unit){
        epoStatisticsDocRef.document("videos").get().addOnSuccessListener {
            onComplete(it.toObject(statisticsModel::class.java)!!)
        }
    }

    override fun onTeacherItemClick(position: Int) {
        when (position){
            0 ->{
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://www.youtube.com/codewithchris")
                startActivity(intent)
            }
            1 ->{
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://www.youtube.com/c/JomaOppa")
                startActivity(intent)
            }
            2 ->{
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://www.youtube.com/c/programmingwithmosh")
                startActivity(intent)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        youTubePlayerView!!.release()
    }


}