package com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome

data class FlatListOfVisitorGateKeeperList(
    var data: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var result: ArrayList<Result>
    ) {
        data class Result(
            var __v: Int,
            var _id: String,
            var addedBy: String,
            var address: String,
            var buildingId: String,
            var createdAt: String,
            var createdBy: String,
            var currentStatus: String?=null,
            var document: List<String>,
            var entryTime: String?,
            var exitTime: String?,
            var flatId: String,
            var fromDate: String,
            var fromTime: String,
            var is_delete: Boolean,
            var mobileNumber: String,
            var note: String,
            var owner: String,
            var ownerInfo: List<OwnerInfo>,
            var photo: String,
            var regularVisitorStatus: String,
            var status: String,
            var tenantInfo: List<Any>,
            var toDate: String,
            var toTime: String,
            var updatedAt: String,
            var visitCategoryId: String,
            var visitCategoryName: String,
            var visitorName: String,
            var visitorStatus: String,
            var visitorType: String
        ) {
            data class OwnerInfo(
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
                    var coordinates: List<Double>,
                    var type: String
                )
            }
        }
    }
}