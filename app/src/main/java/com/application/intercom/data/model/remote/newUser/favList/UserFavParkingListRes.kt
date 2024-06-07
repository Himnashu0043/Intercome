package com.application.intercom.data.model.remote.newUser.favList

data class UserFavParkingListRes(
    var data: ArrayList<Data>,
    var message: String,
    var status: Int
) {
    data class Data(
        var WishListType: String,
        var __v: Int,
        var _id: String,
        var createdAt: String,
        var ownerDetail: OwnerDetail,
        var parkingId: String,
        var parkingInfo: ParkingInfo,
        var status: String,
        var updatedAt: String,
        var userId: String
    ) {
        data class OwnerDetail(
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

        data class ParkingInfo(
            var __v: Int,
            var _id: String,
            var buildingId: String,
            var createdAt: String,
            var flatId: String,
            var is_delete: Boolean,
            var owner: String,
            var parkingDate: String,
            var parkingDescription: String,
            var parkingImages: ArrayList<String>,
            var parkingListStatus: String,
            var parkingLocation: String,
            var parkingNumber: String,
            var parkingStatus: String,
            var parking_number: String,
            var price: String,
            var projectId: String,
            var status: String,
            var updatedAt: String
        )
    }
}