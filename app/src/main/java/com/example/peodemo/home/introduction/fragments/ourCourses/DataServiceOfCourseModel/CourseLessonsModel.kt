package com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel

import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseLessonQuizDetials.courseLessonQuizDetailsModel
import java.io.Serializable


data class CourseLessonsModel(
    val id:String?,
    val name:String?,
    val number:Int?,
    var percentage:Float?,
    var finished:Boolean?,
    val lessonDetails:courseLessonDetailsModel?,
    val lessonQUIZDetails:courseLessonQuizDetailsModel?,
    val lessonChallengeDetails:courseLessonChellengeDetailsModel?,
    var description:ArrayList<String>?
): Serializable{
    constructor():this(null,null,null,null,null,null,null,null,null)
}