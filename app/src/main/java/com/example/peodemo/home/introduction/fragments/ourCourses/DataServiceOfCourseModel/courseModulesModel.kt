package com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel

import java.io.Serializable

class courseModulesModel(
    val id:String,
    val name:String,
    val image:String,
    val lessonsCount:Int,
    val lessonDetails:ArrayList<CourseLessonsModel>,
    var finishedLessons:Int,
    var Finished:Boolean,
    var description:ArrayList<String>
): Serializable