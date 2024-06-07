package com.application.intercom.data.model.factory.ownerfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo

class OwnerSideFactory(private val repository: OwnerSideRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OwnerSideViewModel(repository) as T
    }
}