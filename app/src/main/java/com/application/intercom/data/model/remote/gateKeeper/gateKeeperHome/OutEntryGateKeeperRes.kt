package com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome

data class OutEntryGateKeeperRes(
    var `data`: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var acknowledged: Boolean,
        var matchedCount: Int,
        var modifiedCount: Int,
        var upsertedCount: Int,
        var upsertedId: Any
    )
}