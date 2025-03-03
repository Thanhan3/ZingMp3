package com.example.musicplayer.data.source

sealed class Result<T> {
    class Success<T>(val data: T) : Result<T>()
    class Error<T>(val exception : Exception) : Result<T>()
}