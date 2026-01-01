package com.alquranplusai.data.network.api

import com.alquranplusai.data.network.dto.QuranFoundationSearchResultDto

class FakeSearchApiService : SearchApiService {
    var searchResults: List<QuranFoundationSearchResultDto> = emptyList()
    var shouldThrowException = false
    var lastQuery: String? = null
    
    override suspend fun searchText(
        query: String,
        language: String,
        page: Int,
        size: Int
    ): List<QuranFoundationSearchResultDto> {
        lastQuery = query
        if (shouldThrowException) throw Exception("Fake API Error")
        return searchResults
    }

    override suspend fun searchByTopic(topic: String): List<QuranFoundationSearchResultDto> {
        lastQuery = topic
        return searchResults
    }

    override suspend fun searchByKeyword(keyword: String): List<QuranFoundationSearchResultDto> {
        lastQuery = keyword
        return searchResults
    }

    override suspend fun getSearchSuggestions(query: String): List<String> = emptyList()
}
