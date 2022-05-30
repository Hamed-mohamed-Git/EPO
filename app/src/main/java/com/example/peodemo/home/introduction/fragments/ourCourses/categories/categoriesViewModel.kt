package com.example.peodemo.home.introduction.fragments.ourCourses.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

import com.example.peodemo.R
import java.util.ArrayList

class categoriesViewModel(
    //assigning a item arrayList parameter that type i declared before
    var items: ArrayList<gategoriesModel>,
    //assigning a listener parameter to create
    // a onClickListener method to make an action each item in recycle view if user press on card view
    private val listener: categoriesViewModel.OnItemClickListener
)
//inherit the Recycle view class
    : RecyclerView.Adapter<categoriesViewModel.ViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //assigning a itemCard property that return view by using inflate methods to access each item on view card layout
        val itemCard = LayoutInflater.from(parent.context).inflate(R.layout.categories_card_item, parent,false)
        //and return a view value to use it in the ViewHolder class
        return ViewHolder(itemCard)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //assigning a tap property to set an item in the array to set it to view in card view
        var taps =  items[position]
        //if a user tap on the card view change the items on it
        if (taps.pressed == 1){
            //change the background

            holder.image.setImageResource(taps.image)
            holder.text.text = taps.name
        }
        //if not let the items as they are
        else if (taps.pressed == 0) {
            holder.image.setImageResource(taps.image)
            holder.text.text = taps.name
        }
    }

    override fun getItemCount(): Int {
        //return the size of array to access the position
        return items.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val image  = itemView.findViewById<ImageView>(R.id.introductionCourseImageView)
        val imageLayout  = itemView.findViewById<FrameLayout>(R.id.introductionImageCourseTabsLayout)
        val layout  = itemView.findViewById<LinearLayout>(R.id.introductionCourseTabsLayout)
        val text = itemView.findViewById<TextView>(R.id.introductionCourseTextView)
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