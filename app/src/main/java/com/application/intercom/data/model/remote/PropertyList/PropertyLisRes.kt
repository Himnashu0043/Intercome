package com.application.intercom.data.model.remote.PropertyList

import com.application.intercom.data.model.local.localModel.LocalAmentitiesModel
import java.io.Serializable

data class PropertyLisRes(
    var data: ArrayList<Data>,
    var message: String,
    var status: Int
) : Serializable {
    data class Data(
        var _id: String,
        var accHolderNAme: String,
        var accNumber: String,
        var addedBy: String? = null,
        var address: String,
        var amentities: ArrayList<Amentity>,
        var amount: Int,
        var approvedStatus: String,
        var bankName: String,
        var bathroom: Int? = null,
        var bedroom: Int? = null,
        var branchName: String,
        var buildingName: String,
        var building_number: String,
        var createdAt: String,
        var createdBy: String,
        var description: String,
        var distance: Double,
        var district: String,
        var division: String,
        var flatDetail: FlatDetail? = null,
        var flatInfo: FlatInfo? = null,
        var flatStatus: String,
        var floorLevel: Int,
        var gateKeepers: List<GateKeeper>,
        var isCommFeedSame: Boolean,
        var isGateKeeperSame: Boolean,
        var isManagerSame: Boolean,
        var isWishList: Boolean,
        var is_assign: Boolean,
        var is_delete: Boolean,
        var latitude: Double,
        var location: Location,
        var longitude: Double,
        var manager: String,
        var mfs: String,
        var mfsAccnHolderName: String,
        var mfsAccnNumber: String,
        var owner: String,
        var ownerDetail: OwnerDetail,
        var packageFlats: String,
        var packageId: String,
        var packageName: String,
        var packageTypes: String,
        var payMethod: String,
        var paymentType: String,
        var photos: ArrayList<String>,
        var policeStation: String,
        var postOffice: String,
        var price: Int,
        var projectId: String,
        var propertyType: String?,
        var sqft: Int,
        var status: String,
        var subPropertyType: String,
        var subscription_active: Boolean,
        var title: String,
        var totalFLats: Int,
        var totalFloor: Int,
        var updatedAt: String,
        var validFrom: String,
        var validUpto: String
    ) : Serializable {
        data class Amentity(
            var _id: String,
            var image: String,
            var name: String
        ) : Serializable

        data class FlatDetail(
            var __v: Int,
            var _id: String,
            var bathroom: Int? = null,
            var bedroom: Int? = null,
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
            var tenant: String,
            var tenantId: String,
            var updatedAt: String
        ) : Serializable

        data class FlatInfo(
            var __v: Int,
            var _id: String,
            var amentities: ArrayList<Data.Amentity>,
            var buildingId: String,
            var createdAt: String,
            var description: String,
            var flatId: String,
            var flatStatus: String,
            var includeParking: Boolean,
            var is_delete: Boolean,
            var owner: String,
            var parkingPrice: Int,
            var photo: ArrayList<String>,
            var price: Int,
            var projectId: String,
            var status: String,
            var tolet_flat_number: String,
            var updatedAt: String
        ) : Serializable {
            data class Amentity(
                var _id: String,
                var image: String,
                var name: String
            ) : Serializable
        }

        data class GateKeeper(
            var _id: String,
            var gateKeeperId: String
        ) : Serializable

        data class Location(
            var coordinates: List<Double>,
            var type: String
        ) : Serializable

        data class OwnerDetail(
            var __v: Int,
            var _id: String,
            var accnHolder: String,
            var accnNumber: String,
            var activePlan: String,
            var addDoc: String,
            var address: String,
            var adminId: String,
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
            var deviceToken: String,
            var deviceType: String,
            var documentBack: String,
            var documentFront: String,
            var duration: Int,
            var email: String,
            var fatherName: String,
            var fullName: String,
            var is_assign: Boolean,
            var is_delete: Boolean,
            var is_profile: Boolean,
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
            var referenceNidBack: String,
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
        ) : Serializable {
            data class Location(
                var coordinates: List<Double>,
                var type: String
            ) : Serializable
        }
    }
}