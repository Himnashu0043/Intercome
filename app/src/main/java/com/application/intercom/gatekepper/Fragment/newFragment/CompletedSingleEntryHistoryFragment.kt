package com.application.intercom.gatekepper.Fragment.newFragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.ViewModel.gateKeeperViewModel.GateKeeperHomeViewModel
import com.application.intercom.data.model.factory.gateKeeperFactory.GateKeeperFactory
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.SingleEntryHistoryList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.gateKeeperRepo.GateKeeperHomeRepo
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.FragmentCompletedSingleEntryHistoryBinding
import com.application.intercom.gatekepper.gatekeeperAdapter.singleEntryHistory.CancelledSingleEntryHisAdapter
import com.application.intercom.gatekepper.gatekeeperAdapter.singleEntryHistory.CompletedSingleEntryHisAdapter
import com.application.intercom.gatekepper.gatekeeperAdapter.singleEntryHistory.OngoingSingleEntryHisAdapter
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.utils.*
import java.util.*
import kotlin.collections.ArrayList

class CompletedSingleEntryHistoryFragment(val flatOfBuildingId: String, val from: String) :
    Fragment(),
    CompletedSingleEntryHisAdapter.CompletedClick {
    lateinit var binding: FragmentCompletedSingleEntryHistoryBinding
    private var completedAdapter: CompletedSingleEntryHisAdapter? = null
    private lateinit var viewModel: GateKeeperHomeViewModel
    private lateinit var manager_viewModel: ManagerSideViewModel
    private var list = ArrayList<SingleEntryHistoryList.Data.Result>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompletedSingleEntryHistoryBinding.inflate(layoutInflater)
        initView()
        listener()
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()


    }

    private fun initialize() {
        val repo = GateKeeperHomeRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, GateKeeperFactory(repo))[GateKeeperHomeViewModel::class.java]
        val repo1 = ManagerSideRepo(BaseApplication.apiService)
        manager_viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo1))[ManagerSideViewModel::class.java]
    }

    private fun singleEntryHistroyList() {
        if (from.equals("manager")) {
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            manager_viewModel.manager_singleEntryHistoryList(token, "Completed", flatOfBuildingId)
        } else {
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            viewModel.singleEntryHistoryList(token, "Completed", flatOfBuildingId)
        }

    }

    private fun observer() {
        viewModel.singleEntryHistoryListLiveData.observe(
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
                                list.addAll(it.data.result)
                                binding.rcyCompleted.layoutManager =
                                    LinearLayoutManager(requireContext())
                                completedAdapter =
                                    CompletedSingleEntryHisAdapter(requireContext(), this, list)
                                binding.rcyCompleted.adapter = completedAdapter
                                completedAdapter!!.notifyDataSetChanged()
                                if (list.isEmpty()) {
                                    binding.lottieEmpty.visibility = View.VISIBLE
                                    binding.rcyCompleted.visibility = View.INVISIBLE
                                } else {
                                    binding.lottieEmpty.visibility = View.INVISIBLE
                                    binding.rcyCompleted.visibility = View.VISIBLE
                                }
                            } else if (it.status == AppConstants.STATUS_500) {
                                //requireContext().longToast(it.message)
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcyCompleted.visibility = View.INVISIBLE
                            } else if (it.status == AppConstants.STATUS_404) {
                                //requireContext().longToast(it.message)
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcyCompleted.visibility = View.INVISIBLE
                            } else {
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcyCompleted.visibility = View.INVISIBLE
                            }
                        }
                    }

                    is EmpResource.Failure -> {
                        EmpCustomLoader.hideLoader()
                        binding.lottieEmpty.visibility = View.VISIBLE
                        binding.rcyCompleted.visibility = View.INVISIBLE
                        ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                    }
                    else -> {}
                }
            })
        manager_viewModel.manager_singleEntryHistoryListLiveData.observe(
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
                                list.addAll(it.data.result)
                                binding.rcyCompleted.layoutManager =
                                    LinearLayoutManager(requireContext())
                                completedAdapter =
                                    CompletedSingleEntryHisAdapter(requireContext(), this, list)
                                binding.rcyCompleted.adapter = completedAdapter
                                completedAdapter!!.notifyDataSetChanged()
                                if (list.isEmpty()) {
                                    binding.lottieEmpty.visibility = View.VISIBLE
                                    binding.rcyCompleted.visibility = View.INVISIBLE
                                } else {
                                    binding.lottieEmpty.visibility = View.INVISIBLE
                                    binding.rcyCompleted.visibility = View.VISIBLE
                                }
                            } else if (it.status == AppConstants.STATUS_500) {
                                //requireContext().longToast(it.message)
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcyCompleted.visibility = View.INVISIBLE
                            } else if (it.status == AppConstants.STATUS_404) {
                                //requireContext().longToast(it.message)
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcyCompleted.visibility = View.INVISIBLE
                            } else {
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcyCompleted.visibility = View.INVISIBLE
                            }
                        }
                    }

                    is EmpResource.Failure -> {
                        EmpCustomLoader.hideLoader()
                        binding.lottieEmpty.visibility = View.VISIBLE
                        binding.rcyCompleted.visibility = View.INVISIBLE
                        ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                    }
                    else -> {}
                }
            })


    }

    private fun listener() {
        binding.seach.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                var text = p0.toString().trim()
                if (text.isNotEmpty()) {
                    if (text.length > 0) {
                        var tempFilterList = list.filter {
                            it.visitorName.lowercase(Locale.ROOT)
                                .contains(text.lowercase(Locale.ROOT)) || it.mobileNumber.lowercase(
                                Locale.ROOT
                            ).contains(
                                text.lowercase(
                                    Locale.ROOT
                                )
                            )
                        }


                        binding.rcyCompleted.layoutManager = LinearLayoutManager(requireContext())
                        completedAdapter = CompletedSingleEntryHisAdapter(
                            requireContext(), this@CompletedSingleEntryHistoryFragment,
                            tempFilterList as ArrayList<SingleEntryHistoryList.Data.Result>
                        )
                        binding.rcyCompleted.adapter = completedAdapter
                        completedAdapter!!.notifyDataSetChanged()

                    }
                } else {
                    binding.rcyCompleted.layoutManager = LinearLayoutManager(requireContext())
                    completedAdapter = CompletedSingleEntryHisAdapter(
                        requireContext(),
                        this@CompletedSingleEntryHistoryFragment,
                        list
                    )
                    binding.rcyCompleted.adapter = completedAdapter
                    completedAdapter!!.notifyDataSetChanged()
                }
            }

        })
    }

    private fun completedPopup(msg: SingleEntryHistoryList.Data.Result) {
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
        val tvCall = dialog.findViewById<ImageView>(R.id.imageView101)
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

        tvCall.setOnClickListener {
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

    override fun onClick(position: Int, msg: SingleEntryHistoryList.Data.Result) {
        completedPopup(msg)
    }

    override fun onResume() {
        super.onResume()
        if (!flatOfBuildingId.isNullOrEmpty()) {
            singleEntryHistroyList()
        }else{
            singleEntryHistroyList()
        }
    }
}