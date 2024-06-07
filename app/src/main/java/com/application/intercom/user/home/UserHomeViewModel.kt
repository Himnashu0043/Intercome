package com.application.intercom.user.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.intercom.data.model.local.PropertyListPostModel.PropertyListUserPostModel
import com.application.intercom.data.model.local.UserViewContact.UserViewContactPostModel
import com.application.intercom.data.model.local.newFlow.StatusUpdatePostModel
import com.application.intercom.data.model.local.newFlow.UserPlanDetailsList
import com.application.intercom.data.model.remote.*
import com.application.intercom.data.model.remote.PropertyList.PropertyLisRes
import com.application.intercom.data.model.remote.UserParkingListRes.UserParkingList
import com.application.intercom.data.model.remote.newUser.AddUserNewPropertyList
import com.application.intercom.data.model.remote.newUser.MyList.ActiveNewPhaseList
import com.application.intercom.data.model.remote.newUser.MyList.RejectNewPhaseList
import com.application.intercom.data.model.remote.newUser.NewUserAmenitiesList
import com.application.intercom.data.model.remote.newUser.favList.UserFavListRes
import com.application.intercom.data.model.remote.newUser.favList.UserFavParkingListRes
import com.application.intercom.data.model.remote.newUser.favList.UserFavPropertyListRes
import com.application.intercom.data.model.remote.owner.owner_communityChat.OwnerCommunityChatList
import com.application.intercom.data.model.remote.userCode.UserPromoCodeRes
import com.application.intercom.data.model.remote.userCreateRoom.UserCreateRoomRes
import com.application.intercom.data.model.remote.userCreateRoom.UserMessageHistoryList
import com.application.intercom.data.model.remote.userCreateRoom.UserPropertyChatList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.user.newflow.modal.EditUserTestPostModel
import com.application.intercom.user.newflow.modal.UserTestPostModel
import kotlinx.coroutines.launch

class UserHomeViewModel(private val repository: UserHomeRepository) : ViewModel() {

    private val _userAdvertisementLiveData =
        MutableLiveData<EmpResource<UserAdvertimentNewResponse>>()
    val userAdvertisementLiveData: LiveData<EmpResource<UserAdvertimentNewResponse>>
        get() = _userAdvertisementLiveData

    fun getUserAdvertisementList(token: String) {
        viewModelScope.launch {
            _userAdvertisementLiveData.value = EmpResource.Loading
            _userAdvertisementLiveData.value = repository.normalUserAdvertisementList(token)
        }
    }

    private val _userNotificationLiveData =
        MutableLiveData<EmpResource<NotificationList>>()
    val userNotificationLiveData: LiveData<EmpResource<NotificationList>>
        get() = _userNotificationLiveData

    fun getUserNotificationList(token: String) {
        viewModelScope.launch {
            _userNotificationLiveData.value = EmpResource.Loading
            _userNotificationLiveData.value = repository.normalUserNotification(token)
        }
    }

    private val _userPropertyListLiveData = MutableLiveData<EmpResource<PropertyLisRes>>()
    val userPropertyListLiveData: LiveData<EmpResource<PropertyLisRes>>
        get() = _userPropertyListLiveData

    fun getUserPropertyListList(token: String, model: PropertyListUserPostModel) {
        viewModelScope.launch {
            _userPropertyListLiveData.value = EmpResource.Loading
            _userPropertyListLiveData.value = repository.normalPropertyList(token, model)
        }
    }

    private val _userParkingLiveData = MutableLiveData<EmpResource<UserParkingList>>()
    val userParkingListLiveData: LiveData<EmpResource<UserParkingList>>
        get() = _userParkingLiveData

    fun getUserParkingListList(token: String, model: PropertyListUserPostModel) {
        viewModelScope.launch {
            _userParkingLiveData.value = EmpResource.Loading
            _userParkingLiveData.value = repository.normalParkingList(token, model)
        }
    }

    private val _userViewContactLiveData = MutableLiveData<EmpResource<UserViewContactRes>>()
    val userViewContactLiveData: LiveData<EmpResource<UserViewContactRes>>
        get() = _userViewContactLiveData

