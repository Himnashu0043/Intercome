package com.application.intercom.data.model.remote.owner.flat

data class OwnerFlatListRes(
    var data: ArrayList<Data>,
    var message: String,
    var status: Int
) : java.io.Serializable {
    data class Data(
        var __v: Int,
        var _id: String,
        var bathroom: Int,
        var bedroom: Int,
        var buildingId: String,
        var buildingInfo: ArrayList<BuildingInfo>,
        var createdAt: String,
        var document: String,
        var flatInfo: List<Any>,
        var flatStatus: String? = null,
        var is_assign: Boolean,
        var is_delete: Boolean,
        var name: String,
        var owner: String,
        var sqft: Int,
        var is_home: Boolean,
        var status: String,
        var tenant: ArrayList<Tenant>,
        var updatedAt: String,
        var isSelected: Boolean,
        var isSelected1: Boolean
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

        data class Tenant(
            var __v: Int,
            var _id: String,
            var agreement: List<String>,
            var billingDate: String,
            var buildingId: String,
            var createdAt: String,
            var dateOfOccupancy: String,
            var email: String,
            var flatId: String,
            var fullName: String,
            var includeParking: Boolean,
            var is_delete: Boolean,
            var mobileNumber: String,
            var owner: String,
            var parkingPrice: String,
            var photo: String,
            var referenceNidBack: String,
            var referenceNidFront: String,
            var referenceNidNumber: String,
            var roomRent: String,
            var status: String,
            var tenantNidBack: String,
            var tenantNidFront: String,
            var tenantNidNumber: String,
            var updatedAt: String,
            var password: String?,
            var planPassword: String?


        ) : java.io.Serializable
    }
   /* data class Data(
        var __v: Int,
        var _id: String,
        var agreement: List<String>,
        var billingDate: String,
        var buildingId: String,
        var buildingName: String,
        var createdAt: String,
        var dateOfOccupancy: String,
        var email: String,
        var flatId: FlatId,
        var fullName: String,
        var includeParking: Boolean,
        var is_delete: Boolean,
        var mobileNumber: String,
        var owner: Owner,
        var ownerName: String,
        var ownerNumber: String,
        var parkingPrice: Int,
        var photo: String,
        var referenceNidBack: String,
        var referenceNidFront: String,
        var referenceNidNumber: String,
        var roomRent: Int,
        var status: String,
        var tenant: String,
        var tenantNidBack: String,
        var tenantNidFront: String,
        var tenantNidNumber: String,
        var updatedAt: String
    ) : java.io.Serializable {
        data class FlatId(
            var __v: Int,
            var _id: String,
            var bathroom: Int,
            var bedroom: Int,
            var buildingId: BuildingId,
            var createdAt: String,
            var document: String,
            var flatStatus: String,
            var is_assign: Boolean,
            var is_delete: Boolean,
            var is_home: Boolean,
            var name: String,
            var owner: Owner,
            var projectId: String,
            var sqft: Int,
            var status: String,
            var tenant: Tenant,
            var tenantId: String,
            var updatedAt: String
        ) : java.io.Serializable {
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
                var photos: ArrayList<String>,
                var policeStation: String,
                var postOffice: String,
                var projectId: ProjectId,
                var propertyType: String,
                var status: String,
                var subPropertyType: String,
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
                ) : java.io.Serializable {
                    data class Location(
                        var coordinates: List<Double>,
                        var type: String
                    ) : java.io.Serializable
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
                ) : java.io.Serializable

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
                    var role: String,
                    var status: String,
                    var updatedAt: String
                ) : java.io.Serializable {
                    data class Location(
                        var coordinates: List<Double>,
                        var type: String
                    ) : java.io.Serializable
                }
            }

            data class Owner(
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
                var totalContacts: Int,
                var updatedAt: String
            ) : java.io.Serializable {
                data class Location(
                    var coordinates: List<Int>,
                    var type: String
                ) : java.io.Serializable
            }

            data class Tenant(
                var __v: Int,
                var _id: String,
                var accnHolder: String,
                var accnNumber: String,
                var addDoc: String,
                var availableContacts: Int,
                var bankName: String,
                var branchName: String,
                var buildingId: BuildingId,
                var createdAt: String,
                var createdBy: String,
                var defaultLanguage: String,
                var description: String,
                var documentBack: String,
                var documentFront: String,
                var email: String,
                var fatherName: String,
                var fullName: String,
                var is_assign: Boolean,
                var is_delete: Boolean,
                var is_profile: Boolean,
                var location: Location,
                var mfs: String,
                var mfsAccnNumber: String,
                var mfsHolder: String,
                var motherName: String,
                var nid: String,
                var nidImage: String,
                var notification_status: Boolean,
                var parkingPrice: Int,
                var payMenthod: String,
                var phoneNumber: String,
                var profilePic: String,
                var projectId: String,
                var refName: String,
                var refNid: String,
                var refNidImage: String,
                var refNumber: String,
                var referenceNidBack: String,
                var role: String,
                var roomRent: Int,
                var shiftEnd: String,
                var shiftStart: String,
                var status: String,
                var subscription_active: Boolean,
                var tenantId: String,
                var totalContacts: Int,
                var updatedAt: String
            ) : java.io.Serializable {
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
                    var propertyType: String,
                    var status: String,
                    var subPropertyType: String,
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
                    ) : java.io.Serializable {
                        data class Location(
                            var coordinates: List<Double>,
                            var type: String
                        ) : java.io.Serializable
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
                    ) : java.io.Serializable

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
                        var role: String,
                        var status: String,
                        var updatedAt: String
                    ) : java.io.Serializable {
                        data class Location(
                            var coordinates: List<Double>,
                            var type: String
                        ) : java.io.Serializable
                    }
                }

                data class Location(
                    var coordinates: List<Double>,
                    var type: String
                ) : java.io.Serializable
            }
        }

        data class Owner(
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
            var totalContacts: Int,
            var updatedAt: String
        ) : java.io.Serializable {
            data class Location(
                var coordinates: List<Double>,
                var type: String
            ) : java.io.Serializable
        }
    }*/
}