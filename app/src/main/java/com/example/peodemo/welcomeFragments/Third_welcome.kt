package com.example.peodemo.welcomeFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.peodemo.R

class Third_welcome : Fragment() {

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
        return inflater.inflate(R.layout.fragment_third_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //assigning this property to context the activity on it
        val window = this.activity?.window
        //this line to change the state bar by using statusBarColor
        window?.statusBarColor = this.resources.getColor(R.color.white)

        //those lines to get the view in xml file by them id
        //assigning a nextButton property to control the imageview in the fragment xml file
        val nextButton = view.findViewById<ImageView>(R.id.thirdWelcomeButton)
        //assigning a text1 property to control the TextView1 in the fragment xml file
        val text1 = view.findViewById<TextView>(R.id.thirdWelcomeTextView1)
        //assigning a text2 property to control the TextView2 in the fragment xml file
        val text2 = view.findViewById<TextView>(R.id.thirdWelcomeTextView2)
        //assigning a minute property to control the imageview in the fragment xml file
        val minute = view.findViewById<ImageView>(R.id.minuteThirdFragment)


        //here use the animation methods to make our fragment looks good
        //this line change the next button position about 567 degree at 6 second
        nextButton.animate().translationX(567f).duration = 6300
        //this line change the text1 view position about 470 degree at 6 seconds
        text1.animate().translationX(570f).duration = 6300
        //this line change the text2 view  position about 489 degree at 6 seconds
        text2.animate().translationX(589f).duration = 6300
        //this line change the minute image position about 450 degree at 6 seconds
        minute.animate().translationX(450f).duration = 6300


        //this line to make an action when the user click on next button
        nextButton.setOnClickListener {
            //create an action to move to another fragment by save change
            val action = Third_welcomeDirections.actionThirdWelcomeToGetStartedFragment()
            //to add this action to navigate method to move to second fragment
            mNavController.navigate(action)
        }
    }
}