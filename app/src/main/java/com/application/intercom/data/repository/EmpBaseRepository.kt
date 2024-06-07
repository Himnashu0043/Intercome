package com.application.intercom.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class EmpBaseRepository {

    suspend fun <T> safeApiCall(apiCall: suspend () -> T) : EmpResource<T> {
        return withContext(Dispatchers.IO) {
            try {
                EmpResource.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                EmpResource.Failure(throwable)
            }
        }
    }
}