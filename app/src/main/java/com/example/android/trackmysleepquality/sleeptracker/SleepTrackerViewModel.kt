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
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.AndroidViewModel
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import kotlinx.coroutines.Job

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        val database: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) {

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
//    
// TODO (03) Create tonight live data var and use a coroutine to initialize it from the database.
//  TODO(04) Get all nights from the database.
//   TODO(05) Add local functions for insert(), update(), and clear().
//    TODO(06) Implement click handlers for Start, Stop, and Clear buttons using coroutines to do the database
//     TODO(09) Transform nights into a nightsString using formatNights()
}

