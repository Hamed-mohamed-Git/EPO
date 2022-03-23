package com.example.peodemo.home.introduction.fragments.ourCourses

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.peodemo.DashBoard.fragmentPages.courses.model
import com.example.peodemo.DashBoard.fragmentPages.courses.viewModel
import com.example.peodemo.R
import com.example.peodemo.home.introduction.fragments.ourCourses.categories.categoriesViewModel
import com.example.peodemo.home.introduction.fragments.ourCourses.categories.gategoriesModel
import kotlinx.android.synthetic.main.fragment_dash_board_course.*
import kotlinx.android.synthetic.main.fragment_dash_board_course.tabsCoursesDashboardRE
import kotlinx.android.synthetic.main.fragment_our_courses.*
import java.util.ArrayList


class ourCoursesFragment : Fragment(), categoriesViewModel.OnItemClickListener {
    //assigning a tapList array property to store the recycle view in it
    private val  categoryTabList = ArrayList<gategoriesModel>()
    //assigning a adapter property to store the tapView instance in ti
    private val adapter = categoriesViewModel(categoryTabList,this)
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
    }

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

            }
        }
    }
    override fun onPause() {
        super.onPause()
        categoryTabList.clear()
    }


}