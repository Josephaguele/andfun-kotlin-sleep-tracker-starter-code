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

import android.app.Application
import androidx.lifecycle.*
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
    val database: SleepDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    // Define a variable, tonight, to hold the current night, and make it MutableLiveData:
    // We need a variable to hold the current night, we make this LiveData because we want to be able  // to observe it, MutableLiveData because we want to be able to change it.
    private var tonight = MutableLiveData<SleepNight?>()

    //Define a variable, nights. Then getAllNights() from the database and assign to the nights variable:
    // WE ALSO want to get all the nights in the database when we create the viewModel
    private val nights = database.getAllNights()

    //Add code to transform nights into a nightsString using the formatNights() function from Util.kt:
    val nightsString = Transformations.map(nights) { nights ->
        formatNights(nights, application.resources)
    }

    //To initialize the tonight variable, create an init block and call initializeTonight(), which you'll define in the next step:
    init {
        initializeTonight()
    }

    /*Executes when the CLEAR button is clicked.*/
    fun onClear() {
        viewModelScope.launch {
            clear()
            tonight.value = null

            // this is to trigger the snack bar event
            _showSnackbarEvent.value = true
        }
    }

    suspend fun clear() {
        database.clear()
    }

    /*Inside, get the value for tonight from the database by calling getTonightFromDatabase(),
    which you will define in the next step, and assign it to tonight.value*/
    // inside initializeTonight() we are using a coroutine to getTonight() from the database so that
    // we are not blocking the UI while waiting for the result.
    // So we specify the UI scope and launch the coroutine. Launching a coroutine
    private fun initializeTonight() {
        viewModelScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }


    /* Implement getTonightFromDatabase(). Define is as a private suspend function that returns a
    nullable SleepNight, if there is no current started sleepNight.
     This leaves you with an error, because you will have to return something.*/
    private suspend fun getTonightFromDatabase(): SleepNight? {   // This is done here because it would take a longer time to complete, hence the need to be
        // done in a coroutine
        // if the start time and end time are the same, then we know that we are continuing with
        // an existing night. If the start time and end time are not the same, then we have no night
        // started and so we return null.
        /* Let the coroutine get tonight from the database. If the start and end times are the not the
        same, meaning, the night has already been completed, return null. Otherwise, return night:*/
        var night = database.getTonight()
        if (night?.endTimeMilli != night?.startTimeMilli) {
            night = null
        }
        return night
    }

    /*Executes when the STOP button is clicked*/
    //Implement onStartTracking(), the click handler for the Start button:
    fun onStartTracking() { //    Inside onStartTracking(), launch a coroutine in viewModelScope:
        viewModelScope.launch {
            // Inside the coroutine, create a new SleepNight, which captures the current time as
            // the start time:
            val newNight = SleepNight()

            //Call insert() to insert it into the database. You will define insert() shortly:
            insert(newNight)
            // Set tonight to the new night:
            tonight.value = getTonightFromDatabase()
        }
    }

    // Define insert() as a private suspend function that takes a SleepNight as its argument:
    private suspend fun insert(night: SleepNight) {
        //For the body of insert(), insert the night into the database:
        database.insert(night)
    }


    /*You need to add Navigation, so that when the user taps the STOP button, you navigate to the
     SleepQualityFragment to collect a quality rating.
    In SleepTrackerViewModel.kt, in onStopTracking() set a LiveData that changes when you want
    to navigate. Use encapsulation to expose only a gettable version to the fragment*/
    private val _navigateToSleepQuality = MutableLiveData<SleepNight>()

    val navigateToSleepQuality: LiveData<SleepNight>
        get() = _navigateToSleepQuality

    // We add a doneNavigating() function that resets the event.
    fun doneNavigating() {
        _navigateToSleepQuality.value = null
    }

    // The scope determines what thread the coroutine will run on and it also needs to know about the job
//    If it hasn't been set yet, set the endTimeMilli to the current system time and call update()
//    with the night. There are several ways to implement this, and one is shown below:
    fun onStopTracking() {
        viewModelScope.launch {
            val oldNight = tonight.value ?: return@launch
            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)

            //In the click handler for the STOP button, onStopTracking(), trigger this navigation
            _navigateToSleepQuality.value = oldNight
        }
    }

    /*Assign each button a Transformations that tests it against the value of tonight.
    The START button should be visible when tonight is null, the STOP button when tonight is not null,
    and the CLEAR button if nights contains any nights:*/
    // The state of the variables changes based on the state of tonight, so whenever tonight changes,
    // these variables are updated.  Tonight is null at the beginning,so if that is the case, we
    // want the start button to be visible. If tonight has a value, the stop is visible, and the
    // clear button should only be visible when we have something to clear.
    val startButtonVisible = Transformations.map(tonight) {
        null == it
    }
    val stopButtonVisible = Transformations.map(tonight) {
        null != it
    }
    val clearButtonVisible = Transformations.map(nights) {
        it?.isNotEmpty()
    }

    // Use the same pattern as for navigation to set up and trigger a snackbar when the data is cleared.
    //In the SleepTrackerViewModel, create the encapsulated event:
    private var _showSnackbarEvent = MutableLiveData<Boolean>()

    val showSnackBarEvent: LiveData<Boolean>
       get() = _showSnackbarEvent

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }



    //  Implement update() using the same pattern as insert():
    private suspend fun update(night: SleepNight) {
        database.update(night)
    }

    // (01) Create a viewModelJob and override onCleared() for canceling coroutines.
    /*We use coroutines because among other things, triggering the buttons calls  up our database
    * operations..and we do not want that to slow down other things.
    * To manage our coroutines, we need a job. This ViewModelJob allows us to cancel all
    * coroutines started by this ViewModel when the viewModel when the ViewModel is no longer used
    * and destroyed so that we don't end up with coroutines that have no where to return to */
    private var viewModelJob = Job()

    // When the viewModel is destroyed, onCleared is called.
    // We tell the job to cancel all coroutines.
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

