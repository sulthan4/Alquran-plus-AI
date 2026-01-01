package com.alquranplusai.data.network.api

import com.alquranplusai.data.network.dto.QuranFoundationSearchResponse
import com.alquranplusai.data.network.dto.QuranFoundationSearchResultDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class SearchApiServiceImpl(
    private val client: HttpClient
) : SearchApiService {

    private val baseUrl = "https://api.quran.com/api/v4"

    override suspend fun searchText(
        query: String,
        language: String,
        page: Int,
        size: Int
    ): List<QuranFoundationSearchResultDto> {
        return try {
            val response = client.get("$baseUrl/search") {
                parameter("q", query)
                parameter("language", language)
                parameter("size", 20)
                parameter("page", 1)
            }
            
            // Handle 204 No Content (no results found)
            if (response.status.value == 204) {
                println("API returned 204 No Content for query: $query")
                return emptyList()
            }
            
            // Parse response body
            val searchResponse: QuranFoundationSearchResponse = response.body()
            searchResponse.search.results ?: emptyList()
        } catch (e: Exception) {
            println("API search failed: ${e.message}")
            emptyList()
        }
    }

    override suspend fun searchByTopic(topic: String): List<QuranFoundationSearchResultDto> {
        return searchText(topic, "en")
    }

    override suspend fun searchByKeyword(keyword: String): List<QuranFoundationSearchResultDto> {
        return searchText(keyword, "en")
    }

    override suspend fun getSearchSuggestions(query: String): List<String> {
        // For now, using the search endpoint as suggestions if a dedicated one isn't available
        val results = searchText(query, "en")
        return results.take(5).map { it.text }
    }
}
