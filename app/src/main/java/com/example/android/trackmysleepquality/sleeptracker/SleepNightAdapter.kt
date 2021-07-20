package com.example.android.trackmysleepquality.sleeptracker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight

class SleepNightAdapter: RecyclerView.Adapter<TextItemViewHolder>() {

    // define a data variable and assign it to a list of SleepNights
    var data = listOf<SleepNight>()

    //Override getItemCount() and have it return data.size.
    override fun getItemCount(): Int {
        return data.size
    }

    /*onBindViewHolder is used for binding views to the ViewHolder*/
    //The function should retrieve the item from the data list,
    // and set holder.textView.text to item.sleepQuality.toString().
    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        var item = data[position]
        holder.textView.text = item.sleepQualityRating.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        TODO("Not yet implemented")
    }
}


