package com.application.intercom.data.model.remote.manager.managerSide

data class ManagerGateKepperListRes(
    var data: Data,
    var message: String,
    var status: Int
) : java.io.Serializable {
    data class Data(
        var result: ArrayList<Result>
    ) : java.io.Serializable {
        data class Result(
            var __v: Int,
            var _id: String,
            var buildingId: String,
            var createdAt: String,
            var document: String,
            var documentBack: String,
            var documentFront: String,
            var fatherName: String,
            var fullName: String,
            var is_delete: Boolean,
            var manager: String,
            var mobileNumber: String,
            var motherName: String,
            var nidBack: String,
            var nidFront: String,
            var nidNumber: String,
            var password: String,
            var photo: String,
            var referenceMobile: String,
            var referenceName: String,
            var referenceNidBack: String,
            var referenceNidFront: String,
            var referenceNidNumber: String,
            var shiftEndTime: String,
            var shiftStartTime: String,
            var status: String,
            var updatedAt: String,
            var plainPassword: String,
            var user: User
        ) : java.io.Serializable {
            data class User(
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
                var documentBack: String,
                var documentFront: String,
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
                var referenceNidBack: String,
                var role: String,
                var shiftEnd: String,
                var shiftStart: String,
                var status: String,
                var subscription_active: Boolean,
                var updatedAt: String
            ) : java.io.Serializable {
                data class Location(
                    var coordinates: List<Double>,
                    var type: String
                ) : java.io.Serializable
            }
        }
    }
}