    fun getuserViewContact(token: String, model: UserViewContactPostModel) {
        viewModelScope.launch {
            _userViewContactLiveData.value = EmpResource.Loading
            _userViewContactLiveData.value = repository.userViewContact(token, model)
        }
    }

    private val _userCreateRoomLiveData = MutableLiveData<EmpResource<UserCreateRoomRes>>()
    val userCreateRoomLiveData: LiveData<EmpResource<UserCreateRoomRes>>
        get() = _userCreateRoomLiveData

    fun userCreateRoom(token: String, userId: String, chatType: String) {
        viewModelScope.launch {
            _userCreateRoomLiveData.value = EmpResource.Loading
            _userCreateRoomLiveData.value = repository.userCreateRoom(token, userId, chatType)
        }
    }

    private val _userMessageHistoryLiveData = MutableLiveData<EmpResource<UserMessageHistoryList>>()
    val userMessageHistoryLiveData: LiveData<EmpResource<UserMessageHistoryList>>
        get() = _userMessageHistoryLiveData

    fun userMessageHistory(token: String, roomId: String, chatType: String) {
        viewModelScope.launch {
            _userMessageHistoryLiveData.value = EmpResource.Loading
            _userMessageHistoryLiveData.value =
                repository.userMessageHistory(token, roomId, chatType)
        }
    }

    private val _userRoomListLiveData = MutableLiveData<EmpResource<UserPropertyChatList>>()
    val userRoomListLiveData: LiveData<EmpResource<UserPropertyChatList>>
        get() = _userRoomListLiveData

    fun userRoomList(token: String, chatType: String) {
        viewModelScope.launch {
            _userRoomListLiveData.value = EmpResource.Loading
            _userRoomListLiveData.value =
                repository.userRoomList(token, chatType)
        }
    }

    private val _ownerCommunityRoomListLiveData =
        MutableLiveData<EmpResource<OwnerCommunityChatList>>()
    val ownerCommunityRoomListLiveData: LiveData<EmpResource<OwnerCommunityChatList>>
        get() = _ownerCommunityRoomListLiveData

    fun ownerCommunityChatRoomList(token: String, chatType: String) {
        viewModelScope.launch {
            _ownerCommunityRoomListLiveData.value = EmpResource.Loading
            _ownerCommunityRoomListLiveData.value =
                repository.ownerCommunityRoomList(token, chatType)
        }
    }

    private val _userCodeLiveData = MutableLiveData<EmpResource<UserPromoCodeRes>>()
    val userCodeLiveData: LiveData<EmpResource<UserPromoCodeRes>>
        get() = _userCodeLiveData

    fun userCode(token: String, title: String) {
        viewModelScope.launch {
            _userCodeLiveData.value = EmpResource.Loading
            _userCodeLiveData.value =
                repository.userCode(token, title)
        }
    }

    private val _userFaqLiveData = MutableLiveData<EmpResource<UserFAQList>>()
    val userFaqLiveData: LiveData<EmpResource<UserFAQList>>
        get() = _userFaqLiveData

    fun userFaq(token: String) {
        viewModelScope.launch {
            _userFaqLiveData.value = EmpResource.Loading
            _userFaqLiveData.value =
                repository.userFaq(token)
        }
    }

    private val _userNewAmenitiesLiveData = MutableLiveData<EmpResource<NewUserAmenitiesList>>()
    val userNewAmenitiesLiveData: LiveData<EmpResource<NewUserAmenitiesList>>
        get() = _userNewAmenitiesLiveData

    fun userNewAmenities(token: String) {
        viewModelScope.launch {
            _userNewAmenitiesLiveData.value = EmpResource.Loading
            _userNewAmenitiesLiveData.value =
                repository.userNewAmenities(token)
        }
    }


    private val _adduserNewPropertyListLiveData =
        MutableLiveData<EmpResource<AddUserNewPropertyList>>()
    val addUserNewPropertyListLiveData: LiveData<EmpResource<AddUserNewPropertyList>>
        get() = _adduserNewPropertyListLiveData

    fun addUserNewPropertyList(token: String, model: UserTestPostModel) {
        viewModelScope.launch {
            _adduserNewPropertyListLiveData.value = EmpResource.Loading
            _adduserNewPropertyListLiveData.value = repository.userNewProperty(token, model)
        }
    }

