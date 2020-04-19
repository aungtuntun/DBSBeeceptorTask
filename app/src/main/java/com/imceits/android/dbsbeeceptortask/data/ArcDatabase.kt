package com.imceits.android.dbsbeeceptortask.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Article::class], version = 1, exportSchema = false)
abstract class ArcDatabase: RoomDatabase() {

    abstract fun arcDao(): ArcDao
}