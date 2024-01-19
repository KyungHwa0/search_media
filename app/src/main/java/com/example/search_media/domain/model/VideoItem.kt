package com.example.search_media.domain.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class VideoListResponse(
    val documents: List<VideoItem>
)

data class VideoItem(
    @SerializedName("thumbnail") override val thumbnailUrl: String,
    val title: String,
    @SerializedName("play_time") val playTime: Int,
    val author: String,
    @SerializedName("datetime") override val dateTime: Date,
    override var isFavorite : Boolean = false
) : ListItem