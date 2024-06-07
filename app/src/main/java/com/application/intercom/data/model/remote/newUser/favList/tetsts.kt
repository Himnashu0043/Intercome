package com.application.intercom.data.model.remote.newUser.favList

data class tetsts(
    var `data`: List<Data>,
    var message: String,
    var status: Int
) {
    data class Data(
        var WishListType: String,
        var __v: Int,
        var _id: String,
        var buildingInfo: List<BuildingInfo>,
        var createdAt: String,
        var flatDetail: FlatDetail,
        var flatInfo: FlatInfo,
        var ownerInfo: List<OwnerInfo>,
        var propertyId: String,
        var status: String,
        var updatedAt: String,
        var userId: String
    ) {
        data class BuildingInfo(
            var __v: Int,
            var _id: String,
            var accHolderNAme: String,
            var accNumber: String,
            var address: String,
            var aggrement: String,
            var amount: Int,
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
            var packageId: String,
            var payMethod: String,
            var paymentType: String,
            var photos: List<String>,
            var policeStation: String,
            var postOffice: String,
            var projectId: String,
            var propertyType: String,
            var status: String,
            var subPropertyType: String,
            var subscription_active: Boolean,
            var totalFLats: Int,
            var updatedAt: String,
            var validFrom: String,
            var validUpto: String
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
            var addedBy: String,
            var address: String,
            var adminId: String,
            var amentities: List<Amentity>,
            var approvedStatus: String,
            var bathroom: Int,
            var bedroom: Int,
            var buildingId: String,
            var createdAt: String,
            var description: String,
            var email: String,
            var flatId: String,
            var flatStatus: String,
            var floorLevel: Int,
            var includeParking: Boolean,
            var is_assign: Boolean,
            var is_delete: Boolean,
            var latitude: Double,
            var location: Location,
            var longitude: Double,
            var name: String,
            var owner: String,
            var parkingPrice: Int,
            var phoneNumber: String,
            var photo: List<String>,
            var price: Int,
            var projectId: String,
            var propertyType: String,
            var sqft: Int,
            var status: String,
            var subPropertyType: String,
            var title: String,
            var tolet_flat_number: String,
            var totalFloor: Int,
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
            var is_assign: Boolean,
            var is_delete: Boolean,
            var name: String,
            var owner: String,
            var projectId: String,
            var sqft: Int,
            var status: String,
            var updatedAt: String
        )

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
            var email: String,
            var fatherName: String,
            var fullName: String,
            var is_assign: Boolean,
            var is_delete: Boolean,
            var is_profile: Boolean,
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
            var totalContacts: Int,
            var updatedAt: String
        ) {
            data class Location(
                var coordinates: List<Double>,
                var type: String
            )
        }
    }
}