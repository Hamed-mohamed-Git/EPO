package com.example.peodemo.DashBoard.courseGropie.CoursePage

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peodemo.DashBoard.courseGropie.ModulesPage.modulesOfCourseGropie
import com.example.peodemo.DashBoard.courseGropie.lessonDetailsPage.lessonDetailsPageActivity
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.CourseLessonsModel
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseModulesModel
import com.example.peodemo.logPages.model.User
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
import kotlinx.coroutines.launch

class coursePageMainDashboardActivity : AppCompatActivity() {


    private lateinit var courseInfoFromDashBoard: String
    private lateinit var moduleInfoFromModuleScreen: String
    private lateinit var moduleInfoFromModuleScreenPlusOne: String


    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val fireStoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private val currentUserCourseDocRef: CollectionReference
        get() = fireStoreInstance.collection("Users/${mAuth.currentUser?.uid.toString()}/My Courses")

    private var coursesItems = mutableListOf<Item>()
    private var coursesName = ArrayList<String>()
    private lateinit var courseItemSection: Section

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

        GlobalScope.launch(Dispatchers.IO){
            getDataFromServer()
        }
        getCourseInformation(::initRecycleView)
        back_button.setOnClickListener {
            finish()
        }
        getLessonsName()




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
                intent.putExtra("courseLessonName",coursesName[Item.index - 1])
                intent.putExtra("courseLessonNamePlusOne",coursesName[Item.index])
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
                coursesName.add(it.id)
            }
        }
    }















    private fun getDataFromServer(){
        getUserCourseInformation {
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
                getUserCourseInformationPlusOne {
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
                getUserCourseInformationPlusOne {
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

    private fun getUserCourseInformation(onComplete:(courseModulesModel) -> Unit){
        currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreen).get().addOnSuccessListener {
            onComplete(it.toObject(courseModulesModel::class.java)!!)
        }
    }

    private fun getUserCourseInformationPlusOne(onComplete:(courseModulesModel) -> Unit){
        currentUserCourseDocRef.document(courseInfoFromDashBoard).collection("Modules").document(moduleInfoFromModuleScreenPlusOne).get().addOnSuccessListener {
            onComplete(it.toObject(courseModulesModel::class.java)!!)
        }
    }
}