package com.example.peodemo.DashBoard.courseGropie

import android.annotation.SuppressLint
import android.content.Context
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.CoursesModel
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.main_dashboard_course_card_item.*


class courseItems(var courseInfo: CoursesModel, val context:Context):Item() {

    @SuppressLint("SetTextI18n")
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.mainDashboardCourseImage.setImageResource(courseInfo.image!!)
        viewHolder.mainDashboardCourseName.text = courseInfo.name
        viewHolder.mainDashboardCourseModelsFinished.text = "${courseInfo.percentage!!.toInt()}/${courseInfo.modulesCount} Modules"
        viewHolder.mainDashboardCourseFrameLayout.setOnClickListener {

        }
    }

    override fun getLayout(): Int {
        return R.layout.main_dashboard_course_card_item
    }
}