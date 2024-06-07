package com.application.intercom.data.repository

import com.application.intercom.data.api.ApiService
import com.application.intercom.data.model.local.*

class LoginRepository(private val apiService: ApiService):EmpBaseRepository() {


    suspend fun userSendPhoneOtp(model: UserSendPhoneOtpLocalModel)=safeApiCall {
        apiService.userSendPhoneOtp(model)
    }

    suspend fun userSendForgotPasswordPhoneOtp(model: UserSendForgotPhoneOtpLocalModel)=safeApiCall {
        apiService.userSendForgotPasswordPhoneOtp(model)
    }
    suspend fun userOtpLogin(model: UserOtpLoginLocalModel)=safeApiCall {
        apiService.userOtpLogin(model)
    }

    suspend fun userLogout(token: String,deviceToken:String)=safeApiCall {
        apiService.userLogout(token,deviceToken)
    }

    suspend fun userLoginUsingPassword(model: UserLoginWithPasswordLocalModel)=safeApiCall {
        apiService.userLoginWithPassword(model)
    }

    suspend fun userForgotPassword(model: UserForgetPasswordLocalModel)=safeApiCall {
        apiService.userForgetPassword(model)
    }

    suspend fun userChangePassword(token: String,model: UserChangePasswordLocalModel)=safeApiCall {
        apiService.userChangePassword(token,model)
    }


    suspend fun userVerifyOtpCommon(model: UserVerifyOtpCommonLocalModel)=safeApiCall {
        apiService.userVerifyOtpCommon(model)
    }
    suspend fun getTutorial() = safeApiCall {
        apiService.getTutorial()
    }
}