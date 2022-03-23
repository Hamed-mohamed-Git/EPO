package com.example.peodemo.home.introduction

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.HomeFragment
import com.example.peodemo.home.introduction.fragments.InformationFragment
import com.example.peodemo.home.introduction.fragments.ourCourses.ourCoursesFragment
import com.example.peodemo.home.introduction.fragments.teachOnEPOFragment
import com.example.peodemo.logPages.signInActivity
import kotlinx.android.synthetic.main.activity_introduction.*
import java.util.*

open class introductionActivity : AppCompatActivity(), tapViewModel.OnItemClickListener {

    //assigning a tapList array property to store the recycle view in it
    private val  tapList = ArrayList<tapModel>()
    //assigning a adapter property to store the tapView instance in ti
    private val adapter = tapViewModel(tapList,this)
    //assigning a mHomeFragment to get a HomeFragment instance to access this fragment
    private val mHomeFragment = HomeFragment()
    //assigning a mInformationFragment to get a InformationFragment instance to access this fragment
    private val mInformationFragment = InformationFragment()
    //assigning a mTeachOnEPOFragment to get a TeachOnEPOFragment instance to access this fragment
    private val mTeachOnEPOFragment = teachOnEPOFragment()
    //assigning a mOurCoursesFragment to get a OurCoursesFragment instance to access this fragment
    private val mOurCoursesFragment = ourCoursesFragment()


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)

        //make a condition if the phone sdk above 15 or not
        //if matches
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //assigning this property to context the activity on it
            val window = this.window
            //this line to change the state bar by using statusBarColor
            window?.statusBarColor = this.resources.getColor(R.color.barColor)
            //addition the Home item to tapList array to access it in adapter class
            tapList.add(tapModel("Home",R.drawable.home,R.drawable.homepressed,this.getColor(R.color.white),this.getColor(R.color.recycleViewCardPressedColor),1))
            //addition the Information item to tapList array to access it in adapter class
            tapList.add(tapModel("Information",R.drawable.info,R.drawable.infopressed,this.getColor(R.color.white),this.getColor(R.color.recycleViewCardPressedColor),0))
            //addition the Teach on EPO item to tapList array to access it in adapter class
            tapList.add(tapModel("Teach on EPO",R.drawable.edit,R.drawable.editpressed,this.getColor(R.color.white),this.getColor(R.color.recycleViewCardPressedColor),0))
            //addition the Our Courses item to tapList array to access it in adapter class
            tapList.add(tapModel("Our Courses",R.drawable.cap,R.drawable.cappressed,this.getColor(R.color.white),this.getColor(R.color.recycleViewCardPressedColor),0))
        }

        //set a home fragment as a init fragment if the activity is created
        setFragment(mHomeFragment)
        //control the recycle view to by horizontal
        fragmentMarksButton.layoutManager = LinearLayoutManager(this,LinearLayout.HORIZONTAL,false)
        //set an adapter to this recycle view
        fragmentMarksButton.adapter = adapter

        //make an action if the user tap on signIn button take they to SignIn Activity
        signInIntroductionButton.setOnClickListener {
            val intent = Intent(this,signInActivity::class.java)
            startActivity(intent)
        }

    }

    //declare a private SetFragment method to control in the fragments when the user press on the items in recycle view
    private fun setFragment(fragment: Fragment) {
        //declare an instance from beginTransaction class to call the method in it and store it in fr property
        val fr = supportFragmentManager.beginTransaction()
        //calling the replace method to trans the fragments
        fr.replace(R.id.introductionCoordinatorLayoutNestedScrollView,fragment)
        //and commit it
        fr.commit()

    }

    //calling a onItemClick that we crated in tapViewModel class to make actions if the user tap on any item
    override fun onItemClick(position: Int) {
        //use a conditional loop to access each item by index
        when (position){
            // if the user tap on the first item
            0 ->{
                //call this item and put it in tap property
                var taps = tapList[position]
                //and set a last var in this item to 1 if the user tap on this item
                taps.pressed = 1
                //And set 0 to other items
                tapList[1].pressed = 0
                tapList[2].pressed = 0
                tapList[3].pressed = 0
                //And change the fragment
                setFragment(mHomeFragment)
                //And change the item in the tap array
                adapter.notifyItemChanged(position)
                adapter.notifyItemChanged(1)
                adapter.notifyItemChanged(2)
                adapter.notifyItemChanged(3)

            }
            // if the user tap on the second item
            1 ->{
                //call this item and put it in tap property
                var taps = tapList[position]
                //and set a last var in this item to 1, if the user tap on this item
                taps.pressed = 1
                //And set 0 to other items
                tapList[0].pressed = 0
                tapList[2].pressed = 0
                tapList[3].pressed = 0
                //And change the fragment
                setFragment(mInformationFragment)
                //And change the item in the tap array
                adapter.notifyItemChanged(position)
                adapter.notifyItemChanged(0)
                adapter.notifyItemChanged(2)
                adapter.notifyItemChanged(3)

            }
            // if the user tap on the third item
            2 ->{
                //call this item and put it in tap property
                var taps = tapList[position]
                //and set a last var in this item to 1, if the user tap on this item
                taps.pressed = 1
                //And set 0 to other items
                tapList[0].pressed = 0
                tapList[1].pressed = 0
                tapList[3].pressed = 0
                //And change the fragment
                setFragment(mTeachOnEPOFragment)
                //And change the item in the tap array
                adapter.notifyItemChanged(position)
                adapter.notifyItemChanged(0)
                adapter.notifyItemChanged(1)
                adapter.notifyItemChanged(3)
            }
            // if the user tap on the last item
            3 ->{
                //call this item and put it in tap property
                var taps = tapList[position]
                //and set a last var in this item to 1, if the user tap on this item
                taps.pressed = 1
                //And set 0 to other items
                tapList[0].pressed = 0
                tapList[1].pressed = 0
                tapList[2].pressed = 0
                //And change the fragment
                setFragment(mOurCoursesFragment)
                //And change the item in the tap array
                adapter.notifyItemChanged(position)
                adapter.notifyItemChanged(0)
                adapter.notifyItemChanged(1)
                adapter.notifyItemChanged(2)
            }
        }
    }

}