package com.application.intercom.owner.activity.ownerTenantRegular

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.remote.owner.regularHistory.RegularHistoryList
import com.application.intercom.data.model.remote.singleEntryHistory.OwnerTenantSingleEntryHistoryList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.databinding.ActivityOwnerTenantRegularEntry2Binding
import com.application.intercom.gatekepper.activity.newFlow.ownerTenantRegularEntry.OwnerTenantRegularEntryActivity
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.owner.adapter.ShowImgAdapter
import com.application.intercom.owner.adapter.regular.RegularEntryAdapter
import com.application.intercom.utils.*

class NewOwnerTenantRegularEntryActivity : BaseActivity<ActivityOwnerTenantRegularEntry2Binding>(),
    RegularEntryAdapter.CommonCLick {

    override fun getLayout(): ActivityOwnerTenantRegularEntry2Binding {
        return ActivityOwnerTenantRegularEntry2Binding.inflate(layoutInflater)
    }

    private var adptr: RegularEntryAdapter? = null
    private lateinit var viewModel: OwnerSideViewModel
    private var list = ArrayList<RegularHistoryList.Data.Result>()
    private var from: String = ""
    private var showphotoAdapter: ShowImgAdapter? = null
    private var photo_upload_list = java.util.ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from").toString()
        initView()
        lstnr()
    }

    private fun initView() {
        initialize()
        observer()
        binding.include4.tvTittle.text = getString(R.string.regular_entry)
        binding.include4.tvText.visibility = View.VISIBLE
        binding.include4.tvText.text = getString(R.string.add)

    }

    private fun initialize() {
        val repo = OwnerSideRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, OwnerSideFactory(repo))[OwnerSideViewModel::class.java]


    }

    private fun regularVisitorHistoryList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.regularVisitorHistoryOwner(token, "", "")
    }

    private fun regularVisitorHistoryListTenant() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.regularVisitorHistoryTenant(token, "", "")
    }

    private fun observer() {
        viewModel.regularVisitorHistoryOwnerLiveData.observe(this, androidx.lifecycle.Observer {
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
                            binding.rcyRegular.layoutManager = LinearLayoutManager(this)
                            adptr = RegularEntryAdapter(
                                this, list, this
                            )
                            binding.rcyRegular.adapter = adptr
                            adptr!!.notifyDataSetChanged()
                            if (list.isEmpty()) {
                                binding.rcyRegular.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                binding.rcyRegular.visibility = View.VISIBLE
                                binding.lottieEmpty.visibility = View.INVISIBLE
                            }
                        } else if (it.status == AppConstants.STATUS_404) {
                            //this.longToast(it.message)
                            binding.rcyRegular.visibility = View.INVISIBLE
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else {
                            binding.rcyRegular.visibility = View.INVISIBLE
                            binding.lottieEmpty.visibility = View.VISIBLE
                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    binding.lottieEmpty.visibility = View.VISIBLE
                    ErrorUtil.handlerGeneralError(this, it.throwable!!)
                }
                else -> {}
            }
        })
        viewModel.regularVisitorHistoryTenantLiveData.observe(this, androidx.lifecycle.Observer {
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
                            binding.rcyRegular.layoutManager = LinearLayoutManager(this)
                            adptr = RegularEntryAdapter(
                                this, list, this
                            )
                            binding.rcyRegular.adapter = adptr
                            adptr!!.notifyDataSetChanged()
                            if (list.isEmpty()) {
                                binding.rcyRegular.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                binding.rcyRegular.visibility = View.VISIBLE
                                binding.lottieEmpty.visibility = View.INVISIBLE
                            }
                        } else if (it.status == AppConstants.STATUS_404) {
                            //this.longToast(it.message)
                            binding.rcyRegular.visibility = View.INVISIBLE
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else {
                            binding.rcyRegular.visibility = View.INVISIBLE
                            binding.lottieEmpty.visibility = View.VISIBLE
                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    binding.lottieEmpty.visibility = View.VISIBLE
                    ErrorUtil.handlerGeneralError(this, it.throwable!!)
                }
                else -> {}
            }
        })
        viewModel.deleteVisitorOwnerLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            if (from.equals("tenant")) {
                                regularVisitorHistoryListTenant()
                            } else {
                                regularVisitorHistoryList()
                            }
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

    private fun lstnr() {
        binding.include4.ivBack.setOnClickListener {
            finish()
        }
        binding.include4.tvText.setOnClickListener {
            if (from.equals("tenant")) {
                startActivity(
                    Intent(
                        this, OwnerTenantRegularEntryActivity::class.java
                    ).putExtra("from", "tenant")
                )
            } else {
                startActivity(Intent(this, OwnerTenantRegularEntryActivity::class.java))
            }
        }
    }

    override fun onEdit(position: Int) {
        if (from.equals("tenant")) {
            startActivity(
                Intent(
                    this, OwnerTenantRegularEntryActivity::class.java
                ).putExtra("from", from).putExtra("from_edit", "editData").putExtra(
                    "editList", list[position]
                )
            )
        } else {
            startActivity(
                Intent(
                    this, OwnerTenantRegularEntryActivity::class.java
                ).putExtra("from_edit", "editData").putExtra(
                    "editList", list[position]
                )
            )
        }

    }

    override fun onDelete(visitorId: String) {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.deleteVisitorOwner(token, visitorId)
    }

    override fun onClickItem(msg: RegularHistoryList.Data.Result, position: Int) {
        popup(msg, position)
    }

    private fun popup(msg: RegularHistoryList.Data.Result, position: Int) {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.new_owner_tenant_edit_regular_entry_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

        val tvName = dialog.findViewById<TextView>(R.id.textView166)
        val tvflat = dialog.findViewById<TextView>(R.id.textView167)
        val tvPhone = dialog.findViewById<TextView>(R.id.textView170)
        val tvAddress = dialog.findViewById<TextView>(R.id.tvAddresssss)
        val tvFromDate = dialog.findViewById<TextView>(R.id.textView172)
        val tvInTime = dialog.findViewById<TextView>(R.id.tvInTime)
        val tvNote = dialog.findViewById<TextView>(R.id.tvNote)
        val tvDelivery = dialog.findViewById<TextView>(R.id.textView168)
        val tvimg = dialog.findViewById<ImageView>(R.id.imageView91)
        val tvCalling = dialog.findViewById<ImageView>(R.id.imageView100)
        val rcy = dialog.findViewById<RecyclerView>(R.id.rcyPhoto)
        val tvEdit = dialog.findViewById<TextView>(R.id.tvEditHis)
        val tvDelete = dialog.findViewById<TextView>(R.id.tvDelete)
        tvName.text = msg.visitorName
        tvflat.text = msg.flatInfo.get(0).name
        tvPhone.text = msg.mobileNumber
        tvAddress.text = msg.address
        if (!msg.fromDate.isNullOrEmpty() && !msg.toDate.isNullOrEmpty()) {
            val fromDate = setNewFormatDate(msg.fromDate)
            val toDate = setNewFormatDate(msg.toDate)
            tvFromDate.text = "${fromDate} to ${toDate}"
            println("---fromDate${fromDate}")
        } else {
            tvFromDate.text = "--"
        }

        photo_upload_list.addAll(msg.document)
        if (!msg.fromTime.isNullOrEmpty()) {
//            tvInTime.text = msg.fromTime
            println("---fromTime${msg.fromTime}")
        }
        if (!msg.toTime.isNullOrEmpty()) {
            tvInTime.text = "${msg.fromTime} to ${msg.toTime}"
            println("---toTime${msg.toTime}")

        }
        tvNote.text = msg.note
        tvimg.loadImagesWithGlideExt(msg.photo)
        tvDelivery.text = "${msg.visitCategoryName} | ${msg.visitorType} Entry"
        rcy.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        showphotoAdapter = ShowImgAdapter(this, photo_upload_list)
        rcy.adapter = showphotoAdapter
        showphotoAdapter!!.notifyDataSetChanged()
        tvEdit.setOnClickListener {
            dialog.dismiss()
            if (from.equals("tenant")) {
                startActivity(
                    Intent(
                        this, OwnerTenantRegularEntryActivity::class.java
                    ).putExtra("from", from).putExtra("from_edit", "editData").putExtra(
                        "editList", list[position]
                    )
                )
            } else {
                startActivity(
                    Intent(
                        this, OwnerTenantRegularEntryActivity::class.java
                    ).putExtra("from_edit", "editData").putExtra(
                        "editList", list[position]
                    )
                )
            }
        }
        tvDelete.setOnClickListener {
            dialog.dismiss()
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            viewModel.deleteVisitorOwner(token, msg._id)
        }
        tvCalling.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${msg.mobileNumber}")
            startActivity(intent)
        }
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    override fun onResume() {
        super.onResume()
        if (from.equals("tenant")) {
            regularVisitorHistoryListTenant()
        } else {
            regularVisitorHistoryList()
        }
    }
}