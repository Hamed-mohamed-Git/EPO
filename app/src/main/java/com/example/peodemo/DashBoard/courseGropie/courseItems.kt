package com.example.peodemo.DashBoard.courseGropie

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.core.view.marginStart
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.CoursesModel
import com.google.android.exoplayer2.util.ColorParser
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.main_dashboard_course_card_item.*


class courseItems(var courseInfo: CoursesModel, val context:Context):Item() {

    @SuppressLint("SetTextI18n")
    override fun bind(viewHolder: ViewHolder, position: Int) {
        if (courseInfo.id == null){
            viewHolder.mainDashboardCourseImage.visibility = View.GONE
            viewHolder.mainDashboardCourseName.visibility = View.GONE
            viewHolder.mainDashboardCourseNameNew.visibility = View.VISIBLE
            viewHolder.mainDashboardCourseModelsFinished.visibility = View.GONE
            viewHolder.mainDashboardCourseGifImage.visibility = View.VISIBLE
            viewHolder.mainDashboardCourseGifImage.setImageResource(courseInfo.image!!)
        }else{
            viewHolder.mainDashboardCourseImage.setImageResource(courseInfo.image!!)
            viewHolder.mainDashboardCourseName.text = courseInfo.name
            viewHolder.mainDashboardCourseModelsFinished.text = "${courseInfo.percentage!!.toInt()}/${courseInfo.modulesCount} Modules"
        }

    }

    override fun getLayout(): Int {
        return R.layout.main_dashboard_course_card_item
    }
}