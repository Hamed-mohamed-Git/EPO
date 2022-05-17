package com.example.peodemo.DashBoard.courseGropie.lessonDetailsPage.quizPage

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.ourCourses.CourseDetails.IntroductionVideo
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseLessonQuizDetials.courseLessonQuizDetailsModel
import kotlinx.android.synthetic.main.activity_questions_page.*
import kotlinx.android.synthetic.main.activity_quiz_page.*
import kotlinx.android.synthetic.main.activity_quiz_page.startQuizButton

class quizPageActivity : AppCompatActivity() {
    private var question = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_page)
        //this line to change the state bar by using statusBarColor
        val window = this.window
        window?.statusBarColor = this.resources.getColor(R.color.white)
        window?.decorView!!.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        question = intent.getStringExtra("questionsRoot") as String
        quizBackButton.setOnClickListener {
            finish()
        }
        startQuizButton.setOnClickListener {
            val intent  = Intent(this, questionsPageActivity::class.java)
            intent.putExtra("questionRoot",question)
            startActivity(intent)
            this.overridePendingTransition(R.anim.slide_in_left_introduction_activity,R.anim.silde_out_right_introduction_activity)
        }
    }
}