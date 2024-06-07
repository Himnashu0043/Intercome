package com.application.intercom.data.model.local

data class UserChangePasswordLocalModel(
    val newPassword: String,
    val oldPassword: String
)
