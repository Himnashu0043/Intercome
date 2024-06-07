package com.application.intercom.data.model.remote.manager.managerProperty

data class ManagerToletFlatListRes(
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
            var bathroom: Int,
            var bedroom: Int,
            var buildingId: String,
            var buildingInfo: ArrayList<BuildingInfo>,
            var createdAt: String,
            var document: String,
            var flatStatus: String,
            var is_delete: Boolean,
            var name: String,
            var owner: String,
            var ownerInfo: ArrayList<OwnerInfo>,
            var sqft: Int,
            var status: String,
            var updatedAt: String
        ) : java.io.Serializable {
            data class BuildingInfo(
                var __v: Int,
                var _id: String,
                var accHolderNAme: String,
                var accNumber: String,
                var aggrement: String,
                var amount: Int,
                var address: String,
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
                var address: String? = null,
                var availableContacts: Int,
                var bankName: String,
                var branchName: String,
                var createdAt: String,
                var createdBy: String,
                var defaultLanguage: String,
                var description: String,
                var email: String,
                var fatherName: String,
                var fullName: String? = null,
                var is_delete: Boolean,
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