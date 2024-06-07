package com.application.intercom.tenant.Model.tenantFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.user.home.UserHomeViewModel

class TenantHomeFactory(private val repository: UserHomeRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UserHomeViewModel(repository) as T
    }
}