package com.example.peodemo.home.introduction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.peodemo.R
import java.util.ArrayList
//declare a tapView model that inherited by Recycle view adapter class to control each item in a recycle view in introduction layout
class tapViewModel(
    //assigning a item arrayList parameter that type i declared before
    var items:ArrayList<tapModel>,
    //assigning a listener parameter to create
    // a onClickListener method to make an action each item in recycle view if user press on card view
    private val listener: OnItemClickListener)
    //inherit the Recycle view class
    :RecyclerView.Adapter<tapViewModel.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //assigning a itemCard property that return view by using inflate methods to access each item on view card layout
        val itemCard = LayoutInflater.from(parent.context).inflate(R.layout.fragment_markes_bar, parent,false)
        //and return a view value to use it in the ViewHolder class
        return ViewHolder(itemCard)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //assigning a tap property to set an item in the array to set it to view in card view
        var taps =  items[position]

        //if a user tap on the card view change the items on it
        if (taps.pressed == 1){
            //change the background
            holder.marksLayout.setBackgroundResource(R.drawable.viewcard_layout_pressed)
            //change the text
            holder.tapNameInstance.text = taps.tapName
            //change the image
            holder.tapImage.setImageResource(taps.imagePressed)
            //change the text color
            holder.tapNameInstance.setTextColor(taps.colorpressed)
        }
        //if not let the items as they are
        else if (taps.pressed == 0) {
            //set the init background
            holder.marksLayout.setBackgroundResource(R.drawable.fragment_markes_bar_viewcard_background)
            //set the init text
            holder.tapNameInstance.text = taps.tapName
            //set the init image
            holder.tapImage.setImageResource(taps.image)
            //set the init text color
            holder.tapNameInstance.setTextColor(taps.color)
        }
    }

    override fun getItemCount(): Int {
        //return the size of array to access the position
        return items.size
    }


    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView),View.OnClickListener{
        //assigning a tapNameInstance property to access the name on card view by it's id
        val tapNameInstance =  itemView.findViewById<TextView>(R.id.tapNameTextView)
        //assigning a tapImage property to access the image on card view by it's id
        val tapImage = itemView.findViewById<ImageView>(R.id.tapImage)
        //assigning a tapNameInstance property to access the layout of the card view by it's id
        val marksLayout = itemView.findViewById<LinearLayout>(R.id.marksBarLayout)

        //declare a init method to create a set of clickListener method if this is called
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