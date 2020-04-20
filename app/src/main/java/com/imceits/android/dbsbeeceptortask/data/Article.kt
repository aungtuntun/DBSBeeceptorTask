package com.imceits.android.dbsbeeceptortask.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "Article")
data class Article(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("title")
    @Expose
    var title: String?,
    @SerializedName("last_update")
    @Expose
    var last_update: Long,
    @SerializedName("short_description")
    @Expose
    var description: String?,
    @SerializedName("avatar")
    @Expose
    var img_url: String?
) {

    fun getLastUpdate(): String {
        val date = Date()
        date.time = last_update
        return SimpleDateFormat("dd/MM/yyyy", Locale.US).format(date)
    }
}