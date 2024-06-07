package com.application.intercom.gatekepper.Fragment.newFragment.ownerTenantSingleEntryHistory

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.application.intercom.databinding.FragmentCancelledOwnerTenantSingleEntryHIstoryBinding
import com.application.intercom.gatekepper.gatekeeperAdapter.ownerTenantSingleEntryHistory.CancelledOwnerTenantSingleEntryHistoryAdapter
import com.application.intercom.gatekepper.gatekeeperAdapter.ownerTenantSingleEntryHistory.OngoingOwnerTenantSingleEntryHistoryAdapter
import com.application.intercom.helper.GPSService
import com.application.intercom.utils.*
import java.util.*
import kotlin.collections.ArrayList

class CancelledOwnerTenantSingleEntryHIstoryFragment(val type: String) : Fragment(),
    CancelledOwnerTenantSingleEntryHistoryAdapter.CancelOwnerTenant {
    lateinit var binding: FragmentCancelledOwnerTenantSingleEntryHIstoryBinding
    private var adptr: CancelledOwnerTenantSingleEntryHistoryAdapter? = null
    private lateinit var viewModel: OwnerSideViewModel
    private var list = ArrayList<OwnerTenantSingleEntryHistoryList.Data>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCancelledOwnerTenantSingleEntryHIstoryBinding.inflate(layoutInflater)
        initView()
        listener()
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()


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
        viewModel.singleVisitorHistoryTenant(token, "Rejected", "")
    }

    private fun ownerSingleVisitorHistoryList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.singleVisitorHistoryOwner(token, "Rejected", "")
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
                                binding.rcy.layoutManager = LinearLayoutManager(requireContext())
                                adptr = CancelledOwnerTenantSingleEntryHistoryAdapter(
                                    requireContext(),
                                    list, this
                                )
                                binding.rcy.adapter = adptr
                                adptr!!.notifyDataSetChanged()
                                if (list.isEmpty()) {
                                    binding.lottieEmpty.visibility = View.VISIBLE
                                    binding.rcy.visibility = View.INVISIBLE
                                } else {
                                    binding.lottieEmpty.visibility = View.INVISIBLE
                                    binding.rcy.visibility = View.VISIBLE
                                }
                            } else if (it.status == AppConstants.STATUS_404) {
                                // requireContext().longToast(it.message)
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcy.visibility = View.INVISIBLE
                            } else {
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcy.visibility = View.INVISIBLE
                            }
                        }
                    }

                    is EmpResource.Failure -> {
                        EmpCustomLoader.hideLoader()
                        binding.lottieEmpty.visibility = View.VISIBLE
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
                                binding.rcy.layoutManager = LinearLayoutManager(requireContext())
                                adptr =
                                    CancelledOwnerTenantSingleEntryHistoryAdapter(
                                        requireContext(),
                                        list,
                                        this
                                    )
                                binding.rcy.adapter = adptr
                                adptr!!.notifyDataSetChanged()
                                if (list.isEmpty()) {
                                    binding.lottieEmpty.visibility = View.VISIBLE
                                    binding.rcy.visibility = View.INVISIBLE
                                } else {
                                    binding.lottieEmpty.visibility = View.INVISIBLE
                                    binding.rcy.visibility = View.VISIBLE
                                }
                            } else if (it.status == AppConstants.STATUS_404) {
                                //requireContext().longToast(it.message)
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcy.visibility = View.INVISIBLE
                            } else {
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcy.visibility = View.INVISIBLE
                            }
                        }
                    }

                    is EmpResource.Failure -> {
                        EmpCustomLoader.hideLoader()
                        binding.lottieEmpty.visibility = View.VISIBLE
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


                        binding.rcy.layoutManager = LinearLayoutManager(requireContext())
                        adptr =
                            CancelledOwnerTenantSingleEntryHistoryAdapter(
                                requireContext(),
                                tempFilterList as ArrayList<OwnerTenantSingleEntryHistoryList.Data>,
                                this@CancelledOwnerTenantSingleEntryHIstoryFragment
                            )
                        binding.rcy.adapter = adptr
                        adptr!!.notifyDataSetChanged()

                    }
                } else {
                    binding.rcy.layoutManager = LinearLayoutManager(requireContext())
                    adptr =
                        CancelledOwnerTenantSingleEntryHistoryAdapter(
                            requireContext(),
                            list,
                            this@CancelledOwnerTenantSingleEntryHIstoryFragment
                        )
                    binding.rcy.adapter = adptr
                    adptr!!.notifyDataSetChanged()
                }
            }

        })
    }

    override fun onCLick(msg: OwnerTenantSingleEntryHistoryList.Data, position: Int) {
        detailsPopup(msg)
    }

    private fun detailsPopup(msg: OwnerTenantSingleEntryHistoryList.Data) {
        val dialog = this.let { Dialog(requireContext()) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.single_entry_details_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        val tvNotity = dialog.findViewById<TextView>(R.id.tvnotify)
        val tvName = dialog.findViewById<TextView>(R.id.textView166)
        val tvflat = dialog.findViewById<TextView>(R.id.textView167)
        val tvPhone = dialog.findViewById<TextView>(R.id.textView170)
        val tvAddress = dialog.findViewById<TextView>(R.id.textView172)
        val ivCall = dialog.findViewById<ImageView>(R.id.imageView102)
        val tvNote = dialog.findViewById<TextView>(R.id.textView174)
        val tvDelivery = dialog.findViewById<TextView>(R.id.textView168)
        val tvEdit = dialog.findViewById<TextView>(R.id.tvEdit)
        val tvimg = dialog.findViewById<ImageView>(R.id.imageView91)
        tvNotity.visibility = View.INVISIBLE
        ivCall.visibility = View.VISIBLE
        tvEdit.visibility = View.INVISIBLE
        tvName.text = msg.visitorName
        tvflat.text = msg.visitorName
        tvPhone.text = msg.mobileNumber
        tvAddress.text = msg.address
        tvNote.text = msg.note
        tvimg.loadImagesWithGlideExt(msg.photo)
        tvDelivery.text = "${msg.visitCategoryName} | ${msg.visitorType} Entry"

        tvNotity.setOnClickListener {
            dialog.dismiss()
            //  startActivity(Intent(this, SingleEntryHistoryActivity::class.java))
        }
        tvEdit.setOnClickListener {
            dialog.dismiss()
        }
        ivCall.setOnClickListener {
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
        if (type.equals("tenant")) {
            tenantSingleVisitorHistoryList()
        } else {
            ownerSingleVisitorHistoryList()
        }
    }
}