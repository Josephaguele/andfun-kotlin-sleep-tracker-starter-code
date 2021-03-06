package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding

class SleepNightAdapter : ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()){

    // note that after the first refactor, the ViewHolder class still has view as its parameter.
    // I changed the name from view to binding based on the tutorial
    class ViewHolder(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SleepNight)
        {
            val res = itemView.context.resources
                // In ViewHolder, replace findViewById calls with references to binding object fields,
                // then inline them:
            binding.sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
            binding.qualityString.text = convertNumericQualityToString(item.sleepQualityRating, res)
            binding.qualityImage.setImageResource(
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
            //layout now inflated using data binding.
            val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)
            return ViewHolder(binding)
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


