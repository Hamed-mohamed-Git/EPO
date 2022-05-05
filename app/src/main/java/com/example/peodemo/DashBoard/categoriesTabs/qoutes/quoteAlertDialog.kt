package com.example.peodemo.DashBoard.categoriesTabs.qoutes

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.peodemo.R
import kotlinx.android.synthetic.main.quotes_alert_dialog.*

class quoteAlertDialog(var quoteInformation:quoteModel?):DialogFragment() {
    private var images = listOf<Int>(R.drawable.fquoteimage,R.drawable.squoteimagee,R.drawable.thquoteimage,R.drawable.fourthquoteimage,R.drawable.fifthquoteimage
    ,R.drawable.sixieethquoteimage,R.drawable.seventhquoteimage,R.drawable.eighteethquoteimage,R.drawable.nineteethquoteimage
    ,R.drawable.tenthquoteimage,R.drawable.ellthquoteimage,R.drawable.twethquoteimage,R.drawable.thirtthquoteimage,R.drawable.fourteenthquoteimage
    ,R.drawable.fifteenthquoteimage,R.drawable.sixieethquoteimage,R.drawable.seventeenthquoteimage,R.drawable.eiighteenquoteimage
    ,R.drawable.ninteenquoteimage,R.drawable.twentyquoteimage,R.drawable.twentyonequoteimage,R.drawable.twentytwoquoteimage,R.drawable.twentythreequoteimage
    ,R.drawable.twentyfourquoteimage,R.drawable.twentyfourquoteimage,R.drawable.twentyfivequoteimage,R.drawable.twentysixquoteimage,R.drawable.twentysevenquoteimage
    ,R.drawable.twentyeigntquoteimage,R.drawable.twentyninequoteimage,R.drawable.thirteenquoteimage
    )
    private lateinit var timer:CountDownTimer
    @SuppressLint("SetTextI18n")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {return activity?.let {
        val randomImagesIndex = (images.indices).random()
        val builder = AlertDialog.Builder(it)
        // Get the layout inflater
        val inflater = requireActivity().layoutInflater
        val inflation = inflater.inflate(R.layout.quotes_alert_dialog, null)
        // Inflate and set the layout for the dialog
        val cancel = inflation.findViewById<TextView>(R.id.Close)
        val person = inflation.findViewById<TextView>(R.id.personWhoQuoted)
        val quote = inflation.findViewById<TextView>(R.id.quote)
        val mainLayout = inflation.findViewById<FrameLayout>(R.id.Mainlayout)
        mainLayout.setBackgroundResource(images[randomImagesIndex])
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