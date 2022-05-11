package com.example.peodemo.DashBoard.courseGropie.ModulesPage

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseModulesModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.course_modules_card_view.*

class modulesOfCourseGropie(var moduleInfo: courseModulesModel, val context: Context, val index:Int
):Item() {
    @SuppressLint("SetTextI18n")
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.module_name.text = moduleInfo.name
        viewHolder.module_Number.text = index.toString()
        viewHolder.module_hours.text = "${moduleInfo.lessonsCount.toString()} Lesson"
        viewHolder.module_Videos.text = "${moduleInfo.videosCount.toString()} Videos"
        viewHolder.module_Files.text = "${moduleInfo.tasksCount.toString()} Tasks"

        if (moduleInfo.Process!!){
            viewHolder.module_info.visibility = View.GONE
            viewHolder.module_progress_bar.visibility = View.VISIBLE
            viewHolder.module_progress_bar.max = moduleInfo.lessonsCount!!
            viewHolder.module_progress_bar.setProgress(moduleInfo.finishedLessons!!)

        }
        if (moduleInfo.enabled!!){
            viewHolder.lock_state.visibility = View.GONE
        }
    }

    override fun getLayout(): Int {
        return R.layout.course_modules_card_view
    }

}