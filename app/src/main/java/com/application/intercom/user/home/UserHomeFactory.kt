package com.application.intercom.user.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.data.repository.UserHomeRepository

class UserHomeFactory(private val repository: UserHomeRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserHomeViewModel(repository) as T
    }
}