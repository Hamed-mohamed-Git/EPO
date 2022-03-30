package com.example.peodemo.home.introduction.fragments.ourCourses.CourseDetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.peodemo.R
import kotlinx.android.synthetic.main.activity_course_details.*

class courseDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_details)
        //assigning this property to context the activity on it
        val window = this.window
        //this line to change the state bar by using statusBarColor
        window?.statusBarColor = this.resources.getColor(R.color.introductionCourseDetailsStatusBarColor)
        //addition the Home item to tapList array to access it in adapter class
        window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        courseIncluded.setOnClickListener {
            courseIncludedLayout.visibility = View.VISIBLE
            descriptionLayout.visibility = View.GONE

        }
        description.setOnClickListener {
            courseIncludedLayout.visibility = View.GONE
            descriptionLayout.visibility = View.VISIBLE

        }
        IntroductionVideoButton.setOnClickListener {
            val intent = Intent(this,IntroductionVideo::class.java)
            startActivity(intent)
        }

    }
}