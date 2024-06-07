package com.application.intercom.data.model.local

data class UserUpdateProfileLocalModel(
    val fullName: String,
    val profilePic: String,
    val email: String,
    val city: String,
    val lat: Double,
    val long: Double,
    val password:String
)
