package com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome

data class teete(
    var `data`: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var accnHolder: String,
        var accnNumber: String,
        var addDoc: String,
        var availableContacts: Int,
        var bankName: String,
        var branchName: String,
        var createdAt: String,
        var createdBy: String,
        var defaultLanguage: String,
        var description: String,
        var deviceToken: String,
        var documentBack: String,
        var documentFront: String,
        var fatherName: String,
        var fullName: String,
        var is_assign: Boolean,
        var is_delete: Boolean,
        var is_profile: Boolean,
        var jwtToken: String,
        var location: Location,
        var mfs: String,
        var mfsAccnNumber: String,
        var mfsHolder: String,
        var motherName: String,
        var nid: String,
        var nidImage: String,
        var notification_status: Boolean,
        var password: String,
        var payMenthod: String,
        var phoneNumber: String,
        var plainPassword: String,
        var profilePic: String,
        var projectId: String,
        var refName: String,
        var refNid: String,
        var refNidImage: String,
        var refNumber: String,
        var referenceNidBack: String,
        var role: String,
        var shiftEnd: String,
        var shiftStart: String,
        var status: String,
        var subscription_active: Boolean,
        var updatedAt: String
    ) {
        data class Location(
            var coordinates: List<Int>,
            var type: String
        )
    }
}