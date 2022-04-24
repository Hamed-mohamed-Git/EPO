package com.example.peodemo.home.introduction.fragments.ourCourses.checkout

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.CoursesModel
import com.example.peodemo.logPages.RegisterActivitypage
import kotlinx.android.synthetic.main.activity_check_out.*

class CheckOutActivity : AppCompatActivity() {

    private lateinit var DataFromCourseDetailsFragment: CheckOutProcessingModel
    private lateinit var courseDetails: CoursesModel
    private var mainColorFromCourseDetails = 0

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)
        //assigning this property to context the activity on it
        val window = this.window
        //this line to change the state bar by using statusBarColor
        window?.statusBarColor = this.resources.getColor(R.color.introductionCourseDetailsStatusBarColor)
        //addition the Home item to tapList array to access it in adapter class
        window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.window.navigationBarColor = getColor(R.color.introductionCourseDetailsStatusBarColor)

        }
        CheckOutProcessBack.setOnClickListener {
            finish()
        }

        DataFromCourseDetailsFragment = intent.getSerializableExtra("ID") as CheckOutProcessingModel
        courseDetails = intent.getSerializableExtra("DataOfCourse") as CoursesModel
        mainColorFromCourseDetails = intent.extras!!.getInt("MainColor")
        price.text = ((DataFromCourseDetailsFragment.price + 1).toInt()).toString()
        module.text = DataFromCourseDetailsFragment.modules.toString()
        hour.text = DataFromCourseDetailsFragment.hours.toString()
        resource.text = DataFromCourseDetailsFragment.resources.toString()
        firstPoint.background.setTint(mainColorFromCourseDetails)
        secondPoint.background.setTint(mainColorFromCourseDetails)
        thirdPoint.background.setTint(mainColorFromCourseDetails)
        fourthPoint.background.setTint(mainColorFromCourseDetails)
        fifthPoint.background.setTint(mainColorFromCourseDetails)
        sixthPoint.background.setTint(mainColorFromCourseDetails)
        billedSwitch.trackDrawable.setTint(mainColorFromCourseDetails)
        frameStartMyFreeTrailLayout.background.setTint(mainColorFromCourseDetails)
        billedMonthly.setTextColor(Color.parseColor("#FF000000"))
        billedSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                billedAnnually.setTextColor(Color.parseColor("#FF000000"))
                billedMonthly.setTextColor(Color.parseColor("#808080"))
                savingText.visibility = View.VISIBLE
                bracket.visibility = View.VISIBLE
                TheSavingAmount.visibility = View.VISIBLE
                var annuallyPrice = DataFromCourseDetailsFragment.price.toInt() + 1
                TheSavingAmount.text = ((annuallyPrice * 20 / 100)).toString()
                price.text = ((annuallyPrice * 80/100)).toString()
            }else if (!isChecked){
                billedAnnually.setTextColor(Color.parseColor("#808080"))
                billedMonthly.setTextColor(Color.parseColor("#FF000000"))
                savingText.visibility = View.GONE
                bracket.visibility = View.GONE
                TheSavingAmount.visibility = View.GONE
                price.text = ((DataFromCourseDetailsFragment.price + 1).toInt()).toString()
            }

        }

        frameStartMyFreeTrailLayout.setOnClickListener {
                val intent = Intent(this,RegisterActivitypage::class.java)
                intent.putExtra("CourseID",DataFromCourseDetailsFragment.courseID)
                intent.putExtra("DataOfCourse",courseDetails)
                startActivity(intent)
        }


    }
}