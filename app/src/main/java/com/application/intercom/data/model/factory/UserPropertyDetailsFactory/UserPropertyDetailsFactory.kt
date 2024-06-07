package com.application.intercom.data.model.factory.UserPropertyDetailsFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.data.model.ViewModel.UserPlanViewModel
import com.application.intercom.data.model.ViewModel.UserPropertyDetailsViewModel.UserPropertyDetailsViewModel
import com.application.intercom.data.model.local.UserPropertyDetailsPostModel.UserPropertyDetailsPostModel
import com.application.intercom.data.repository.UserPlanRespo.UserPlanRepo
import com.application.intercom.data.repository.UserPropertyDetailsRepo.UserPropertyDetailsRepo

class UserPropertyDetailsFactory(private val repository: UserPropertyDetailsRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserPropertyDetailsViewModel(repository) as T
    }
}