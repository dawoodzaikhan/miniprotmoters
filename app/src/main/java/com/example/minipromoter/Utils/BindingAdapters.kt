package com.example.minipromoter.Utils

import android.graphics.Color
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.example.minipromoter.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


/*
* binding adapter so we can set the card background color
* */
@BindingAdapter("setCartBacgroundColor")
fun setCartBacgroundColor(view: CardView, errorMessage: String?) {
    view.setCardBackgroundColor(getRandomColorCode())

}

/* a function to get random color*/
fun getRandomColorCode(): Int {
    val random = Random()
    return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
}

/* a function to which we pass text view and time in long
* and then it convert long to a specific format and set it on text view
* */
@BindingAdapter("setCreationTime")
fun com.google.android.material.textview.MaterialTextView.setCreationDate(timeInMilliSec: Long) {
    text = timeConverted(timeInMilliSec)
}

/*a function to set the backgroud based on boolean
* */
@BindingAdapter("setMessageBackground")
fun setMessageBackground(view: ConstraintLayout, isIncoming: Boolean) {
    if (isIncoming) {
        view.background = view.context.resources.getDrawable(R.drawable.incoming_message_drawable)
    } else {
        view.background = view.context.resources.getDrawable(R.drawable.outgoing_message_drawable)
    }
}

// to convert time to a specific format
fun timeConverted(time: Long): String {
    val date = Date(time)
    val formatter: DateFormat = SimpleDateFormat("HH:mm   MMM, dd, YYYY")
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(date)
}
