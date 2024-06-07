package com.application.intercom.owner.activity.propertyDetails

import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.MainActivity
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome.OwnerHomeViewModel
import com.application.intercom.data.model.factory.ownerfactory.ownerHome.OwnerHomeFactory
import com.application.intercom.data.model.local.owner.OwnerAddTenantPostModel
import com.application.intercom.data.model.remote.owner.flat.OwnerFlatListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.databinding.ActivityOwnerSecondPropertyDetailsBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.owner.activity.ownerCreateTenant.OwnerCreateTenantActivity
import com.application.intercom.owner.activity.ownerTenantHistory.OwnerTenantHistoryActivity
import com.application.intercom.owner.activity.properties.OwnerPropertiesActivity
import com.application.intercom.owner.adapter.propertyDetails.OwnerSecondPropertyDetailsAdapter
import com.application.intercom.utils.*

class OwnerSecondPropertyDetailsActivity : BaseActivity<ActivityOwnerSecondPropertyDetailsBinding>() {

    override fun getLayout(): ActivityOwnerSecondPropertyDetailsBinding {
        return ActivityOwnerSecondPropertyDetailsBinding.inflate(layoutInflater)
    }

    private var adptr: OwnerSecondPropertyDetailsAdapter? = null
    private var send_PropertyList: OwnerFlatListRes.Data? = null
    private lateinit var viewModel: OwnerHomeViewModel
    private var flatId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        send_PropertyList =
            intent.getSerializableExtra("send_property_list") as OwnerFlatListRes.Data
        println("----list${send_PropertyList}")
        initView()
        listener()
    }

    private fun initView() {
        initialize()
        observer()
        binding.ownerPSDetails.tvTittle.text = getString(R.string.property_details)
        binding.commonBtn.tv.text = getString(R.string.tenant_history)
        binding.rcyTenantDetails.layoutManager = LinearLayoutManager(this)
        adptr = OwnerSecondPropertyDetailsAdapter(this)
        binding.rcyTenantDetails.adapter = adptr
        adptr!!.notifyDataSetChanged()
        ////fetchData
        binding.textView132.text = send_PropertyList!!.buildingInfo[0].buildingName
        binding.textView133.text = send_PropertyList!!.buildingInfo[0].address
        binding.tvFlatName.text = send_PropertyList!!.name
        binding.tvFit.text = "${send_PropertyList!!.sqft} ${getString(R.string.ft)}"
        binding.tvBedroom.text = "${send_PropertyList!!.bedroom} ${getString(R.string.bhk)}"
        binding.tvBathroom.text = "${send_PropertyList!!.bedroom} ${getString(R.string.bath)}"
        val htmlAsString = send_PropertyList!!.buildingInfo[0].description
        val htmlAsSpanned = Html.fromHtml(htmlAsString)
        binding.textView135.text = htmlAsSpanned
        binding.imageView68.loadImagesWithGlideExt(send_PropertyList!!.buildingInfo[0].photos.get(0))
        //tenant data
        binding.textView137.text = send_PropertyList!!.tenant[0].fullName
        binding.textView139.text = send_PropertyList!!.tenant[0].mobileNumber
        binding.textView140.text = send_PropertyList!!.tenant[0].email
        binding.imageView72.loadImagesWithGlideExt(send_PropertyList!!.tenant[0].photo)
        flatId = send_PropertyList!!._id

    }

    private fun initialize() {
        val repo = OwnerHomeRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(
            this, OwnerHomeFactory(repo)
        )[OwnerHomeViewModel::class.java]
    }

    private fun deleteOwnerTenant() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation?.latitude.toString()
        )

        viewModel.deleteOwnerTenant(token, send_PropertyList!!.tenant[0]._id)
    }

    private fun observer() {
        viewModel.deleteOwnerTenantLiveData.observe(this) {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }
                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            startActivity(
                                Intent(
                                    this,
                                    OwnerPropertiesActivity::class.java
                                )/*.putExtra("from", "from_side_property")*/.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                            finish()
                            this.longToast(getString(R.string.remove_successfully))
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else {
                            CommonUtil.showSnackBar(this, "Something went wrong!!")
                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(this, it.throwable!!)
                }
                else -> {}
            }
        }

    }

    private fun listener() {
        binding.ownerPSDetails.ivBack.setOnClickListener {
            finish()
        }
        binding.tvRemoveTenant.setOnClickListener {
            deleteOwnerTenant()
        }
        binding.tvEditTenant.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    OwnerCreateTenantActivity::class.java
                ).putExtra("send_property_list", send_PropertyList).putExtra("flag", "edit_tenant")
            )
        }
        binding.commonBtn.tv.setOnClickListener {
            startActivity(
                Intent(this, OwnerTenantHistoryActivity::class.java).putExtra(
                    "flatId",
                    flatId
                )
            )
        }

    }
}