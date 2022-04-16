package com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseLessonQuizDetials

import java.io.Serializable

data class courseLessonQuizDetailsModel(
    var name:String,
    var Questions:ArrayList<QuestionsModel>,
    val correctID:Int
): Serializable
