package com.example.peodemo.home.introduction.fragments.ourCourses.checkout

import java.io.Serializable

class CheckOutProcessingModel(
    val courseID:String,
    val price:Float,
    val hours:Int,
    val resources:Int,
    val modules:Int
) : Serializable