package com.application.intercom.gatekepper.activity.newFlow.regularEntryHistroyDetails

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.gateKeeperViewModel.GateKeeperHomeViewModel
import com.application.intercom.data.model.factory.gateKeeperFactory.GateKeeperFactory
import com.application.intercom.data.model.local.gateKeeper.AddRegularVisitorEntryPostModel
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.RegularEntryHistoryDetailsList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.gateKeeperRepo.GateKeeperHomeRepo
import com.application.intercom.databinding.ActivityRegularEntryHistoryDetailsBinding
import com.application.intercom.gatekepper.gatekeeperAdapter.regularEntryHistory.OngoingRegularEntryHisAdapter
import com.application.intercom.gatekepper.gatekeeperAdapter.regularEntryHistoryDetails.ParentHistoryAdapter
import com.application.intercom.helper.GPSService
import com.application.intercom.owner.adapter.ShowImgAdapter
import com.application.intercom.utils.*

class RegularEntryHistoryDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegularEntryHistoryDetailsBinding
    private var adptr: ParentHistoryAdapter? = null
    private lateinit var viewModel: GateKeeperHomeViewModel
    private var isSelect: Boolean = false
    private var from: String = ""
    private var visitorId: String = ""
    private var list = ArrayList<RegularEntryHistoryDetailsList.Data.Result.History>()
    private var key: String = "In"
    private var showphotoAdapter: ShowImgAdapter? = null
    private var photo_upload_list = ArrayList<String>()
    private var mobileNumber: String = ""
    private var flatId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegularEntryHistoryDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        from = intent.getStringExtra("from").toString()
        visitorId = intent.getStringExtra("visitorId").toString()
        initView()
        listener()
    }

    private fun initView() {
//        if (from.equals("completed")) {
//            binding.tvIn.visibility = View.INVISIBLE
//        } else {
//            binding.tvIn.visibility = View.VISIBLE
//        }
        initialize()
        observer()
        regularEntryHistoryDetails()

        binding.toolbar.tvTittle.text = getString(R.string.regular_entry_history_details)


    }

    private fun initialize() {
        val repo = GateKeeperHomeRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, GateKeeperFactory(repo))[GateKeeperHomeViewModel::class.java]
    }

    private fun regularEntryHistoryDetails() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.regularEntryHistoryDetailsList(token, visitorId)
    }

    private fun observer() {
        viewModel.regularEntryHistoryDetailsListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }
                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            mobileNumber = it.data.result.get(0).mobileNumber
                            binding.imageView91.loadImagesWithGlideExt(it.data.result.get(0).photo)
                            binding.textView166.text = it.data.result.get(0).visitorName
                            binding.textView167.text = it.data.result.get(0).flatInfo.get(0).name
                            binding.textView170.text = it.data.result.get(0).mobileNumber
                            binding.tvAddress.text = it.data.result.get(0).address
                            binding.tvNote.text = it.data.result.get(0).note
                            flatId = it.data.result.get(0).flatId
                            visitorId = it.data.result.get(0)._id
                            binding.textView168.text =
                                "${it.data.result.get(0).visitCategoryName} | ${
                                    it.data.result.get(0).visitorStatus
                                } Entry"
                            if (from.equals("completed")) {
                                binding.tvIn.visibility = View.INVISIBLE
                            } else if (!it.data.result.get(0).currentStatus.isNullOrEmpty()) {
                                if (it.data.result.get(0).currentStatus.equals("In")) {
                                    key = it.data.result.get(0).currentStatus
                                    binding.tvOut.visibility = View.VISIBLE
                                    binding.tvIn.visibility = View.INVISIBLE
                                } else {
                                    key = it.data.result.get(0).currentStatus
                                    binding.tvOut.visibility = View.INVISIBLE
                                    binding.tvIn.visibility = View.VISIBLE
                                }
                            }
                            list.clear()
                            list.addAll(it.data.result.first().history)

                            binding.rcyparentHistory.layoutManager = LinearLayoutManager(this)
                            adptr = ParentHistoryAdapter(this, list, key, from)
                            binding.rcyparentHistory.adapter = adptr
                            adptr!!.notifyDataSetChanged()
                            photo_upload_list.clear()
                            for (img in it.data.result) {
                                photo_upload_list.addAll(img.document)
                            }

                            binding.rcyPhoto.layoutManager =
                                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
                            showphotoAdapter = ShowImgAdapter(this, photo_upload_list)
                            binding.rcyPhoto.adapter = showphotoAdapter
                            showphotoAdapter!!.notifyDataSetChanged()
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            this.longToast(it.message)
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

        viewModel.addRegularVisitorEntryLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }
                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            this.longToast("In successfully!!")
                            regularEntryHistoryDetails()
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
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
        viewModel.outRegularVisitorEntryLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }
                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            this.longToast("Out successfully!!")
                            regularEntryHistoryDetails()
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
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

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.tvIn.setOnClickListener {
            val token = prefs.getString(
                SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
            )
            val model = AddRegularVisitorEntryPostModel(flatId, visitorId)
            viewModel.addRegularVisitorEntry(token, model)
        }
        binding.tvOut.setOnClickListener {
            val token = prefs.getString(
                SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
            )
            viewModel.outRegularVisitorEntry(token, visitorId)
        }
        binding.imageView99.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${mobileNumber}")
            startActivity(intent)
        }
    }
}