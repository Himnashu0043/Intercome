package com.application.intercom.gatekepper.Fragment.newFragment.ownerTenantSingleEntryHistory

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.SingleEntryHistoryList
import com.application.intercom.data.model.remote.singleEntryHistory.OwnerTenantSingleEntryHistoryList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.databinding.FragmentCompletedOwnerTenantSingleEntryHistoryBinding
import com.application.intercom.gatekepper.gatekeeperAdapter.ownerTenantSingleEntryHistory.CancelledOwnerTenantSingleEntryHistoryAdapter
import com.application.intercom.gatekepper.gatekeeperAdapter.ownerTenantSingleEntryHistory.CompletedOwnerTenantSingleEntryHistoryAdapter
import com.application.intercom.gatekepper.gatekeeperAdapter.singleEntryHistory.CompletedSingleEntryHisAdapter
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.utils.*


class CompletedOwnerTenantSingleEntryHistoryFragment(val type: String) : Fragment(),CompletedOwnerTenantSingleEntryHistoryAdapter.CompletedOwnerTenant {
    lateinit var bin: FragmentCompletedOwnerTenantSingleEntryHistoryBinding
    private var completedAdapter: CompletedOwnerTenantSingleEntryHistoryAdapter? = null
    private lateinit var viewModel: OwnerSideViewModel
    private var list = ArrayList<OwnerTenantSingleEntryHistoryList.Data>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bin = FragmentCompletedOwnerTenantSingleEntryHistoryBinding.inflate(layoutInflater)
        initView()
        listener()
        return bin.root
    }

    private fun initView() {
        initialize()
        observer()
        if (type.equals("tenant")) {
            tenantSingleVisitorHistoryList()
        } else {
            ownerSingleVisitorHistoryList()
        }

    }

    private fun initialize() {
        val repo = OwnerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, OwnerSideFactory(repo))[OwnerSideViewModel::class.java]


    }

    private fun tenantSingleVisitorHistoryList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.singleVisitorHistoryTenant(token, "Completed","")
    }

    private fun ownerSingleVisitorHistoryList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.singleVisitorHistoryOwner(token, "Completed","")
    }

    private fun observer() {
        viewModel.singleVisitorHistoryTenantLiveData.observe(
            requireActivity(),
            androidx.lifecycle.Observer {
                when (it) {
                    is EmpResource.Loading -> {
                        EmpCustomLoader.showLoader(requireActivity())
                    }

                    is EmpResource.Success -> {
                        EmpCustomLoader.hideLoader()
                        it.value.let {
                            if (it.status == AppConstants.STATUS_SUCCESS) {
                                list.clear()
                                list.addAll(it.data)
                                bin.rcy.layoutManager = LinearLayoutManager(requireContext())
                                completedAdapter = CompletedOwnerTenantSingleEntryHistoryAdapter(
                                    requireContext(),
                                    list, this
                                )
                                bin.rcy.adapter = completedAdapter
                                completedAdapter!!.notifyDataSetChanged()
                                if (list.isEmpty()) {
                                    bin.lottieEmpty.visibility = View.VISIBLE
                                    bin.rcy.visibility = View.INVISIBLE
                                } else {
                                    bin.lottieEmpty.visibility = View.INVISIBLE
                                    bin.rcy.visibility = View.VISIBLE
                                }
                            } else if (it.status == AppConstants.STATUS_404) {
                                //requireContext().longToast(it.message)
                                bin.lottieEmpty.visibility = View.VISIBLE
                                bin.rcy.visibility = View.INVISIBLE
                            } else {
                                bin.lottieEmpty.visibility = View.VISIBLE
                                bin.rcy.visibility = View.INVISIBLE
                            }
                        }
                    }

                    is EmpResource.Failure -> {
                        EmpCustomLoader.hideLoader()
                        bin.lottieEmpty.visibility = View.VISIBLE
                        ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                    }
                    else -> {}
                }
            })

        viewModel.singleVisitorHistoryOwnerLiveData.observe(
            requireActivity(),
            androidx.lifecycle.Observer {
                when (it) {
                    is EmpResource.Loading -> {
                        EmpCustomLoader.showLoader(requireActivity())
                    }

                    is EmpResource.Success -> {
                        EmpCustomLoader.hideLoader()
                        it.value.let {
                            if (it.status == AppConstants.STATUS_SUCCESS) {
                                list.clear()
                                list.addAll(it.data)
                                bin.rcy.layoutManager = LinearLayoutManager(requireContext())
                                completedAdapter =
                                    CompletedOwnerTenantSingleEntryHistoryAdapter(
                                        requireContext(),
                                        list, this
                                    )
                                bin.rcy.adapter = completedAdapter
                                completedAdapter!!.notifyDataSetChanged()
                                if (list.isEmpty()) {
                                    bin.lottieEmpty.visibility = View.VISIBLE
                                    bin.rcy.visibility = View.INVISIBLE
                                } else {
                                    bin.lottieEmpty.visibility = View.INVISIBLE
                                    bin.rcy.visibility = View.VISIBLE
                                }
                            } else if (it.status == AppConstants.STATUS_404) {
                                //requireContext().longToast(it.message)
                                bin.lottieEmpty.visibility = View.VISIBLE
                                bin.rcy.visibility = View.INVISIBLE
                            } else {
                                bin.lottieEmpty.visibility = View.VISIBLE
                                bin.rcy.visibility = View.INVISIBLE
                            }
                        }
                    }

                    is EmpResource.Failure -> {
                        EmpCustomLoader.hideLoader()
                        bin.lottieEmpty.visibility = View.VISIBLE
                        ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                    }
                    else -> {}
                }
            })

    }

    private fun listener() {

    }


    private fun completedPopup(msg: OwnerTenantSingleEntryHistoryList.Data) {
        val dialog = this.let { Dialog(requireContext()) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.out_single_entry_his_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        val tvOut = dialog.findViewById<TextView>(R.id.tvOut)
        val ivImg = dialog.findViewById<ImageView>(R.id.imageView95)
        tvOut.visibility = View.GONE
        ivImg.visibility = View.GONE
        val tvName = dialog.findViewById<TextView>(R.id.textView166)
        val tvflat = dialog.findViewById<TextView>(R.id.textView167)
        val tvPhone = dialog.findViewById<TextView>(R.id.textView170)
        val tvAddress = dialog.findViewById<TextView>(R.id.textView172)
        val tvFromDate = dialog.findViewById<TextView>(R.id.textView174)
        val tvToDate = dialog.findViewById<TextView>(R.id.tvOutDate)
        val tvInTime = dialog.findViewById<TextView>(R.id.tvInTime)
        val tvOutTime = dialog.findViewById<TextView>(R.id.tvoutTime)
        val tvNote = dialog.findViewById<TextView>(R.id.tvNote)
        val tvDelivery = dialog.findViewById<TextView>(R.id.textView168)
        val tvimg = dialog.findViewById<ImageView>(R.id.imageView91)
        val tvphoneCall = dialog.findViewById<ImageView>(R.id.imageView101)
        tvName.text = msg.visitorName
        tvflat.text = msg.visitorName
        tvPhone.text = msg.mobileNumber
        tvAddress.text = msg.address
        val fromDate = setNewFormatDate(msg.createdAt)
        tvFromDate.text = fromDate
        tvToDate.text = fromDate
        tvInTime.text = msg.entryTime
        tvOutTime.text = msg.exitTime
        tvNote.text = msg.note
        tvimg.loadImagesWithGlideExt(msg.photo)
        tvDelivery.text = "${msg.visitCategoryName} | ${msg.visitorType} Entry"
        tvphoneCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${msg.mobileNumber}")
            startActivity(intent)
        }

        /*tvOut.setOnClickListener {
            dialog.dismiss()
        }*/
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    override fun onCLick(msg: OwnerTenantSingleEntryHistoryList.Data, position: Int) {
        completedPopup(msg)
    }

    override fun onResume() {
        super.onResume()
        if (type.equals("tenant")) {
            tenantSingleVisitorHistoryList()
        } else {
            ownerSingleVisitorHistoryList()
        }
    }
}