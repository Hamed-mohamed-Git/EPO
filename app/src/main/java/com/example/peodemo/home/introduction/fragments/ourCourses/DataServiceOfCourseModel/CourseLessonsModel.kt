package com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel

import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseLessonQuizDetials.courseLessonQuizDetailsModel
import java.io.Serializable


data class CourseLessonsModel(
    val id:String,
    val name:String,
    val number:Int,
    var percentage:Float,
    var finished:Boolean,
    val lessonDetails:ArrayList<courseLessonDetailsModel>,
    val lessonQUIZDetails:ArrayList<courseLessonQuizDetailsModel>,
    val lessonChallengeDetails:ArrayList<courseLessonChellengeDetailsModel>,
    var description:ArrayList<String>
): Serializable