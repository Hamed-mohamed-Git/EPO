package com.example.peodemo.logPages

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.CoursesModel
import kotlinx.android.synthetic.main.activity_register_activitypage.*

class RegisterActivitypage : AppCompatActivity() {
    private var DataFromOurCourses = ""
    private lateinit var courseDetails: CoursesModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_activitypage)
        //assigning this property to context the activity on it

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = this.window
            //addition the Home item to tapList array to access it in adapter class
            window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.navigationBarColor = getColor(R.color.theSubIOSDesignCourse)
        }
        DataFromOurCourses = intent.getStringExtra("CourseID") as String
        courseDetails = intent.getSerializableExtra("DataOfCourse") as CoursesModel
        register.setOnClickListener {
            val intent = Intent(this,signUpActivity::class.java)
            intent.putExtra("CourseID",DataFromOurCourses)
            intent.putExtra("DataOfCourse",courseDetails)
            startActivity(intent)
        }

        signIn.setOnClickListener {
            val intent = Intent(this,signInActivity::class.java)
            intent.putExtra("CourseID",DataFromOurCourses)
            intent.putExtra("DataOfCourse",courseDetails)
            startActivity(intent)
        }
    }
}