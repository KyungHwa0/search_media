package com.example.search_media.repository

import com.example.search_media.SearchService
import com.example.search_media.model.ListItem

class SearchRepositoryImpl(private val service: SearchService) : SearchRepository {
    override suspend fun search(query: String): List<ListItem>{
        val imageResult = service.searchImage(query = query)
        val videoResult = service.searchVideo(query = query)
        return (imageResult.documents + videoResult.documents).sortedBy { it.dateTime }
    }
}
