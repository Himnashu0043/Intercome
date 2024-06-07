package com.application.intercom.tenant.activity.chat

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.services.s3.AmazonS3Client
import com.application.intercom.BuildConfig
import com.application.intercom.R
import com.application.intercom.aws.AWSConstants
import com.application.intercom.aws.AWSUtils
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.remote.UserPropertyDetails.UserPostPropertyListRes
import com.application.intercom.data.model.remote.UserPropertyDetails.UserPropertyDetailsRes
import com.application.intercom.data.model.remote.owner.community.OwnerCommunityListRes
import com.application.intercom.data.model.remote.owner.owner_communityChat.OwnerCommunityChatList
import com.application.intercom.data.model.remote.tenant.tenantSide.getAllMember.GetAllMemberListRes
import com.application.intercom.data.model.remote.userCreateRoom.SocketMessageResponse
import com.application.intercom.data.model.remote.userCreateRoom.UserMessageHistoryList
import com.application.intercom.data.model.remote.userCreateRoom.UserPropertyChatList
import com.application.intercom.data.model.remote.userFlatData.UserFlatListRes
import com.application.intercom.data.model.remote.userParkingActivityData.UserParkingActivityListRes
import com.application.intercom.data.model.remote.userParkingDetails.UserParkingDetailsRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.databinding.ActivityChatDetailsBinding
import com.application.intercom.helper.*
import com.application.intercom.helper.SocketConstants.*
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.owner.activity.playVideo.PlayVideoActivity
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.tenant.adapter.Chat.ChatDetailsAdapter
import com.application.intercom.user.home.UserHomeFactory
import com.application.intercom.user.home.UserHomeViewModel
import com.application.intercom.utils.*
import com.catalyist.aws.AWSListner
import com.google.gson.Gson
import io.socket.client.Socket
import org.json.JSONObject
import java.io.File


