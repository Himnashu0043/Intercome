package com.application.intercom.data.model.remote.tenant.tenantSide

data class TenantComplainListRes(
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
            var compId: String,
            var complainName: String,
            var complaintBy: List<ComplaintBy>,
            var complaint_by: String,
            var createdAt: String,
            var description: String,
            var managerAction: String,
            var managerId: String,
            var photo: ArrayList<String>,
            var serviceCategory: ArrayList<ServiceCategory>?,
            var status: String,
            var updatedAt: String
        ) {
            data class ComplaintBy(
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
                var email: String,
                var fatherName: String,
                var fullName: String,
                var is_delete: Boolean,
                var jwtToken: String,
                var location: Location,
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
            )
        }
    }
}