package com.example.peodemo.welcomeFragments

import android.animation.Animator
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.peodemo.R


class First_welcome : Fragment() {
    //create a member of nav control to navigate the fragments
    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this line to find the nav host that be in main activity
        mNavController = findNavController()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //assigning this property to context the activity on it
        val window = this.activity?.window
        //this line to change the state bar by using statusBarColor
        window?.statusBarColor = this.resources.getColor(R.color.white)


        //those lines to get the view in xml file by them id
        //to assigning the view this view is imageview called first welcome button
        val nextButton = view.findViewById<ImageView>(R.id.firstWelcomeButton)
        //assigning a text1 property to control the textview 1  in fragment xml file
        val text1 = view.findViewById<TextView>(R.id.textView)
        //assigning a text2 property to control the textview 2  in fragment xml file
        val text2 = view.findViewById<TextView>(R.id.textView2)
        //assigning a minute property to control the image(a minute image) in fragment xml file
        val minute = view.findViewById<ImageView>(R.id.imageView8)

        //here use the animation methods to make our fragment looks good
        //this line change the next button position about 560 degree at 6 seconds
        nextButton.animate().translationX(560f).duration = 6300
        //this line change the text1 view position about 450 degree at 6 seconds
        text1.animate().translationX(450f).duration = 6300
        //this line change the text2 view  position about 435 degree at 6 seconds
        text2.animate().translationX(435f).duration = 6300
        //this line change the minute image position about 455 degree at 6 seconds
        minute.animate().translationX(455f).duration = 6300

        //this line to make an action when the user click on next button
        nextButton.setOnClickListener {
            //create an action to move to another fragment by save change
            val action = First_welcomeDirections.actionFirstWelcomeToSecondWelcome()
            //to add this action to navigate method to move to second fragment
            mNavController.navigate(action)
        }

    }

}

