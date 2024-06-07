package com.application.intercom.user.property

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.intercom.MainActivity
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.UserPropertyDetailsViewModel.UserPropertyDetailsViewModel
import com.application.intercom.data.model.factory.UserPropertyDetailsFactory.UserPropertyDetailsFactory
import com.application.intercom.data.model.local.UserPropertyDetailsPostModel.UserPropertyDetailsPostModel
import com.application.intercom.data.model.remote.UserPropertyDetails.UserPostPropertyListRes
import com.application.intercom.data.model.remote.UserPropertyDetails.UserPropertyDetailsRes
import com.application.intercom.data.model.remote.userParkingDetails.UserParkingDetailsRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserPropertyDetailsRepo.UserPropertyDetailsRepo
import com.application.intercom.databinding.ActivityPropertyDetailsBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.tenant.activity.chat.ChatDetailsActivity
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.utils.*
import io.socket.client.Socket

class PropertyDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPropertyDetailsBinding
    private var from: String = ""
    private var id: String = ""
    private lateinit var viewModel: UserPropertyDetailsViewModel
    private var list = ArrayList<UserPropertyDetailsRes.Data>()
    private var postlist = ArrayList<UserPostPropertyListRes.Data>()
    private var parkinglist = ArrayList<UserParkingDetailsRes.Data>()
    private var mobileNumber: String = ""
    private var addedBy: String = ""

    ///chat Socket
    private var mSocket: Socket? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPropertyDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        from = intent.getStringExtra("from").toString()
        id = intent.getStringExtra("id").toString()
        addedBy = intent.getStringExtra("addedBy").toString()
        println("------comm$from")
        println("------id$id")
        println("------addedBy$addedBy")
        initView()
        listener()

        if (from.equals("user_property_details")) {
            binding.toolbar.tvTittle.text = getString(R.string.property_details_1)
        } else if (from.equals("tenant_property")) {
            binding.toolbar.tvTittle.text = getString(R.string.property_details_1)
        } else if (from.equals("ownerSide_property")) {
            binding.toolbar.tvTittle.text = getString(R.string.property_details_1)
        } else if (from.equals("complete_profile_property")) {
            binding.toolbar.tvTittle.text = getString(R.string.property_details_1)
        } else if (from.equals("complete_profile_parking")) {
            binding.toolbar.tvTittle.text = getString(R.string.parking_details_1)
            binding.tvPropertyDetails.text = getString(R.string.parking_details_2)
        } else {
            binding.toolbar.tvTittle.text = getString(R.string.parking_details_1)
            binding.tvPropertyDetails.text = getString(R.string.parking_details_2)
            binding.layRent.visibility = View.INVISIBLE
            binding.layResidental.visibility = View.INVISIBLE
//            binding.layoutCommunication.visibility = View.INVISIBLE
//            binding.layoutCommunication1.visibility = View.VISIBLE
        }

    }

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            if (from.equals("ownerSide_property")) {
                startActivity(
                    Intent(
                        this, OwnerMainActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()
            } else if (from.equals("ownerSide_parking")) {
                startActivity(
                    Intent(
                        this, OwnerMainActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()
            } else if (from.equals("tenant_Side_property")) {
                startActivity(
                    Intent(
                        this, TenantMainActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()
            } else if (from.equals("tenant_Side_parking")) {
                startActivity(
                    Intent(
                        this, TenantMainActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()
            } else if (from.equals("complete_tenant_parking_details")) {
                startActivity(
                    Intent(
                        this, TenantMainActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()
            } else if (from.equals("tenant_parking")) {
                startActivity(
                    Intent(
                        this, TenantMainActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()
            } else if (from.equals("tenant_property")) {
                startActivity(
                    Intent(
                        this, TenantMainActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()
            } else {
//                startActivity(
//                    Intent(
//                        this, MainActivity::class.java
//                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                )
                finish()
            }

        }

        binding.ivMessage.setOnClickListener {
            if (from.equals("user_property_details")) {
                if (addedBy.equals("User")) {
                    startActivity(
                        Intent(this, ChatDetailsActivity::class.java).putExtra(
                            "key", from
                        ).putExtra(
                            "added", addedBy
                        ).putExtra("list", postlist)
                    )
                } else {
                    startActivity(
                        Intent(this, ChatDetailsActivity::class.java).putExtra(
                            "key", from
                        ).putExtra("list", list)
                    )
                }

            } else if (from.equals("ownerSide_property")) {
                if (addedBy == "User") {
                    startActivity(
                        Intent(this, ChatDetailsActivity::class.java).putExtra(
                            "key", from
                        ).putExtra(
                            "added", addedBy
                        ).putExtra("list", postlist)
                    )
                } else {
                    startActivity(
                        Intent(this, ChatDetailsActivity::class.java).putExtra(
                            "key", from
                        ).putExtra("list", list)
                    )
                }

            } else if (from.equals("tenant_property")) {
                startActivity(
                    Intent(this, ChatDetailsActivity::class.java).putExtra(
                        "key", from
                    ).putExtra("list", list)
                )
            } else if (from.equals("tenant_parking")) {
                startActivity(
                    Intent(this, ChatDetailsActivity::class.java).putExtra(
                        "key", from
                    ).putExtra("parkinglist", parkinglist)
                )
            } else if (from.equals("ownerSide_parking")) {
                startActivity(
                    Intent(this, ChatDetailsActivity::class.java).putExtra(
                        "key", from
                    ).putExtra("parkinglist", parkinglist)
                )
            } else {
                startActivity(
                    Intent(this, ChatDetailsActivity::class.java).putExtra(
                        "key", from
                    ).putExtra("parkinglist", parkinglist)
                )
            }


        }
        binding.ivMessage1.setOnClickListener {
            startActivity(
                Intent(this, TenantMainActivity::class.java).putExtra(
                    "from", "from_property_details"
                )
            )
        }
        binding.ivCalling.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${mobileNumber}")
            startActivity(intent)
        }
    }

    private fun initView() {
        binding.toolbar.tvTittle.text = getString(R.string.property_details)
        initialize()
        propertyDetails()
        observer()
    }

    private fun initialize() {
        val repo = UserPropertyDetailsRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(
            this, UserPropertyDetailsFactory(repo)
        )[UserPropertyDetailsViewModel::class.java]


    }

    private fun propertyDetails() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        if (from.equals("user_parking_details")) {
            if (addedBy.equals("User")) {
                val model = UserPropertyDetailsPostModel(
                    null, id
                )
                viewModel.userParkingDetail(token, model)
            } else {
                val model = UserPropertyDetailsPostModel(
                    id, null
                )
                viewModel.userParkingDetail(token, model)
            }


        } else if (from.equals("tenant_parking")) {
            if (addedBy.equals("User")) {
                val model = UserPropertyDetailsPostModel(null, id)
                viewModel.userParkingDetail(token, model)
            } else {
                val model = UserPropertyDetailsPostModel(id, null)
                viewModel.userParkingDetail(token, model)
            }

        } else if (from.equals("ownerSide_parking")) {
            if (addedBy.equals("User")) {
                val model = UserPropertyDetailsPostModel(null, id)
                viewModel.userParkingDetail(token, model)
            } else {
                val model = UserPropertyDetailsPostModel(id, null)
                viewModel.userParkingDetail(token, model)
            }

        } else {
            if (addedBy.equals("User")) {
                val model = UserPropertyDetailsPostModel(
                    null, id
                )
                viewModel.userPostPropertyDetail(token, model)
            } else {
                val model = UserPropertyDetailsPostModel(
                    id, null
                )
                viewModel.userPropertyDetail(token, model)

            }

        }


    }


    private fun observer() {
        viewModel.userPropertyDetailsLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }
                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            list.clear()
                            list.addAll(it.data)

                            if (!it.data[0].buildingId.photos.get(0).isNullOrEmpty()) {
                                binding.ivProperty.loadImagesWithGlideExt(
                                    it.data[0].buildingId.photos.get(
                                        0
                                    )
                                )
                            }

                            binding.tvPropertyName.text = it.data.get(0).buildingId.buildingName
                            binding.tvLocation.text =
                                "${it.data.get(0).buildingId.district} , ${it.data.get(0).buildingId.division}"
                            binding.tvFit.text = "${it.data.get(0).flatId.sqft} ${getString(R.string.ft)}"
                            binding.tvBedroom.text = "${it.data.get(0).flatId.bedroom} ${getString(R.string.bedroom)}"
                            binding.tvBathroom.text = "${it.data.get(0).flatId.bathroom} ${getString(R.string.bathrooms)}"
                            binding.tvDetailsDesc.text = "${it.data.get(0).description}"
                            if (it.data.get(0).description.length > 100) {
                                binding.tvReadMore.visibility = View.VISIBLE
                            } else {
                                binding.tvReadMore.visibility = View.GONE
                            }
                            binding.tvOwnerName.text = "${it.data.get(0).flatId.owner.fullName}"
                            binding.tvrole.text = "${it.data.get(0).flatId.owner.role}"
                            binding.tvPropertyType.text = "${it.data.get(0).flatStatus}"
                            binding.tvPropertyType1.text =
                                "${it.data.get(0).flatId.buildingId.propertyType ?: ""}"
                            mobileNumber = it.data.get(0).flatId.owner.phoneNumber
                            binding.tvMobileNo.text = "${it.data.get(0).flatId.owner.phoneNumber}"
                            if (it.data.get(0).flatId.owner.email.isNullOrEmpty()) {
                                binding.tvEmail.text = "--"
                            } else {
                                binding.tvEmail.text = "${it.data.get(0).flatId.owner.email}"
                            }

                            binding.imageView146.loadImagesWithGlideExt(it.data.get(0).flatId.owner.profilePic)
                            binding.tvPriceValue.text = "৳${it.data.get(0).price}"


                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else {
                            println("------ppplist$list")
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
        viewModel.userPostPropertyDetailsLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }
                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            postlist.clear()
                            postlist.addAll(it.data)
                            if (!it.data[0].photos.get(0).isNullOrEmpty()) {
                                binding.ivProperty.loadImagesWithGlideExt(
                                    it.data[0].photos.get(
                                        0
                                    )
                                )
                            }
                            binding.tvPropertyName.text = it.data.get(0).name
                            binding.tvLocation.text = if (it.data.get(0).address.isEmpty()) {
                                it.data.get(0).owner.address
                            } else {
                                it.data.get(0).address
                            }
                            binding.tvFit.text = "${it.data.get(0).sqft} ft"
                            binding.tvBedroom.text = "${it.data.get(0).bedroom} Bedroom"
                            binding.tvBathroom.text = "${it.data.get(0).bathroom} Bathroom"
                            binding.tvDetailsDesc.text = "${it.data.get(0).description}"

//
                            if (it.data.get(0).description.length > 100) {
                                binding.tvReadMore.visibility = View.VISIBLE
                            } else {
                                binding.tvReadMore.visibility = View.GONE
                            }
                            binding.tvOwnerName.text = "${it.data.get(0).owner.fullName}"
                            binding.tvrole.text = "${it.data.get(0).owner.role}"
                            binding.tvPropertyType.text = "${it.data.get(0).flatStatus}"
                            binding.tvPropertyType1.text =
                                "${it.data.get(0).propertyType ?: ""}"
                            mobileNumber = it.data.get(0).owner.phoneNumber
//                            binding.tvMobileNo.text = "${it.data.get(0).flatId.owner.phoneNumber}"
//                            if (it.data.get(0).flatId.owner.email.isNullOrEmpty()) {
//                                binding.tvEmail.text = "--"
//                            } else {
//                                binding.tvEmail.text = "${it.data.get(0).flatId.owner.email}"
//                            }
////
                            binding.imageView146.loadImagesWithGlideExt(it.data.get(0).owner.profilePic)
                            binding.tvPriceValue.text = "৳${it.data.get(0).price}"
//

                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
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
        viewModel.userParkingDetailsLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }
                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            parkinglist.clear()
                            parkinglist.addAll(it.data)
                            binding.ivProperty.loadImagesWithGlideExt(
                                it.data[0].buildingId.photos.get(
                                    0
                                )
                            )
                            binding.tvPropertyName.text = it.data.get(0).buildingId.buildingName
                            binding.tvLocation.text =
                                "${it.data.get(0).buildingId.district} , ${it.data.get(0).buildingId.division}"
                            binding.tvFit.text = "${it.data.get(0).flatId.sqft} ft"
                            binding.tvBedroom.text = "${it.data.get(0).flatId.bedroom} Bedroom"
                            binding.tvBathroom.text = "${it.data.get(0).flatId.bathroom} Bathroom"
                            val htmlAsString = it.data.get(0).buildingId.description
                            val htmlAsSpanned = Html.fromHtml(htmlAsString)
                            binding.tvDetailsDesc.text = "${htmlAsSpanned}"
                            if (it.data.get(0).buildingId.description.length > 100) {
                                binding.tvReadMore.visibility = View.VISIBLE
                            } else {
                                binding.tvReadMore.visibility = View.GONE
                            }
                            //binding.tvProfileName.text = "${it.data.get(0).flatId.owner.fullName}"
                            binding.tvMobileNo.text = "${it.data.get(0).flatId.owner.phoneNumber}"
                            binding.tvOwnerName.text = "${it.data.get(0).flatId.owner.fullName}"
                            binding.tvrole.text = "${it.data.get(0).flatId.owner.role}"
                            binding.tvPropertyType.text = "${it.data.get(0).parkingStatus}"
//                            binding.tvPropertyType1.text = "${it.data.get(0).flatId.buildingId.propertyType}"
                            mobileNumber = it.data.get(0).flatId.owner.phoneNumber
                            if (it.data.get(0).buildingId.projectId.email.isNullOrEmpty()) {
                                binding.tvEmail.text = "--"
                            } else {
                                binding.tvEmail.text =
                                    "${it.data.get(0).buildingId.projectId.email}"
                            }

                            binding.imageView146.loadImagesWithGlideExt(it.data.get(0).flatId.owner.profilePic)
                            binding.tvPriceValue.text = "৳${it.data.get(0).price}"


                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
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
}