package com.example.search_media.list

import com.example.search_media.model.ListItem

interface ItemHandler {
    fun onClickFavorite(item : ListItem)
}