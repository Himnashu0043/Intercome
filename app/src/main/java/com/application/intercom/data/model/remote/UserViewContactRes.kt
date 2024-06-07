package com.application.intercom.data.model.remote

data class UserViewContactRes(
    var data: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var dataUpdate: DataUpdate,
        var insertedData: InsertedData
    ) {
        data class DataUpdate(
            var __v: Int,
            var _id: String,
            var accnHolder: String,
            var accnNumber: String,
            var activePlan: String,
            var addDoc: String,
            var amount: Int,
            var availableContacts: Int,
            var bankName: String,
            var branchName: String,
            var city: String,
            var contacts: Int,
            var createdAt: String,
            var createdBy: String,
            var defaultLanguage: String,
            var description: String,
            var documentBack: String,
            var documentFront: String,
            var duration: Int,
            var email: String,
            var fatherName: String,
            var fullName: String,
            var is_assign: Boolean,
            var is_delete: Boolean,
            var is_profile: Boolean,
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
            var password: String,
            var payMenthod: String,
            var phoneNumber: String,
            var plainPassword: String,
            var planTitle: String,
            var profilePic: String,
            var refName: String,
            var refNid: String,
            var refNidImage: String,
            var refNumber: String,
            var referenceNidBack: String,
            var renewed_Date: String,
            var role: String,
            var shiftEnd: String,
            var shiftStart: String,
            var status: String,
            var subscription_Date: String,
            var subscription_active: Boolean,
            var totalContacts: Int,
            var updatedAt: String
        ) {
            data class Location(
                var coordinates: List<Double>,
                var type: String
            )
        }

        data class InsertedData(
            var __v: Int,
            var _id: String,
            var createdAt: String,
            var flatId: String,
            var `for`: String,
            var is_active: Boolean,
            var updatedAt: String,
            var userId: String
        )
    }
}