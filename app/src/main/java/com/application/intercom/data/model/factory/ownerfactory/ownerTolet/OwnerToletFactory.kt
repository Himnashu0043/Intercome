package com.application.intercom.data.model.factory.ownerfactory.ownerTolet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerTolet.OwnerToletSaleViewModel
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.data.repository.ownerRepo.ownerTolet.OwnerToletSale

class OwnerToletFactory(private val repository: OwnerToletSale) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OwnerToletSaleViewModel(repository) as T
    }
}