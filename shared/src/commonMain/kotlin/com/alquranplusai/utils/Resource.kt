package com.alquranplusai.utils

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String, val exception: Throwable? = null) : Resource<Nothing>()
    data class Loading<T>(val data: T? = null) : Resource<T>()
    
    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    val isLoading: Boolean get() = this is Loading
    
    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Loading -> data
        is Error -> null
    }
    
    fun getOrDefault(default: @UnsafeVariance T): T = when (this) {
        is Success -> data
        is Loading -> data ?: default
        is Error -> default
    }
    
    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Loading -> data ?: throw IllegalStateException("Resource is loading")
        is Error -> throw exception ?: Exception(message)
    }
    
    inline fun <R> map(transform: (T) -> R): Resource<R> = when (this) {
        is Success -> Success(transform(data))
        is Loading -> Loading(data?.let(transform))
        is Error -> Error(message, exception)
    }
    
    inline fun onSuccess(action: (T) -> Unit): Resource<T> {
        if (this is Success) action(data)
        return this
    }
    
    inline fun onError(action: (String, Throwable?) -> Unit): Resource<T> {
        if (this is Error) action(message, exception)
        return this
    }
    
    inline fun onLoading(action: (T?) -> Unit): Resource<T> {
        if (this is Loading) action(data)
        return this
    }
}

fun <T> Resource<T>.toResult(): Result<T> = when (this) {
    is Resource.Success -> Result.success(data)
    is Resource.Error -> Result.failure(exception ?: Exception(message))
    is Resource.Loading -> Result.failure(IllegalStateException("Resource is still loading"))
}

fun <T> Result<T>.toResource(): Resource<T> = fold(
    onSuccess = { Resource.Success(it) },
    onFailure = { Resource.Error(it.message ?: "Unknown error", it) }
)
