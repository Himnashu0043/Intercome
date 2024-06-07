package com.application.intercom.data.model.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.data.model.ViewModel.UserPlanViewModel
import com.application.intercom.data.repository.UserPlanRespo.UserPlanRepo


class UserPLanFactory(private val repository: UserPlanRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserPlanViewModel(repository) as T
    }
}