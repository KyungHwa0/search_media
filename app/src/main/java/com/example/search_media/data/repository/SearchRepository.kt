package com.example.search_media.data.repository

import com.example.search_media.domain.model.ListItem

interface SearchRepository {
    suspend fun search(query: String): List<ListItem>
}