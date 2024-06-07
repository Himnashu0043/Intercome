package com.application.intercom.manager.visitorAndGatePass.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.GateKeeperListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.FragmentManagerCompletedGatePassListBinding
import com.application.intercom.gatekepper.gatekeeperAdapter.gatePassHistory.SecondGatePassHistoryAdapter
import com.application.intercom.helper.setnewFormatDate
import com.application.intercom.owner.adapter.ShowImgAdapter
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter
import com.application.intercom.utils.*


class ManagerCompletedGatePassListFragment(val flatOfBuildingId: String) : Fragment(),
    SecondGatePassHistoryAdapter.Click,
    CommunityImgAdapter.ClickImg {
    lateinit var binding: FragmentManagerCompletedGatePassListBinding
    private lateinit var manager_viewModel: ManagerSideViewModel
    private var list = ArrayList<GateKeeperListRes.Data.Result>()
    private var adptr: SecondGatePassHistoryAdapter? = null
    private var photo_upload_list = ArrayList<String>()
    private var showphotoAdapter: ShowImgAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManagerCompletedGatePassListBinding.inflate(layoutInflater)
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()
    }

    private fun initialize() {
        val repo1 = ManagerSideRepo(BaseApplication.apiService)
        manager_viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo1))[ManagerSideViewModel::class.java]


    }

    private fun managerGateKeeperList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        manager_viewModel.managergateKeeperList(token, prefs.getString(SessionConstants.NEWBUILDINGID, ""), flatOfBuildingId, "Completed")

    }

    private fun observer() {

        manager_viewModel.managerGateKeeperListLiveData.observe(requireActivity(), Observer {
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
                            binding.rcy.layoutManager = LinearLayoutManager(
                                requireContext()
                            )
                            adptr = SecondGatePassHistoryAdapter(requireContext(), list, this, this,"")
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
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
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
    }

    private fun lstnr() {

    }

    override fun onViewPass(msg: GateKeeperListRes.Data.Result, position: Int) {
        inPopup(msg)
    }

    override fun onExitGatePass(id: String) {
        ///no implement in Completed
    }

    override fun showImg(img: String) {

    }

    private fun inPopup(msg: GateKeeperListRes.Data.Result) {
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

    override fun onResume() {
        super.onResume()
        if (!flatOfBuildingId.isNullOrEmpty()) {
            managerGateKeeperList()

        } else {
            managerGateKeeperList()
        }
    }

}