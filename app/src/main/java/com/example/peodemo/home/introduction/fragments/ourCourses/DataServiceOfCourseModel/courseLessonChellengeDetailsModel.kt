package com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel

import java.io.Serializable

class courseLessonChellengeDetailsModel(
    val name:String,
    val challengeDescription:ArrayList<String>?,
    val URI:String?,
    var finished:Boolean?
): Serializable{
    constructor() : this("",null,null,null)
}