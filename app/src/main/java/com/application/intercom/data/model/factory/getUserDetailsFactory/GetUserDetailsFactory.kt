package com.application.intercom.data.model.factory.getUserDetailsFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.data.model.ViewModel.getUserDetailsViewModel.GetUserDetailsViewModel

import com.application.intercom.data.repository.getUserDetailsRepo.GetUserDetailsRepo
class GetUserDetailsFactory(private val repository: GetUserDetailsRepo) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GetUserDetailsViewModel(repository) as T
    }
}