package com.application.intercom.user.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.intercom.data.model.local.*
import com.application.intercom.data.model.remote.*
import com.application.intercom.data.model.remote.getTutorial.GetTutorialList

import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: LoginRepository) : ViewModel() {


    private val _loginLiveData = MutableLiveData<EmpResource<SendPhoneOtpResponse>>()
    val loginLiveData: LiveData<EmpResource<SendPhoneOtpResponse>>
        get() = _loginLiveData

    fun sendPhoneOtp(model: UserSendPhoneOtpLocalModel) {
        viewModelScope.launch {
            _loginLiveData.value = EmpResource.Loading
            _loginLiveData.value = repository.userSendPhoneOtp(model)
        }
    }

    private val _forgetPasswordPhoneOtpLiveData =
        MutableLiveData<EmpResource<SendPhoneOtpResponse>>()
    val forgetPasswordPhoneOtpLiveData: LiveData<EmpResource<SendPhoneOtpResponse>>
        get() = _forgetPasswordPhoneOtpLiveData

    fun sendForgotPasswordPhoneOtp(model: UserSendForgotPhoneOtpLocalModel) {
        viewModelScope.launch {
            _forgetPasswordPhoneOtpLiveData.value = EmpResource.Loading
            _forgetPasswordPhoneOtpLiveData.value = repository.userSendForgotPasswordPhoneOtp(model)
        }
    }

    private val _otpLoginLiveData = MutableLiveData<EmpResource<OtpLoginResponse>>()
    val otpLoginLiveData: LiveData<EmpResource<OtpLoginResponse>>
        get() = _otpLoginLiveData

    fun userOtpLogin(model: UserOtpLoginLocalModel) {
        viewModelScope.launch {
            _otpLoginLiveData.value = EmpResource.Loading
            _otpLoginLiveData.value = repository.userOtpLogin(model)
        }
    }

    private val _logoutLiveData = MutableLiveData<EmpResource<LogoutResponse>>()
    val logoutLiveData: LiveData<EmpResource<LogoutResponse>>
        get() = _logoutLiveData

    fun userLogout(token: String,deviceToken:String) {
        viewModelScope.launch {
            _logoutLiveData.value = EmpResource.Loading
            _logoutLiveData.value = repository.userLogout(token,deviceToken)
        }
    }

    private val _loginUsingPasswordLiveData =
        MutableLiveData<EmpResource<LoginWithPasswordResponse>>()
    val loginUsingPasswordLiveData: LiveData<EmpResource<LoginWithPasswordResponse>>
        get() = _loginUsingPasswordLiveData

    fun userloginUsingPassword(model: UserLoginWithPasswordLocalModel) {
        viewModelScope.launch {
            _loginUsingPasswordLiveData.value = EmpResource.Loading
            _loginUsingPasswordLiveData.value = repository.userLoginUsingPassword(model)
        }
    }

    private val _resetPasswordLiveData = MutableLiveData<EmpResource<CommonResponse>>()
    val resetPasswordLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _resetPasswordLiveData

    fun userForgotPassword(model: UserForgetPasswordLocalModel) {
        viewModelScope.launch {
            _resetPasswordLiveData.value = EmpResource.Loading
            _resetPasswordLiveData.value = repository.userForgotPassword(model)
        }
    }

    private val _changePasswordLiveData = MutableLiveData<EmpResource<CommonResponse>>()
    val changePasswordLiveData: LiveData<EmpResource<CommonResponse>>
        get() = _changePasswordLiveData

    fun userChangePassword(token: String, model: UserChangePasswordLocalModel) {
        viewModelScope.launch {
            _changePasswordLiveData.value = EmpResource.Loading
            _changePasswordLiveData.value = repository.userChangePassword(token, model)
        }
    }

    private val _verifyOtpCommonLiveData = MutableLiveData<EmpResource<VerifyOtpCommonResponse>>()
    val verifyOtpCommonLiveData: LiveData<EmpResource<VerifyOtpCommonResponse>>
        get() = _verifyOtpCommonLiveData

    fun userVerifyOtpCommon(model: UserVerifyOtpCommonLocalModel) {
        viewModelScope.launch {
            _verifyOtpCommonLiveData.value = EmpResource.Loading
            _verifyOtpCommonLiveData.value = repository.userVerifyOtpCommon(model)
        }
    }

    private val _getTutorialLiveData = MutableLiveData<EmpResource<GetTutorialList>>()
    val getTutorialLiveData: LiveData<EmpResource<GetTutorialList>>
        get() = _getTutorialLiveData

    fun getTutorial() {
        viewModelScope.launch {
            _getTutorialLiveData.value = EmpResource.Loading
            _getTutorialLiveData.value = repository.getTutorial()
        }
    }

}