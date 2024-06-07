package com.application.intercom.data.repository

sealed class EmpResource<out T> {
    data class Success<out T>(val value: T) : EmpResource<T>()
    data class Failure(val throwable: Throwable?) : EmpResource<Nothing>()
    object Loading :EmpResource<Nothing>()
    object Empty : EmpResource<Nothing>()
}