package com.example.peodemo.DashBoard.fragmentPages.courses

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.peodemo.R
import kotlinx.android.synthetic.main.fragment_dash_board_course.*
import java.util.ArrayList


class dashBoardCourseFragment : Fragment(), viewModel.OnItemClickListener {
    //assigning a tapList array property to store the recycle view in it
    private val  tapList = ArrayList<model>()
    //assigning a adapter property to store the tapView instance in ti
    private val adapter = viewModel(tapList,this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dash_board_course, container, false)
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tapList.add(model("All",R.drawable.allpressed,R.drawable.all, requireActivity().getColor(R.color.dashboardAllTapCourses),requireActivity().getColor(R.color.dashboardAllTapCoursesPressed),1,requireActivity().getColor(R.color.dashboardAllTapCoursesPressed),requireActivity().getColor(R.color.dashboardAllTapCourses)))
            tapList.add(model("Completed",R.drawable.completepressed,R.drawable.complete, requireActivity().getColor(R.color.dashboardCompleteTapCourses),requireActivity().getColor(R.color.dashboardAllTapCoursesPressed),0,requireActivity().getColor(R.color.dashboardCompleteTapCoursesPressed),requireActivity().getColor(R.color.dashboardCompleteTapCourses)))
            tapList.add(model("On going",R.drawable.activepressed,R.drawable.active, requireActivity().getColor(R.color.dashboardOngoingTapCourses),requireActivity().getColor(R.color.dashboardAllTapCoursesPressed),0,requireActivity().getColor(R.color.dashboardOngoingTapCoursesPressed),requireActivity().getColor(R.color.dashboardOngoingTapCourses)))
            tapList.add(model("New",R.drawable.newtap,R.drawable.newpressed, requireActivity().getColor(R.color.dashboardNewTapCourses),requireActivity().getColor(R.color.dashboardAllTapCoursesPressed),0,requireActivity().getColor(R.color.dashboardOngoingTapCoursesPressed),requireActivity().getColor(R.color.dashboardNewTapCourses)))
        }


        tabsCoursesDashboardRE.layoutManager = LinearLayoutManager(this.activity, LinearLayout.HORIZONTAL,false)
        tabsCoursesDashboardRE.adapter = adapter

    }

    override fun onItemClick(position: Int) {
        when (position){
            // if the user tap on the first item
            0 ->{
                //call this item and put it in tap property
                var taps = tapList[position]
                taps.pressed = 1
                //And set 0 to other items
                tapList[1].pressed = 0
                tapList[2].pressed = 0
                tapList[3].pressed = 0
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
                //And change the item in the tap array
                adapter.notifyItemChanged(position)
                adapter.notifyItemChanged(0)
                adapter.notifyItemChanged(1)
                adapter.notifyItemChanged(3)

            }
            // if the user tap on the third item
            3 ->{
                //call this item and put it in tap property
                var taps = tapList[position]
                //and set a last var in this item to 1, if the user tap on this item
                taps.pressed = 1
                //And set 0 to other items
                tapList[0].pressed = 0
                tapList[1].pressed = 0
                tapList[2].pressed = 0
                //And change the item in the tap array
                adapter.notifyItemChanged(position)
                adapter.notifyItemChanged(0)
                adapter.notifyItemChanged(1)
                adapter.notifyItemChanged(2)

            }

        }
    }

    override fun onPause() {
        super.onPause()
        tapList.clear()
    }



}