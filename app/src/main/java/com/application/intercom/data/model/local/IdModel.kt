package com.application.intercom.data.model.local

import com.application.intercom.data.model.remote.manager.managerSide.serviceCharege.ManagerServiceChargeList

data class IdModel(
    val _id: String,
    val months:String,
    val year:String
) : java.io.Serializable