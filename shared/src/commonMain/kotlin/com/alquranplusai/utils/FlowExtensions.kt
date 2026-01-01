package com.alquranplusai.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

fun <T> Flow<T>.asResource(): Flow<Resource<T>> = this
    .map<T, Resource<T>> { Resource.Success(it) }
    .onStart { emit(Resource.Loading()) }
    .catch { emit(Resource.Error(it.message ?: "Unknown error", it)) }

fun <T> Flow<Resource<T>>.mapResource(transform: (T) -> T): Flow<Resource<T>> = this
    .map { resource ->
        when (resource) {
            is Resource.Success -> Resource.Success(transform(resource.data))
            is Resource.Loading -> Resource.Loading(resource.data?.let(transform))
            is Resource.Error -> resource
        }
    }

fun <T> Flow<Resource<T>>.onResourceSuccess(action: suspend (T) -> Unit): Flow<Resource<T>> = this
    .map { resource ->
        if (resource is Resource.Success) {
            action(resource.data)
        }
        resource
    }

fun <T> Flow<Resource<T>>.onResourceError(action: suspend (String, Throwable?) -> Unit): Flow<Resource<T>> = this
    .map { resource ->
        if (resource is Resource.Error) {
            action(resource.message, resource.exception)
        }
        resource
    }
