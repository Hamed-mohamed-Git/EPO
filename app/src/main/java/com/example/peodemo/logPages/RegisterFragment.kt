package com.example.peodemo.logPages

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.peodemo.R
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_register, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //assigning this property to context the activity on it
        val window = activity?.window
        //addition the Home item to tapList array to access it in adapter class
        window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        signIn.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToSignInActivity()
            mNavController.navigate(action)
        }
        register.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToSignUpActivity()
            mNavController.navigate(action)
        }
    }
}