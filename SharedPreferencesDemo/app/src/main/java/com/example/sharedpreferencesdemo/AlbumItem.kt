package com.example.sharedpreferencesdemo

data class AlbumItem (
    @SerializedName("id")
    val id: Int,
    val userId: Int,
    val title: String
)
