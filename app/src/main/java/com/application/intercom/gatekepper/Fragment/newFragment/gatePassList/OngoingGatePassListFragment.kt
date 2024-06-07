package com.application.intercom.gatekepper.Fragment.newFragment.gatePassList

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
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
import com.application.intercom.data.model.ViewModel.gateKeeperViewModel.GateKeeperHomeViewModel
import com.application.intercom.data.model.factory.gateKeeperFactory.GateKeeperFactory
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.GateKeeperListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.gateKeeperRepo.GateKeeperHomeRepo
import com.application.intercom.databinding.FragmentOngoingGatePassListBinding
import com.application.intercom.gatekepper.gatekeeperAdapter.gatePassHistory.SecondGatePassHistoryAdapter
import com.application.intercom.helper.getCurrentDate
import com.application.intercom.owner.adapter.ShowImgAdapter
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter
import com.application.intercom.utils.*

class OngoingGatePassListFragment(var flatOfBuildingId: String) : Fragment(),
    SecondGatePassHistoryAdapter.Click,
    CommunityImgAdapter.ClickImg {
    private var showphotoAdapter: ShowImgAdapter? = null
    private var photo_upload_list = ArrayList<String>()
    private var mobileOwnerNumber: String = ""
    private var mobileNumber: String = ""
    private var showImg: String = ""
    lateinit var binding: FragmentOngoingGatePassListBinding
    private lateinit var viewModel: GateKeeperHomeViewModel
    private var list = ArrayList<GateKeeperListRes.Data.Result>()
    private var adptr: SecondGatePassHistoryAdapter? = null
    private lateinit var dialog: Dialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOngoingGatePassListBinding.inflate(layoutInflater)
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()


    }

    private fun initialize() {
        val repo = GateKeeperHomeRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(
            this, GateKeeperFactory(repo)
        )[GateKeeperHomeViewModel::class.java]


    }

    private fun gateKeeperList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        viewModel.gateKeeperList(token, null, flatOfBuildingId, "Ongoing")

    }

    private fun observer() {
        viewModel.gateKeeperListLiveData.observe(requireActivity(), Observer {
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
                                requireContext(),
                                RecyclerView.VERTICAL, false
                            )
                            adptr = SecondGatePassHistoryAdapter(
                                requireContext(),
                                list,
                                this,
                                this, "ongoing_exit_gatePass"
                            )
                            binding.rcy.adapter = adptr
                            adptr!!.notifyDataSetChanged()
                            if (list.isEmpty()) {
                                binding.lottieEmpty.visibility = View.VISIBLE
                                binding.rcy.visibility = View.INVISIBLE
                            } else {
                                binding.lottieEmpty.visibility = View.GONE
                                binding.rcy.visibility = View.VISIBLE
                            }

                        } else if (it.status == AppConstants.STATUS_404) {
                            // requireContext().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                            binding.rcy.visibility = View.INVISIBLE
                        } else if (it.status == AppConstants.STATUS_500) {
                            binding.lottieEmpty.visibility = View.VISIBLE
                            binding.rcy.visibility = View.INVISIBLE
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
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
        viewModel.exitGatePassLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            gateKeeperList()

                        } else if (it.status == AppConstants.STATUS_404) {
                            requireContext().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_500) {
                            requireContext().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            requireContext().longToast(it.message)
                        } else {
                            requireContext().longToast(it.message)
                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                }
                else -> {}
            }
        })
    }

    private fun lstnr() {

    }

    override fun onViewPass(msg: GateKeeperListRes.Data.Result, position: Int) {
        val dialog = this.let { Dialog(requireContext()) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.gate_pass_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
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
        val crossImg = dialog.findViewById<ImageView>(R.id.ivCrossGatePassGateKeeper)
        val tvOwnerCalling = dialog.findViewById<ImageView>(R.id.imageView98)
        val date = getCurrentDate()
        tvDelivery.visibility = View.GONE
        tvNotity.visibility = View.GONE
        crossImg.visibility = View.VISIBLE
        tvName.text = msg.contactName
        tvphone.text = msg.phoneNumber
        mobileNumber = msg.phoneNumber
        mobileOwnerNumber = msg.ownerInfo[0].phoneNumber
        tvTime.text = msg.exitTime
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


        tvRecy.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        showphotoAdapter = ShowImgAdapter(requireContext(), photo_upload_list)
        tvRecy.adapter = showphotoAdapter
        showphotoAdapter!!.notifyDataSetChanged()
        crossImg.setOnClickListener {
            dialog.dismiss()
        }
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
    }

    override fun onExitGatePass(id: String) {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        viewModel.exitGatePass(token, id)
    }

    override fun showImg(img: String) {
        showImg = img
        dialogProile()
    }

    private fun dialogProile() {
        dialog = Dialog(requireContext())
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

    override fun onResume() {
        super.onResume()
        if (!flatOfBuildingId.isNullOrEmpty()) {
            gateKeeperList()
        } else {
            gateKeeperList()
        }
    }


}