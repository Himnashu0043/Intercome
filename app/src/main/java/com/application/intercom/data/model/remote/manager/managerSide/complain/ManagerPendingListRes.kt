package com.application.intercom.data.model.remote.manager.managerSide.complain

data class ManagerPendingListRes(
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
            var compId: String,
            var complainName: String,
            var complaint_by: String,
            var createdAt: String,
            var description: String,
            var flatId: String,
            var flatInfo: List<FlatInfo>,
            var managerAction: String,
            var managerId: String,
            var photo: ArrayList<String>,
            var serviceCategory: ArrayList<ServiceCategory>,
            var status: String,
            var updatedAt: String,
            var denyReason: String?,
            var userInfo: List<UserInfo>
        ) : java.io.Serializable {
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
                var name: String,
                var owner: String,
                var sqft: Int,
                var status: String,
                var tenant: String,
                var tenantId: String,
                var updatedAt: String
            ) : java.io.Serializable

            data class ServiceCategory(
                var __v: Int,
                var _id: String,
                var category_name: String,
                var category_number: String,
                var createdAt: String,
                var image: String,
                var is_delete: Boolean,
                var status: String,
                var type: String,
                var updatedAt: String
            ) : java.io.Serializable

            data class UserInfo(
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
                var contacts: Int,
                var createdAt: String,
                var createdBy: String,
                var defaultLanguage: String,
                var description: String,
                var deviceToken: String,
                var duration: Int,
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
                var renewed_Date: String,
                var role: String,
                var shiftEnd: String,
                var shiftStart: String,
                var status: String,
                var subscription_Date: String,
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