    private val _edituserNewPropertyListLiveData =
        MutableLiveData<EmpResource<AddUserNewPropertyList>>()
    val editUserNewPropertyListLiveData: LiveData<EmpResource<AddUserNewPropertyList>>
        get() = _edituserNewPropertyListLiveData

    fun editUserNewPropertyList(token: String, model: UserTestPostModel) {
        viewModelScope.launch {
            _edituserNewPropertyListLiveData.value = EmpResource.Loading
            _edituserNewPropertyListLiveData.value = repository.edituserNewProperty(token, model)
        }
    }

    private val _pendingLiveData = MutableLiveData<EmpResource<ActiveNewPhaseList>>()
    val pendingListLiveData: LiveData<EmpResource<ActiveNewPhaseList>>
        get() = _pendingLiveData

    fun pendingList(token: String, status: String) {
        viewModelScope.launch {
            _pendingLiveData.value = EmpResource.Loading
            _pendingLiveData.value =
                repository.pendingList(token, status)
        }
    }

    private val _rejectLiveData = MutableLiveData<EmpResource<ActiveNewPhaseList>>()
    val rejectListLiveData: LiveData<EmpResource<ActiveNewPhaseList>>
        get() = _rejectLiveData

    fun rejectList(token: String, status: String) {
        viewModelScope.launch {
            _rejectLiveData.value = EmpResource.Loading
            _rejectLiveData.value =
                repository.rejectList(token, status)
        }
    }

    private val _activeLiveData = MutableLiveData<EmpResource<ActiveNewPhaseList>>()
    val activeListLiveData: LiveData<EmpResource<ActiveNewPhaseList>>
        get() = _activeLiveData

    fun activeList(token: String, status: String) {
        viewModelScope.launch {
            _activeLiveData.value = EmpResource.Loading
            _activeLiveData.value =
                repository.activeList(token, status)
        }
    }

    private val _statusUpdateLiveData = MutableLiveData<EmpResource<CommonResponse>>()
    val statusUpdateLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _statusUpdateLiveData

    fun statusUpdate(token: String, model: StatusUpdatePostModel) {
        viewModelScope.launch {
            _statusUpdateLiveData.value = EmpResource.Loading
            _statusUpdateLiveData.value =
                repository.statusUpdate(token, model)
        }
    }

    private val _userPlanDetailsListLiveData = MutableLiveData<EmpResource<UserPlanDetailsList>>()
    val userPlanDetailsListLiveData: LiveData<EmpResource<UserPlanDetailsList>>
        get() = _userPlanDetailsListLiveData

    fun userPlanDetails(token: String) {
        viewModelScope.launch {
            _userPlanDetailsListLiveData.value = EmpResource.Loading
            _userPlanDetailsListLiveData.value =
                repository.userPlanDetailsList(token)
        }
    }

    private val _userFavListLiveData = MutableLiveData<EmpResource<UserFavPropertyListRes>>()
    val userFavListLiveData: LiveData<EmpResource<UserFavPropertyListRes>>
        get() = _userFavListLiveData

    fun userfav(token: String, type: String) {
        viewModelScope.launch {
            _userFavListLiveData.value = EmpResource.Loading
            _userFavListLiveData.value =
                repository.userFavList(token, type)
        }
    }

    private val _userFavParkingListLiveData = MutableLiveData<EmpResource<UserFavParkingListRes>>()
    val userFavParkingListLiveData: LiveData<EmpResource<UserFavParkingListRes>>
        get() = _userFavParkingListLiveData

    fun userfavParking(token: String, type: String) {
        viewModelScope.launch {
            _userFavParkingListLiveData.value = EmpResource.Loading
            _userFavParkingListLiveData.value =
                repository.userFavParkingList(token, type)
        }
    }


    private val _userAddFavListLiveData = MutableLiveData<EmpResource<CommonResponse>>()
    val userAddFavListLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _userAddFavListLiveData

    fun userAddfavProperty(token: String, propertyId: String?, parkingId: String?) {
        viewModelScope.launch {
            _userAddFavListLiveData.value = EmpResource.Loading
            _userAddFavListLiveData.value =
                repository.userAddFavList(token, propertyId, parkingId)
        }
    }


}