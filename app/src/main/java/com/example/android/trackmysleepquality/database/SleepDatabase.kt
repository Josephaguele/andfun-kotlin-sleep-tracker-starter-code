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

package com.example.android.trackmysleepquality.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// creating an abstract class SleepDatabase and annotate it with @Database
@Database(entities = [SleepNight::class], version = 1, exportSchema = false)
abstract class SleepDatabase: RoomDatabase()
{

    // Declare an abstract value of type SleepDatabaseDao so we can interact with our entity
    abstract val sleepDatabaseDao: SleepDatabaseDao

    // Declare a companion object
    /*The companion object allows clients to access the methods for creating or getting the database
    * without instantiating the class*/
    companion object
    {
        // Declare a @Volatile INSTANCE variable. This will help to avoid opening connections
        // to the database repeatedly which is expensive.
        /*What does it mean that the variable is annotated with volatile? This helps us make sure
         the value of INSTANCE is always up to date and the same to all execution threats.
          The value of a volatile variable will never be cached, and all writes and reads will be
          done to and from the main memory,*/
        @Volatile
        private var INSTANCE : SleepDatabase? = null

        // Define a getInstance() method with a synchronized block.
        fun getInstance(context: Context): SleepDatabase
        {
            // wrapping our code into synchronized means only one thread of execution at a time
            // can enter into this block of code,which makes sure the database only gets initialized once.
            synchronized(this)
            {
                var instance = INSTANCE
                // Inside the synchronized block:
                // Check whether the database already exist, and if it does not,
                // use Room.databaseBuilder to create it.
                if(instance == null)
                {
                    instance = Room.databaseBuilder(context.applicationContext,SleepDatabase::class.java,
                    "sleep_history_database").fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}
