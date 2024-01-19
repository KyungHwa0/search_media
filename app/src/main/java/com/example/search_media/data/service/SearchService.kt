package com.example.search_media.data.service

import com.example.search_media.domain.model.ImageListResponse
import com.example.search_media.domain.model.VideoListResponse
import com.example.search_media.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchService {
        @GET("image")
        suspend fun searchImage(
                @Header("Authorization") authHeader: String = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}",
                @Query("query") query: String
        ): ImageListResponse

        @GET("vclip")
        suspend fun searchVideo(
                @Header("Authorization") authHeader: String = "KakaoAK ${BuildConfig.KAKAO_REST_API_KEY}",
                @Query("query") query: String
        ): VideoListResponse
}

