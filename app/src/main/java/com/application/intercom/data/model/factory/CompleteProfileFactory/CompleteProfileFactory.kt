package com.application.intercom.data.model.factory.CompleteProfileFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.data.model.ViewModel.CompleteProfileViewModel.CompleteProfileViewModel
import com.application.intercom.data.model.ViewModel.UserPlanViewModel
import com.application.intercom.data.repository.CompletProfile.CompleteProfileRepo
import com.application.intercom.data.repository.UserPlanRespo.UserPlanRepo

class CompleteProfileFactory(private val repository: CompleteProfileRepo) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CompleteProfileViewModel(repository) as T
    }
}