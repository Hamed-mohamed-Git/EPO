package com.example.peodemo.home.introduction.fragments.ourCourses.CourseDetails

import java.io.Serializable

class courseDetailModel(
    val name:String,
    val teacher:String,
    val price:String,
    val VideoURI:String,
    val videoTime:String,
    val amongOfVideos: String,
    val firstSectionOfDescription:String?,
    val secondSectionOfDescription:String?,
    val thirdSectionOfDescription:String?,
    val fourthSectionOfDescription:String?,
    val mainColor:Int,
    val subColor:Int,
    val Image:Int
    ):Serializable
