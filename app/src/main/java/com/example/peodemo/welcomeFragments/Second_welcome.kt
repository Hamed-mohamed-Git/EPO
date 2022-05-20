package com.example.peodemo.welcomeFragments

import android.graphics.Color
import android.os.Build
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
import javax.net.ssl.SSLEngineResult

class Second_welcome : Fragment() {

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
        return inflater.inflate(R.layout.fragment_second_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //assigning this property to context the activity on it
        val window = this.activity?.window
        //this line to change the state bar by using statusBarColor
        window?.statusBarColor = this.resources.getColor(R.color.secondFragmentStatusBarColor)


        //to assigning the view this view is imageview called first welcome button
        val nextButton = view.findViewById<ImageView>(R.id.secondWelcomeButton)

        //this line to make an action when the user click on next button
        nextButton.setOnClickListener {
            //create an action to move to another fragment by save change
            val action = Second_welcomeDirections.actionSecondWelcomeToThirdWelcome()
            //to add this action to navigate method to move to second fragment
            mNavController.navigate(action)
        }
    }

}