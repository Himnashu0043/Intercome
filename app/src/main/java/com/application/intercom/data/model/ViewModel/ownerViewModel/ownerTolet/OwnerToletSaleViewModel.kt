package com.application.intercom.data.model.ViewModel.ownerViewModel.ownerTolet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.intercom.data.model.local.owner.propertyToletSale.OwnerPropertySalePostModel
import com.application.intercom.data.model.local.owner.propertyToletSale.OwnerPropertyToletPostModel
import com.application.intercom.data.model.remote.CommonResponse
import com.application.intercom.data.model.remote.owner.amenities.OwnerAmenitiesListRes
import com.application.intercom.data.model.remote.owner.noticBoard.OwnerNoticBoardListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.data.repository.ownerRepo.ownerTolet.OwnerToletSale
import kotlinx.coroutines.launch

class OwnerToletSaleViewModel(private val repository: OwnerToletSale) : ViewModel() {
    private val _getOwnerAmenitiesLiveData = MutableLiveData<EmpResource<OwnerAmenitiesListRes>>()
    val getOwnerAmenitiesLiveData: LiveData<EmpResource<OwnerAmenitiesListRes>>
        get() = _getOwnerAmenitiesLiveData

    fun getOwnerAmenitiesList(token: String) {
        viewModelScope.launch {
            _getOwnerAmenitiesLiveData.value = EmpResource.Loading
            _getOwnerAmenitiesLiveData.value = repository.owneramenities(token)
        }
    }

    private val _addFlatOwnerLiveData = MutableLiveData<EmpResource<CommonResponse>>()
    val addFlatOwnerLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _addFlatOwnerLiveData

    fun addFlatOwner(token: String, model: OwnerPropertyToletPostModel) {
        viewModelScope.launch {
            _addFlatOwnerLiveData.value = EmpResource.Loading
            _addFlatOwnerLiveData.value = repository.addFlatOwner(token,model)
        }
    }

    private val _addParkingOwnerLiveData = MutableLiveData<EmpResource<CommonResponse>>()
    val addParkingOwnerLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _addParkingOwnerLiveData

    fun addParkingOwner(token: String, model: OwnerPropertySalePostModel) {
        viewModelScope.launch {
            _addParkingOwnerLiveData.value = EmpResource.Loading
            _addParkingOwnerLiveData.value = repository.addParkingOwner(token,model)
        }
    }
}