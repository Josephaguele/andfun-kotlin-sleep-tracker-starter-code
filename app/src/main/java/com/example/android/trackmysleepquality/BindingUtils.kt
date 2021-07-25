package com.example.android.trackmysleepquality

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.trackmysleepquality.database.SleepNight

/*This helps to refactor SleepNightAdapter to use DataBinding, and replace bind.*/
    /*In BindingUtils, define three Binding Adapters, one for each view in list_item_sleep_night:*/

//Step 1: For the ImageView, move the code from SleepNightAdapter.ViewHolder bind() function into an extension function:
@BindingAdapter("sleepImage")
fun ImageView.setSleepImage(item: SleepNight)
{
    setImageResource(when (item.sleepQualityRating) {
        0 -> R.drawable.ic_sleep_0
        1 -> R.drawable.ic_sleep_1
        2 -> R.drawable.ic_sleep_2
        3 -> R.drawable.ic_sleep_3
        4 -> R.drawable.ic_sleep_4
        5 -> R.drawable.ic_sleep_5
        else -> R.drawable.ic_sleep_active
    })
}
// Step 2: For the two TextViews, convert the data to formatted text using the functions provided in Util.kt:
@BindingAdapter("sleepDurationFormatted")
fun TextView.setSleepDurationFormatted(item: SleepNight?) {
    item?.let {
        text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli,context.resources)
    }
}

    @BindingAdapter("sleepQualityString")
    fun TextView.setSleepQualityString(item: SleepNight?) {
        item?.let {
            text = convertNumericQualityToString(item.sleepQualityRating, context.resources)
        }
    }
