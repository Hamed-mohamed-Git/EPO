package com.example.peodemo.home.introduction.fragments.ourCourses.topCourses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.peodemo.R
import java.util.ArrayList

class topCoursesViewModel(
    //assigning a item arrayList parameter that type i declared before
    var items: ArrayList<topCoursesModel>,
    //assigning a listener parameter to create
    // a onClickListener method to make an action each item in recycle view if user press on card view
    private val listener: topCoursesViewModel.OnItemClickListener
)
//inherit the Recycle view class
    : RecyclerView.Adapter<topCoursesViewModel.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //assigning a itemCard property that return view by using inflate methods to access each item on view card layout
        val itemCard = LayoutInflater.from(parent.context)
            .inflate(R.layout.top_courses_card_item, parent, false)
        //and return a view value to use it in the ViewHolder class
        return ViewHolder(itemCard)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //assigning a tap property to set an item in the array to set it to view in card view
        var taps = items[position]
        //if a user tap on the card view change the items on it
        holder.name.text = taps.courseName
        holder.Image.setImageResource(taps.image)
        holder.lesssonAmount.text = taps.lessonCount
        holder.price.text = taps.price
    }

    override fun getItemCount(): Int {
        //return the size of array to access the position
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val Image = itemView.findViewById<ImageView>(R.id.topCoursesImage)
        val price = itemView.findViewById<TextView>(R.id.topCoursesHowMuchCosts)
        val name = itemView.findViewById<TextView>(R.id.topCoursesText)
        val lesssonAmount = itemView.findViewById<TextView>(R.id.topCoursesHowManyCourses)

        init {
            //and put context in it
            itemView.setOnClickListener(this)
        }


        override fun onClick(v: View?) {
            //assigning a position property to access each item
            val position = adapterPosition
            //declare a condition if the recycle view not empty
            if (position != RecyclerView.NO_POSITION) {
                //set this position to on item click listener method in OnItemClickListener interface to access each item alone
                listener.onCoursesItemClick(position)
            }
        }


    }

    //declare a OnItemClickListener interface
    interface OnItemClickListener {
        //declare OnItemClick method
        fun onCoursesItemClick(position: Int)
    }
}