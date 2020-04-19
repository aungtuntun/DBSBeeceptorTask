package com.imceits.android.dbsbeeceptortask.data

import java.text.SimpleDateFormat
import java.util.*

data class Article(

    var id: Int,
    var title: String,
    var last_update: Long,
    var description: String,
    var img_url: String
) {

    fun getLastUpdate(): String {
        val date = Date()
        date.time = last_update
        return SimpleDateFormat("dd/MM/yyyy", Locale.US).format(date)
    }
}