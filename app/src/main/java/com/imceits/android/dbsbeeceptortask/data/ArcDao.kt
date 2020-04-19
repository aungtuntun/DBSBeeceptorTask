package com.imceits.android.dbsbeeceptortask.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface ArcDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: List<Article>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(data: Article): Long

}