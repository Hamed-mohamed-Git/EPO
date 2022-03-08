package com.example.peodemo.DashBoard

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.peodemo.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main_dash_board2.*

class mainDashBoardActivity : AppCompatActivity() {
    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_dash_board2)
        //assigning this property to context the activity on it
        val window = this.window
        //this line to change the state bar by using statusBarColor
        window?.statusBarColor = this.resources.getColor(R.color.barColor)
        //window?.decorView!!.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR


        dashBoardCourse.setOnClickListener {
            homeFrameLayout.visibility = View.GONE
            progressFrameLayout.visibility = View.GONE
            profileFrameLayout.visibility = View.GONE
            courseFrameLayout.visibility = View.VISIBLE
            dashBoardHome.setImageResource(R.drawable.home)
            dashBoardProgress.setImageResource(R.drawable.progress)
            dashBoardProfile.setImageResource(R.drawable.profile)
            dashBoardCourse.setImageResource(R.drawable.coursepressed)
        }
        dashBoardHome.setOnClickListener {
            courseFrameLayout.visibility = View.GONE
            progressFrameLayout.visibility = View.GONE
            profileFrameLayout.visibility = View.GONE
            homeFrameLayout.visibility = View.VISIBLE
            dashBoardHome.setImageResource(R.drawable.homepressed)
            dashBoardProgress.setImageResource(R.drawable.progress)
            dashBoardProfile.setImageResource(R.drawable.profile)
            dashBoardCourse.setImageResource(R.drawable.course)
        }
        dashBoardProgress.setOnClickListener {
            homeFrameLayout.visibility = View.GONE
            progressFrameLayout.visibility = View.VISIBLE
            profileFrameLayout.visibility = View.GONE
            courseFrameLayout.visibility = View.GONE
            dashBoardHome.setImageResource(R.drawable.home)
            dashBoardProgress.setImageResource(R.drawable.progresspressed)
            dashBoardProfile.setImageResource(R.drawable.profile)
            dashBoardCourse.setImageResource(R.drawable.course)
        }
        dashBoardProfile.setOnClickListener {
            homeFrameLayout.visibility = View.GONE
            progressFrameLayout.visibility = View.GONE
            profileFrameLayout.visibility = View.VISIBLE
            courseFrameLayout.visibility = View.GONE
            dashBoardHome.setImageResource(R.drawable.home)
            dashBoardProgress.setImageResource(R.drawable.progress)
            dashBoardProfile.setImageResource(R.drawable.profilepressed)
            dashBoardCourse.setImageResource(R.drawable.course)
        }
    }




}