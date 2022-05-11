package com.example.peodemo.DashBoard.courseGropie.lessonDetailsPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.CourseLessonsModel
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseModulesModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_course_page_main_dashboard.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson_details_page)

        courseInfoFromDashBoard = intent.getStringExtra("courseName") as String
        moduleInfoFromModuleScreen = intent.getStringExtra("courseModuleName") as String
        lessonInfoFromLessonScreen = intent.getStringExtra("courseLessonName") as String
        //Toast.makeText(this,lessonInfoFromLessonScreen,Toast.LENGTH_LONG).show()
        //Toast.makeText(this,lessonInfoFromLessonScreenPlusOne,Toast.LENGTH_LONG).show()
        getDataFromServer()


    }

    private fun getDataFromServer(){
        getUserLessonInformation {
            val moduleDataByHashMap = mutableMapOf<String, Any?>()
            moduleDataByHashMap["assignmentCount"] = it.assignmentCount
            moduleDataByHashMap["description"] = it.description
            moduleDataByHashMap["enabled"] = it.enabled
            moduleDataByHashMap["finished"] = it.finished
            moduleDataByHashMap["finishedCount"] = it.finishedCount
            moduleDataByHashMap["id"] = it.id
            moduleDataByHashMap["lessonChallengeDetails"] = it.lessonChallengeDetails
            moduleDataByHashMap["lessonDetails"] = it.lessonDetails
            moduleDataByHashMap["lessonQUIZDetails"] = it.lessonQUIZDetails
            moduleDataByHashMap["name"] = it.name
            moduleDataByHashMap["number"] = it.number
            moduleDataByHashMap["process"] = true
            moduleDataByHashMap["quizCount"] = it.quizCount
            moduleDataByHashMap["videosCount"] = it.videosCount
            currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).collection("Lessons").document(lessonInfoFromLessonScreen).update(moduleDataByHashMap)
        }
    }

    private fun getUserLessonInformation(onComplete:(CourseLessonsModel) -> Unit){
        currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).collection("Lessons").document(lessonInfoFromLessonScreen).get().addOnSuccessListener {
            onComplete(it.toObject(CourseLessonsModel::class.java)!!)
        }
    }


}