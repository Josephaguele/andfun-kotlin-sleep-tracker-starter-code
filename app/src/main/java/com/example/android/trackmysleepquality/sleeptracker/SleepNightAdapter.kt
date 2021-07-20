package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepNight

class SleepNightAdapter: RecyclerView.Adapter<SleepNightAdapter.TextItemViewHolder>() {

    class TextItemViewHolder (view: View) : RecyclerView.ViewHolder(view)
    {
        // locates the single text view to be shown on the screen
         val textView: TextView = itemView.findViewById(R.id.textV)
    }

    // define a data variable and assign it to a list of SleepNights
    var data = listOf<SleepNight>()

        /*In SleepNightAdapter, add a custom setter to data that calls notifyDataSetChanged()
        and tell Kotlin to save the new value by setting field = value*/
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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

    /*In SleepNightAdapter, onCreateViewHolder(), inflate the text_item_view layout and return the ViewHolder.
    Get the LayoutInflater from parent.context and inflate R.layout.text_item_view.
    Remember to pass false to attachParent since the RecyclerView will take care of attaching it for us.*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.text_item_view, parent, false) as TextView
        return TextItemViewHolder(view)
    }

}


