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

package com.example.android.trackmysleepquality.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import kotlinx.coroutines.launch

/*Create a SleepQualityViewModel that takes sleepNightKey and database as arguments:*/
class SleepQualityViewModel(private val sleepNightKey: Long = 0L, val database: SleepDatabaseDao) : ViewModel() {

    /*To navigate back to the SleepTrackerFragment, analogously implement navigateToSleepTracker
    and _navigateToSleepTracker, as well as doneNavigating():*/
    private val _navigateToSleepTracker =  MutableLiveData<Boolean?>()

    val navigateToSleepTracker: LiveData<Boolean?>
        get() = _navigateToSleepTracker

    fun doneNavigating() {
        _navigateToSleepTracker.value = null
    }

    /*Now, create one click handler that you will use for all the smiley sleep quality images,
     onSetSleepQuality().*/
    /*Use the same coroutine pattern. Launch a coroutine in uiScope, switch to the IO dispatcher,
     get tonight using the sleepNightKey, set the sleep quality, update the database, and trigger navigation:*/
    fun onSetSleepQuality(quality: Int) {
        viewModelScope.launch withContext@{
            val tonight = database.get(sleepNightKey) ?: return@withContext
            tonight.sleepQualityRating = quality
            database.update(tonight)
            _navigateToSleepTracker.value = true
        }
    }
}