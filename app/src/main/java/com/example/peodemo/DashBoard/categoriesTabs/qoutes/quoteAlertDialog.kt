package com.example.peodemo.DashBoard.categoriesTabs.qoutes

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.peodemo.DashBoard.categoriesTabs.models.Photo
import com.example.peodemo.DashBoard.glide.GlideApp
import com.example.peodemo.R
import kotlinx.android.synthetic.main.activity_profile_page.*
import kotlinx.android.synthetic.main.quotes_alert_dialog.*

class quoteAlertDialog(var quoteInformation:quoteModel?,var quotephoto:List<String>):DialogFragment() {

    private lateinit var timer:CountDownTimer
    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {return activity?.let {
        val randomImagesIndex = (quotephoto.indices).random()
        val builder = AlertDialog.Builder(it)
        // Get the layout inflater
        val inflater = requireActivity().layoutInflater
        val inflation = inflater.inflate(R.layout.quotes_alert_dialog, null)
        // Inflate and set the layout for the dialog
        val cancel = inflation.findViewById<TextView>(R.id.Close)
        val person = inflation.findViewById<TextView>(R.id.personWhoQuoted)
        val quote = inflation.findViewById<TextView>(R.id.quote)
        val quotePhoto = inflation.findViewById<ImageView>(R.id.quoteImage)
        GlideApp.with(requireContext())
            .load(quotephoto[randomImagesIndex])
            .placeholder(R.drawable.fourteenthquoteimage).into(quotePhoto)
        //Toast.makeText(requireContext(),quotephoto[randomImagesIndex],Toast.LENGTH_LONG).show()

        val randomIndex = (0 until quoteInformation!!.Quotes!!.size).random()
        val quoteValue = quoteInformation!!.Quotes!![randomIndex]
        person.text = "— ${quoteInformation!!.name} "
        quote.text = "'' ${quoteValue} ''"
        cancel.setOnClickListener {
            dismiss()
        }
        timer =  object : CountDownTimer(30000, 1000) {

            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {

            }
            // Callback function, fired
            // when the time is up
            override fun onFinish() {
                dismiss()
            }

        }.start()
        // Pass null as the parent view because its going in the dialog layout

        builder.setView(inflation)
            // Add action buttons
        builder.create()
    } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }







}