package com.application.intercom.data.model.remote.owner.gatePass

data class OwnerGatepassList(
    var data: Data,
    var message: String,
    var status: Int
):java.io.Serializable {
    data class Data(
        var result: ArrayList<Result>
    ):java.io.Serializable {
        data class Result(
            var __v: Int,
            var _id: String,
            var buildingId: String,
            var contactName: String,
            var createdAt: String,
            var createdBy: String,
            var description: String,
            var exitTime: String,
            var flatId: String,
            var flatInfo: FlatInfo,
            var is_delete: Boolean,
            var ownerInfo: ArrayList<OwnerInfo>,
            var passNo: String,
            var phoneNumber: String,
            var photo: ArrayList<String>,
            var status: String,
            var tenantInfo: ArrayList<TenantInfo>,
            var toDate: String,
            var updatedAt: String
        ):java.io.Serializable {
            data class FlatInfo(
                var __v: Int,
                var _id: String,
                var bathroom: Int,
                var bedroom: Int,
                var buildingId: String,
                var createdAt: String,
                var document: String,
                var flatStatus: String,
                var is_assign: Boolean,
                var is_delete: Boolean,
                var is_home: Boolean,
                var name: String,
                var owner: String,
                var projectId: String,
                var sqft: Int,
                var status: String,
                var tenant: String,
                var tenantId: String,
                var updatedAt: String
            ):java.io.Serializable

            data class OwnerInfo(
                var __v: Int,
                var _id: String,
                var accnHolder: String,
                var accnNumber: String,
                var addDoc: String,
                var address: String,
                var adminId: String,
                var availableContacts: Int,
                var bankName: String,
                var branchName: String,
                var createdAt: String,
                var createdBy: String,
                var defaultLanguage: String,
                var description: String,
                var deviceToken: String,
                var deviceType: String,
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
            ):java.io.Serializable {
                data class Location(
                    var coordinates: List<Double>,
                    var type: String
                ):java.io.Serializable
            }

            data class TenantInfo(
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
                var roomRent: Int,
                var shiftEnd: String,
                var shiftStart: String,
                var status: String,
                var subscription_active: Boolean,
                var updatedAt: String
            ):java.io.Serializable {
                data class Location(
                    var coordinates: List<Double>,
                    var type: String
                ):java.io.Serializable
            }
        }
    }
}