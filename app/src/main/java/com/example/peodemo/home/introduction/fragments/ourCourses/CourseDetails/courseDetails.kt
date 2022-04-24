package com.example.peodemo.home.introduction.fragments.ourCourses.CourseDetails

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.CoursesModel
import com.example.peodemo.home.introduction.fragments.ourCourses.checkout.CheckOutActivity
import kotlinx.android.synthetic.main.activity_course_details.*

class courseDetails : AppCompatActivity() {

    private lateinit var DataFromOurCorsesFragment:courseDetailModel
    private lateinit var courseDetails:CoursesModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_details)

        DataFromOurCorsesFragment = intent.getSerializableExtra("data") as courseDetailModel
        courseDetailsTeacherName.text = DataFromOurCorsesFragment.teacher
        courseDetailsCourseName.text = DataFromOurCorsesFragment.name
        courseDetailsLessenCount.text = DataFromOurCorsesFragment.amongOfVideos
        courseDetailsCourseImage.setImageResource(DataFromOurCorsesFragment.Image)
        courseDetailsPrice.text = DataFromOurCorsesFragment.price
        priceFrameLayout.background.setTint(DataFromOurCorsesFragment.mainColor)
        peopleCourseDetailsFrameLayout.background.setTint(DataFromOurCorsesFragment.mainColor)
        peopleCourseDetails.background.setTint(DataFromOurCorsesFragment.subColor)

        starCourseDetailsFrameLayout.background.setTint(DataFromOurCorsesFragment.mainColor)
        starCourseDetails.background.setTint(DataFromOurCorsesFragment.subColor)

        likeCourseDetailsFrameLayout.background.setTint(DataFromOurCorsesFragment.mainColor)
        likeCourseDetails.background.setTint(DataFromOurCorsesFragment.subColor)

        coursePlayFrameLayout.background.setTint(DataFromOurCorsesFragment.mainColor)
        CourseDetailsPlayButton.background.setTint(DataFromOurCorsesFragment.subColor)

        VideoTime.text = DataFromOurCorsesFragment.videoTime
        VideoTime.setTextColor(DataFromOurCorsesFragment.mainColor)
        VideoTimeMinutes.setTextColor(DataFromOurCorsesFragment.mainColor)

        courseIncluded.background.setTint(DataFromOurCorsesFragment.mainColor)
        courseIncluded.setTextColor(DataFromOurCorsesFragment.subColor)

        description.background.setTint(DataFromOurCorsesFragment.subColor)
        description.setTextColor(DataFromOurCorsesFragment.mainColor)

        if (DataFromOurCorsesFragment.firstSectionOfDescription == null){
            FirstParagraph.visibility = View.GONE
        }else{
            FirstParagraph.text = DataFromOurCorsesFragment.firstSectionOfDescription
        }

        if (DataFromOurCorsesFragment.secondSectionOfDescription == null){
            SecondParagraph.visibility = View.GONE
        }else{
            SecondParagraph.text = DataFromOurCorsesFragment.secondSectionOfDescription
        }

        if (DataFromOurCorsesFragment.thirdSectionOfDescription == null){
            ThirdParagraph.visibility = View.GONE
        }else{
            ThirdParagraph.text = DataFromOurCorsesFragment.thirdSectionOfDescription
        }

        if (DataFromOurCorsesFragment.fourthSectionOfDescription == null){
            FourthParagraph.visibility = View.GONE
        }else{
            FourthParagraph.text = DataFromOurCorsesFragment.fourthSectionOfDescription
        }


        checkoutButton.background.setTint(DataFromOurCorsesFragment.mainColor)
        hello.setTextColor(DataFromOurCorsesFragment.mainColor)




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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.window.navigationBarColor = getColor(R.color.introductionCourseDetailsStatusBarColor)
        }
        description.setOnClickListener {
            courseIncludedLayout.visibility = View.GONE
            descriptionLayout.visibility = View.VISIBLE

        }
        IntroductionVideoButton.setOnClickListener {
            val intent = Intent(this,IntroductionVideo::class.java)
            intent.putExtra("url",DataFromOurCorsesFragment.VideoURI)
            startActivity(intent)
        }

        back.setOnClickListener {
            finish()
        }

        checkoutButton.setOnClickListener {
            val intent = Intent(this, CheckOutActivity::class.java)
            intent.putExtra("ID",DataFromOurCorsesFragment.checkoutDetails)
            intent.putExtra("MainColor",DataFromOurCorsesFragment.mainColor)
            intent.putExtra("DataOfCourse",DataFromOurCorsesFragment.CourseDetails)
            startActivity(intent)
        }

    }
}