package com.application.intercom.data.model.remote.manager.managerHome

data class ManagerGetDetailsRes(
    var data: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var userData: UserData,
        var userDetails: UserDetails
    ) {
        data class UserData(
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
            var photos: ArrayList<String>,
            var policeStation: String,
            var postOffice: String,
            var projectId: String,
            var propertyType: String,
            var associationType: String?,
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
        data class UserDetails(
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
            var documentBack: String,
            var documentFront: String,
            var email: String,
            var fatherName: String,
            var fullName: String? = null,
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
            var referenceNidBack: String,
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
    }
}