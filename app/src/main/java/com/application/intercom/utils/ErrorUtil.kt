package com.application.intercom.utils

import android.content.Context
import com.application.intercom.utils.CommonUtil.showSnackBar
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


object ErrorUtil {
    fun handlerGeneralError(context: Context, throwable: Throwable) {
        throwable.printStackTrace()
        when (throwable) {
            is ConnectException -> showSnackBar(context, "Please turn on Internet")
            is SocketTimeoutException -> showSnackBar(context, "Socket Time Out Exception")
            is UnknownHostException -> showSnackBar(context, "No Internet Connection")
            is InternalError -> showSnackBar(context, "Internal Server Error")
            is HttpException -> {
               // showSnackBar(context, "Something went wrong")
            }
            else -> {
               // showSnackBar(context, "Something went wrong")
            }
        }
    }

}


