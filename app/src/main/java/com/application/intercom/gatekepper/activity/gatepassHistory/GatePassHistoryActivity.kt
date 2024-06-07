package com.application.intercom.gatekepper.activity.gatepassHistory

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.data.model.ViewModel.gateKeeperViewModel.GateKeeperHomeViewModel
import com.application.intercom.data.model.factory.gateKeeperFactory.GateKeeperFactory
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.GateKeeperListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.gateKeeperRepo.GateKeeperHomeRepo
import com.application.intercom.databinding.ActivityGatePassHistoryBinding
import com.application.intercom.gatekepper.gatekeeperAdapter.gatePassHistory.SecondGatePassHistoryAdapter
import com.application.intercom.helper.getCurrentDate
import com.application.intercom.owner.adapter.ShowImgAdapter
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter
import com.application.intercom.utils.*

class GatePassHistoryActivity : AppCompatActivity(), SecondGatePassHistoryAdapter.Click ,CommunityImgAdapter.ClickImg{
    lateinit var binding: ActivityGatePassHistoryBinding
    private lateinit var viewModel: GateKeeperHomeViewModel
    private var adptr: SecondGatePassHistoryAdapter? = null
    private var list = ArrayList<GateKeeperListRes.Data.Result>()
    private var showphotoAdapter: ShowImgAdapter? = null
    private var photo_upload_list = ArrayList<String>()
    private var mobileOwnerNumber: String = ""
    private var mobileNumber: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGatePassHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        lstnr()
    }

    private fun initView() {
        initialize()
        observer()
//        gateKeeperList()
        binding.gatepassHistoryToolbar.tvTittle.text = "Gatepass History"


    }

    private fun initialize() {
        val repo = GateKeeperHomeRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(
            this, GateKeeperFactory(repo)
        )[GateKeeperHomeViewModel::class.java]


    }

//    private fun gateKeeperList() {
//        val token = prefs.getString(
//            SessionConstants.TOKEN,
//            GPSService.mLastLocation?.latitude.toString()
//        )
//        viewModel.gateKeeperList(token)
//
//    }

    private fun observer() {
        viewModel.gateKeeperListLiveData.observe(this, Observer {
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
                            binding.rcyGatePassHistory.layoutManager = LinearLayoutManager(this)
                            adptr = SecondGatePassHistoryAdapter(this, list, this, this, "")
                            binding.rcyGatePassHistory.adapter = adptr
                            adptr!!.notifyDataSetChanged()

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
        binding.gatepassHistoryToolbar.ivBack.setOnClickListener {
            finish()
        }

    }

    override fun onViewPass(msg: GateKeeperListRes.Data.Result, position: Int) {
        notifyPopup(msg)
    }

    override fun onExitGatePass(id: String) {
        ///no implement in Completed
    }

    private fun notifyPopup(msg: GateKeeperListRes.Data.Result) {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.gate_pass_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val tvNotity = dialog.findViewById<TextView>(R.id.tvgatepssnotify)
        val tvName = dialog.findViewById<TextView>(R.id.textView166)
        val tvflat = dialog.findViewById<TextView>(R.id.textView167)
        val tvDelivery = dialog.findViewById<TextView>(R.id.textView168)
        val tvphone = dialog.findViewById<TextView>(R.id.textView170)
        val tvOwnerphone = dialog.findViewById<TextView>(R.id.textView1722)
        val tvDate = dialog.findViewById<TextView>(R.id.textView172)
        val tvTime = dialog.findViewById<TextView>(R.id.tvInTime)
        val tvNote = dialog.findViewById<TextView>(R.id.tvNote)
        val tvRecy = dialog.findViewById<RecyclerView>(R.id.rcyPhoto)
        val tvimg = dialog.findViewById<ImageView>(R.id.imageView91)
        val tvCalling = dialog.findViewById<ImageView>(R.id.imageView97)
        val tvOwnerCalling = dialog.findViewById<ImageView>(R.id.imageView98)
        val date = getCurrentDate()
        tvDelivery.visibility = View.GONE
        tvName.text = msg.contactName
        tvphone.text = msg.phoneNumber
        mobileNumber = msg.phoneNumber
        mobileOwnerNumber = msg.ownerInfo[0].phoneNumber
        tvTime.text = msg.entryTime
        tvNote.text = msg.description
        tvOwnerphone.text = mobileOwnerNumber
        tvDate.text = date
        tvflat.text = msg.flatInfo.name
        photo_upload_list.clear()
        msg.photo.forEach {
            photo_upload_list.add(msg.photo[0])
        }
        println("----list$photo_upload_list")
        tvCalling.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${mobileNumber}")
            startActivity(intent)
        }
        tvOwnerCalling.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${mobileOwnerNumber}")
            startActivity(intent)
        }
        if (!msg.profilePic.isNullOrEmpty()) {
            tvimg.loadImagesWithGlideExt(msg.profilePic)
        }


        tvRecy.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        showphotoAdapter = ShowImgAdapter(this, photo_upload_list)
        tvRecy.adapter = showphotoAdapter
        showphotoAdapter!!.notifyDataSetChanged()
        tvNotity.setOnClickListener {
            dialog.dismiss()

        }
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    override fun showImg(img: String) {

    }
}