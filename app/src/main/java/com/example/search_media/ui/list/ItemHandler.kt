package com.example.search_media.ui.list

import com.example.search_media.domain.model.ListItem

interface ItemHandler {
    fun onClickFavorite(item : ListItem)
}