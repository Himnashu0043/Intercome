package com.application.intercom.user.myactivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.application.intercom.data.model.remote.owner.flat.OwnerFlatListRes
import com.application.intercom.data.model.remote.userFlatData.UserFlatListRes
import com.application.intercom.data.model.remote.userParkingActivityData.UserParkingActivityListRes
import com.application.intercom.databinding.ActivityMyActivityPropertyParkingDetailsBinding
import com.application.intercom.tenant.activity.chat.ChatDetailsActivity
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.utils.loadImagesWithGlideExt

class MyActivityPropertyParkingDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyActivityPropertyParkingDetailsBinding
    private var from: String = ""
    private var activityPropertyList: UserFlatListRes.Data? = null
    private var activityParkingList: UserParkingActivityListRes.Data? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyActivityPropertyParkingDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.tvTittle.text = "Property Details"
        binding.tvDistance.visibility = View.INVISIBLE
        from = intent.getStringExtra("from").toString()
        println("----from$from")
        if (from.equals("propertyActivity")) {
            activityPropertyList =
                intent.getSerializableExtra("propertyActivityList") as UserFlatListRes.Data?
            binding.tvPropertyName.text = activityPropertyList!!.buildingInfo.buildingName
            binding.tvLocation.text =
                "${activityPropertyList!!.buildingInfo.district} , ${activityPropertyList!!.buildingInfo.division}"
            binding.tvPriceValue.text = "৳${activityPropertyList!!.flatInfo.price}"
            binding.tvFit.text = "${activityPropertyList!!.flatDetail.sqft} ft"
            binding.tvBedroom.text = "${activityPropertyList!!.flatDetail.bedroom} Bedroom"
            binding.tvBathroom.text = "${activityPropertyList!!.flatDetail.bathroom} Bathroom"
            binding.tvDetailsDesc.text = "${activityPropertyList!!.buildingInfo.description}"
            binding.ivProperty.loadImagesWithGlideExt(activityPropertyList!!.flatInfo.photo.get(0))
            //owner details
            binding.tvProfileName.text = activityPropertyList!!.ownerDetail.fullName
            binding.tvMobileNo.text = activityPropertyList!!.ownerDetail.phoneNumber
            binding.tvEmail.text = activityPropertyList!!.ownerDetail.email
            binding.ivProfile.loadImagesWithGlideExt(activityPropertyList!!.ownerDetail.profilePic)
        } else {
            activityParkingList =
                intent.getSerializableExtra("parkingActivityList") as UserParkingActivityListRes.Data?
            binding.tvPropertyName.text = activityParkingList!!.buildingInfo.buildingName
            binding.tvLocation.text =
                "${activityParkingList!!.buildingInfo.district} , ${activityParkingList!!.buildingInfo.division}"
            binding.tvPriceValue.text = "৳${activityParkingList!!.parkingInfo.price}"
            binding.tvFit.text = "${activityParkingList!!.flatDetail.sqft} ft"
            binding.tvBedroom.text = "${activityParkingList!!.flatDetail.bedroom} Bedroom"
            binding.tvBathroom.text = "${activityParkingList!!.flatDetail.bathroom} Bathroom"
            binding.tvDetailsDesc.text = "${activityParkingList!!.buildingInfo.description}"
            binding.ivProperty.loadImagesWithGlideExt(
                activityParkingList!!.parkingInfo.parkingImages.get(
                    0
                )
            )
            //owner details
            binding.tvProfileName.text = activityParkingList!!.ownerDetail.fullName
            binding.tvMobileNo.text = activityParkingList!!.ownerDetail.phoneNumber
            // binding.tvEmail.text = activityParkingList!!.flatDetail.email
            binding.ivProfile.loadImagesWithGlideExt(activityParkingList!!.ownerDetail.profilePic)
        }
        /*if (from.equals("user_parking")) {
            binding.layoutCommunication.visibility = View.VISIBLE
            binding.layoutCommunication1.visibility = View.VISIBLE
        } else {
            binding.layoutCommunication.visibility = View.INVISIBLE
            binding.layoutCommunication1.visibility = View.VISIBLE
        }*/
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.ivMessage.setOnClickListener {
            if (from.equals("propertyActivity")) {
                startActivity(
                    Intent(this, ChatDetailsActivity::class.java).putExtra(
                        "key",
                        "activity_user_property_details"
                    ).putExtra("list", activityPropertyList)
                )
            } else {
                startActivity(
                    Intent(this, ChatDetailsActivity::class.java).putExtra(
                        "key",
                        "activity_user_parking_details"
                    ).putExtra("parkinglist", activityParkingList)
                )
            }

        }
        /* binding.ivMessage.setOnClickListener {
             startActivity(
                 Intent(this, TenantMainActivity::class.java).putExtra(
                     "from",
                     "from_property_details"
                 )
             )
         }
         binding.ivMessage1.setOnClickListener {
             startActivity(
                 Intent(this, TenantMainActivity::class.java).putExtra(
                     "from",
                     "from_property_details"
                 )
             )
         }*/
    }
}