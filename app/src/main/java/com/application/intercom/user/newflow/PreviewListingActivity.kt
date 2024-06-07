package com.application.intercom.user.newflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.local.newFlow.AddUserNewPropertyPostModel
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.databinding.ActivityPreviewListingBinding
import com.application.intercom.user.home.UserHomeFactory
import com.application.intercom.user.home.UserHomeViewModel
import com.application.intercom.user.newflow.adapter.FeatureAdapter
import com.application.intercom.user.newflow.modal.EditUserTestPostModel
import com.application.intercom.user.newflow.modal.UserTestPostModel
import com.application.intercom.user.property.UserPropertyViewPagerAdapter
import com.application.intercom.utils.*
import java.util.*
import kotlin.collections.ArrayList

class PreviewListingActivity : AppCompatActivity() {
    lateinit var binding: ActivityPreviewListingBinding
    private var sendlist = ArrayList<UserTestPostModel>()
    private lateinit var notificationsAdapterItem: UserPropertyViewPagerAdapter
    var currentPos = 0
    val handler = Handler()
    private var editFrom: String = ""
    private var editId: String = ""
    private lateinit var viewModel: UserHomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewListingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        editFrom = intent.getStringExtra("editFrom").toString()
        editId = intent.getStringExtra("edit_id").toString()
        sendlist = intent.getSerializableExtra("testList") as ArrayList<UserTestPostModel>
        println("---list$sendlist")
        println("---editId$editId")
        println("---editFrom$editFrom")
        initView()
        lstnr()
    }

    private fun initView() {
        binding.toolbar.tvTittle.text = getString(R.string.preview_listing)
        binding.commonBtn.tv.text = getString(R.string.submit_listing)

        ////fechData
        binding.tvPropertyName.text = sendlist.get(0).title
        binding.tvPropertyPrice.text = "৳${sendlist.get(0).price}"
        binding.tvLocation.text = sendlist.get(0).address
        binding.tvFit.text = sendlist.get(0).sqft.toString()
        binding.tvBedroom.text = "${sendlist.get(0).bedroom} BHK"
        binding.tvBathroom.text = "${sendlist.get(0).bathroom} Bathroom"
        notificationsAdapterItem =
            UserPropertyViewPagerAdapter(
                this,
                sendlist.get(0).photos!!
            )
        binding.viewPager1.adapter =
            UserPropertyViewPagerAdapter(this, sendlist.get(0).photos!!)
        binding.tabLayout1.setupWithViewPager(binding.viewPager1, true)
        val runnable = Runnable {
            if (currentPos == sendlist.get(0).photos!!.size - 1) currentPos = 0
            else currentPos++
            if (binding.viewPager1 != null) {
                binding.viewPager1.setCurrentItem(currentPos, true)
            }
        }

        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler.post(runnable)
            }
        }, CommonUtil.DELAY_MS, CommonUtil.PERIOD_MS)
        initialize()
        observer()

    }

    private fun initialize() {
        val repo = UserHomeRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, UserHomeFactory(repo))[UserHomeViewModel::class.java]


    }

    private fun edituserNewProperty() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        val model = UserTestPostModel(
            editId,
            sendlist.get(0).address,
            sendlist.get(0).amentities,
            sendlist.get(0).bedroom,
            sendlist.get(0).bathroom,
            sendlist.get(0).description,
            sendlist.get(0).district,
            sendlist.get(0).division,
            sendlist.get(0).flatStatus,
            sendlist.get(0).floorLevel,
            sendlist.get(0).lat,
            sendlist.get(0).long,
            sendlist.get(0).photos,
            "",
            "",
            sendlist.get(0).price,
            sendlist.get(0).propertyType,
            sendlist.get(0).sqft,
            sendlist.get(0).subPropertyType,
            sendlist.get(0).title,
            sendlist.get(0).totalFloor

        )
        viewModel.editUserNewPropertyList(token, model)

    }
    private fun adduserNewProperty() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        val model = UserTestPostModel(
            null,
            sendlist.get(0).address,
            sendlist.get(0).amentities,
            sendlist.get(0).bedroom,
            sendlist.get(0).bathroom,
            sendlist.get(0).description,
            sendlist.get(0).district,
            sendlist.get(0).division,
            sendlist.get(0).flatStatus,
            sendlist.get(0).floorLevel,
            sendlist.get(0).lat,
            sendlist.get(0).long,
            sendlist.get(0).photos,
            "",
            "",
            sendlist.get(0).price,
            sendlist.get(0).propertyType,
            sendlist.get(0).sqft,
            sendlist.get(0).subPropertyType,
            sendlist.get(0).title,
            sendlist.get(0).totalFloor

        )
        viewModel.addUserNewPropertyList(token, model)

    }

    private fun observer() {
        viewModel.addUserNewPropertyListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            startActivity(Intent(this, ThankYouSUbmitingActivity::class.java))
                            finish()
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
        viewModel.editUserNewPropertyListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            startActivity(Intent(this, ThankYouSUbmitingActivity::class.java))
                            finish()
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
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.commonBtn.tv.setOnClickListener {
            if (editFrom.equals("editData")){
                edituserNewProperty()
            }else{
                adduserNewProperty()
            }

        }

    }
}