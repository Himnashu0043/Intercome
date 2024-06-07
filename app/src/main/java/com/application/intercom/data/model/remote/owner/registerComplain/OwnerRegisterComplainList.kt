package com.application.intercom.data.model.remote.owner.registerComplain

data class OwnerRegisterComplainList(
    var data: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var result: ArrayList<Result>
    ) {
        data class Result(
            var __v: Int?,
            var _id: String?,
            var compId: String?,
            var complainName: String?,
            var complaintBy: ArrayList<ComplaintBy>?,
            var complaint_by: String?,
            var createdAt: String?,
            var denyReason: String?,
            var description: String?,
            var flatId: String?,
            var managerAction: String?,
            var managerId: String?,
            var photo: ArrayList<String>?,
            var serviceCategory: ArrayList<ServiceCategory>?,
            var status: String?,
            var updatedAt: String?
        ) {
            data class ComplaintBy(
                var __v: Int?,
                var _id: String?,
                var accnHolder: String?,
                var accnNumber: String?,
                var addDoc: String?,
                var address: String?,
                var adminId: String?,
                var availableContacts: Int?,
                var bankName: String?,
                var branchName: String?,
                var createdAt: String?,
                var createdBy: String?,
                var defaultLanguage: String?,
                var description: String?,
                var deviceToken: String?,
                var deviceTokenArr: ArrayList<String>?,
                var deviceType: String?,
                var documentBack: String?,
                var documentFront: String?,
                var fatherName: String?,
                var fullName: String?,
                var is_assign: Boolean?,
                var is_delete: Boolean?,
                var is_profile: Boolean?,
                var jwtToken: String?,
                var location: Location?,
                var mfs: String?,
                var mfsAccnNumber: String?,
                var mfsHolder: String?,
                var motherName: String?,
                var nid: String?,
                var nidImage: String?,
                var notification_status: Boolean?,
                var password: String?,
                var payMenthod: String?,
                var phoneNumber: String?,
                var plainPassword: String?,
                var profilePic: String?,
                var refName: String?,
                var refNid: String?,
                var refNidImage: String?,
                var refNumber: String?,
                var referenceNidBack: String?,
                var role: String?,
                var shiftEnd: String?,
                var shiftStart: String?,
                var status: String?,
                var subscription_active: Boolean?,
                var totalContacts: Int?,
                var updatedAt: String?
            ) {
                data class Location(
                    var coordinates: List<Double>?,
                    var type: String?
                )
            }

            data class ServiceCategory(
                var __v: Int?,
                var _id: String?,
                var adminId: String?,
                var category_name: String?,
                var category_number: String?,
                var createdAt: String?,
                var image: String?,
                var is_delete: Boolean?,
                var status: String?,
                var type: String?,
                var updatedAt: String?
            )
        }
    }
}