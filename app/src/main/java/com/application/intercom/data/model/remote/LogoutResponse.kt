package com.application.intercom.data.model.remote

data class LogoutResponse(
    var data: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var __v: Int,
        var _id: String,
        var accnHolder: String,
        var accnNumber: String,
        var addDoc: String,
        var address: String,
        var availableContacts: Int,
        var bankName: String,
        var branchName: String,
        var createdAt: String,
        var createdBy: String,
        var defaultLanguage: String,
        var description: String,
        var deviceToken: String,
        var email: String,
        var fatherName: String,
        var fullName: String,
        var is_delete: Boolean,
        var jwtToken: String,
        var latitude: Double,
        var location: Location,
        var longitude: Double,
        var mfs: String,
        var mfsAccnNumber: String,
        var mfsHolder: String,
        var motherName: String,
        var nid: String,
        var nidImage: String,
        var notification_status: Boolean,
        var payMenthod: String,
        var phoneNumber: String,
        var profilePic: String,
        var refName: String,
        var refNid: String,
        var refNidImage: String,
        var refNumber: String,
        var role: String,
        var shiftEnd: String,
        var shiftStart: String,
        var status: String,
        var subscription_active: Boolean,
        var updatedAt: String
    ) {
        data class Location(
            var coordinates: List<Double>,
            var type: String
        )
    }

}