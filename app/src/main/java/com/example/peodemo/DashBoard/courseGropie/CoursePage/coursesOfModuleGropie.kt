package com.example.peodemo.DashBoard.courseGropie.CoursePage

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.CourseLessonsModel
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_course_modules_page.*
import kotlinx.android.synthetic.main.course_lessons_card_view.*

class coursesOfModuleGropie(var lessonInfo: CourseLessonsModel, val context: Context, val index:Int):Item() {
    @SuppressLint("SetTextI18n")
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.courseNumber.text = index.toString()
        viewHolder.lessonName.text = lessonInfo.name
        viewHolder.lessonVideoCount.text = "${lessonInfo.videosCount} Videos"
        viewHolder.lessonQuizCount.text = "${lessonInfo.quizCount} Quiz"
        viewHolder.lessonAssignmentsCount.text = "${lessonInfo.assignmentCount} Assignments"
        if (lessonInfo.enabled!!){
            viewHolder.lessonLock.visibility = View.GONE
        }else{
            viewHolder.lessonLock.visibility = View.VISIBLE
        }
        if (lessonInfo.Process!!){
            viewHolder.CourseInformationLayout.visibility = View.GONE
            viewHolder.lessonPageProgressBar.visibility = View.VISIBLE
            viewHolder.lessonPageProgressBar.max = lessonInfo.finishCount!!
            viewHolder.lessonPageProgressBar.setProgress(lessonInfo.finishedCount!!)
        }else{
            viewHolder.CourseInformationLayout.visibility = View.VISIBLE
            viewHolder.lessonPageProgressBar.visibility = View.GONE
        }
    }

    override fun getLayout(): Int {
        return R.layout.course_lessons_card_view
    }
}