package com.application.intercom.data.api

import com.application.intercom.data.model.local.*
import com.application.intercom.data.model.local.BuySubscribe.BuySubscribePost
import com.application.intercom.data.model.local.PropertyListPostModel.PropertyListUserPostModel
import com.application.intercom.data.model.local.UserPropertyDetailsPostModel.UserPropertyDetailsPostModel
import com.application.intercom.data.model.local.UserViewContact.UserViewContactPostModel
import com.application.intercom.data.model.local.addUserEnquiryPost.AddUserEnquiryPostModel
import com.application.intercom.data.model.local.gateKeeper.AddGatePassPostModel
import com.application.intercom.data.model.local.gateKeeper.AddRegularVisitorEntryPostModel
import com.application.intercom.data.model.local.gateKeeper.AddSingleEntryGateKeeperPostModel
import com.application.intercom.data.model.local.manager.ManagerAddNoticeBoardPostModel
import com.application.intercom.data.model.local.manager.gatePass.ManagerAddGatePassPostModel
import com.application.intercom.data.model.local.manager.managerSide.actionToComplain.ManagerActionToComplainPostModel
import com.application.intercom.data.model.local.manager.managerSide.addBill.EditPendingManagerPostModel
import com.application.intercom.data.model.local.manager.managerSide.addBill.ManagerAddBillPostModel
import com.application.intercom.data.model.local.manager.managerSide.addBill.ManagerBillCategoryListRes
import com.application.intercom.data.model.local.manager.managerSide.gateKeeper.ManagerCreateGateKeeperPostModel
import com.application.intercom.data.model.local.manager.managerSide.gateKeeper.ManagerEditGateKeeperPostModel
import com.application.intercom.data.model.local.manager.managerSide.newflow.AddExpensesManagerPostModel
import com.application.intercom.data.model.local.manager.managerSide.newflow.balanceSheet.AddNoteBalanceSheetManagerPostModel
import com.application.intercom.data.model.local.manager.managerSide.newflow.balanceSheet.IncomeReportManagerPostModel
import com.application.intercom.data.model.local.manager.managerSide.newflow.balanceSheet.PdFGenertePostModel
import com.application.intercom.data.model.local.manager.managerSide.rentEdit.RentEditManagerPostModel
import com.application.intercom.data.model.local.manager.managerSide.serviceCharge.ManagerAddServiceChargePostModel
import com.application.intercom.data.model.local.manager.managerSide.serviceCharge.ManagerNewEditServiceChargePostModel
import com.application.intercom.data.model.local.newFlow.PayUnPaidManagerPostModel
import com.application.intercom.data.model.local.newFlow.StatusUpdatePostModel
import com.application.intercom.data.model.local.newFlow.UserPlanDetailsList
import com.application.intercom.data.model.local.owner.OwnerAddTenantPostModel
import com.application.intercom.data.model.local.owner.OwnerCommunityPostModel
import com.application.intercom.data.model.local.owner.OwnerUpdateTenantPostModel
import com.application.intercom.data.model.local.owner.actiontoComplain.OwnerActionToComplainPostModel
import com.application.intercom.data.model.local.owner.addRegularEntry.AddRegularEntryOwnerPostModel
import com.application.intercom.data.model.local.owner.addRegularEntry.EditRegularEntryOwnerPostModel
import com.application.intercom.data.model.local.owner.commentPost.EditCommentPostModel
import com.application.intercom.data.model.local.owner.commentPost.OwnerCommentPostModel
import com.application.intercom.data.model.local.owner.createPost.OwnerCreatePostModel
import com.application.intercom.data.model.local.owner.createPost.OwnerEditMyCommunityPostModel
import com.application.intercom.data.model.local.owner.gatePass.OwnerAddGatePassPostModal
import com.application.intercom.data.model.local.owner.gatePass.OwnerEditGatePassPostModel
import com.application.intercom.data.model.local.owner.getCommentPostModel.OwnerGetCommentPostModel
import com.application.intercom.data.model.local.owner.likeCommunty.OwnerLikeCommunityPostModel
import com.application.intercom.data.model.local.owner.propertyToletSale.OwnerPropertySalePostModel
import com.application.intercom.data.model.local.owner.propertyToletSale.OwnerPropertyToletPostModel
import com.application.intercom.data.model.local.owner.registerComplain.OwnerRegisterComplainPostModel
import com.application.intercom.data.model.local.tenant.TenantPayInAdvancePostModel
import com.application.intercom.data.model.local.tenant.TenantPayNowPostModel
import com.application.intercom.data.model.remote.*
import com.application.intercom.data.model.remote.PropertyList.PropertyLisRes
import com.application.intercom.data.model.remote.UserParkingListRes.UserParkingList
import com.application.intercom.data.model.remote.UserPlanList.UserPlanListRes
import com.application.intercom.data.model.remote.UserPropertyDetails.UserPostPropertyListRes
import com.application.intercom.data.model.remote.UserPropertyDetails.UserPropertyDetailsRes
import com.application.intercom.data.model.remote.adduserEnquiry.AddUserEnquiryRes
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.*
import com.application.intercom.data.model.remote.getTutorial.GetTutorialList
import com.application.intercom.data.model.remote.getUserDetails.GetUserDetailsRes
import com.application.intercom.data.model.remote.manager.managerHome.ManagerGetDetailsRes
import com.application.intercom.data.model.remote.manager.managerParking.ManagerParkingListRes
import com.application.intercom.data.model.remote.manager.managerParking.ManagerParkingToletListRes
import com.application.intercom.data.model.remote.manager.managerProperty.ManagerPropertyListRes
import com.application.intercom.data.model.remote.manager.managerProperty.ManagerTenantHistoryList
import com.application.intercom.data.model.remote.manager.managerProperty.ManagerToletFlatListRes
import com.application.intercom.data.model.remote.manager.managerSide.ManagerGateKepperListRes
import com.application.intercom.data.model.remote.manager.managerSide.UploadDocumenetRes
import com.application.intercom.data.model.remote.manager.managerSide.bill.*
import com.application.intercom.data.model.remote.manager.managerSide.complain.ManagerPendingListRes
import com.application.intercom.data.model.remote.manager.managerSide.finance.BillCountManagerRes
import com.application.intercom.data.model.remote.manager.managerSide.finance.balanceSheet.*
import com.application.intercom.data.model.remote.manager.managerSide.gatepass.ManagerAddGatePassListRes
import com.application.intercom.data.model.remote.manager.managerSide.gatepass.ManagerFlatOfBuildingListRes
import com.application.intercom.data.model.remote.manager.managerSide.gatepass.ManagerGatePassHistoryListRes
import com.application.intercom.data.model.remote.manager.managerSide.newflow.*
import com.application.intercom.data.model.remote.manager.managerSide.noticeBoard.ManagerNoticeBoardListRes
import com.application.intercom.data.model.remote.manager.managerSide.notifyuser.ManagerNotifyRes
import com.application.intercom.data.model.remote.manager.managerSide.rent.AddRentManagerPostModel
import com.application.intercom.data.model.remote.manager.managerSide.rent.RentManagerListRes
import com.application.intercom.data.model.remote.manager.managerSide.serviceCharege.*
import com.application.intercom.data.model.remote.newUser.AddUserNewPropertyList
import com.application.intercom.data.model.remote.newUser.MyList.ActiveNewPhaseList
import com.application.intercom.data.model.remote.newUser.NewUserAmenitiesList
import com.application.intercom.data.model.remote.newUser.favList.UserFavParkingListRes
import com.application.intercom.data.model.remote.newUser.favList.UserFavPropertyListRes
import com.application.intercom.data.model.remote.owner.addRegularEntry.AddRegularEntryOwnerRes
import com.application.intercom.data.model.remote.owner.amenities.OwnerAmenitiesListRes
import com.application.intercom.data.model.remote.owner.bill.OwnerUnPaidBillListRes
import com.application.intercom.data.model.remote.owner.commentPost.OwnerCommentPostRes
import com.application.intercom.data.model.remote.owner.community.OwnerCommunityListRes
import com.application.intercom.data.model.remote.owner.community.OwnerEditMyCommunityRes
import com.application.intercom.data.model.remote.owner.community.OwnerLikeCommunityRes
import com.application.intercom.data.model.remote.owner.community.OwnerMyCommunityListRes
import com.application.intercom.data.model.remote.owner.flat.OwnerFlatListRes
import com.application.intercom.data.model.remote.owner.gateKeeper.OwnerGateKeeperList
import com.application.intercom.data.model.remote.owner.gatePass.OwnerAddGatePassList
import com.application.intercom.data.model.remote.owner.gatePass.OwnerGatepassList
import com.application.intercom.data.model.remote.owner.getComment.OwnerDeleteCommentRes
import com.application.intercom.data.model.remote.owner.getComment.OwnerEditCommentRes
import com.application.intercom.data.model.remote.owner.getComment.OwnerGetCommentList
import com.application.intercom.data.model.remote.owner.noticBoard.OwnerNoticBoardListRes
import com.application.intercom.data.model.remote.owner.noticBoard.OwnerViewNoticeRes
import com.application.intercom.data.model.remote.owner.notifyUser.OwnerNotifyUserList
import com.application.intercom.data.model.remote.owner.ownerHome.BillCountOwnerRes
import com.application.intercom.data.model.remote.owner.ownerHome.OwnerAdvertisementRes
import com.application.intercom.data.model.remote.owner.ownerHome.OwnerDetailsRes
import com.application.intercom.data.model.remote.owner.ownerTenantHistory.OwnerTenantCurrentHistoryListRes
import com.application.intercom.data.model.remote.owner.owner_communityChat.OwnerCommunityChatList
import com.application.intercom.data.model.remote.owner.parking.OwnerParkingListRes
import com.application.intercom.data.model.remote.owner.registerComplain.OwnerRegisterComplainList
import com.application.intercom.data.model.remote.owner.registerComplain.OwnerRegisterComplainRes
import com.application.intercom.data.model.remote.owner.regularHistory.RegularHistoryList
import com.application.intercom.data.model.remote.owner.setAsHome.OwnerSetasHomeList
import com.application.intercom.data.model.remote.owner.viewPostCount.ViewPostCountList
import com.application.intercom.data.model.remote.singleEntryHistory.OwnerTenantSingleEntryHistoryList
import com.application.intercom.data.model.remote.tenant.tenantHome.AdvertisementTenantRes
import com.application.intercom.data.model.remote.tenant.tenantHome.TenantDetailsRes
import com.application.intercom.data.model.remote.tenant.tenantSide.TenantComplainListRes
import com.application.intercom.data.model.remote.tenant.tenantSide.TenantNoticeListRes
import com.application.intercom.data.model.remote.tenant.tenantSide.TenantViewPostDetailsLIst
import com.application.intercom.data.model.remote.tenant.tenantSide.getAllMember.GetAllMemberListRes
import com.application.intercom.data.model.remote.tenant.tenantSide.payInAdavance.TenantPayInAdavanceList
import com.application.intercom.data.model.remote.tenant.tenantSide.payNow.TenantPayNowRes
import com.application.intercom.data.model.remote.tenant.tenantSide.tenantBill.TenantUnPaidListRes
import com.application.intercom.data.model.remote.userCode.UserPromoCodeRes
import com.application.intercom.data.model.remote.userCreateRoom.UserCreateRoomRes
import com.application.intercom.data.model.remote.userCreateRoom.UserMessageHistoryList
import com.application.intercom.data.model.remote.userCreateRoom.UserPropertyChatList
import com.application.intercom.data.model.remote.userFlatData.UserFlatListRes
import com.application.intercom.data.model.remote.userParkingActivityData.UserParkingActivityListRes
import com.application.intercom.data.model.remote.userParkingDetails.UserParkingDetailsRes
import com.application.intercom.user.newflow.modal.UserTestPostModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    /********************AUTH SCREENS API'S*************************/

    @POST("user/otpLogin")
    suspend fun userOtpLogin(@Body model: UserOtpLoginLocalModel): OtpLoginResponse

    @POST("user/sendPhoneOtp")
    suspend fun userSendPhoneOtp(@Body model: UserSendPhoneOtpLocalModel): SendPhoneOtpResponse

    @POST("user/sendPhoneOtp")
    suspend fun userSendForgotPasswordPhoneOtp(@Body model: UserSendForgotPhoneOtpLocalModel): SendPhoneOtpResponse


    @POST("user/loginWithPassword")
    suspend fun userLoginWithPassword(@Body model: UserLoginWithPasswordLocalModel): LoginWithPasswordResponse

    @POST("user/verifyOtpCommon")
    suspend fun userVerifyOtpCommon(@Body model: UserVerifyOtpCommonLocalModel): VerifyOtpCommonResponse

    @POST("user/forgetPassword")
    suspend fun userForgetPassword(@Body model: UserForgetPasswordLocalModel): CommonResponse

    @POST("user/changePassword")
    suspend fun userChangePassword(
        @Header("authorization") jwtToken: String, @Body model: UserChangePasswordLocalModel
    ): CommonResponse

    @POST("user/logout")
    @FormUrlEncoded
    suspend fun userLogout(
        @Header("authorization") authorization: String,
        @Field("deviceToken") deviceToken:String
    ): LogoutResponse

    @POST("user/getUserDetails")
    suspend fun userGetUserDetails(@Header("authorization") jwtToken: String): CommonResponse


    @POST("user/userUpdateSettings")
    suspend fun userUserUpdateSettings(@Body model: UserUserUpdateSettingsLocalModel): UserUpdateSettingResponse


    @POST("user/userUpdateProfile")
    suspend fun userUserUpdateProfile(
        @Header("authorization") jwtToken: String, @Body model: UserUpdateProfileLocalModel
    ): CommonResponse

    @GET("user/getPrivacyAndPolicy")
    suspend fun userGetPrivacyAndPolicy(@Header("authorization") jwtToken: String): PrivacyPolicyResponse

    @GET("user/getTutorial")
    suspend fun getTutorial(): GetTutorialList


    @GET("user/getAboutUs")
    suspend fun userGetAboutUs(@Header("authorization") jwtToken: String): PrivacyPolicyResponse


    @GET("user/getContactUs")
    suspend fun userGetContactUs(@Header("authorization") jwtToken: String): ContactUsResponse

    @GET("user/getTermsAndCond")
    suspend fun userGetTermsOfService(@Header("authorization") jwtToken: String): PrivacyPolicyResponse

    @POST("normalUser/serviceCategoryList")
    @FormUrlEncoded
    suspend fun noramlUserServicesListAndSearch(
        @Header("authorization") jwtToken: String, @Field("search") search: String
    ): ServicesListResponse


    @POST("normalUser/serviceProviderList")
    @FormUrlEncoded
    suspend fun noramlUserServiceProviderList(
        @Header("authorization") jwtToken: String, @Field("category_Id") category_Id: String
    ): UserServiceProviderResponse

    @GET("normalUser/advertisementList")
    suspend fun normalUserAdvertisementList(
        /*   @Url url :String="http://3.130.15.227:5500/api/v1/normalUser/advertisementList",*/
        @Header("authorization") jwtToken: String
    ): UserAdvertimentNewResponse

    @POST("user/getNotificationList")
    suspend fun normalUserNotification(
        @Header("authorization") jwtToken: String
    ): NotificationList

    @POST("normalUser/propertyList")

    suspend fun normalUserPropertyList(
        @Header("authorization") jwtToken: String, @Body model: PropertyListUserPostModel
    ): PropertyLisRes

    @POST("normalUser/parkingList")
    suspend fun normalUserParkingList(
        @Header("authorization") jwtToken: String, @Body model: PropertyListUserPostModel
    ): UserParkingList

    @POST("normalUser/buySubscription")
    suspend fun userBuySubscrition(
        @Header("authorization") jwtToken: String, @Body model: UserBuySubscriprtionPostModel
    ): CommonResponse

    ///UserPlan
    @GET("normalUser/planList")
    suspend fun userPlanList(
        @Header("authorization") jwtToken: String,
    ): UserPlanListRes

    @GET("normalUser/getCoupon")
    suspend fun userCode(
        @Header("authorization") jwtToken: String,
        @Query("title") title: String,
    ): UserPromoCodeRes

    @GET("normalUser/faqList")
    suspend fun userFAQ(
        @Header("authorization") jwtToken: String
    ): UserFAQList


    //buySubscribe
    @POST("normalUser/buySubscription")
    suspend fun buySubscription(
        @Header("authorization") jwtToken: String, @Body model: BuySubscribePost
    ): CommonResponse

    @POST("normalUser/propertyDetail")
    suspend fun userPropertyDetails(
        @Header("authorization") jwtToken: String, @Body model: UserPropertyDetailsPostModel
    ): UserPropertyDetailsRes

    @POST("normalUser/propertyDetail")
    suspend fun userPostPropertyDetails(
        @Header("authorization") jwtToken: String, @Body model: UserPropertyDetailsPostModel
    ): UserPostPropertyListRes


    @POST("normalUser/parkingDetail")
    suspend fun userParkingDetails(
        @Header("authorization") jwtToken: String, @Body model: UserPropertyDetailsPostModel
    ): UserParkingDetailsRes

    @POST("normalUser/viewContact")
    suspend fun userViewContact(
        @Header("authorization") jwtToken: String, @Body model: UserViewContactPostModel
    ): UserViewContactRes

    @POST("user/getUserDetails")
    suspend fun getUserDetails(
        @Header("authorization") jwtToken: String
    ): GetUserDetailsRes

    @POST("normalUser/addEnquiry")
    suspend fun addUserEnquiry(
        @Header("authorization") jwtToken: String, @Body model: AddUserEnquiryPostModel
    ): AddUserEnquiryRes

    @GET("normalUser/flatActivityList")
    suspend fun getFlatData(
        @Header("authorization") jwtToken: String,
    ): UserFlatListRes

    @GET("normalUser/parkingActivityList")
    suspend fun getParkingActivity(
        @Header("authorization") jwtToken: String,
    ): UserParkingActivityListRes

    @POST("normalUser/createRoom")
    @FormUrlEncoded
    suspend fun userCreateRoom(
        @Header("authorization") jwtToken: String,
        @Field("userId") userId: String,
        @Field("chatType") chatType: String
    ): UserCreateRoomRes

    @GET("normalUser/messageList")
    suspend fun userMessageHistory(
        @Header("authorization") jwtToken: String,
        @Query("roomId") roomId: String,
        @Query("chatType") chatType: String
    ): UserMessageHistoryList

    @GET("normalUser/roomList")
    suspend fun userRoomList(
        @Header("authorization") jwtToken: String, @Query("chatType") chatType: String
    ): UserPropertyChatList

    @GET("normalUser/roomList")
    suspend fun ownerCommunityRoomList(
        @Header("authorization") jwtToken: String, @Query("chatType") chatType: String
    ): OwnerCommunityChatList

    @GET("normalUser/getAmenities")
    suspend fun userNewAmenities(
        @Header("authorization") jwtToken: String,

        ): NewUserAmenitiesList

    @POST("normalUser/addProperty")
    suspend fun addUserNewProperty(
        @Header("authorization") jwtToken: String, @Body model: UserTestPostModel
    ): AddUserNewPropertyList

    @PUT("normalUser/editProperty")
    suspend fun editUserNewProperty(
        @Header("authorization") jwtToken: String, @Body model: UserTestPostModel
    ): AddUserNewPropertyList

    @GET("normalUser/myPropertyList")
    suspend fun pendingList(
        @Header("authorization") jwtToken: String, @Query("status") status: String
    ): ActiveNewPhaseList

    @GET("normalUser/myPropertyList")
    suspend fun rejectList(
        @Header("authorization") jwtToken: String, @Query("status") status: String
    ): ActiveNewPhaseList

    @GET("normalUser/myPropertyList")
    suspend fun activeList(
        @Header("authorization") jwtToken: String, @Query("status") status: String
    ): ActiveNewPhaseList

    @GET("user/getAllBuildings")
    suspend fun getAllMemeberList(
        @Header("authorization") jwtToken: String, @Query("projectId") projectId: String
    ): GetAllMemberListRes

    @PATCH("normalUser/statusUpdate")
    suspend fun statusUpdateNewPhase(
        @Header("authorization") jwtToken: String, @Body model: StatusUpdatePostModel
    ): CommonResponse

    @GET("normalUser/planDetailList")
    suspend fun userPlanDetailsList(
        @Header("authorization") jwtToken: String
    ): UserPlanDetailsList

    @POST("normalUser/myWishList")
    @FormUrlEncoded
    suspend fun userFavList(
        @Header("authorization") jwtToken: String, @Field("type") type: String
    ): UserFavPropertyListRes

    @POST("normalUser/myWishList")
    @FormUrlEncoded
    suspend fun userFavParkingList(
        @Header("authorization") jwtToken: String, @Field("type") type: String
    ): UserFavParkingListRes

    @POST("normalUser/addWishList")
    @FormUrlEncoded
    suspend fun addFavProperty(
        @Header("authorization") jwtToken: String,
        @Field("propertyId") propertyId: String? = null,
        @Field("parkingId") parkingId: String? = null,
    ): CommonResponse

    ///owner
    @POST("owner/createGatepass")
    suspend fun addGatePassOwner(
        @Header("authorization") jwtToken: String, @Body model: OwnerAddGatePassPostModal
    ): OwnerAddGatePassList

    @PUT("owner/editGatepass")
    suspend fun editGatePassOwner(
        @Header("authorization") jwtToken: String, @Body model: OwnerEditGatePassPostModel
    ): CommonResponse

    @FormUrlEncoded
    @POST("gatekeeper/gatePassList")
    suspend fun gatePassListOwner(
        @Header("authorization") jwtToken: String,
        @Field("status") status: String,
        @Field("flatId") flatId: String
    ): OwnerGatepassList

    @PUT("owner/setAsHome")
    suspend fun setAsHomeOwnerList(
        @Header("authorization") jwtToken: String, @Query("flatId") flatId: String
    ): OwnerSetasHomeList

    @GET("owner/noticeList")
    suspend fun getOwnerNoticBoardList(
        @Header("authorization") jwtToken: String,
    ): OwnerNoticBoardListRes

    @POST("user/getUserDetails")
    suspend fun getOwnerDetails(
        @Header("authorization") jwtToken: String
    ): OwnerDetailsRes

    @GET("owner/advertisementList")
    suspend fun getAdvertisementListOwner(
        @Header("authorization") jwtToken: String,
        @Query("_id") _id: String
    ): OwnerAdvertisementRes

    @POST("owner/communityFeed")
    suspend fun getOwnerCommunityList(
        @Header("authorization") jwtToken: String, @Body hashMap: HashMap<String, Any>
    ): OwnerCommunityListRes

    @POST("owner/myCommunityFeed")
    suspend fun ownerMyCommunityList(
        @Header("authorization") jwtToken: String, @Body model: OwnerCommunityPostModel
    ): OwnerMyCommunityListRes

    @PUT("owner/editPost")
    suspend fun ownerEditMyCommunityList(
        @Header("authorization") jwtToken: String, @Body model: OwnerEditMyCommunityPostModel
    ): OwnerEditMyCommunityRes

    @PATCH("owner/deletePost")
    @FormUrlEncoded
    suspend fun ownerDeleteMyCommunityList(
        @Header("authorization") jwtToken: String,
        @Field("_id") _id: String
    ): CommonResponse

    @DELETE("owner/deleteComment")
    suspend fun ownerDeleteCommentList(
        @Header("authorization") jwtToken: String,
        @Query("commentId") commentId: String,
        @Query("post") post: String
    ): OwnerDeleteCommentRes


    @PUT("owner/editComment")
    suspend fun ownerEditCommentList(
        @Header("authorization") jwtToken: String,
        @Body model: EditCommentPostModel
    ): OwnerEditCommentRes

    @DELETE("tenant/deleteComment")
    suspend fun tenantDeleteCommentList(
        @Header("authorization") jwtToken: String,
        @Query("commentId") commentId: String,
        @Query("post") post: String
    ): OwnerDeleteCommentRes

    @PUT("tenant/editComment")
    suspend fun tenantEditCommentList(
        @Header("authorization") jwtToken: String,
        @Body model: EditCommentPostModel
    ): OwnerEditCommentRes

    @GET("owner/flatList")
    suspend fun getOwnerFlatList(
        @Header("authorization") jwtToken: String
    ): OwnerFlatListRes

    @GET("owner/gatekeeperList")
    suspend fun ownergatekeeperList(
        @Header("authorization") jwtToken: String, @Query("buildingId") buildingId: String
    ): OwnerGateKeeperList

    @GET("owner/parkingList")
    suspend fun getOwnerParkingList(
        @Header("authorization") jwtToken: String
    ): OwnerParkingListRes

    @POST("owner/createTenant")
    suspend fun addOwnerTenant(
        @Header("authorization") jwtToken: String, @Body model: OwnerAddTenantPostModel
    ): CommonResponse

    @POST("owner/updateTenant")
    suspend fun updateOwnerTenant(
        @Header("authorization") jwtToken: String, @Body model: OwnerUpdateTenantPostModel
    ): CommonResponse

    @GET("owner/tenantHistory")
    suspend fun getOwnerCurrentHistoryList(
        @Header("authorization") jwtToken: String,
        @Query("type") type: String,
        @Query("flatId") flatId: String
    ): OwnerTenantCurrentHistoryListRes

    @DELETE("owner/removeTenant")
    suspend fun deleteOwnerTenant(
        @Header("authorization") jwtToken: String, @Query("tenantId") type: String
    ): CommonResponse

    @POST("owner/likeDislikePost")
    suspend fun likeOwnerCommunity(
        @Header("authorization") jwtToken: String, @Body model: OwnerLikeCommunityPostModel
    ): OwnerLikeCommunityRes

    @FormUrlEncoded
    @POST("owner/viewNotice")
    suspend fun viewNoticeOwner(
        @Header("authorization") jwtToken: String, @Field("_id") _id: String
    ): OwnerViewNoticeRes

    @POST("owner/createComplaint")
    suspend fun registerComplainOwner(
        @Header("authorization") jwtToken: String, @Body model: OwnerRegisterComplainPostModel
    ): OwnerRegisterComplainRes

    @GET("owner/complaintList")
    suspend fun listRegisterComplainOwner(
        @Header("authorization") jwtToken: String
    ): OwnerRegisterComplainList

    @GET("owner/getAmenities")
    suspend fun getAmemities(
        @Header("authorization") jwtToken: String,
    ): OwnerAmenitiesListRes

    @POST("owner/addFlat")
    suspend fun addFlatOwner(
        @Header("authorization") jwtToken: String, @Body model: OwnerPropertyToletPostModel
    ): CommonResponse

    @POST("owner/addParking")
    suspend fun addParkingOwner(
        @Header("authorization") jwtToken: String, @Body model: OwnerPropertySalePostModel
    ): CommonResponse

    @POST("owner/actionToComplain")
    suspend fun actionToComplainOwner(
        @Header("authorization") jwtToken: String, @Body model: OwnerActionToComplainPostModel
    ): CommonResponse

    @GET("owner/billList")
    suspend fun unpaidBillOwner(
        @Header("authorization") jwtToken: String,
        @Query("userBillStatus") userBillStatus: String,
        @Query("flatId") flatId: String?
    ): OwnerUnPaidBillListRes

    @GET("owner/notifyTo")
    suspend fun notifyOwner(
        @Header("authorization") jwtToken: String,
        @Query("billId") billId: String,
    ): OwnerNotifyUserList

    @GET("owner/notifyToTenant")
    suspend fun notifyOwnertoTenant(
        @Header("authorization") jwtToken: String,
        @Query("billId") billId: String,
    ): OwnerNotifyUserList

    @POST("owner/getComments")
    suspend fun getCommentOwner(
        @Header("authorization") jwtToken: String, @Body model: OwnerGetCommentPostModel
    ): OwnerGetCommentList

    @POST("owner/commentPost")
    suspend fun commentPostOwner(
        @Header("authorization") jwtToken: String, @Body model: OwnerCommentPostModel
    ): OwnerCommentPostRes

    @POST("owner/createPost")
    suspend fun createPostOwner(
        @Header("authorization") jwtToken: String, @Body model: OwnerCreatePostModel
    ): CommonResponse

    @GET("owner/getVistCategoryList")
    suspend fun getOwnerVisitorCategoryList(
        @Header("authorization") jwtToken: String
    ): GetVisitorCategoryList

    @GET("owner/deleteRegularVisitor")
    suspend fun deleteOwnerVisitor(
        @Header("authorization") jwtToken: String, @Query("visitorId") visitorId: String
    ): CommonResponse

    @POST("owner/addVisitor")
    suspend fun addVisitorOwner(
        @Header("authorization") jwtToken: String, @Body model: AddRegularEntryOwnerPostModel
    ): AddRegularEntryOwnerRes

    @POST("owner/editRegularVisitor")
    suspend fun editVisitorOwner(
        @Header("authorization") jwtToken: String, @Body model: EditRegularEntryOwnerPostModel
    ): CommonResponse

    @FormUrlEncoded
    @POST("owner/regularVisitorList")
    suspend fun regularVistorHistoryOwner(
        @Header("authorization") jwtToken: String,

        @Field("visitorStatus") visitorStatus: String, @Field("flatId") flatId: String? = null
    ): RegularHistoryList

    @FormUrlEncoded
    @POST("owner/visitorList")
    suspend fun singleVistorHistoryOwner(
        @Header("authorization") jwtToken: String,
        @Field("visitorStatus") visitorStatus: String,
        @Field("flatId") flatId: String,
    ): OwnerTenantSingleEntryHistoryList

    @GET("owner/visitorAction")
    suspend fun ownerVisitorAction(
        @Header("authorization") jwtToken: String,
        @Query("visitorId") visitorId: String,
        @Query("visitorStatus") visitorStatus: String
    ): CommonResponse

    @GET("owner/visitorDetails")
    suspend fun ownerTenantEntryHistoryDetailsOwner(
        @Header("authorization") jwtToken: String, @Query("visitorId") visitorId: String
    ): RegularEntryHistoryDetailsList

    @GET("owner/billCount")
    suspend fun billCountOwnerList(
        @Header("authorization") jwtToken: String
    ): BillCountOwnerRes

    @FormUrlEncoded
    @POST("owner/viewPost")
    suspend fun viewPostCountOwner(
        @Header("authorization") jwtToken: String, @Field("_id") _id: String
    ): ViewPostCountList

    @POST("owner/addRentBill")
    suspend fun addOwnerRent(
        @Header("authorization") jwtToken: String, @Body model: /*ManagerAddServiceChargePostModel*/AddRentManagerPostModel
    ): ManagerAddServiceChargesListRes

    @PUT("owner/editBill")
    suspend fun editOwnerRent(
        @Header("authorization") jwtToken: String, @Body model: RentEditManagerPostModel
    ): EditPendingBillManagerRes

    @GET("owner/rentBillList")
    suspend fun getOwnerRent(
        @Header("authorization") jwtToken: String
    ): RentManagerListRes

    @PATCH("owner/deleteBill")
    @FormUrlEncoded
    suspend fun deleteOwnerRent(
        @Header("authorization") jwtToken: String, @Field("billId") billId: String
    ): DeleteUnPaidManagerRes

    //tenant
    @POST("tenant/likeDislikePost")
    suspend fun likeTenantCommunity(
        @Header("authorization") jwtToken: String, @Body model: OwnerLikeCommunityPostModel
    ): OwnerLikeCommunityRes

    @FormUrlEncoded
    @POST("tenant/viewPost")
    suspend fun viewPostCountTenant(
        @Header("authorization") jwtToken: String, @Field("_id") _id: String
    ): ViewPostCountList

    @FormUrlEncoded
    @POST("tenant/visitorList")
    suspend fun singleVistorHistoryTenant(
        @Header("authorization") jwtToken: String,
        @Field("visitorStatus") visitorStatus: String,
        @Field("flatId") flatId: String,

        ): OwnerTenantSingleEntryHistoryList

    @GET("tenant/visitorAction")
    suspend fun tenantVisitorAction(
        @Header("authorization") jwtToken: String,
        @Query("visitorId") visitorId: String,
        @Query("visitorStatus") visitorStatus: String
    ): CommonResponse

   /* @GET("tenant/advertisementList")
    suspend fun tenantAdvertisementList(@Header("authorization") jwtToken: String): UserAdvertismentResponse*/

    @GET("tenant/noticeList")
    suspend fun getTenantNoticBoardList(
        @Header("authorization") jwtToken: String,
    ): TenantNoticeListRes

    @POST("user/getUserDetails")
    suspend fun getTenantDetails(
        @Header("authorization") jwtToken: String,
    ): TenantDetailsRes

    @GET("tenant/advertisementList")
    suspend fun advertisementTenantRes(
        @Header("authorization") jwtToken: String,
        @Query("_id") _id: String,
    ): AdvertisementTenantRes

    @POST("tenant/createComplaint")
    suspend fun tenant_registerComplain(
        @Header("authorization") jwtToken: String, @Body model: OwnerRegisterComplainPostModel
    ): OwnerRegisterComplainRes

    @GET("tenant/complaintList")
    suspend fun tenantComplainList(
        @Header("authorization") jwtToken: String
    ): TenantComplainListRes

    @GET("tenant/advertisementList")
    suspend fun tenantAdvertisementRes(
        @Header("authorization") jwtToken: String,
        @Query("userBillStatus") userBillStatus: String,
    ): AdvertisementTenantRes

    @GET("tenant/billList")
    suspend fun tenantUnPaidList(
        @Header("authorization") jwtToken: String,
        @Query("userBillStatus") userBillStatus: String,
        @Query("flatId") flatId: String?
    ): TenantUnPaidListRes

    @GET("tenant/notifyTo")
    suspend fun notifyUserTenant(
        @Header("authorization") jwtToken: String, @Query("billId") billId: String
    ): OwnerNotifyUserList

    @POST("tenant/payNow")
    suspend fun payNowTenant(
        @Header("authorization") jwtToken: String, @Body model: TenantPayNowPostModel
    ): TenantPayNowRes

    @POST("owner/payNow")
    suspend fun payNowOwner(
        @Header("authorization") jwtToken: String, @Body model: TenantPayNowPostModel
    ): TenantPayNowRes

    @POST("tenant/editRegularVisitor")
    suspend fun editVisitorTenant(
        @Header("authorization") jwtToken: String, @Body model: EditRegularEntryOwnerPostModel
    ): CommonResponse

    @POST("tenant/addVisitor")
    suspend fun addVisitorTenant(
        @Header("authorization") jwtToken: String, @Body model: AddRegularEntryOwnerPostModel
    ): AddRegularEntryOwnerRes

    @FormUrlEncoded
    @POST("tenant/regularVisitorList")
    suspend fun regularVistorHistoryTenant(
        @Header("authorization") jwtToken: String,
        @Field("visitorStatus") visitorStatus: String,
        @Field("flatId") flatId: String? = null
    ): RegularHistoryList

    @GET("tenant/visitorDetails")
    suspend fun ownerTenantEntryHistoryDetailsTenant(
        @Header("authorization") jwtToken: String, @Query("visitorId") visitorId: String
    ): RegularEntryHistoryDetailsList

    @POST("tenant/billPayInAdvance")
    suspend fun payInAdvanceTenant(
        @Header("authorization") jwtToken: String, @Body model: TenantPayInAdvancePostModel
    ): TenantPayInAdavanceList

    @FormUrlEncoded
    @POST("tenant/communityFeedDetails")
    suspend fun viewPostDetailsTenant(
        @Header("authorization") jwtToken: String, @Field("postId") postId: String
    ): TenantViewPostDetailsLIst

    @FormUrlEncoded
    @POST("owner/communityFeedDetails")
    suspend fun viewPostDetailsOwner(
        @Header("authorization") jwtToken: String, @Field("postId") postId: String
    ): TenantViewPostDetailsLIst

    @GET("owner/markPaid")
    suspend fun markAsPaidOwnerRes(
        @Header("authorization") jwtToken: String, @Query("billId") billId: String
    ): ManagerMarkAsPaidRes

    @POST("owner/rejectBill")
    @FormUrlEncoded
    suspend fun rejectBillOwnerRes(
        @Header("authorization") jwtToken: String,
        @Field("billId") billId: String,
        @Field("rejectReason") rejectReason: String
    ): RejectBillManagerRes

    @GET("tenant/flatList")
    suspend fun getTenantFlatList(
        @Header("authorization") jwtToken: String
    ): OwnerFlatListRes

    @POST("tenant/createGatepass")
    suspend fun addGatePassTenant(
        @Header("authorization") jwtToken: String, @Body model: OwnerAddGatePassPostModal
    ): OwnerAddGatePassList

    @PUT("tenant/editGatepass")
    suspend fun editGatePassTenant(
        @Header("authorization") jwtToken: String, @Body model: OwnerEditGatePassPostModel
    ): CommonResponse

    ////manager
    @POST("user/getUserDetails")
    suspend fun getManagerDetails(
        @Header("authorization") jwtToken: String
    ): ManagerGetDetailsRes

    @POST("manager/addExpense")
    suspend fun addExpensesManager(
        @Header("authorization") jwtToken: String,
        @Body model: AddExpensesManagerPostModel
    ): AddExpensesManagerRes

    //    @GET("manager/viewServiceCharge")
