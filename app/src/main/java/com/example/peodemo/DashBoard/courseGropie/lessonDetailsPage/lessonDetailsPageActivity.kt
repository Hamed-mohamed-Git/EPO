package com.example.peodemo.DashBoard.courseGropie.lessonDetailsPage

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.peodemo.DashBoard.courseGropie.lessonDetailsPage.quizPage.quizPageActivity
import com.example.peodemo.DashBoard.glide.GlideApp
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.ourCourses.CourseDetails.IntroductionVideo
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.CourseLessonsModel
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseLessonDetailsModel
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseLessonQuizDetials.courseLessonQuizDetailsModel
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseModulesModel
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.lessonImageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_course_page_main_dashboard.*
import kotlinx.android.synthetic.main.activity_lesson_details_page.*
import kotlinx.android.synthetic.main.activity_main_dash_board2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
            }
        }

        getUserLessonQuizInformation {
            if (it.finished == true){
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

        lessonDetailsBackButton.setOnClickListener {
            finish()
        }

        quizFrameLayout.setOnClickListener {

            val intent = Intent(this,quizPageActivity::class.java)
            intent.putExtra("questionsRoot","$courseInfoFromDashBoard/Modules/$moduleInfoFromModuleScreen/Lessons/$lessonInfoFromLessonScreen")
            startActivity(intent)
            this.overridePendingTransition(R.anim.slide_in_left_introduction_activity,R.anim.silde_out_right_introduction_activity)
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
        currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).collection("Lessons")
            .document(lessonInfoFromLessonScreen).collection("lesson Details").document("lesson info")
            .get().addOnSuccessListener {
            onComplete(it.toObject(courseLessonDetailsModel::class.java)!!)
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
            }
        }

        getUserLessonQuizInformation {
            if (it.finished == true){
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