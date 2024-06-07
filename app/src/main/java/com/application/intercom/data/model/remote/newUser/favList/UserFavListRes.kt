package com.application.intercom.data.model.remote.newUser.favList

data class UserFavListRes(
    var data: ArrayList<Data>,
    var message: String,
    var status: Int
) {
    data class Data(
        var WishListType: String,
        var __v: Int,
        var _id: String,
        var buildingInfo: ArrayList<BuildingInfo>,
        var createdAt: String,
        var flatDetail: FlatDetail,
        var flatInfo: FlatInfo,
        var ownerInfo: ArrayList<OwnerInfo>,
        var propertyId: String,
        var status: String,
        var updatedAt: String,
        var userId: String,
        var title: String
    ) {
        data class BuildingInfo(
            var __v: Int,
            var _id: String,
            var accNumber: String,
            var aggrement: String,
            var bankName: String,
            var branchName: String,
            var buildingName: String,
            var building_number: String,
            var createdAt: String,
            var description: String,
            var district: String,
            var division: String,
            var gateKeepers: List<GateKeeper>,
            var isCommFeedSame: Boolean,
            var isGateKeeperSame: Boolean,
            var isManagerSame: Boolean,
            var is_delete: Boolean,
            var latitude: Double,
            var location: Location,
            var longitude: Double,
            var manager: String,
            var mfs: String,
            var mfsAccnHolderName: String,
            var mfsAccnNumber: String,
            var packageFlats: String,
            var payMethod: String,
            var photos: List<String>,
            var policeStation: String,
            var postOffice: String,
            var projectId: String,
            var propertyType: String,
            var status: String,
            var subscription_active: Boolean,
            var totalFLats: Int,
            var updatedAt: String
        ) {
            data class GateKeeper(
                var _id: String,
                var gateKeeperId: String
            )

            data class Location(
                var coordinates: List<Double>,
                var type: String
            )
        }

        data class FlatDetail(
            var __v: Int,
            var _id: String,
            var address: String,
            var amentities: List<Amentity>,
            var bathroom: Int,
            var bedroom: Int,
            var buildingId: String,
            var createdAt: String,
            var description: String,
            var district: String,
            var flatId: String,
            var floorLevel: Int,
            var includeParking: Boolean,
            var is_assign: Boolean,
            var is_delete: Boolean,
            var latitude: Double,
            var location: Location,
            var longitude: Double,
            var owner: String,
            var parkingPrice: Any,
            var photo: ArrayList<String>,
            var photos: ArrayList<String>,
            var pinCode: Int,
            var policeStation: String,
            var postOffice: String,
            var price: Int,
            var projectId: String,
            var propertyType: String,
            var sqft: Int,
            var status: String,
            var subPropertyType: String,
            var title: String,
            var tolet_flat_number: String,
            var totalFloor: Int,
            var type: String,
            var updatedAt: String
        ) {
            data class Amentity(
                var _id: String,
                var image: String,
                var name: String
            )

            data class Location(
                var coordinates: List<Double>,
                var type: String
            )
        }

        data class FlatInfo(
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
        )

        data class OwnerInfo(
            var __v: Int,
            var _id: String,
            var accnHolder: String,
            var accnNumber: String,
            var activePlan: String,
            var addDoc: String,
            var address: String,
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
            var duration: Int,
            var email: String,
            var fatherName: String,
            var fullName: String,
            var is_delete: Boolean,
            var jwtToken: String,
            var lastActivityDate: String,
            var lastActivityTimeStamp: Long,
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
            var preferences: List<Any>,
            var profilePic: String,
            var refName: String,
            var refNid: String,
            var refNidImage: String,
            var refNumber: String,
            var referralCount: Int,
            var reffer_code_generated: Boolean,
            var reffer_code_use_status: Boolean,
            var renewed_Date: String,
            var role: String,
            var shiftEnd: String,
            var shiftStart: String,
            var status: String,
            var subscription_Date: String,
            var subscription_active: Boolean,
            var totalCancelAmount: Int,
            var totalCancelOrders: Int,
            var totalOrders: Int,
            var totalReturnAmount: Int,
            var totalReturnOrders: Int,
            var totalSpent: Int,
            var updatedAt: String
        ) {
            data class Location(
                var coordinates: List<Double>,
                var type: String
            )
        }
    }
}