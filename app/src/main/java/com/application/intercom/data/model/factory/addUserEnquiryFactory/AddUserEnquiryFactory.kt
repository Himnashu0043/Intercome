package com.application.intercom.data.model.factory.addUserEnquiryFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.data.model.ViewModel.UserPlanViewModel
import com.application.intercom.data.model.ViewModel.addUserEnquriy.AddUserEnquiryViewModel
import com.application.intercom.data.repository.UserPlanRespo.UserPlanRepo
import com.application.intercom.data.repository.userAddEnquiry.UserAddEnquiryRepo

class AddUserEnquiryFactory(private val repository: UserAddEnquiryRepo) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddUserEnquiryViewModel(repository) as T
    }
}