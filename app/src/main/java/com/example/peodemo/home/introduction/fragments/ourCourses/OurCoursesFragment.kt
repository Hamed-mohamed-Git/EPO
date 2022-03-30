package com.example.peodemo.home.introduction.fragments.ourCourses

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.ourCourses.CourseDetails.courseDetails
import com.example.peodemo.home.introduction.fragments.ourCourses.categories.categoriesViewModel
import com.example.peodemo.home.introduction.fragments.ourCourses.categories.gategoriesModel

import com.example.peodemo.home.introduction.fragments.ourCourses.topCourses.topCoursesModel
import com.example.peodemo.home.introduction.fragments.ourCourses.topCourses.topCoursesViewModel
import kotlinx.android.synthetic.main.fragment_our_courses.*
import java.util.ArrayList


class ourCoursesFragment : Fragment(), categoriesViewModel.OnItemClickListener,
    topCoursesViewModel.OnItemClickListener {
    //assigning a tapList array property to store the recycle view in it
    private val  categoryTabList = ArrayList<gategoriesModel>()
    //assigning a adapter property to store the tapView instance in ti
    private val adapter = categoriesViewModel(categoryTabList,this)

    //assigning a tapList array property to store the recycle view in it
    private val  topCoursesTabList = ArrayList<topCoursesModel>()
    //assigning a adapter property to store the tapView instance in ti
    private val topCoursesAdapter = topCoursesViewModel(topCoursesTabList,this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_our_courses, container, false)

    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryTabList.add(gategoriesModel("Ios Development",R.drawable.ios,1))
        categoryTabList.add(gategoriesModel("Android Development",R.drawable.androiddevelopment,0))
        categoryTabList.add(gategoriesModel("Data Structure & Algorithm",R.drawable.datastructure,0))

        categoriesRecycleViewCourseIntroduction.layoutManager = LinearLayoutManager(this.activity, LinearLayout.HORIZONTAL,false)
        categoriesRecycleViewCourseIntroduction.adapter = adapter


        addCourses("Foundation","199.99","60",R.drawable.foundation)
        addCourses("Design app","49.99","15",R.drawable.iosdesigingcourse)
        addCourses("Database","29.99","12",R.drawable.data)
        TopCoursesRecycleViewCourseIntroduction.layoutManager = LinearLayoutManager(this.activity, LinearLayout.HORIZONTAL,false)
        TopCoursesRecycleViewCourseIntroduction.adapter = topCoursesAdapter



    }

    private fun addCourses(name: String,price:String, count: String, image: Int) {
        topCoursesTabList.add(topCoursesModel(name,price,count,image))

    }

    @SuppressLint("WrongConstant")
    override fun onItemClick(position: Int) {
        when (position){
            // if the user tap on the first item
            0 ->{
                //call this item and put it in tap property
                var taps = categoryTabList[position]
                taps.pressed = 1
                //And set 0 to other items
                categoryTabList[1].pressed = 0
                categoryTabList[2].pressed = 0
                adapter.notifyItemChanged(position)
                adapter.notifyItemChanged(1)
                adapter.notifyItemChanged(2)
                topCoursesTabList.clear()
                addCourses("Foundation","199.99","60",R.drawable.foundation)
                addCourses("Design app","49.99","15",R.drawable.iosdesigingcourse)
                addCourses("Database","29.99","12",R.drawable.data)
                TopCoursesRecycleViewCourseIntroduction.layoutManager = LinearLayoutManager(this.activity, LinearLayout.HORIZONTAL,false)
                TopCoursesRecycleViewCourseIntroduction.adapter = topCoursesAdapter
            }
            // if the user tap on the second item
            1 ->{
                //call this item and put it in tap property
                var taps = categoryTabList[position]
                //and set a last var in this item to 1, if the user tap on this item
                taps.pressed = 1
                //And set 0 to other items
                categoryTabList[0].pressed = 0
                categoryTabList[2].pressed = 0
                adapter.notifyItemChanged(position)
                adapter.notifyItemChanged(0)
                adapter.notifyItemChanged(2)
                topCoursesTabList.clear()
                addCourses("Foundation","299.99","180",R.drawable.androidfoundationcourse)
                addCourses("Design","149.99","25",R.drawable.androidxmlcourse)
                addCourses("Android JC ","399.99","130",R.drawable.androiddesigncourse)
                addCourses("Pattern","99.99","40",R.drawable.androidpatterncourse)
                TopCoursesRecycleViewCourseIntroduction.layoutManager = LinearLayoutManager(this.activity, LinearLayout.HORIZONTAL,false)
                TopCoursesRecycleViewCourseIntroduction.adapter = topCoursesAdapter


            }
            // if the user tap on the third item
            2 ->{
                //call this item and put it in tap property
                var taps = categoryTabList[position]
                //and set a last var in this item to 1, if the user tap on this item
                taps.pressed = 1
                //And set 0 to other items
                categoryTabList[0].pressed = 0
                categoryTabList[1].pressed = 0
                //And change the item in the tap array
                adapter.notifyItemChanged(position)
                adapter.notifyItemChanged(0)
                adapter.notifyItemChanged(1)
                topCoursesTabList.clear()
                addCourses("Part 1","99.99","35",R.drawable.datastructurepart1course)
                addCourses("Part 2","199.99","10",R.drawable.datastructurepart2course)
                addCourses("Algorithms","149.99","32",R.drawable.algorithmscourse)
                TopCoursesRecycleViewCourseIntroduction.layoutManager = LinearLayoutManager(this.activity, LinearLayout.HORIZONTAL,false)
                TopCoursesRecycleViewCourseIntroduction.adapter = topCoursesAdapter

            }
        }
    }

    override fun onPause() {
        super.onPause()
        categoryTabList.clear()
        topCoursesTabList.clear()
    }

    override fun onCoursesItemClick(position: Int) {
        when(position){
            0 -> {
                val intent = Intent(this.activity, courseDetails::class.java)
                startActivity(intent)
            }
        }
    }


}