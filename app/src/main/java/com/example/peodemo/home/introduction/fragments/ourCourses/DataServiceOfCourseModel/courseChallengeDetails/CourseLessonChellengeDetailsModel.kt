package com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.courseChallengeDetails

import java.io.Serializable

class courseLessonChellengeDetailsModel(
    val name:String?,
    val challengeInfo:challengeInformation?,
    var finished:Boolean?
): Serializable{
    constructor() : this(null,null,null)
}