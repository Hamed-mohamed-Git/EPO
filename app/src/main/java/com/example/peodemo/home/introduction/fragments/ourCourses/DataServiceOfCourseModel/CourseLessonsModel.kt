package com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel

import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseChallengeDetails.courseLessonChellengeDetailsModel
import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseLessonQuizDetials.courseLessonQuizDetailsModel
import java.io.Serializable


data class CourseLessonsModel(
    val id:String?,
    val name:String?,
    val number:Int?,
    val minutes:Int?,
    val videosCount:Int?,
    val quizCount:Int?,
    val assignmentCount:Int?,
    var finishedCount:Int?,
    var finishCount:Int?,
    var videoFinishedCount:Int?,
    var quizFinishedCount:Int?,
    var challengeFinishedCount:Int?,
    var resourceFinishedCount:Int?,
    var finished:Boolean?,
    var Process:Boolean?,
    var enabled:Boolean?,
    val lessonDetails:courseLessonDetailsModel?,
    val lessonQUIZDetails:courseLessonQuizDetailsModel?,
    val lessonChallengeDetails: courseLessonChellengeDetailsModel?,
    val lessonResourceDetails:courseLessonResourceDetails?,
    var description:String?
): Serializable{
    constructor():this(null,null,null,null,null,null,null,null,null,null,null,null,
        null,null,null,null,null,
        null,null,null,null)
}