class ChatDetailsActivity : BaseActivity<ActivityChatDetailsBinding>(), SocketCallbacks, AWSListner,
    ChatDetailsAdapter.ClickPic {
    val TAG = "ChatDetailsActivity"

    private var adptr: ChatDetailsAdapter? = null
    private lateinit var viewModel: UserHomeViewModel
    override fun getLayout(): ActivityChatDetailsBinding {
        return ActivityChatDetailsBinding.inflate(layoutInflater)
    }

    private var from: String = ""
    private var key: String = ""
    private var addedBy: String = ""
    private var mSocket: Socket? = null
    private var list = ArrayList<UserPropertyDetailsRes.Data>()
    private var postUserlist = ArrayList<UserPostPropertyListRes.Data>()
    private var parkinglistDeatils = ArrayList<UserParkingDetailsRes.Data>()
    private var chatPropertylist: UserPropertyChatList.Data3? = null
    private var activity_Propertylist: UserFlatListRes.Data? = null
    private var activity_Parkinglist: UserParkingActivityListRes.Data? = null
    private var ownerCommunityList: OwnerCommunityListRes.Data? = null
    private var ownerTenantCommunityChatList: OwnerCommunityChatList.Data8? = null
    private var getAllMemberCommunityChatList: GetAllMemberListRes.Data? = null
    private var userId = ""
    var clickImg = false
    private var mReceiverId = ""
    private var mSenderName: String? = ""
    private var mSenderId: String = ""
    private var mRoomId: String = ""
    private var showImg: String? = ""
    private var mProfilePic: String = ""
    private var reciverProfilePic: String = ""
    private var mobileNumber: String = ""
    private var chat_type: String = ""
    private lateinit var dialog: Dialog
    private val PICK_VIDEO_REQUESTT = 10199188
    var isRoomJoin = true
    var strTemp: String = "laxman12312"
    val tempList = ArrayList<UserMessageHistoryList.Data2>()
    private val messageHistoryList: ArrayList<UserMessageHistoryList.Data2> = ArrayList()
    private var current: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from").toString()
        key = intent.getStringExtra("key").toString()
        addedBy = intent.getStringExtra("added").toString()
        println("---key$key")
        println("---from$from")
        userId = prefs.getString(
            SessionConstants.USERID, ""
        )


        initView()
        lstnr()


    }

    private fun initView() {
        initialize()
        observer()

        if (key.equals("user_property_details")) {
            if (addedBy.equals("User")) {
                postUserlist =
                    intent.getSerializableExtra("list") as ArrayList<UserPostPropertyListRes.Data>
                mobileNumber = postUserlist.get(0).owner.phoneNumber
                binding.chatDetailsToolbar.tvTittle.text = postUserlist.get(0).owner.fullName
                mSenderName = postUserlist.get(0).owner.fullName
                mReceiverId = postUserlist.get(0).owner._id
                binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(postUserlist.get(0).owner.profilePic)
                mProfilePic = prefs.getString(
                    SessionConstants.PROFILEPIC, ""
                )
//                chat_type = "POST"
               chat_type = "PROPERTY"
                userCreateRoom()
            } else {
                list = intent.getSerializableExtra("list") as ArrayList<UserPropertyDetailsRes.Data>
                mobileNumber = list.get(0).flatId.owner.phoneNumber
                binding.chatDetailsToolbar.tvTittle.text = list.get(0).flatId.owner.fullName
                mSenderName = list.get(0).flatId.owner.fullName
                mReceiverId = list.get(0).flatId.owner._id
                binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(list.get(0).flatId.owner.profilePic)
                mProfilePic = prefs.getString(
                    SessionConstants.PROFILEPIC, ""
                )
                chat_type = "PROPERTY"
                userCreateRoom()
            }
        } else if (key.equals("activity_user_property_details")) {
            mProfilePic = prefs.getString(
                SessionConstants.PROFILEPIC, ""
            )
            activity_Propertylist = intent.getSerializableExtra("list") as UserFlatListRes.Data
            mobileNumber = activity_Propertylist!!.ownerDetail.phoneNumber
            binding.chatDetailsToolbar.tvTittle.text = activity_Propertylist!!.ownerDetail.fullName
            mSenderName = activity_Propertylist!!.ownerDetail.fullName
            mReceiverId = activity_Propertylist!!.ownerDetail._id
            binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(activity_Propertylist!!.ownerDetail.profilePic)
            chat_type = "PROPERTY"
            userMessageHistory()
            userCreateRoom()
        } else if (key.equals("ownerSide_property")) {
            if (addedBy == "User") {
                postUserlist =
                    intent.getSerializableExtra("list") as ArrayList<UserPostPropertyListRes.Data>
                mobileNumber = postUserlist.get(0).owner.phoneNumber
                binding.chatDetailsToolbar.tvTittle.text = postUserlist.get(0).owner.fullName
                mSenderName = postUserlist.get(0).owner.fullName
                mReceiverId = postUserlist.get(0).owner._id
                binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(postUserlist.get(0).owner.profilePic)
                mProfilePic = prefs.getString(
                    SessionConstants.PROFILEPIC, ""
                )
                chat_type = "PROPERTY"
                userCreateRoom()
                userMessageHistory()
            } else {
                list = intent.getSerializableExtra("list") as ArrayList<UserPropertyDetailsRes.Data>
                println("=======$list")
                mobileNumber = list.get(0).flatId.owner.phoneNumber
                binding.chatDetailsToolbar.tvTittle.text = list.get(0).flatId.owner.fullName
                println("--------${list.get(0).flatId.owner.fullName}")
                mSenderName = list.get(0).flatId.owner.fullName
                binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(list.get(0).flatId.owner.profilePic)
                mProfilePic = prefs.getString(
                    SessionConstants.PROFILEPIC, ""
                )
                mReceiverId = list.get(0).flatId.owner._id
                chat_type = "PROPERTY"
                println("${mReceiverId}")
                userCreateRoom()
            }
        } else if (key.equals("tenant_property")) {
            list = intent.getSerializableExtra("list") as ArrayList<UserPropertyDetailsRes.Data>
            mobileNumber = list.get(0).flatId.owner.phoneNumber
            binding.chatDetailsToolbar.tvTittle.text = list.get(0).flatId.owner.fullName
            mSenderName = list.get(0).flatId.owner.fullName
            binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(list.get(0).flatId.owner.profilePic)
            mProfilePic = prefs.getString(
                SessionConstants.PROFILEPIC, ""
            )
            // reciverProfilePic = chatPropertylist!!.senderId.profilePic

            mReceiverId = list.get(0).flatId.owner._id
            chat_type = "PROPERTY"

            userCreateRoom()
        } else if (key.equals("tenant_parking")) {

            parkinglistDeatils =
                intent.getSerializableExtra("parkinglist") as ArrayList<UserParkingDetailsRes.Data>
            mobileNumber = parkinglistDeatils.get(0).flatId.owner.phoneNumber
            binding.chatDetailsToolbar.tvTittle.text =
                parkinglistDeatils.get(0).flatId.owner.fullName
            mSenderName = parkinglistDeatils.get(0).flatId.owner.fullName
            binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(parkinglistDeatils.get(0).flatId.owner.profilePic)
            mProfilePic = prefs.getString(
                SessionConstants.PROFILEPIC, ""
            )
            mReceiverId = parkinglistDeatils.get(0).flatId.owner._id
            chat_type = "PROPERTY"

            userCreateRoom()
        } else if (key.equals("ownerSide_parking")) {
            parkinglistDeatils =
                intent.getSerializableExtra("parkinglist") as ArrayList<UserParkingDetailsRes.Data>
            mobileNumber = parkinglistDeatils.get(0).flatId.owner.phoneNumber
            binding.chatDetailsToolbar.tvTittle.text =
                parkinglistDeatils.get(0).flatId.owner.fullName
            mSenderName = parkinglistDeatils.get(0).flatId.owner.fullName
            binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(parkinglistDeatils.get(0).flatId.owner.profilePic)
            mReceiverId = parkinglistDeatils.get(0).flatId.owner._id
            chat_type = "PROPERTY"
            mProfilePic = prefs.getString(
                SessionConstants.PROFILEPIC, ""
            )
            userCreateRoom()
        } else if (key.equals("owner_chatProperty")) {
            chatPropertylist =
                intent.getSerializableExtra("chatPropertyList") as UserPropertyChatList.Data3?
            mobileNumber = chatPropertylist!!.reciverId.phoneNumber
            binding.chatDetailsToolbar.tvTittle.text = chatPropertylist!!.senderId.fullName
            mSenderName = chatPropertylist!!.reciverId.fullName
            binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(chatPropertylist!!.senderId.profilePic)
            mProfilePic = chatPropertylist!!.reciverId.profilePic
            reciverProfilePic = chatPropertylist!!.senderId.profilePic
            chat_type = "PROPERTY"
            mSenderId = chatPropertylist!!.reciverId._id
            mReceiverId = chatPropertylist!!.senderId._id
            mRoomId = chatPropertylist!!.roomId
            userMessageHistory()
        } else if (key.equals("owner_chatParking")) {
            chatPropertylist =
                intent.getSerializableExtra("chatParkingList") as UserPropertyChatList.Data3?
            mobileNumber = chatPropertylist!!.reciverId.phoneNumber
            binding.chatDetailsToolbar.tvTittle.text = chatPropertylist!!.senderId.fullName
            mSenderName = chatPropertylist!!.reciverId.fullName
            binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(chatPropertylist!!.senderId.profilePic)
            mProfilePic = chatPropertylist!!.reciverId.profilePic
            reciverProfilePic = chatPropertylist!!.senderId.profilePic
            mSenderId = chatPropertylist!!.reciverId._id
            mReceiverId = chatPropertylist!!.senderId._id
            mRoomId = chatPropertylist!!.roomId
            chat_type = "PROPERTY"

            userMessageHistory()

        } else if (key.equals("tenant_chatProperty")) {
            chatPropertylist =
                intent.getSerializableExtra("chatPropertyList") as UserPropertyChatList.Data3?
            mobileNumber = chatPropertylist!!.reciverId.phoneNumber
            binding.chatDetailsToolbar.tvTittle.text = chatPropertylist!!.reciverId.fullName
            mSenderName = chatPropertylist!!.reciverId.fullName
            binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(chatPropertylist!!.reciverId.profilePic)
            mProfilePic = chatPropertylist!!.senderId.profilePic
            reciverProfilePic = chatPropertylist!!.reciverId.profilePic
            chat_type = "PROPERTY"
            mSenderId = chatPropertylist!!.senderId._id
            mReceiverId = chatPropertylist!!.reciverId._id
            mRoomId = chatPropertylist!!.roomId
            userMessageHistory()
        } else if (key.equals("chatProperty")) {
            chatPropertylist =
                intent.getSerializableExtra("chatPropertyList") as UserPropertyChatList.Data3?
            mobileNumber = chatPropertylist!!.reciverId.phoneNumber
            binding.chatDetailsToolbar.tvTittle.text = chatPropertylist!!.reciverId.fullName
            mSenderName = chatPropertylist!!.reciverId.fullName
            binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(chatPropertylist!!.reciverId.profilePic)
            mProfilePic = chatPropertylist!!.senderId.profilePic
            reciverProfilePic = chatPropertylist!!.reciverId.profilePic
            chat_type = "PROPERTY"
            mSenderId = chatPropertylist!!.senderId._id
            mReceiverId = chatPropertylist!!.reciverId._id
            mRoomId = chatPropertylist!!.roomId

            userMessageHistory()

        } else if (key.equals("tenant_chatParking")) {
            chatPropertylist =
                intent.getSerializableExtra("chatPropertyList") as UserPropertyChatList.Data3?
            mobileNumber = chatPropertylist!!.reciverId.phoneNumber
            binding.chatDetailsToolbar.tvTittle.text = chatPropertylist!!.reciverId.fullName
            mSenderName = chatPropertylist!!.reciverId.fullName
            binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(chatPropertylist!!.reciverId.profilePic)
            chat_type = "PROPERTY"
            mProfilePic = chatPropertylist!!.senderId.profilePic
            reciverProfilePic = chatPropertylist!!.reciverId.profilePic
            mSenderId = chatPropertylist!!.senderId._id
            mReceiverId = chatPropertylist!!.reciverId._id
            mRoomId = chatPropertylist!!.roomId
            userMessageHistory()
        } else if (key.equals("chatParking")) {
            chatPropertylist =
                intent.getSerializableExtra("chatPropertyList") as UserPropertyChatList.Data3?
            mobileNumber = chatPropertylist!!.reciverId.phoneNumber
            binding.chatDetailsToolbar.tvTittle.text = chatPropertylist!!.reciverId.fullName
            mSenderName = chatPropertylist!!.reciverId.fullName
            binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(chatPropertylist!!.reciverId.profilePic)
            chat_type = "PROPERTY"
            mProfilePic = chatPropertylist!!.senderId.profilePic
            reciverProfilePic = chatPropertylist!!.reciverId.profilePic
            mSenderId = chatPropertylist!!.senderId._id
            mReceiverId = chatPropertylist!!.reciverId._id
            mRoomId = chatPropertylist!!.roomId
            userMessageHistory()
        } else if (key.equals("activity_user_parking_details")) {
            activity_Parkinglist =
                intent.getSerializableExtra("parkinglist") as UserParkingActivityListRes.Data?
            mobileNumber = activity_Parkinglist!!.ownerDetail.phoneNumber
            binding.chatDetailsToolbar.tvTittle.text =
                activity_Parkinglist!!.ownerDetail.fullName
            mSenderName = activity_Parkinglist!!.ownerDetail.fullName
            mReceiverId = activity_Parkinglist!!.ownerDetail._id
            binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(activity_Parkinglist!!.ownerDetail.profilePic)
            mProfilePic = prefs.getString(
                SessionConstants.PROFILEPIC, ""
            )
            chat_type = "PARKING"

            userCreateRoom()
        } else if (key.equals("user_parking_details")) {
            parkinglistDeatils =
                intent.getSerializableExtra("parkinglist") as ArrayList<UserParkingDetailsRes.Data>
            mobileNumber = parkinglistDeatils.get(0).flatId.owner.phoneNumber
            binding.chatDetailsToolbar.tvTittle.text =
                parkinglistDeatils.get(0).flatId.owner.fullName
            mSenderName = parkinglistDeatils.get(0).flatId.owner.fullName
            mReceiverId = parkinglistDeatils.get(0).flatId.owner._id
            binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(parkinglistDeatils.get(0).flatId.owner.profilePic)
            mProfilePic = prefs.getString(
                SessionConstants.PROFILEPIC, ""
            )
            chat_type = "PROPERTY"
            userCreateRoom()
        } else if (key.equals("owner_home_community")) {
            ownerCommunityList =
                intent.getSerializableExtra("home_communityList") as OwnerCommunityListRes.Data?
            println("========${Gson().toJson(ownerCommunityList)}")

            mReceiverId = ownerCommunityList!!.userId._id
            binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(ownerCommunityList!!.userId.profilePic)
            reciverProfilePic = ownerCommunityList!!.userId.profilePic
            mProfilePic = prefs.getString(
                SessionConstants.PROFILEPIC,
                GPSService.mLastLocation?.latitude.toString()
            )
            binding.chatDetailsToolbar.tvOwnerName.visibility = View.VISIBLE
            binding.chatDetailsToolbar.tvOwnerName.text =
                ownerCommunityList!!.userId.role
            binding.chatDetailsToolbar.tvTittle.text =
                ownerCommunityList!!.userId.fullName
            mobileNumber = ownerCommunityList!!.userId.phoneNumber
            chat_type = "POST"
            userCreateRoom()


            /* if (from.equals("tenant")) {
                 mProfilePic = prefs.getString(
                     SessionConstants.PROFILEPIC,
                     GPSService.mLastLocation?.latitude.toString()
                 )
                 binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(ownerCommunityList!!.flatId.tenant.profilePic)
                 mobileNumber = ownerCommunityList!!.flatId.tenant.phoneNumber
                 binding.chatDetailsToolbar.tvTittle.text =
                     ownerCommunityList!!.flatId.tenant.fullName
                 println("-------tetetet${ownerCommunityList!!.flatId.tenant.fullName}")
                 mReceiverId = ownerCommunityList!!.flatId.tenant._id
                 mobileNumber = ownerCommunityList!!.flatId.tenant.phoneNumber
                 binding.chatDetailsToolbar.tvTittle.text = ownerCommunityList!!.flatId.tenant.fullName
                 mReceiverId = ownerCommunityList!!.flatId.tenant._id
                 chat_type = "POST"
             } else {
                 mProfilePic = prefs.getString(
                     SessionConstants.PROFILEPIC,
                     GPSService.mLastLocation?.latitude.toString()
                 )
                 binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(ownerCommunityList!!.flatId.owner.profilePic)
                 mobileNumber = ownerCommunityList!!.flatId.owner.phoneNumber
                 binding.chatDetailsToolbar.tvTittle.text =
                     ownerCommunityList!!.flatId.owner.fullName
                 mReceiverId = ownerCommunityList!!.flatId.owner._id
                 mobileNumber = ownerCommunityList!!.flatId.owner.phoneNumber
                 binding.chatDetailsToolbar.tvTittle.text = ownerCommunityList!!.flatId.owner.fullName
                 mReceiverId = ownerCommunityList!!.flatId.owner._id
                 chat_type = "POST"
             }*/

        } else if (key == "get_all_member_community") {
            getAllMemberCommunityChatList =
                intent.getSerializableExtra("get_all_member_communityList") as GetAllMemberListRes.Data?
            if (getAllMemberCommunityChatList?.tenatInfo?.size!! > 0) {
                mProfilePic = prefs.getString(
                    SessionConstants.PROFILEPIC, GPSService.mLastLocation?.latitude.toString()
                )
                binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(
                    getAllMemberCommunityChatList!!.tenatInfo?.get(0)!!.profilePic ?: ""
                )
                mobileNumber = getAllMemberCommunityChatList!!.tenatInfo?.get(0)!!.phoneNumber ?: ""
                binding.chatDetailsToolbar.tvTittle.text =
                    getAllMemberCommunityChatList!!.tenatInfo?.get(0)!!.fullName ?: ""
                mReceiverId = getAllMemberCommunityChatList!!.tenatInfo?.get(0)!!._id ?: ""
                chat_type = "POST"
                userCreateRoom()
            } else {
                mProfilePic = prefs.getString(
                    SessionConstants.PROFILEPIC, GPSService.mLastLocation?.latitude.toString()
                )
                binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(
                    getAllMemberCommunityChatList!!.ownerInfo?.get(0)!!.profilePic ?: ""
                )
                mobileNumber = getAllMemberCommunityChatList!!.ownerInfo?.get(0)!!.phoneNumber ?: ""
                binding.chatDetailsToolbar.tvTittle.text =
                    getAllMemberCommunityChatList!!.ownerInfo?.get(0)!!.fullName ?: ""
                mReceiverId = getAllMemberCommunityChatList!!.ownerInfo?.get(0)!!._id ?: ""
                chat_type = "POST"
                userCreateRoom()
            }

        } else if (key.equals("owner_tenant_community_list")) {
            ownerTenantCommunityChatList =
                intent.getSerializableExtra("owner_tenant_communityList") as OwnerCommunityChatList.Data8?
            println("-----ownerTenantCommunityChatList${ownerTenantCommunityChatList!!}")
            if (ownerTenantCommunityChatList?.senderId?._id == userId) {
                mProfilePic = prefs.getString(
                    SessionConstants.PROFILEPIC, GPSService.mLastLocation?.latitude.toString()
                )
                mobileNumber = ownerTenantCommunityChatList!!.reciverId.phoneNumber
                binding.chatDetailsToolbar.tvTittle.text =
                    ownerTenantCommunityChatList!!.reciverId.fullName
                binding.chatDetailsToolbar.tvOwnerName.visibility = View.VISIBLE
                binding.chatDetailsToolbar.tvOwnerName.text =
                    ownerTenantCommunityChatList!!.reciverId.role
                binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(
                    ownerTenantCommunityChatList!!.reciverId.profilePic
                )
                mSenderId = ownerTenantCommunityChatList!!.senderId._id
                mReceiverId = ownerTenantCommunityChatList!!.reciverId._id
                mRoomId = ownerTenantCommunityChatList!!.roomId
                reciverProfilePic = ownerTenantCommunityChatList!!.reciverId.profilePic
            } else {
                mProfilePic = prefs.getString(
                    SessionConstants.PROFILEPIC, GPSService.mLastLocation?.latitude.toString()
                )
                mobileNumber = ownerTenantCommunityChatList!!.senderId.phoneNumber
                binding.chatDetailsToolbar.tvTittle.text =
                    ownerTenantCommunityChatList!!.senderId.fullName
                binding.chatDetailsToolbar.tvOwnerName.visibility = View.VISIBLE
                binding.chatDetailsToolbar.tvOwnerName.text =
                    ownerTenantCommunityChatList!!.senderId.role
                binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(
                    ownerTenantCommunityChatList!!.senderId.profilePic
                )
                mSenderId = ownerTenantCommunityChatList!!.reciverId._id
                mReceiverId = ownerTenantCommunityChatList!!.senderId._id
                mRoomId = ownerTenantCommunityChatList!!.roomId
                reciverProfilePic = ownerTenantCommunityChatList!!.senderId.profilePic
            }
            /*  mobileNumber = ownerTenantCommunityChatList!!.reciverId.phoneNumber
              binding.chatDetailsToolbar.tvTittle.text =
                  ownerTenantCommunityChatList!!.reciverId.fullName
              binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(
                  ownerTenantCommunityChatList!!.reciverId.profilePic
              )
              mProfilePic = prefs.getString(
                  SessionConstants.PROFILEPIC, GPSService.mLastLocation?.latitude.toString()
              )
              reciverProfilePic = ownerTenantCommunityChatList!!.senderId.profilePic
              mSenderId = ownerTenantCommunityChatList!!.senderId._id
              mReceiverId = ownerTenantCommunityChatList!!.reciverId._id
              mRoomId = ownerTenantCommunityChatList!!.roomId*/
            chat_type = "POST"
            userMessageHistory()
        } else if (key == "from_noti_side") {
            if (userId == intent.getStringExtra("senderId")) {
                mReceiverId = intent.getStringExtra("recieverId") ?: ""
                mSenderId = userId
            } else {
                mReceiverId = intent.getStringExtra("senderId") ?: ""
                mSenderId = userId
            }
            /*mReceiverId = intent.getStringExtra("recieverId") ?: ""*/
            binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(
                intent.getStringExtra("senderProfilePic") ?: ""
            )
            reciverProfilePic = intent.getStringExtra("senderProfilePic") ?: ""
            mProfilePic = prefs.getString(
                SessionConstants.PROFILEPIC,
                GPSService.mLastLocation?.latitude.toString()
            )
            binding.chatDetailsToolbar.tvOwnerName.visibility = View.VISIBLE
            binding.chatDetailsToolbar.tvOwnerName.text =
                intent.getStringExtra("senderRole") ?: ""
            binding.chatDetailsToolbar.tvTittle.text =
                intent.getStringExtra("senderProfileName") ?: ""
            mobileNumber = intent.getStringExtra("senderPhoneNo") ?: ""
            mRoomId = intent.getStringExtra("roomNotiId") ?: ""

            chat_type = "POST"
//            userCreateRoom()
            userMessageHistory()
        } else if (key == "kill_state_noti") {
            if (userId == prefs.getString(SessionConstants.SENDER, "")) {
                mReceiverId = prefs.getString(SessionConstants.RECIEVER, "")
                mSenderId = userId
            } else {
                mReceiverId = prefs.getString(SessionConstants.SENDER, "")
                mSenderId = userId
            }
            binding.chatDetailsToolbar.imageView7.loadImagesWithGlideExt(
                prefs.getString(SessionConstants.SENDER_PROFILE_PIC, "")
            )
            reciverProfilePic = prefs.getString(
                SessionConstants.SENDER_PROFILE_PIC,
                ""
            )
            mProfilePic = prefs.getString(
                SessionConstants.PROFILEPIC,
                GPSService.mLastLocation?.latitude.toString()
            )
            binding.chatDetailsToolbar.tvOwnerName.visibility = View.VISIBLE
            binding.chatDetailsToolbar.tvOwnerName.text =
                prefs.getString(SessionConstants.SENDER_ROLE, "")
            binding.chatDetailsToolbar.tvTittle.text =
                prefs.getString(SessionConstants.SENDER_PROFILE_NAME, "")
            mobileNumber = prefs.getString(SessionConstants.SENDER_PHONE_NO, "")
            mRoomId = prefs.getString(SessionConstants.ROOMID, "")
            chat_type = "POST"
            userMessageHistory()
        }

        /*if (from.equals("owner")) {
            binding.chatDetailsToolbar.tvOwnerName.visibility = View.VISIBLE
            binding.chatDetailsToolbar.tvOwnerName.text = getString(R.string.owner)
        } else if (from.equals("manager")) {
            binding.chatDetailsToolbar.tvOwnerName.visibility = View.VISIBLE
            binding.chatDetailsToolbar.tvOwnerName.text = getString(R.string.manager)
        } else if (from.equals("tenant")) {
            binding.chatDetailsToolbar.tvOwnerName.visibility = View.VISIBLE
            binding.chatDetailsToolbar.tvOwnerName.text = getString(R.string.tenant)
        }*/
        binding.rcyChatDetails.layoutManager = LinearLayoutManager(this)

        adptr =
            ChatDetailsAdapter(this, mSenderName!!, mProfilePic, this, userId, reciverProfilePic)


    }

    private fun initialize() {
        val repo = UserHomeRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, UserHomeFactory(repo))[UserHomeViewModel::class.java]

    }

    private fun userCreateRoom() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.userCreateRoom(token, mReceiverId, chat_type)

    }

    private fun userMessageHistory() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.userMessageHistory(token, mRoomId, chat_type)

    }

    private fun observer() {
        viewModel.userCreateRoomLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.response_code == AppConstants.STATUS_SUCCESS) {
                            mSenderId = it.Data.senderId
                            mRoomId = it.Data.roomId
                            BaseApplication.mRoomId = it.Data.roomId
                            mReceiverId = it.Data.reciverId
                            connectSocket()
                            userMessageHistory()
                        } else if (it.response_code == AppConstants.STATUS_404) {
                            // this.longToast(it.message)
                        } else if (it.response_code == AppConstants.STATUS_FAILURE) {
                            // this.longToast(it.message)
                            mSenderId = it.Data.senderId
                            mRoomId = it.Data.roomId
                            mReceiverId = it.Data.reciverId
                            BaseApplication.mRoomId = it.Data.roomId
                            connectSocket()
                            userMessageHistory()

                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(this, it.throwable!!)

                }
                else -> {}
            }
        })
        viewModel.userMessageHistoryLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.response_code == AppConstants.STATUS_SUCCESS) {
                            messageHistoryList.clear()
                            messageHistoryList.addAll(it.Data)
                            binding.rcyChatDetails.adapter = adptr
                            adptr?.setMessageData(it.Data)
                            binding.rcyChatDetails.scrollToPosition(messageHistoryList.size - 1)
                        } else if (it.response_code == AppConstants.STATUS_404) {
                            this.longToast("Data not Found!!")
                        } else if (it.response_code == AppConstants.STATUS_FAILURE) {
                            this.longToast("Data not Found!!")
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(this, it.throwable!!)

                }
                else -> {}
            }
        })

    }

    private fun lstnr() {
        binding.chatDetailsToolbar.ivBack.setOnClickListener {
            finish()
            if (key == "from_noti_side") {
                if (from == "owner") {
                    startActivity(
                        Intent(
                            this,
                            OwnerMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    finish()
                } else {
                    startActivity(
                        Intent(
                            this,
                            TenantMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    finish()
                }
            } else if (key == "kill_state_noti") {
                if (from == "owner") {
                    startActivity(
                        Intent(
                            this,
                            OwnerMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    finish()
                } else {
                    startActivity(
                        Intent(
                            this,
                            TenantMainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                    finish()
                }
            } else {
                finish()
            }
        }
        binding.imageView8.setOnClickListener {
            if (TextUtils.isEmpty(binding.edName.text.trim().toString())) {
                Toast.makeText(this, "Please enter message", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            } else {
                sendMessage(binding.edName.text.trim().toString())
                binding.edName.setText("")
            }
        }
        binding.imageView9.setOnClickListener {
            clickImg = true
            showImagePickDialog()


        }
        binding.chatDetailsToolbar.ivCalling.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${mobileNumber}")
            startActivity(intent)
        }
        binding.ivVideos.setOnClickListener {
            clickImg = false
            //pickVideoFromGallery()
        }

    }


    private fun connectSocket() {
        try {
            mSocket = SocketConnection.connectSocket(this)
            if (mSocket?.connected() != true) {
                mSocket?.connect()
            }
            if (isRoomJoin) {
                val `object` = JSONObject()
                `object`.put(SENDER_ID, mSenderId)
                `object`.put(ROOM_ID, mRoomId)
                Log.d("CheckingRoomIds", "connectSocket: $`object`")
                mSocket?.emit(EVENT_ROOM_JOIN, `object`)
            }


        } catch (e: Exception) {
            Log.e(TAG, "connectSocket: ${e.printStackTrace()}")
            e.printStackTrace()
        }
    }


    private fun sendMessage(msg: String) {
        try {
            val `object` = JSONObject()
            `object`.put(ROOM_ID, mRoomId)
            `object`.put(SENDER_ID, mSenderId)
            `object`.put(RECEIVER_ID, mReceiverId)
            `object`.put(MSGTYPE, "message")
            `object`.put(CHATTYPE, chat_type)
            `object`.put(MESSAGE, msg)

            println("=======$`object`")

            mSocket!!.emit(EVENT_MESSAGE, `object`)

            /* val current = localToGMT()

             val myMessage = UserMessageHistoryList.Data2(
                 chat_type,
                 message = msg,
                 "",
                 mReceiverId,
                 roomId = mRoomId,
                 true,
                 mSenderId,
                 current.toString(),
                 "message"
             )
             val list = ArrayList<UserMessageHistoryList.Data2>()
             messageHistoryList.add(myMessage)
             list.add(myMessage)
             adptr?.setMessageData(list)
             binding.rcyChatDetails.scrollToPosition(messageHistoryList.size - 1)*/

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            println("----catch${e.printStackTrace()}")
        }
    }

    override fun onConnect(vararg args: Any?) {
        Log.d(TAG, "onConnect:  .......");
          isRoomJoin = true

    }


    override fun onDisconnect(vararg args: Any?) {
        Log.d(TAG, "onDisconnect: ..........");

    }

    override fun onConnectError(vararg args: Any?) {
        Log.e(TAG, "onConnectError:" + args[0].toString());
    }

    override fun onRoomJoin(vararg args: Any?) {
        Log.d(TAG, "onRoomJoin: ..............")
        isRoomJoin = false


    }

    override fun onMessage(vararg args: Any?) {
        Log.e(TAG, "onMessage: ........." + args[0].toString())
        tempList.clear()
        runOnUiThread {
            val messageString = args[0].toString()
            val messageItem =
                Gson().fromJson(messageString, SocketMessageResponse::class.java)

            val list = ArrayList<UserMessageHistoryList.Data2>()
            messageHistoryList.add(messageItem.message)
            list.add(messageItem.message)
            adptr?.setMessageData(list)
            binding.rcyChatDetails.scrollToPosition(messageHistoryList.size - 1)
        }

    }

    override fun onRoomLeave(vararg args: Any?) {
        Log.d(TAG, "onRoomLeave: ........." + args[0].toString());
    }

    private fun showImagePickDialog() {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setMessage("Choose image")
        dialog.setPositiveButton(
            "Gallery"
        ) { _, _ ->
            val intent = Intent(this, TakeImageWithCrop::class.java)
            intent.putExtra("from", "gallery")
            startActivityForResult(intent, TakeImageWithCrop.GALLERY_REQUEST)
        }
        dialog.setNegativeButton(
            "Camera"
        ) { _, _ ->

            val intent = Intent(this, TakeImageWithCrop::class.java)
            intent.putExtra("from", "camera")
            startActivityForResult(intent, TakeImageWithCrop.CAMERA_REQUEST)


        }
        dialog.setNeutralButton(
            "Cancel"
        ) { dialog, which -> dialog.dismiss() }
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val path = data?.getStringExtra("filePath")
        if (requestCode == TakeImageWithCrop.CAMERA_REQUEST) {
            if (!path.isNullOrEmpty()) {
                AWSUtils(
                    this, path, this
                )
            }
        } else if (requestCode == TakeImageWithCrop.GALLERY_REQUEST) {
            if (!path.isNullOrEmpty()) {
                AWSUtils(
                    this, path, this
                )
            }
        } else if (requestCode == PICK_VIDEO_REQUESTT) {
            val selectedVideoUri: Uri? = data?.data
            if (selectedVideoUri != null) {
                val videoPath: String = getRealPathFromURI(selectedVideoUri).toString().trim()
               // uploadVideoToS3(videoPath)
            }


//            if (videoPath != null) {
//                // Call the method to upload the video to S3
//                uploadVideoToS3(videoPath)
//            }
        }
    }

    override fun onResume() {
        super.onResume()
        connectSocket()

    }

    override fun onAWSLoader(isLoader: Boolean) {
        EmpCustomLoader.showLoader(this)
        Log.d(TAG, "onAWSLoader: ")
    }

    override fun onAWSSuccess(url: String?) {
        if (url != null) {
            EmpCustomLoader.hideLoader()
            if (clickImg) {
                try {
                    val `object` = JSONObject()
                    `object`.put(ROOM_ID, mRoomId)
                    `object`.put(SENDER_ID, mSenderId)
                    `object`.put(RECEIVER_ID, mReceiverId)
                    `object`.put(MSGTYPE, "photo")
                    `object`.put(CHATTYPE, chat_type)
                    `object`.put(MESSAGE, url.toString())
                    mSocket!!.emit(EVENT_MESSAGE, `object`)
                    val current = getNewFormateCurrentDate()


                    /*val myMessage = UserMessageHistoryList.Data2(
                        chat_type,
                        message = url.toString(),
                        "",
                        mReceiverId,
                        roomId = mRoomId,
                        true,
                        mSenderId,
                        current,
                        "photo"
                    )
                    val list = ArrayList<UserMessageHistoryList.Data2>()
                    messageHistoryList.add(myMessage)
                    list.add(myMessage)
                    adptr?.setMessageData(list)
                    binding.rcyChatDetails.scrollToPosition(messageHistoryList.size - 1)*/


                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    Log.e(TAG, "onAWSSuccess:${e.printStackTrace()} ")
                }
            } else {
                try {
                    val `object` = JSONObject()
                    `object`.put(ROOM_ID, mRoomId)
                    `object`.put(SENDER_ID, mSenderId)
                    `object`.put(RECEIVER_ID, mReceiverId)
                    `object`.put(MSGTYPE, "video")
                    `object`.put(CHATTYPE, chat_type)
                    `object`.put(MESSAGE, url.toString())
                    mSocket!!.emit(EVENT_MESSAGE, `object`)
//                    val current = getNewFormateCurrentDate()
//                    val myMessage = UserMessageHistoryList.Data2(
//                        chat_type,
//                        message = url.toString(),
//                        "",
//                        mReceiverId,
//                        roomId = mRoomId,
//                        true,
//                        mSenderId,
//                        current,
//                        "video"
//                    )
//                    val list = ArrayList<UserMessageHistoryList.Data2>()
//                    messageHistoryList.add(myMessage)
//                    list.add(myMessage)
//                    adptr?.setMessageData(list)
//                    binding.rcyChatDetails.scrollToPosition(messageHistoryList.size - 1)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    println("----catch${e.printStackTrace()}")
                }
            }
            binding.edName.setText("")
        }
    }

    override fun onAWSError(error: String?) {
        EmpCustomLoader.hideLoader()
        Log.e(TAG, "onAWSError: ")
    }

    override fun onAWSProgress(progress: Int?) {
        EmpCustomLoader.showLoader(this)
        Log.d(TAG, "onAWSProgress: ")
    }

    private fun runOnThreadReceived(args: Array<out Any>) {
        tempList.clear()
        runOnUiThread {
            try {
                val `object` = JSONObject(args[0].toString())
                val message: UserMessageHistoryList.Data2 = Gson().fromJson(
                    `object`.getJSONObject("message").toString(),
                    UserMessageHistoryList.Data2::class.java
                )
//              //  current = convertDateyyyymmdd(message.createdAt)
//               current = message.createdAt
                //getNewFormateCurrentDate()
                // println("---curr$current")


                if (mSenderId != message.sender) {
                    if (strTemp == "laxman12312") {
                        Log.e(TAG, "runOnThreadReceived: 1")
                        tempList.add(message)
                        strTemp = message.message
                        messageHistoryList.add(message)
                        adptr?.setMessageData(tempList)
                    } else if (strTemp != message.message) {
                        strTemp = message.message
                        tempList.add(message)
                        messageHistoryList.add(message)
                        adptr?.setMessageData(tempList)
                    } else {
                        Log.e(TAG, "runOnThreadReceived: 3")
                        strTemp = "tyuioegjhegyfeytdhhhyrwetrtw"
                    }
                    binding.rcyChatDetails.scrollToPosition(messageHistoryList.size - 1)
                }


            } catch (e: java.lang.Exception) {
                Log.e("TAG", "run: " + e.message, e)
            }
        }
    }

    override fun onImgDownload(img: String) {
        Toast.makeText(this, "ImageDownloading!!", Toast.LENGTH_SHORT).show()
        CommonUtil.startDownload(img, this, "Image", "ImageDownload")
    }

    override fun onShowImg(img: String) {
        showImg = img
        dialogProile()
    }

    override fun onVideoShow(url: String) {
        showImg = url
        startActivity(
            Intent(this, PlayVideoActivity::class.java).putExtra("key", "chat")
                .putExtra("video_url", showImg)
        )
        //  videoShowPopup()
    }

    private fun dialogProile() {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_profile_owner)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val imgGallery = dialog.findViewById<ImageView>(R.id.ivProImg)
        imgGallery.loadImagesWithGlideExt(showImg.toString())


        dialog.show()

    }

    private fun videoShowPopup() {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.video_show_popup)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val video = dialog.findViewById<VideoView>(R.id.videoView)
        val dummy = dialog.findViewById<ImageView>(R.id.dummyimage)
        val videoUri = Uri.parse(showImg)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(video)
        video.setMediaController(mediaController)
        video.setVideoURI(videoUri)
        /* binding.videoView.requestFocus()
         binding.videoView.start()*/

        video.setOnPreparedListener { mediaPlayer ->
            dummy.visibility = View.INVISIBLE
            video.visibility = View.VISIBLE
            mediaPlayer.isLooping = true // Set looping to true for autoplay
            mediaPlayer.start() // Start the video playback
        }

        video.setOnErrorListener { mediaPlayer, what, extra ->
            // Log or display the error details
            dummy.visibility = View.VISIBLE
            Log.d("tag", "erroeeeeee" + what.toString() + extra.toString())
            return@setOnErrorListener true
        }


        dialog.show()

    }


    private fun getRealPathFromURI(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
        val path = cursor?.getString(columnIndex ?: -1)
        cursor?.close()
        return path
    }

    private fun pickVideoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUESTT)
    }

   /* private fun uploadVideoToS3(videoPath: String) {
       *//* val credentialsProvider = CognitoCachingCredentialsProvider(
            applicationContext, AWSConstants.IDENTITY_POOL_ID, AWSConstants.REGION
        )*//*
        val credentialsProvider = BasicAWSCredentials(BuildConfig.ACCESS_KEY, BuildConfig.SECRET_KEY)
        val s3Client = AmazonS3Client(credentialsProvider)
        val file = File(videoPath)
        val fileName = file.name.toString().trim()

        val transferUtility =
            TransferUtility.builder().context(applicationContext).s3Client(s3Client).build()


        Log.e(TAG, "laxman--: ${file}   ${fileName}")

        val uploadObserver = transferUtility.upload(BuildConfig.BUCKET_NAME, fileName, file)

        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {
                    when (state) {
                        TransferState.COMPLETED -> {
                            onAWSSuccess(BuildConfig.IMAGE_URL.plus(fileName))
                        }
                        TransferState.CANCELED, TransferState.FAILED -> {
                            onAWSError("AWS " + state.name)
                        }
                        TransferState.WAITING_FOR_NETWORK -> {
                            onAWSError("Please turn on internet")
                        }
                        else -> {
                            onAWSError(state.name)
                        }
                    }
                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                val percentDone = (bytesCurrent.toFloat() / bytesTotal.toFloat()) * 100
                // Update progress if needed
            }

            override fun onError(id: Int, ex: Exception) {
                // Handle errors during the upload
            }
        })
    }*/

    override fun onStop() {
        super.onStop()
        SocketConnection.disconnectSocket()
    }

    override fun onDestroy() {
        super.onDestroy()
        BaseApplication.mRoomId = ""
    }


}