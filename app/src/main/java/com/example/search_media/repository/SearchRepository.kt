package com.example.search_media.repository

import com.example.search_media.model.ListItem

interface SearchRepository {
    suspend fun search(query: String): List<ListItem>
}