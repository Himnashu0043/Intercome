package com.application.intercom.data.model.factory.managerFactory.managerHome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerHomeViewModel.ManagerHomeViewModel
import com.application.intercom.data.repository.managerRepo.managerHomeRepo.ManagerHomeRepo

class ManagerHomeFactory(private val repository: ManagerHomeRepo) : ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ManagerHomeViewModel(repository) as T
    }
}