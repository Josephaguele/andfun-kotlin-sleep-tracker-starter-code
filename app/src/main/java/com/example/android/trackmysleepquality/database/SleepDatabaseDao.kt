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

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

// Add annotated insert() method  for inserting a single SleepNight
@Dao
interface SleepDatabaseDao {
    /*To insert an item into the database,we need two things,the insert annotation and
a function definition for an insert function that we can call from our code.
We're going to call it Insert, so you could call it anything you want.
We also need to specify what we are going to insert as a parameter,
which in this case is an instance of our entity class SleepNight.
During compilation, room will generate code to turn this parsed in Kotlin object into
 a row of values for the database.When we call insert room creates the row from the entity
object and inserts it into the database.
After we insert a row representing a SleepNight we want to be able to update it.
For example, with the new end-time or a sleep quality rating.
*/ @Insert
    fun insert(night: SleepNight)

    /// Annotated update() method for updating a SleepNight.
    @Update
    fun update(night: SleepNight)

    // Add annotated get() method that gets the SleepNight by key
    /*In this query, we instruct the database to select all columns in the
    daily sleep quality table and return the rows where the nightID matches the supplied key.
    Because the key is unique, this will return one sleepNight or null if there isn't one */
    @Query("SELECT * from daily_sleep_quality_table WHERE nightId = :key")
    fun get(key: Long): SleepNight

    // Add annotated clear() method and query.
    // SQLite query to delete everything from the daily_sleep_quality_table:
    @Query("DELETE FROM daily_sleep_quality_table")
    fun clear()

    // Add annotated getAllNights() method and query.
    //The SQLite query should return all columns from the daily_sleep_quality_table, ordered in
    // descending order. Let getAllNights() return a list of SleepNight as LiveData.
    // Room keeps this LiveData updated for us, and we don't have to specify an observer for it.
    @Query("select * from daily_sleep_quality_table order by nightId desc")
    fun getAllNights(): LiveData<List<SleepNight>>

    // add annotated getTonight() method and query.
    /*Our final query returns the most recent night by looking at AllNights, ordering them with
     order and returning only one indicated by limit one. Because we sought by nightID in
     descending order, it's the one with the highest ID, which is the latest night.
     The return type, SleepNight is nullable because in the beginning and after we clear all the
      contents, there is no tonight.*/
    /*You get tonight by writing a SQLite query that returns the first element of a list of results
     ordered by nightId in descending order. Use LIMIT 1 to return only one element.*/
    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC LIMIT 1")
    fun getTonight(): SleepNight?
}



