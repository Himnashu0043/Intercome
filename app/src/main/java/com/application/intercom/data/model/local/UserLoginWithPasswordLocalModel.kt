package com.application.intercom.data.model.local

data class UserLoginWithPasswordLocalModel(
    val mobile: String,
    val password: String,
    val role: String, // user,tenant,member,manager,gatekeeper
    val defaultLanguage: String,
    val deviceToken: String,
    val deviceType: String,
    val lat:Double,
    val long:Double

)