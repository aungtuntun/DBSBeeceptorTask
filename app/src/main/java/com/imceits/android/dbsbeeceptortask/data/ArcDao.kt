package com.imceits.android.dbsbeeceptortask.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArcDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: List<Article>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(data: Article): Long

    @Query("SELECT * FROM Article order by last_update DESC")
    fun getArticle(): LiveData<List<Article>>

}