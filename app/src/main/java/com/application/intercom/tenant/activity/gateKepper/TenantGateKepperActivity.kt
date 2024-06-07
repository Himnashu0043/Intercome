package com.application.intercom.tenant.activity.gateKepper

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome.OwnerHomeViewModel
import com.application.intercom.data.model.factory.ownerfactory.ownerHome.OwnerHomeFactory
import com.application.intercom.data.model.remote.owner.gateKeeper.OwnerGateKeeperList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.databinding.ActivityTenantGateKepperBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.tenant.adapter.gateKeeper.TenantGateKeeperAdapter
import com.application.intercom.utils.*

class TenantGateKepperActivity : BaseActivity<ActivityTenantGateKepperBinding>(),
    TenantGateKeeperAdapter.PicClicK {
    private var adptr: TenantGateKeeperAdapter? = null
    private var showImg: String = ""
    private lateinit var viewModel: OwnerHomeViewModel
    private lateinit var dialog: Dialog
    override fun getLayout(): ActivityTenantGateKepperBinding {
        return ActivityTenantGateKepperBinding.inflate(layoutInflater)
    }

    private var buildingId: String = ""
    private var list = ArrayList<OwnerGateKeeperList.Data.Result>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildingId = prefs.getString(
            SessionConstants.BUILDINGID, GPSService.mLastLocation?.latitude.toString()
        )
        initView()
        lstnr()
    }

    private fun initView() {
        initialize()
        observer()
        gateKeeperList()
        binding.gateKepperToobar.tvTittle.text = "Gatekeeper"


    }

    private fun initialize() {
        val ownerModel = OwnerHomeRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(
            this, OwnerHomeFactory(ownerModel)
        )[OwnerHomeViewModel::class.java]
    }

    private fun gateKeeperList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )

        viewModel.gateKeeperOwnerTenant(token, buildingId)
    }

    private fun observer() {
        viewModel.gateKeeperOwnerTenantLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }
                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            list.clear()
                            list.addAll(it.data.result)
                            binding.textView59.text = list.size.toString()
                            binding.rcyGateKeeper.layoutManager = LinearLayoutManager(this)
                            adptr = TenantGateKeeperAdapter(this, list,this)
                            binding.rcyGateKeeper.adapter = adptr
                            adptr!!.notifyDataSetChanged()
                        } else if (it.status == AppConstants.STATUS_404) {
                            binding.textView59.text = "0"
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            binding.textView59.text = "0"
                            this.shortToast(it.message)
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
        binding.gateKepperToobar.ivBack.setOnClickListener {
            finish()
        }

    }

    override fun onImgShow(img: String) {
        showImg = img
        dialogProile()
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
        imgGallery.loadImagesWithGlideExt(showImg)

        dialog.show()

    }
}