package com.alquranplusai.data.network.api

import com.alquranplusai.data.network.dto.QuranFoundationSearchResultDto

/**
 * API service for search functionality
 */
interface SearchApiService {
    
    suspend fun searchText(
        query: String,
        language: String = "ar",
        page: Int = 1,
        size: Int = 20
    ): List<QuranFoundationSearchResultDto>
    
    suspend fun searchByTopic(topic: String): List<QuranFoundationSearchResultDto>
    
    suspend fun searchByKeyword(keyword: String): List<QuranFoundationSearchResultDto>
    
    suspend fun getSearchSuggestions(query: String): List<String>
}
