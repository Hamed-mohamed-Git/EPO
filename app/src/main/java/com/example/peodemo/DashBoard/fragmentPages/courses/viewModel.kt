package com.example.peodemo.DashBoard.fragmentPages.courses

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.peodemo.R
import com.example.peodemo.home.introduction.tapViewModel
import java.util.ArrayList

class viewModel(
    //assigning a item arrayList parameter that type i declared before
    var items: ArrayList<model>,
    //assigning a listener parameter to create
    // a onClickListener method to make an action each item in recycle view if user press on card view
    private val listener: viewModel.OnItemClickListener
)
//inherit the Recycle view class
    : RecyclerView.Adapter<viewModel.ViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //assigning a itemCard property that return view by using inflate methods to access each item on view card layout
        val itemCard = LayoutInflater.from(parent.context).inflate(R.layout.dashboard_courses_tabs_cardview, parent,false)
        //and return a view value to use it in the ViewHolder class
        return ViewHolder(itemCard)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //assigning a tap property to set an item in the array to set it to view in card view
        var taps =  items[position]
        //if a user tap on the card view change the items on it
        if (taps.pressed == 1){
            //change the background

            holder.image.setImageResource(taps.imagePressed)
            holder.layout.background.setTint(taps.colorPressed)
            holder.text.setTextColor(taps.textColorPressed)
            holder.text.text = taps.name
        }
        //if not let the items as they are
        else if (taps.pressed == 0) {
            holder.image.setImageResource(taps.image)
            holder.layout.background.setTint(taps.color)
            holder.text.setTextColor(taps.textColor)
            holder.text.text = taps.name
        }
    }

    override fun getItemCount(): Int {
        //return the size of array to access the position
        return items.size
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val image  = itemView.findViewById<ImageView>(R.id.dashboardCourseImageView)
        val layout  = itemView.findViewById<FrameLayout>(R.id.dashboardCourseTabsLayout)
        val text = itemView.findViewById<TextView>(R.id.dashboardCourseTextView)
        init {
            //and put context in it
            itemView.setOnClickListener(this)
        }


        override fun onClick(v: View?) {
            //assigning a position property to access each item
            val  position = adapterPosition
            //declare a condition if the recycle view not empty
            if (position != RecyclerView.NO_POSITION ){
                //set this position to on item click listener method in OnItemClickListener interface to access each item alone
                listener.onItemClick(position)
            }
        }


    }
    //declare a OnItemClickListener interface
    interface OnItemClickListener {
        //declare OnItemClick method
        fun onItemClick(position: Int)
    }
}