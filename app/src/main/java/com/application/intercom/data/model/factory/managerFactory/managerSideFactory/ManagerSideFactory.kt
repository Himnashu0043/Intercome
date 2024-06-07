package com.application.intercom.data.model.factory.managerFactory.managerSideFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo

class ManagerSideFactory(private val repository: ManagerSideRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ManagerSideViewModel(repository) as T
    }
}