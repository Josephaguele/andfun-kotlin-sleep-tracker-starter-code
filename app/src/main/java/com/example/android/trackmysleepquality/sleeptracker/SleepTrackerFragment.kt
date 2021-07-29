/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.google.android.material.snackbar.Snackbar

/**
 * A fragment with buttons to record start and end times for sleep, which are saved in
 * a database. Cumulative data is displayed in a simple scrollable TextView.
 * (Because we have not learned about RecyclerView yet.)
 */
class SleepTrackerFragment : Fragment() {

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_tracker, container, false)

        // requireNotNull is a kotlin function that throws an illegal argument exception if the value is null.
        val application = requireNotNull(this.activity).application

        // to get a reference to the DAO of the database, we use
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao

        /*Create an instance of the viewModelFactory. You'll need to pass in dataSource
        as well as application.*/
        val viewModelFactory = SleepTrackerViewModelFactory(dataSource,application)

        /*Get a reference to the SleepTrackerViewModel. To the ViewModelProvider, specify to use
         the viewModelFactory and get an instance  of SleepTrackerViewModel::class.java.*/
        val sleepTrackerViewModel =  ViewModelProvider(
                this, viewModelFactory).get(SleepTrackerViewModel::class.java)

        /*In SleepTrackerFragment onCreateView, create a new GridLayoutManager and bind it to the RecyclerView.
        Access the RecyclerView in the binding object using binding.sleepList.*/
        val manager = GridLayoutManager(activity, 3)
        binding.sleepList.layoutManager = manager

        // We add an observer here to navigate to SleepQuality
        sleepTrackerViewModel.navigateToSleepQuality.observe(viewLifecycleOwner,Observer
        {
            /*Inside the observer block, we navigate and pass along the ID of the current night, and
            then call doneNavigating():*/
            night->
            night?.let {
                this.findNavController().navigate(
                    SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToSleepQualityFragment(night.nightId))
                sleepTrackerViewModel.doneNavigating()}
        })

        binding.sleepTrackerViewModel = sleepTrackerViewModel

        // set the current activity as the lifecycle owner of this binding
        binding.lifecycleOwner = this


        //In SleepTrackerFragment, update the SleepNightListener code to pass the data to view model.
        val adapter = SleepNightAdapter(SleepNightListener
        { nightId -> sleepTrackerViewModel.onSleepNightClicked(nightId)
        })

        binding.sleepList.adapter = adapter

        // adding an observer
        //Create an observer on sleepTrackerViewModel.nights that sets the Adapter when there is new data.
        sleepTrackerViewModel.nights.observe(viewLifecycleOwner, Observer
        {
            it?.let{
                adapter.submitList(it)
            }
        })

        // we add an observer for the snackbar
        sleepTrackerViewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer
        {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                sleepTrackerViewModel.doneShowingSnackbar()
            }
        })
//        In SleepTrackerFragment, add an observer to trigger navigation when the listener passes the data to ViewModel.
//        Make sure you call onSleepDataQualityNavigated when navigation is complete.
        sleepTrackerViewModel.navigateToSleepDataQuality.observe(viewLifecycleOwner, Observer {night ->
            night?.let {
                this.findNavController().navigate(SleepTrackerFragmentDirections
                    .actionSleepTrackerFragmentToSleepDetailFragment(night))
                sleepTrackerViewModel.onSleepDataQualityNavigated()
            }
        })



        return binding.root
    }
}

