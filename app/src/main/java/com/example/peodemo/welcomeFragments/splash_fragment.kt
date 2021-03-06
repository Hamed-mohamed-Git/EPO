package com.example.peodemo.welcomeFragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.peodemo.DashBoard.mainDashBoardActivity
import com.example.peodemo.R


class splash_fragment : Fragment() {

    //create a member of nav control to navigate the fragments
    private lateinit var mNavController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //create a splash fragment by post delayed to launch this layout at a time (2 seconds)
        Handler().postDelayed(
            {
                //create a condition to ask onBoardingFinished if the user visited welcome fragments or not
                //if matches move on introduction activity
                if(onBoardingFinished() && !onSignUpFinished())
                {
                    //create an action to move to another Activity called introduction Activity by save change
                    val action = splash_fragmentDirections.actionSplashFragmentToIntroductionActivity()
                    //create a extras property to add flags if the user click on return system button close the app
                    val extras = ActivityNavigator.Extras.Builder()
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .build()
                    //add this action to navigate method to move to introduction Activity
                    findNavController().navigate(action,extras)
                }else if (onSignUpFinished()){
                    //create an action to move to another Activity called introduction Activity by save change
                    val intent = Intent(this.activity,mainDashBoardActivity::class.java)
                    //create a extras property to add flags if the user click on return system button close the app
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra("anotherCourse","")
                    startActivity(intent)
                    activity?.overridePendingTransition(R.anim.slide_in_left_introduction_activity,R.anim.silde_out_right_introduction_activity)
                }
                //if does not move on welcome fragments
                else
                {
                    //create an action to move to another Activity called welcome fragment by save change
                    val action = splash_fragmentDirections.actionSplashFragmentToFirstWelcome()
                    //add this action to navigate method to move to welcome fragment
                    findNavController().navigate(action)
                }

                //set a duration show
            },2000
        )
    }

    //create this method to get the value that returned from get started fragment to make the condition above there
    private fun onBoardingFinished(): Boolean{
        //create a shared pref to set a value that returned
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        //if the user does not click on that button yet return a default value and return it
        return sharedPref.getBoolean("Finished", false)
    }
    //create this method to get the value that returned from get started fragment to make the condition above there
    private fun onSignUpFinished(): Boolean{
        //create a shared pref to set a value that returned
        val sharedPref = requireActivity().getSharedPreferences("onSignUpBoarding", Context.MODE_PRIVATE)
        //if the user does not click on that button yet return a default value and return it
        return sharedPref.getBoolean("signProcessFinished", false)
    }

}