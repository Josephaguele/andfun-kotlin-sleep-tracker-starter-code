package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight

class SleepNightAdapter : ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //we use findViewById() to create properties sleepLength, quality, and qualityImage.
        val sleepLength: TextView = itemView.findViewById(R.id.sleep_length)
        val quality: TextView = itemView.findViewById(R.id.quality_string)
        val qualityImage: ImageView = itemView.findViewById(R.id.quality_image)

        fun bind(item: SleepNight) {
            val res = itemView.context.resources
            sleepLength.text =
                convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
            quality.text = convertNumericQualityToString(item.sleepQualityRating, res)
            qualityImage.setImageResource(
                when (item.sleepQualityRating) {
                    0 -> R.drawable.ic_sleep_0
                    1 -> R.drawable.ic_sleep_1
                    2 -> R.drawable.ic_sleep_2
                    3 -> R.drawable.ic_sleep_3
                    4 -> R.drawable.ic_sleep_4
                    5 -> R.drawable.ic_sleep_5
                    else -> R.drawable.ic_sleep_active
                }
            )
        }
    }

 
    /*onBindViewHolder is used for binding views to the ViewHolder*/
    //The function should retrieve the item from the data list,
    // and set holder.textView.text to item.sleepQuality.toString().
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    /*In SleepNightAdapter, onCreateViewHolder(), inflate the text_item_view layout and return the ViewHolder.
    Get the LayoutInflater from parent.context and inflate R.layout.text_item_view.
    Remember to pass false to attachParent since the RecyclerView will take care of attaching it for us.*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return Companion.viewHolder(parent)
    }

    companion object {
        fun viewHolder(parent: ViewGroup): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.list_item_sleep_night, parent, false)
            return ViewHolder(view)
        }
    }
}
/*The DiffUtil class is an optimized version of onNotifyDataSetChanged.
* It allows the recycler view to update faster based on changes made while scrolling.
* It implements two methods to do this
* Method 1: areItemsTheSame - which checks if the items are the same by using the item id
* Method 2: areContentsTheSame - which checks if the contents of the items are the same.*/
// we create a new class SleepNightDiffCallback that extends DiffUtil.ItemCallback<SleepNight>.
class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>() {
    //Two items are the same if their nightId values are equal.
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem.nightId == newItem.nightId
    }
    //Two items are the same if they have the same value, oldItem == newItem.
    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem == newItem
    }
}


