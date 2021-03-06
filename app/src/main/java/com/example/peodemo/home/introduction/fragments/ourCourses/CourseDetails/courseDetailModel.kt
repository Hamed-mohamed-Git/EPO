package com.example.peodemo.home.introduction.fragments.ourCourses.CourseDetails

import com.example.peodemo.home.introduction.fragments.ourCourses.DataServiceOfCourseModel.CoursesModel
import com.example.peodemo.home.introduction.fragments.ourCourses.checkout.CheckOutProcessingModel
import java.io.Serializable

class courseDetailModel(
    val CourseDetails: CoursesModel?,
    val checkoutDetails: CheckOutProcessingModel,
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
