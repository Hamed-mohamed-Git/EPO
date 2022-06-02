package com.example.peodemo.home.introduction.teacher

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.peodemo.R
import com.example.peodemo.home.introduction.introductionActivity
import com.makeramen.roundedimageview.RoundedImageView
import java.util.ArrayList

class teacherModelView(
    //assigning a item arrayList parameter that type i declared before
    var items: ArrayList<teacherModel>,
    //assigning a listener parameter to create
    // a onClickListener method to make an action each item in recycle view if user press on card view
    private val listener: teacherModelView.OnItemClickListener
): RecyclerView.Adapter<teacherModelView.ViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): teacherModelView.ViewHolder {
        //assigning a itemCard property that return view by using inflate methods to access each item on view card layout
        val itemCard = LayoutInflater.from(parent.context).inflate(R.layout.teacher_card_view, parent,false)
        //and return a view value to use it in the ViewHolder class
        return ViewHolder(itemCard)
    }

    override fun onBindViewHolder(holder: teacherModelView.ViewHolder, position: Int) {
        //assigning a tap property to set an item in the array to set it to view in card view
        var teacherInfo =  items[position]
        //if a user tap on the card view change the items on it
        holder.image.setImageResource(teacherInfo.image)
        holder.info.text = teacherInfo.information
        holder.info.setTextColor(teacherInfo.generalColor)
        holder.job.text = teacherInfo.job
        holder.job.setTextColor(teacherInfo.generalColor)
        holder.name.text = teacherInfo.name
        holder.name.setTextColor(teacherInfo.generalColor)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val info = itemView.findViewById<TextView>(R.id.teacherInfo)
        val name = itemView.findViewById<TextView>(R.id.teacherName)
        val job = itemView.findViewById<TextView>(R.id.teacherJob)
        val image = itemView.findViewById<RoundedImageView>(R.id.teacherImage)

        init {
            //and put context in it
            itemView.setOnClickListener(this)
        }


        @RequiresApi(Build.VERSION_CODES.M)
        override fun onClick(v: View?) {
            //assigning a position property to access each item
            val  position = adapterPosition
            //declare a condition if the recycle view not empty
            if (position != RecyclerView.NO_POSITION ){
                //set this position to on item click listener method in OnItemClickListener interface to access each item alone
                listener.onTeacherItemClick(position)
            }
        }


    }
    //declare a OnItemClickListener interface
    interface OnItemClickListener {
        //declare OnItemClick method
        fun onTeacherItemClick(position: Int)
    }
}