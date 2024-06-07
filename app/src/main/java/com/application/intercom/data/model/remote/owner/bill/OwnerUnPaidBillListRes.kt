package com.application.intercom.data.model.remote.owner.bill

data class OwnerUnPaidBillListRes(
    var data: Data, var message: String, var status: Int
) : java.io.Serializable {
    data class Data(
        var result: ArrayList<Result>
    ) : java.io.Serializable {
        data class Result(
            var __v: Int,
            var _id: String,
            var amount: Int,
            var billType: String,
            var buildingId: String,
            var categoryId: String,
            var createdAt: String,
            var date: String,
            var flatId: String,
            var flatInfo: List<FlatInfo>,
            var is_active: Boolean,
            var is_delete: Boolean,
            var is_notify: Boolean,
            var manager: String,
            var managerInfo: List<ManagerInfo>,
            var owner: String,
            var ownerInfo: List<OwnerInfo>,
            var serviceCategory: ArrayList<ServiceCategory>,
            var status: String,
            var subAdminId: String,
            var updatedAt: String,
            var userBillStatus: String,
            var userType: String,
            var notifyDate: String?,
            var notifyEndDate: String?,
            var notifyStartDate: String?,
            var recieptLink:String?,
            var dueDate:String?,
            var is_bill_type_new:String?,
            var referenceNo:String?,
            var uploadDocument:String?,
            var rejectReason:String?


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
                var updatedAt: String
            ) : java.io.Serializable

            data class ManagerInfo(
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
            ) : java.io.Serializable {
                data class Location(
                    var coordinates: List<Double>, var type: String
                ) : java.io.Serializable
            }

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
            ) : java.io.Serializable {
                data class Location(
                    var coordinates: List<Double>, var type: String
                ) : java.io.Serializable
            }

            data class ServiceCategory(
                var __v: Int,
                var _id: String,
                var createdAt: String,
                var image: String,
                var is_delete: Boolean,
                var name: String,
                var status: String,
                var updatedAt: String
            ) : java.io.Serializable
        }
    }
}