//    suspend fun managerServiceChargeList(
//        @Header("authorization") jwtToken: String
//    ): ManagerServiceChargeList
    @POST("manager/payNow")
    suspend fun payNowManager(
        @Header("authorization") jwtToken: String, @Body model: TenantPayNowPostModel
    ): TenantPayNowRes

    @GET("manager/flatList")
    suspend fun managerFlatList(
        @Header("authorization") jwtToken: String
    ): ManagerPropertyListRes

    @GET("manager/toLetFlatLst")
    suspend fun managerToLetFlatList(
        @Header("authorization") jwtToken: String
    ): ManagerToletFlatListRes

    @GET("manager/parkingList")
    suspend fun managerParkingList(
        @Header("authorization") jwtToken: String
    ): ManagerParkingListRes

    @GET("manager/toLetParkingList")
    suspend fun managerParkingToLetList(
        @Header("authorization") jwtToken: String
    ): ManagerParkingToletListRes

    @GET("manager/tenantHistory")
    suspend fun managerTenantHostoryList(
        @Header("authorization") jwtToken: String,
        @Query("flatId") flatId: String?
    ): ManagerTenantHistoryList

    @GET("manager/expenseList")
    suspend fun unpaidExpensesList(
        @Header("authorization") jwtToken: String,
        @Query("billStatus") billStatus: String,
        @Query("buildingId") buildingId: String
    ): UnPaidExpensesManagerRes

    @GET("manager/gateKeeperList")
    suspend fun getGateKeeperList(
        @Header("authorization") jwtToken: String,
    ): ManagerGateKepperListRes

    @POST("manager/dueFlatListBill")
    suspend fun dueReportManagerList(
        @Header("authorization") jwtToken: String,
        @Body model: IncomeReportManagerPostModel
    ): DueReportManagerList

    @POST("manager/pdfGenerate")
    suspend fun pdfManager(
        @Header("authorization") jwtToken: String,
        @Body model: PdFGenertePostModel
    ): PdfGenerateRes

    @POST("manager/incomeReportList")
    suspend fun incomeReportManager(
        @Header("authorization") jwtToken: String,
        @Body model: IncomeReportManagerPostModel
    ): IncomeReportManagerList

    @POST("manager/addNote")
    suspend fun addNoteManager(
        @Header("authorization") jwtToken: String,
        @Body model: AddNoteBalanceSheetManagerPostModel
    ): AddNoteBalanceSheetRes

    @Multipart
    @POST("user/uploadDocumnet")
    fun uploadAttachment(
        @Header("authorization") jwtToken: String,
        @Part filePart: MultipartBody.Part?
    ): Call<UploadDocumenetRes?>?

    @POST("manager/expenseReportList")
    suspend fun expensesReportManager(
        @Header("authorization") jwtToken: String,
        @Body model: IncomeReportManagerPostModel
    ): ExpensesReportsManagerList

    @GET("manager/billCount")
    suspend fun billCountManagerList(
        @Header("authorization") jwtToken: String,
        @Query("buildingId") buildingId: String
    ): BillCountManagerRes

    @GET("manager/noticeList")
    suspend fun getNoticeBoardList(
        @Header("authorization") jwtToken: String,
        @Query("_id") _id: String?
    ): ManagerNoticeBoardListRes

    @GET("manager/deleteExpenseBill")
    suspend fun deleteUnpaidManager(
        @Header("authorization") jwtToken: String,
        @Query("billId") billId: String
    ): DeleteUnPaidManagerRes

    @POST("manager/addNotice")
    suspend fun addNoticeBoard(
        @Header("authorization") jwtToken: String,
        @Body model: ManagerAddNoticeBoardPostModel
    ): CommonResponse

    @PUT("manager/editExpenseBill")
    suspend fun editUnPaidManager(
        @Header("authorization") jwtToken: String,
        @Body model: EditUnPaidManagerPostModel
    ): EditUnPaidManagerRes

    @POST("manager/updateExpenseStatus")
    suspend fun payUnPaidManager(
        @Header("authorization") jwtToken: String,
        @Body model: PayUnPaidManagerPostModel
    ): CommonResponse

    @POST("manager/createGatekeeper")
    suspend fun createGateKeeper(
        @Header("authorization") jwtToken: String, @Body model: ManagerCreateGateKeeperPostModel
    ): CommonResponse

    @POST("manager/updateGateKeeper")
    suspend fun editGateKeeper(
        @Header("authorization") jwtToken: String, @Body model: ManagerEditGateKeeperPostModel
    ): CommonResponse

    @GET("manager/flatListOfBuilding")
    suspend fun getFlatOfBuildingList(
        @Header("authorization") jwtToken: String,
    ): ManagerFlatOfBuildingListRes

    @GET("manager/expenseCategoryDropDown")
    suspend fun expensesCategoryListManager(
        @Header("authorization") jwtToken: String,
    ): ExpensesCategoryManagerRes

    @PUT("manager/editBill")
    suspend fun editBillPendingManager(
        @Header("authorization") jwtToken: String,
        @Body model: EditPendingManagerPostModel
    ): EditPendingBillManagerRes

    @PUT("manager/editBill")
    suspend fun editRentManager(
        @Header("authorization") jwtToken: String,
        @Body model: RentEditManagerPostModel
    ): EditPendingBillManagerRes

    /* @PUT("manager/editBill")
     suspend fun editRentManager(
         @Header("authorization") jwtToken: String,
         @Body model: RentEditManagerPostModel
     ): EditPendingBillManagerRes*/


    @POST("manager/addGatepass")
    suspend fun addGatePass(
        @Header("authorization") jwtToken: String, @Body model: ManagerAddGatePassPostModel
    ): ManagerAddGatePassListRes

    @GET("manager/gatePassList")
    suspend fun gatePassHistoryList(
        @Header("authorization") jwtToken: String,
    ): ManagerGatePassHistoryListRes

    @GET("manager/complaintList")
    suspend fun getMangerPendingComplainList(
        @Header("authorization") jwtToken: String, @Query("status") status: String
    ): ManagerPendingListRes

    @POST("manager/actionToComplain")
    suspend fun managerActiontoComplain(
        @Header("authorization") jwtToken: String, @Body model: ManagerActionToComplainPostModel
    ): CommonResponse

    @GET("manager/billList")
    suspend fun getBillMangerPendingList(
        @Header("authorization") jwtToken: String,
        @Query("status") status: String,
        @Query("flatId") flatId: String?
    ): MangerBillPendingListRes

    @GET("manager/billList")
    suspend fun getTestBillMangerPendingList(
        @Header("authorization") jwtToken: String,
        @Query("status") status: String
    ): MangerBillPendingListRes

    @POST("manager/addBill")
    suspend fun addBillmanager(
        @Header("authorization") jwtToken: String, @Body model: ManagerAddBillPostModel
    ): AddManagerBillRes

    @POST("manager/billingCategoryList")
    suspend fun billCategorymanagerList(
        @Header("authorization") jwtToken: String

    ): ManagerBillCategoryListRes

    @GET("manager/markPaid")
    suspend fun markAsPaidMangerRes(
        @Header("authorization") jwtToken: String, @Query("billId") billId: String
    ): ManagerMarkAsPaidRes

    @POST("manager/rejectBill")
    @FormUrlEncoded
    suspend fun rejectBillManager(
        @Header("authorization") jwtToken: String,
        @Field("billId") billId: String,
        @Field("rejectReason") rejectReason: String
    ): RejectBillManagerRes

    @PATCH("manager/deleteBill")
    @FormUrlEncoded
    suspend fun deleteUnPaidList(
        @Header("authorization") jwtToken: String, @Field("billId") billId: String
    ): DeleteUnPaidManagerRes

    @POST("manager/addServiceCharge")
    suspend fun addServiceCharge(
        @Header("authorization") jwtToken: String, @Body model: ManagerAddServiceChargePostModel
    ): ManagerAddServiceChargesListRes

    @POST("manager/addRentBill")
    suspend fun addRent(
        @Header("authorization") jwtToken: String, @Body model: AddRentManagerPostModel
    ): ManagerAddServiceChargesListRes



    @GET("manager/viewServiceCharge")
    suspend fun viewServiceCharge(
        @Header("authorization") jwtToken: String
    ): ManagerServiceChargeList

    @GET("manager/rentBillList")
    suspend fun getRent(
        @Header("authorization") jwtToken: String
    ): RentManagerListRes

    @GET("manager/deleteServiceCharge")
    suspend fun deleteServiceCharge(
        @Header("authorization") jwtToken: String, @Query("chargeId") chargeId: String
    ): ManagerDeleteServiceChargeList

    @POST("manager/editServiceCharge")
    suspend fun editServiceCharge(
        @Header("authorization") jwtToken: String, @Body model: ManagerNewEditServiceChargePostModel
    ): ManagerEditServiceChargeListRes

    @POST("manager/visitorList")
    @FormUrlEncoded
    suspend fun manager_singleEntryHistroyList(
        @Header("authorization") jwtToken: String,
        @Field("visitorStatus") visitorStatus: String,
        @Field("flatId") flatId: String,

        ): SingleEntryHistoryList

    @GET("manager/visitorNotifyToUser")
    suspend fun manager_visitorNotifyToUserRes(
        @Header("authorization") jwtToken: String, @Query("visitorId") visitorId: String
    ): VisitorNotifyToUserRes


    @GET("manager/outSingleVisitorEntry")
    suspend fun managerOutSingleVisitorEntryGateKeeper(
        @Header("authorization") jwtToken: String, @Query("visitorId") visitorId: String
    ): CommonResponse

    @POST("manager/addVisitor")
    suspend fun managerAddSingleEntry(
        @Header("authorization") jwtToken: String, @Body model: AddSingleEntryGateKeeperPostModel
    ): CommonResponse

    @POST("manager/regularVisitorList")
    @FormUrlEncoded
    suspend fun managerRegularVisitorHistoryList(
        @Header("authorization") jwtToken: String,
        @Field("visitorStatus") visitorStatus: String,
        @Field("flatId") flatId: String,
        @Field("buildingId") buildingId: String,

        ): RegularVisitorGateKeeperList


    @POST("manager/addRegularVisitorEntry")
    suspend fun managerAddRegularVisitorEntry(
        @Header("authorization") jwtToken: String, @Body model: AddRegularVisitorEntryPostModel
    ): AddRegularVisitorEntryRes


    @GET("manager/outRegularVisitorEntry")
    suspend fun managerOutEntryGateKeeper(
        @Header("authorization") jwtToken: String, @Query("visitorId") visitorId: String
    ): OutEntryGateKeeperRes


    @GET("manager/visitorListOfFlat")
    suspend fun managerFlatListOfVisitor(
        @Header("authorization") jwtToken: String, @Query("flatId") flatId: String
    ): FlatListOfVisitorGateKeeperList


    @FormUrlEncoded
    @POST("manager/gatePassList")
    suspend fun managerGateKeeperList(
        @Header("authorization") jwtToken: String,
        @Field("buildingId") buildingId: String? = null,
        @Field("flatId") flatId: String? = null,
        @Field("status") status: String
    ): GateKeeperListRes

    @GET("manager/gatePassCompleted")
    suspend fun exitGatePassManager(
        @Header("authorization") jwtToken: String,
        @Query("gatePassId") gatePassId: String
    ): CommonResponse


    @GET("manager/notifyUser")
    suspend fun managerNotifyRes(
        @Header("authorization") jwtToken: String, @Query("billId") billId: String
    ): ManagerNotifyRes
    @POST("manager/notifyAll")
    @FormUrlEncoded
    suspend fun managerNotifyAll(
        @Header("authorization") jwtToken: String, @Field("flatId") flatId: String
    ): CommonResponse
    /////gateKeeper


    @POST("user/getUserDetails")
    suspend fun getGateKeeperDetails(
        @Header("authorization") jwtToken: String
    ): GateKeeperDetailsRes

    @GET("gatekeeper/flatListOfBuilding")
    suspend fun flatListOfBuildinggatekeeper(
        @Header("authorization") jwtToken: String
    ): FlatListOfBuildingGateKeeperListRes

    @GET("tenant/getVistCategoryList")
    suspend fun getVisitorCategoryListGatekeeper(
        @Header("authorization") jwtToken: String
    ): GetVisitorCategoryList

    @POST("gatekeeper/addVisitor")
    suspend fun addSingleEntry(
        @Header("authorization") jwtToken: String, @Body model: AddSingleEntryGateKeeperPostModel
    ): CommonResponse

    @FormUrlEncoded
    @POST("gatekeeper/gatePassList")
    suspend fun gateKeeperList(
        @Header("authorization") jwtToken: String,
        @Field("buildingId") buildingId: String? = null,
        @Field("flatId") flatId: String? = null,
        @Field("status") status: String
    ): GateKeeperListRes


    @GET("gatekeeper/gatePassCompleted")
    suspend fun exitGatePass(
        @Header("authorization") jwtToken: String,
        @Query("gatePassId") gatePassId: String
    ): CommonResponse


    @POST("gatekeeper/addGatepass")
    suspend fun addGatePass(
        @Header("authorization") jwtToken: String, @Body model: AddGatePassPostModel
    ): AddGatePassRes

    @POST("gatekeeper/visitorList")
    @FormUrlEncoded
    suspend fun singleEntryHistroyList(
        @Header("authorization") jwtToken: String,
        @Field("visitorStatus") visitorStatus: String,
        @Field("flatId") flatId: String,

        ): SingleEntryHistoryList

    @GET("gatekeeper/visitorNotifyToUser")
    suspend fun visitorNotifyToUserRes(
        @Header("authorization") jwtToken: String, @Query("visitorId") visitorId: String
    ): VisitorNotifyToUserRes

    @GET("gatekeeper/visitorListOfFlat")
    suspend fun flatListOfVisitorGateKeeper(
        @Header("authorization") jwtToken: String, @Query("flatId") flatId: String
    ): FlatListOfVisitorGateKeeperList

    @POST("gatekeeper/addRegularVisitorEntry")
    suspend fun addRegularVisitorEntry(
        @Header("authorization") jwtToken: String, @Body model: AddRegularVisitorEntryPostModel
    ): AddRegularVisitorEntryRes

    @GET("gatekeeper/outRegularVisitorEntry")
    suspend fun outEntryGateKeeper(
        @Header("authorization") jwtToken: String, @Query("visitorId") visitorId: String
    ): OutEntryGateKeeperRes

    @POST("gatekeeper/regularVisitorList")
    @FormUrlEncoded
    suspend fun regularVisitorHistoryList(
        @Header("authorization") jwtToken: String,
        @Field("visitorStatus") visitorStatus: String,
        @Field("flatId") flatId: String,
        @Field("buildingId") buildingId: String,

        ): RegularVisitorGateKeeperList

    @GET("gatekeeper/visitorDetails")
    suspend fun regularEntryHistoryDetailsGateKeeper(
        @Header("authorization") jwtToken: String, @Query("visitorId") visitorId: String
    ): RegularEntryHistoryDetailsList

    @GET("gatekeeper/outSingleVisitorEntry")
    suspend fun outSingleVisitorEntryGateKeeper(
        @Header("authorization") jwtToken: String, @Query("visitorId") visitorId: String
    ): CommonResponse

    @GET("gatekeeper/gatekeeperProfile")
    suspend fun gateKeeperProfile(
        @Header("authorization") jwtToken: String
    ): GateKeeperProfileList
}