package com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome

data class GateKeeperDetailsRes(
    var `data`: Data,
    var message: String,
    var status: Int
) {
    data class Data(
        var userDetails: UserDetails
    ) {
        data class UserDetails(
            var __v: Int,
            var _id: String,
            var accnHolder: String,
            var accnNumber: String,
            var addDoc: String,
            var adminId: String,
            var availableContacts: Int,
            var bankName: String,
            var branchName: String,
            var buildingId: BuildingId,
            var createdAt: String,
            var createdBy: String,
            var defaultLanguage: String,
            var description: String,
            var deviceToken: String,
            var documentBack: String,
            var documentFront: String,
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
            data class BuildingId(
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
                var manager: Manager,
                var mfs: String,
                var mfsAccnHolderName: String,
                var mfsAccnNumber: String,
                var packageFlats: String,
                var packageId: PackageId,
                var payMethod: String,
                var paymentType: String,
                var photos: List<String>,
                var policeStation: String,
                var postOffice: String,
                var projectId: ProjectId,
                var status: String,
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

                data class Manager(
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
                    var documentBack: String,
                    var documentFront: String,
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
                        var coordinates: List<Double>,
                        var type: String
                    )
                }

                data class PackageId(
                    var __v: Int,
                    var _id: String,
                    var adminId: String,
                    var admin_plan_number: String,
                    var createdAt: String,
                    var description: String,
                    var `for`: String,
                    var image: String,
                    var is_delete: Boolean,
                    var is_trial: Boolean,
                    var modules: List<String>,
                    var monthlyPrice: Int,
                    var quarterlyPrice: Int,
                    var status: String,
                    var title: String,
                    var trialDays: Any,
                    var updatedAt: String,
                    var yearlyPrice: Int
                )

                data class ProjectId(
                    var __v: Int,
                    var _id: String,
                    var address: String,
                    var adminId: String,
                    var buildingCount: Int,
                    var contactPerson: String,
                    var createdAt: String,
                    var createdBy: String,
                    var defaultLanguage: String,
                    var description: String,
                    var deviceToken: String,
                    var deviceType: String,
                    var email: String,
                    var flatCount: Int,
                    var is_delete: Boolean,
                    var jwtToken: String,
                    var location: Location,
                    var name: String,
                    var notification_status: Boolean,
                    var password: String,
                    var phoneNumber: String,
                    var plainPassword: String,
                    var profilePic: String,
                    var project_number: String,
                    var status: String,
                    var updatedAt: String
                ) {
                    data class Location(
                        var coordinates: List<Double>,
                        var type: String
                    )
                }
            }

            data class Location(
                var coordinates: List<Double>,
                var type: String
            )
        }
    }
}