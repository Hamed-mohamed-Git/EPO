package com.example.peodemo.DashBoard.courseGropie.lessonDetailsPage

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.example.peodemo.DashBoard.courseGropie.lessonDetailsPage.challengePage.challengePageActivity
import com.example.peodemo.DashBoard.courseGropie.lessonDetailsPage.quizPage.quizPageActivity
import com.example.peodemo.DashBoard.glide.GlideApp
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.ourCourses.CourseDetails.IntroductionVideo
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.*
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseChallengeDetails.courseLessonChellengeDetailsModel
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseLessonQuizDetials.courseLessonQuizDetailsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_course_page_main_dashboard.*
import kotlinx.android.synthetic.main.activity_lesson_details_page.*
import kotlinx.android.synthetic.main.activity_main_dash_board2.*

class lessonDetailsPageActivity : AppCompatActivity() {


    private lateinit var courseInfoFromDashBoard: String
    private lateinit var moduleInfoFromModuleScreen: String
    private lateinit var lessonInfoFromLessonScreen: String

    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val fireStoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val currentUserCourseDocRef: CollectionReference
        get() = fireStoreInstance.collection("Users/${mAuth.currentUser?.uid.toString()}/My Courses")

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson_details_page)

        //this line to change the state bar by using statusBarColor
        val window = this.window
        window?.statusBarColor = this.resources.getColor(R.color.white)
        window?.decorView!!.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        //get data from dashboard
        courseInfoFromDashBoard = intent.getStringExtra("courseName") as String
        moduleInfoFromModuleScreen = intent.getStringExtra("courseModuleName") as String
        lessonInfoFromLessonScreen = intent.getStringExtra("courseLessonName") as String

        getLessonImage { getImage(it.lessonImageURI!!) }

        getDataFromServer()


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
                currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules")
                    .document(moduleInfoFromModuleScreen).collection("Lessons")
                    .document(lessonInfoFromLessonScreen).update(lessonDataByHashMap)
                val number = it.number
                getUserModuleInformation {
                    val moduleDataByHashMap = mutableMapOf<String, Any?>()
                    moduleDataByHashMap["description"] = it.description
                    moduleDataByHashMap["enabled"] = it.enabled
                    moduleDataByHashMap["finished"] = it.Finished
                    moduleDataByHashMap["finishedLessons"] = number
                    moduleDataByHashMap["id"] = it.id
                    moduleDataByHashMap["image"] = it.image
                    moduleDataByHashMap["lessonDetails"] = it.lessonDetails
                    moduleDataByHashMap["lessonsCount"] = it.lessonsCount
                    moduleDataByHashMap["moduleNumber"] = it.moduleNumber
                    moduleDataByHashMap["name"] = it.name
                    moduleDataByHashMap["process"] = it.Process
                    moduleDataByHashMap["tasksCount"] = it.tasksCount
                    moduleDataByHashMap["title"] = it.title
                    moduleDataByHashMap["videosCount"] = it.videosCount
                    currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).update(moduleDataByHashMap)
                }
            }
        }


        getUserLessonDetailsInformation {
            if (it.finished == null){
                videoCheck.setImageResource(R.drawable.notfound)
            }else{
                val lessonDetailsDataByHashMap = mutableMapOf<String, Any?>()
                lessonDetailsDataByHashMap["description"] = it.description
                lessonDetailsDataByHashMap["finished"] = true
                lessonDetailsDataByHashMap["name"] = it.name
                lessonDetailsDataByHashMap["resources"] = it.resources
                lessonDetailsDataByHashMap["uri"] = it.uri
                val uri = it.uri
                lessonVideoFrameLayout.setOnClickListener {
                    val intent  = Intent(this,IntroductionVideo::class.java)
                    intent.putExtra("url",uri)
                    startActivity(intent)
                    this.overridePendingTransition(R.anim.slide_in_left_introduction_activity,R.anim.silde_out_right_introduction_activity)
                    getUserLessonInformation {
                        val lessonDataByHashMap = mutableMapOf<String, Any?>()
                        val finishedCount = it.challengeFinishedCount!! + 1 + it.quizFinishedCount!! + it.resourceFinishedCount!!
                        lessonDataByHashMap["assignmentCount"] = it.assignmentCount
                        lessonDataByHashMap["challengeFinishedCount"] = it.challengeFinishedCount
                        lessonDataByHashMap["description"] = it.description
                        lessonDataByHashMap["enabled"] = it.enabled
                        lessonDataByHashMap["finishCount"] = it.finishCount
                        lessonDataByHashMap["finished"] = it.finished
                        lessonDataByHashMap["finishedCount"] = finishedCount
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
                        lessonDataByHashMap["videoFinishedCount"] = 1
                        lessonDataByHashMap["videosCount"] = it.videosCount
                        currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules")
                            .document(moduleInfoFromModuleScreen).collection("Lessons")
                            .document(lessonInfoFromLessonScreen).update(lessonDataByHashMap)
                    }
                    currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules")
                        .document(moduleInfoFromModuleScreen).collection("Lessons")
                        .document(lessonInfoFromLessonScreen).collection("lesson Details")
                        .document("lesson info").update(lessonDetailsDataByHashMap)
                }
            }
            if (it.finished == true){
                videoCheck.setImageResource(R.drawable.tick)
            }
        }

        getUserLessonQuizInformation {

            getUserLessonInformation {
                val lessonDataByHashMap = mutableMapOf<String, Any?>()
                val finishedCount = it.challengeFinishedCount!! + it.videoFinishedCount!! + it.quizFinishedCount!! + it.resourceFinishedCount!!
                lessonDataByHashMap["assignmentCount"] = it.assignmentCount
                lessonDataByHashMap["challengeFinishedCount"] = it.challengeFinishedCount
                lessonDataByHashMap["description"] = it.description
                lessonDataByHashMap["enabled"] = it.enabled
                lessonDataByHashMap["finishCount"] = it.finishCount
                lessonDataByHashMap["finished"] = it.finished
                lessonDataByHashMap["finishedCount"] = finishedCount
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
                currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules")
                    .document(moduleInfoFromModuleScreen).collection("Lessons")
                    .document(lessonInfoFromLessonScreen).update(lessonDataByHashMap)

            }

        }

        lessonDetailsBackButton.setOnClickListener {
            finish()
        }



        getUserLessonQuizInformation {
            if (it.finished == null){
                quizCheck.setImageResource(R.drawable.notfound)
            }else{
                quizFrameLayout.setOnClickListener {
                    val intent = Intent(this,quizPageActivity::class.java)
                    intent.putExtra("questionsRoot",
                        "$courseInfoFromDashBoard/Modules/$moduleInfoFromModuleScreen/Lessons/$lessonInfoFromLessonScreen")
                    startActivity(intent)
                    this.overridePendingTransition(R.anim.slide_in_left_introduction_activity,R.anim.silde_out_right_introduction_activity)
                }
            }
            if (it.finished == true){
                quizCheck.setImageResource(R.drawable.tick)
            }
        }



        getUserChallengeInformation {
            if (it.finished == null){
                challengeCheck.setImageResource(R.drawable.notfound)
            }else{
                challengeFrameLayout.setOnClickListener {
                    val intent = Intent(this,challengePageActivity::class.java)
                    intent.putExtra("questionsRoot",
                        "$courseInfoFromDashBoard/Modules/$moduleInfoFromModuleScreen/Lessons/$lessonInfoFromLessonScreen")
                    startActivity(intent)
                    this.overridePendingTransition(R.anim.slide_in_left_introduction_activity,R.anim.silde_out_right_introduction_activity)
                }
            }
            if (it.finished == true){
                challengeCheck.setImageResource(R.drawable.tick)
                getUserLessonInformation {
                    val lessonDataByHashMap = mutableMapOf<String, Any?>()
                    val finishedCount = 1 + it.videoFinishedCount!! + it.quizFinishedCount!! + it.resourceFinishedCount!!
                    lessonDataByHashMap["assignmentCount"] = it.assignmentCount
                    lessonDataByHashMap["challengeFinishedCount"] = 1
                    lessonDataByHashMap["description"] = it.description
                    lessonDataByHashMap["enabled"] = it.enabled
                    lessonDataByHashMap["finishCount"] = it.finishCount
                    lessonDataByHashMap["finished"] = it.finished
                    lessonDataByHashMap["finishedCount"] = finishedCount
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
                    currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules")
                        .document(moduleInfoFromModuleScreen).collection("Lessons")
                        .document(lessonInfoFromLessonScreen).update(lessonDataByHashMap)
                }
            }
        }

        getUserResourceInformation {
            if (it.finished == null){
                resourceCheck.setImageResource(R.drawable.notfound)
            }else{
                val uri = it.uri!!.toString()
                resourceFrameLayout.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data =  Uri.parse(uri)
                    startActivity(intent)
                    this.overridePendingTransition(R.anim.slide_in_left_introduction_activity,R.anim.silde_out_right_introduction_activity)
                    getUserResourceInformation {
                        val resourceHasMap = mutableMapOf<String,Any?>()
                        resourceHasMap["finished"] = true
                        resourceHasMap["uri"] = it.uri
                        currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).collection("Lessons")
                            .document(lessonInfoFromLessonScreen).collection("lesson Details")
                            .document("Resource info").update(resourceHasMap)
                        getUserLessonInformation {
                            val lessonDataByHashMap = mutableMapOf<String, Any?>()
                            val finishedCount = it.challengeFinishedCount!! + it.videoFinishedCount!! + it.quizFinishedCount!! + 1
                            lessonDataByHashMap["assignmentCount"] = it.assignmentCount
                            lessonDataByHashMap["challengeFinishedCount"] = it.challengeFinishedCount
                            lessonDataByHashMap["description"] = it.description
                            lessonDataByHashMap["enabled"] = it.enabled
                            lessonDataByHashMap["finishCount"] = it.finishCount
                            lessonDataByHashMap["finished"] = it.finished
                            lessonDataByHashMap["finishedCount"] = finishedCount
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
                            lessonDataByHashMap["resourceFinishedCount"] = 1
                            lessonDataByHashMap["videoFinishedCount"] = it.videoFinishedCount
                            lessonDataByHashMap["videosCount"] = it.videosCount
                            currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules")
                                .document(moduleInfoFromModuleScreen).collection("Lessons")
                                .document(lessonInfoFromLessonScreen).update(lessonDataByHashMap)
                        }
                    }
                }

            }
            if (it.finished == true){
                resourceCheck.setImageResource(R.drawable.tick)
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun getDataFromServer(){
        getUserLessonInformation {
            val lessonDataByHashMap = mutableMapOf<String, Any?>()
            lessonDataByHashMap["assignmentCount"] = it.assignmentCount
            lessonDataByHashMap["challengeFinishedCount"] = it.challengeFinishedCount
            lessonDataByHashMap["description"] = it.description
            lessonDataByHashMap["enabled"] = it.enabled
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
            lessonDataByHashMap["process"] = true
            lessonDataByHashMap["quizCount"] = it.quizCount
            lessonDataByHashMap["quizFinishedCount"] = it.quizFinishedCount
            lessonDataByHashMap["resourceFinishedCount"] = it.resourceFinishedCount
            lessonDataByHashMap["videoFinishedCount"] = it.videoFinishedCount
            lessonDataByHashMap["videosCount"] = it.videosCount
            lessonDetailsName.text = lessonDataByHashMap["name"].toString()
            lessonDescription.text = lessonDataByHashMap["description"].toString()
            lessonNumber.text = "${lessonDataByHashMap["number"].toString()}.0"
            lessonMinutes.text = "${lessonDataByHashMap["minutes"].toString()} minutes"
            currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).collection("Lessons").document(lessonInfoFromLessonScreen).update(lessonDataByHashMap)
        }
    }
    private fun getImage(uri:String){
        GlideApp.with(this@lessonDetailsPageActivity).load(uri).placeholder(R.drawable.lessonimagetrying).into(lessonDetailsImage)
    }

    private fun getUserLessonInformation(onComplete:(CourseLessonsModel) -> Unit){
        currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).collection("Lessons").document(lessonInfoFromLessonScreen).get().addOnSuccessListener {
            onComplete(it.toObject(CourseLessonsModel::class.java)!!)
        }
    }
    private fun getUserLessonDetailsInformation(onComplete:(courseLessonDetailsModel) -> Unit){
        currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules")
            .document(moduleInfoFromModuleScreen).collection("Lessons")
            .document(lessonInfoFromLessonScreen).collection("lesson Details")
            .document("lesson info")
            .get().addOnSuccessListener {
            onComplete(it.toObject(courseLessonDetailsModel::class.java)!!)
        }
    }

    private fun getUserResourceInformation(onComplete:(courseLessonResourceDetails) -> Unit){
        currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).collection("Lessons")
            .document(lessonInfoFromLessonScreen).collection("lesson Details").document("Resource info")
            .get().addOnSuccessListener {
                onComplete(it.toObject(courseLessonResourceDetails::class.java)!!)
            }
    }

    private fun getUserChallengeInformation(onComplete:(courseLessonChellengeDetailsModel) -> Unit){
        currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).collection("Lessons")
            .document(lessonInfoFromLessonScreen).collection("lesson Details").document("challenge info")
            .get().addOnSuccessListener {
                onComplete(it.toObject(courseLessonChellengeDetailsModel::class.java)!!)
            }
    }

    private fun getUserModuleInformation(onComplete:(courseModulesModel) -> Unit){
        currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).get().addOnSuccessListener {
            onComplete(it.toObject(courseModulesModel::class.java)!!)
        }
    }



    private fun getLessonImage(onComplete:(lessonImageModel) -> Unit){
        fireStoreInstance.collection("course").document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).collection("Lessons").document(lessonInfoFromLessonScreen).get().addOnSuccessListener {
            onComplete(it.toObject(lessonImageModel::class.java)!!)

        }
    }

    private fun getUserLessonQuizInformation(onComplete:(courseLessonQuizDetailsModel) -> Unit){
        currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).collection("Lessons")
            .document(lessonInfoFromLessonScreen).collection("lesson Details").document("Quiz info")
            .get().addOnSuccessListener {
            onComplete(it.toObject(courseLessonQuizDetailsModel::class.java)!!)
        }

    }

    override fun onResume() {
        super.onResume()

        getUserLessonDetailsInformation {
            if (it.finished == true){
                videoCheck.setImageResource(R.drawable.tick)
            }
        }

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
                currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules")
                    .document(moduleInfoFromModuleScreen).collection("Lessons")
                    .document(lessonInfoFromLessonScreen).update(lessonDataByHashMap)
                val number = it.number
                getUserModuleInformation {
                    val moduleDataByHashMap = mutableMapOf<String, Any?>()
                    moduleDataByHashMap["description"] = it.description
                    moduleDataByHashMap["enabled"] = it.enabled
                    moduleDataByHashMap["finished"] = it.Finished
                    moduleDataByHashMap["finishedLessons"] = number
                    moduleDataByHashMap["id"] = it.id
                    moduleDataByHashMap["image"] = it.image
                    moduleDataByHashMap["lessonDetails"] = it.lessonDetails
                    moduleDataByHashMap["lessonsCount"] = it.lessonsCount
                    moduleDataByHashMap["moduleNumber"] = it.moduleNumber
                    moduleDataByHashMap["name"] = it.name
                    moduleDataByHashMap["process"] = it.Process
                    moduleDataByHashMap["tasksCount"] = it.tasksCount
                    moduleDataByHashMap["title"] = it.title
                    moduleDataByHashMap["videosCount"] = it.videosCount
                    currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).update(moduleDataByHashMap)
                }
            }
        }


        lessonVideoFrameLayout.setOnClickListener {
            getUserLessonDetailsInformation {
                val intent  = Intent(this,IntroductionVideo::class.java)
                intent.putExtra("url",it.uri)
                startActivity(intent)
                this.overridePendingTransition(R.anim.slide_in_left_introduction_activity,R.anim.silde_out_right_introduction_activity)
                getUserLessonInformation {
                    val lessonDataByHashMap = mutableMapOf<String, Any?>()
                    val finishedCount = it.challengeFinishedCount!! + 1 + it.quizFinishedCount!! + it.resourceFinishedCount!!
                    lessonDataByHashMap["assignmentCount"] = it.assignmentCount
                    lessonDataByHashMap["challengeFinishedCount"] = it.challengeFinishedCount
                    lessonDataByHashMap["description"] = it.description
                    lessonDataByHashMap["enabled"] = it.enabled
                    lessonDataByHashMap["finishCount"] = it.finishCount
                    lessonDataByHashMap["finished"] = it.finished
                    lessonDataByHashMap["finishedCount"] = finishedCount
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
                    lessonDataByHashMap["videoFinishedCount"] = 1
                    lessonDataByHashMap["videosCount"] = it.videosCount
                    currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules")
                        .document(moduleInfoFromModuleScreen).collection("Lessons")
                        .document(lessonInfoFromLessonScreen).update(lessonDataByHashMap)
                }
                val lessonDetailsDataByHashMap = mutableMapOf<String, Any?>()
                lessonDetailsDataByHashMap["description"] = it.description
                lessonDetailsDataByHashMap["finished"] = true
                lessonDetailsDataByHashMap["name"] = it.name
                lessonDetailsDataByHashMap["resources"] = it.resources
                lessonDetailsDataByHashMap["uri"] = it.uri
                currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules")
                    .document(moduleInfoFromModuleScreen).collection("Lessons")
                    .document(lessonInfoFromLessonScreen).collection("lesson Details")
                    .document("lesson info").update(lessonDetailsDataByHashMap)
            }

        }



        getUserLessonQuizInformation {

            if (it.finished == true){
                quizCheck.setImageResource(R.drawable.tick)
                getUserLessonInformation {
                    val lessonDataByHashMap = mutableMapOf<String, Any?>()
                    val finishedCount = it.challengeFinishedCount!! + it.videoFinishedCount!! + it.quizFinishedCount!! + it.resourceFinishedCount!!
                    lessonDataByHashMap["assignmentCount"] = it.assignmentCount
                    lessonDataByHashMap["challengeFinishedCount"] = it.challengeFinishedCount
                    lessonDataByHashMap["description"] = it.description
                    lessonDataByHashMap["enabled"] = it.enabled
                    lessonDataByHashMap["finishCount"] = it.finishCount
                    lessonDataByHashMap["finished"] = it.finished
                    lessonDataByHashMap["finishedCount"] = finishedCount
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
                    currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules")
                        .document(moduleInfoFromModuleScreen).collection("Lessons")
                        .document(lessonInfoFromLessonScreen).update(lessonDataByHashMap)
                }
            }
        }

        getUserResourceInformation {
                if (it.finished == true){
                    resourceCheck.setImageResource(R.drawable.tick)
                    getUserLessonInformation {
                        val lessonDataByHashMap = mutableMapOf<String, Any?>()
                        val finishedCount = it.challengeFinishedCount!! + it.videoFinishedCount!! + it.quizFinishedCount!! + 1
                        lessonDataByHashMap["assignmentCount"] = it.assignmentCount
                        lessonDataByHashMap["challengeFinishedCount"] = it.challengeFinishedCount
                        lessonDataByHashMap["description"] = it.description
                        lessonDataByHashMap["enabled"] = it.enabled
                        lessonDataByHashMap["finishCount"] = it.finishCount
                        lessonDataByHashMap["finished"] = it.finished
                        lessonDataByHashMap["finishedCount"] = finishedCount
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
                        lessonDataByHashMap["resourceFinishedCount"] = 1
                        lessonDataByHashMap["videoFinishedCount"] = it.videoFinishedCount
                        lessonDataByHashMap["videosCount"] = it.videosCount
                        currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules")
                            .document(moduleInfoFromModuleScreen).collection("Lessons")
                            .document(lessonInfoFromLessonScreen).update(lessonDataByHashMap)

                }
            }
        }
        getUserChallengeInformation {
            if (it.finished == true){
                challengeCheck.setImageResource(R.drawable.tick)
                getUserLessonInformation {
                    val lessonDataByHashMap = mutableMapOf<String, Any?>()
                    val finishedCount = 1 + it.videoFinishedCount!! + it.quizFinishedCount!! + it.resourceFinishedCount!!
                    lessonDataByHashMap["assignmentCount"] = it.assignmentCount
                    lessonDataByHashMap["challengeFinishedCount"] = 1
                    lessonDataByHashMap["description"] = it.description
                    lessonDataByHashMap["enabled"] = it.enabled
                    lessonDataByHashMap["finishCount"] = it.finishCount
                    lessonDataByHashMap["finished"] = it.finished
                    lessonDataByHashMap["finishedCount"] = finishedCount
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
                    currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules")
                        .document(moduleInfoFromModuleScreen).collection("Lessons")
                        .document(lessonInfoFromLessonScreen).update(lessonDataByHashMap)
                }
            }
        }



    }


}