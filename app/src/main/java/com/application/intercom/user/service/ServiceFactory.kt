package com.application.intercom.user.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.data.repository.ServiceRepository

class ServiceFactory(private val repository: ServiceRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ServiceViewModel(repository) as T
    }
}