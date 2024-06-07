package com.application.intercom.user.aboutapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.data.repository.AboutRepository

class AboutUsFactory(private val repository: AboutRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AboutViewModel(repository) as T
    }
}