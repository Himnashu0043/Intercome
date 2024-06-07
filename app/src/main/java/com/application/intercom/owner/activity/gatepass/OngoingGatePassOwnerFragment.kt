package com.application.intercom.owner.activity.gatepass

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome.OwnerHomeViewModel
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.ViewModel.tenantViewModel.tenantHomeViewModel.TenantHomeViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.factory.ownerfactory.ownerHome.OwnerHomeFactory
import com.application.intercom.data.model.factory.tenantFactory.TenantHomeFactory
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.GateKeeperListRes
import com.application.intercom.data.model.remote.owner.gatePass.OwnerGatepassList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.data.repository.tenantRepo.tenantHomeRepo.TenantHomeRepo
import com.application.intercom.databinding.FragmentOngoingGatePassOwnerBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.setnewFormatDate
import com.application.intercom.owner.adapter.OwnerGatePassAdapter
import com.application.intercom.owner.adapter.ShowImgAdapter
import com.application.intercom.utils.*
import java.util.ArrayList

class OngoingGatePassOwnerFragment(val flatOfBuildingId: String) : Fragment(),
    OwnerGatePassAdapter.Click {
    lateinit var binding: FragmentOngoingGatePassOwnerBinding
    private lateinit var viewModel: OwnerSideViewModel
    private var gatePassList = ArrayList<OwnerGatepassList.Data.Result>()
    private var adptr: OwnerGatePassAdapter? = null
    private var role: String = ""
    private var photo_upload_list = ArrayList<String>()
    private var showphotoAdapter: ShowImgAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOngoingGatePassOwnerBinding.inflate(layoutInflater)
        role = prefs.getString(SessionConstants.ROLE, GPSService.mLastLocation?.latitude.toString())
        println("----ongoRol3$role")
        initView()
        lstnr()

        return binding.root
    }

    private fun initialize() {
        val repo = OwnerSideRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, OwnerSideFactory(repo))[OwnerSideViewModel::class.java]


    }

    private fun gatePassListOwner() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.gatePassListOwner(token, "Ongoing", flatOfBuildingId)
    }

    private fun observer() {
        viewModel.gatePassListOwnerLiveData.observe(requireActivity(), androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            gatePassList.clear()
                            gatePassList.addAll(it.data.result)
                            binding.rcy.visibility = View.VISIBLE
                            binding.rcy.layoutManager = LinearLayoutManager(requireContext())
                            adptr = OwnerGatePassAdapter(requireContext(), gatePassList, this)
                            binding.rcy.adapter = adptr
                            adptr!!.notifyDataSetChanged()
                            if (gatePassList.isEmpty()) {
                                binding.rcy.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                binding.rcy.visibility = View.VISIBLE
                                binding.lottieEmpty.visibility = View.INVISIBLE
                            }
                        } else if (it.status == AppConstants.STATUS_404) {
                            //requireContext().longToast(it.message)
                            binding.rcy.visibility = View.INVISIBLE
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                           //// requireContext().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                            binding.rcy.visibility = View.INVISIBLE
                        } else {
                            binding.rcy.visibility = View.INVISIBLE
                            binding.lottieEmpty.visibility = View.VISIBLE
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

    private fun initView() {
        initialize()
        observer()
        if (!flatOfBuildingId.equals("null")) {
            gatePassListOwner()
            binding.rcy.visibility = View.VISIBLE

        } else {
            binding.rcy.visibility = View.INVISIBLE
        }
    }

    private fun lstnr() {

    }

    override fun onEditCLick(position: Int, msg: OwnerGatepassList.Data.Result) {
        if (role.equals("tenant")) {
            startActivity(
                Intent(requireContext(), OwnerCreateGatePassActivity::class.java).putExtra(
                    "key",
                    "edit_tenant"
                ).putExtra("editList", gatePassList[position])
            )
        } else {

            startActivity(
                Intent(requireContext(), OwnerCreateGatePassActivity::class.java).putExtra(
                    "key",
                    "edit_owner"
                ).putExtra("editList", gatePassList[position])
            )
        }
    }

    override fun onViewPass(msg: OwnerGatepassList.Data.Result, position: Int) {
        inPopup(msg)
    }

    private fun inPopup(msg: OwnerGatepassList.Data.Result) {
        val dialog = this.let { Dialog(requireContext()) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.gate_pass_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        val tvNotity = dialog.findViewById<TextView>(R.id.tvgatepssnotify)
        val tvName = dialog.findViewById<TextView>(R.id.textView166)
        val tvFlat = dialog.findViewById<TextView>(R.id.textView167)
        val tvPhone = dialog.findViewById<TextView>(R.id.textView170)
        val tvOwnerandTenant = dialog.findViewById<TextView>(R.id.textView1722)
        val tvlastDate = dialog.findViewById<TextView>(R.id.textView172)
        val tvFromTime = dialog.findViewById<TextView>(R.id.tvInTime)
        val tvToTime = dialog.findViewById<TextView>(R.id.tvoutTime)
        val tvNote = dialog.findViewById<TextView>(R.id.tvNote)
        val tvDelivery = dialog.findViewById<TextView>(R.id.textView168)
        val img = dialog.findViewById<ImageView>(R.id.imageView91)
        val phoneCall = dialog.findViewById<ImageView>(R.id.imageView97)
        val ownerphoneCall = dialog.findViewById<ImageView>(R.id.imageView98)
        val rcy = dialog.findViewById<RecyclerView>(R.id.rcyPhoto)
        tvDelivery.visibility = View.GONE
        tvNotity.visibility = View.GONE
        phoneCall.visibility = View.INVISIBLE
        ownerphoneCall.visibility = View.INVISIBLE
        tvName.text = msg.contactName
        tvFlat.text = msg.flatInfo.name
        tvPhone.text = msg.phoneNumber
        tvOwnerandTenant.text = msg.ownerInfo.get(0).phoneNumber
        tvlastDate.text = setnewFormatDate(msg.toDate)
        println("----${setnewFormatDate(msg.toDate)}")
        tvFromTime.text = msg.exitTime
        tvNote.text = msg.description
        img.loadImagesWithGlideExt(msg.flatInfo.document)
        photo_upload_list.clear()
        photo_upload_list.addAll(msg.photo)
        rcy.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        showphotoAdapter = ShowImgAdapter(requireContext(), photo_upload_list)
        rcy.adapter = showphotoAdapter
        showphotoAdapter!!.notifyDataSetChanged()
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }
}