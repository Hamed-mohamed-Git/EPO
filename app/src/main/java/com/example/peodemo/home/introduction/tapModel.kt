 package com.example.peodemo.home.introduction



//declare a data tapModel Class to create a type to put in array to make control taps recycle view in introduction activity
data class tapModel(
    //assigning a tapName parameter to store the text on card view
    var tapName :String,
    //assigning a image parameter to store the image beside the text
    var image:Int,
    //assigning a colorPressed parameter to store the color in card view if it's pressed
    var colorBackground:Int,
    //assigning a color parameter to store the color in card view
    var color:Int,
    //assigning a pressed parameter to store a value if one make the recycle view change
    var pressed:Int)
