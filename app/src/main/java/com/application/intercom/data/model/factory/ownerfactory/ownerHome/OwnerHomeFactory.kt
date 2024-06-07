package com.application.intercom.data.model.factory.ownerfactory.ownerHome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome.OwnerHomeViewModel
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo


class OwnerHomeFactory(private val repository: OwnerHomeRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OwnerHomeViewModel(repository) as T
    }
}