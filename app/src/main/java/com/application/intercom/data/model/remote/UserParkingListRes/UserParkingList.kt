package com.application.intercom.data.model.remote.UserParkingListRes

data class UserParkingList(
    var data: ArrayList<Data>,
    var message: String,
    var status: Int
) : java.io.Serializable {
    data class Data(
        var __v: Int,
        var _id: String,
        var accNumber: String,
        var aggrement: String,
        var branchName: String,
        var buildingName: String,
        var building_number: String,
        var createdAt: String,
        var createdBy: String,
        var description: String?,
        var distance: Double,
        var district: String,
        var division: String,
        var flatDetail: FlatDetail,
        var gateKeepers: List<GateKeeper>,
        var isCommFeedSame: Boolean,
        var isGateKeeperSame: Boolean,
        var isManagerSame: Boolean,
        var is_delete: Boolean,
        var latitude: Double,
        var location: Location,
        var locs: Locs,
        var longitude: Double,
        var manager: String,
        var mfs: String,
        var mfsAccnNumber: String,
        var ownerDetail: OwnerDetail,
        var parkingInfo: ParkingInfo,
        var payMethod: String,
        var photos: List<String>,
        var policeStation: String,
        var postOffice: String,
        var projectId: String,
        var status: String,
        var subscription_active: Boolean,
        var totalFLats: Int,
        var updatedAt: String,
        var isWishList:Boolean
    ) : java.io.Serializable {
        data class FlatDetail(
            var __v: Int,
            var _id: String,
            var bathroom: Int,
            var bedroom: Int,
            var buildingId: String,
            var createdAt: String,
            var document: String,
            var flatStatus: String,
            var is_delete: Boolean,
            var name: String,
            var owner: String,
            var sqft: Int,
            var status: String,
            var updatedAt: String
        ): java.io.Serializable

        data class GateKeeper(
            var _id: String,
            var gateKeeperId: String
        ) : java.io.Serializable

        data class Location(
            var coordinates: List<Double>,
            var type: String
        ) : java.io.Serializable

        data class Locs(
            var coordinates: List<Double>,
            var type: String
        ) : java.io.Serializable

        data class OwnerDetail(
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
            var defaultLanguage: String,
            var description: String,
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
        ) : java.io.Serializable {
            data class Location(
                var coordinates: List<Double>,
                var type: String
            ) : java.io.Serializable
        }

        data class ParkingInfo(
            var __v: Int,
            var _id: String,
            var buildingId: String,
            var createdAt: String,
            var flatId: String,
            var is_delete: Boolean,
            var parkingDate: String,
            var parkingDescription: String?,
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
        ) : java.io.Serializable
    }
}