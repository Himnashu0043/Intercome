package com.application.intercom.data.model.remote.manager.managerParking

data class ManagerParkingListRes(
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
            var buildingId: String,
            var buildingInfo: ArrayList<BuildingInfo>,
            var createdAt: String,
            var flatId: String,
            var is_delete: Boolean,
            var owner: String,
            var ownerInfo: ArrayList<OwnerInfo>,
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
        ) : java.io.Serializable {
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
                var gateKeepers: ArrayList<GateKeeper>,
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
                var packageName: String,
                var packageTypes: String,
                var payMethod: String,
                var photos: List<String>,
                var policeStation: String,
                var postOffice: String,
                var projectId: String,
                var status: String,
                var subscription_active: Boolean,
                var totalFLats: Int,
                var updatedAt: String,
                var validFrom: String,
                var validUpto: String
            ) : java.io.Serializable {
                data class GateKeeper(
                    var _id: String,
                    var gateKeeperId: String
                ) : java.io.Serializable

                data class Location(
                    var coordinates: List<Double>,
                    var type: String
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
        }
    }
}