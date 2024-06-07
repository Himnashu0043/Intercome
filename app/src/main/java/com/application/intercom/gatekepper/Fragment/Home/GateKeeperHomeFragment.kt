package com.application.intercom.gatekepper.Fragment.Home

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
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
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.SingleEntryHistoryList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.gateKeeperRepo.GateKeeperHomeRepo
import com.application.intercom.databinding.FragmentGateKeeperHomeBinding
import com.application.intercom.gatekepper.Main.MainGateKepperActivity
import com.application.intercom.gatekepper.activity.newFlow.singleEntry.SingleEntryActivity
import com.application.intercom.gatekepper.gatekeeperAdapter.gatePassHistory.SecondGatePassHistoryAdapter
import com.application.intercom.gatekepper.gatekeeperAdapter.singleEntryHistory.OngoingSingleEntryHisAdapter
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.setNewFormatDate
import com.application.intercom.owner.adapter.ShowImgAdapter
import com.application.intercom.tenant.activity.notification.TenantNotificationActivity
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter
import com.application.intercom.utils.*


class GateKeeperHomeFragment : Fragment(), OngoingSingleEntryHisAdapter.Click,
    SecondGatePassHistoryAdapter.Click,
    CommunityImgAdapter.ClickImg {
    lateinit var binding: FragmentGateKeeperHomeBinding
    private lateinit var viewModel: GateKeeperHomeViewModel
    private lateinit var activity: MainGateKepperActivity
    private var singleEntrylist = ArrayList<SingleEntryHistoryList.Data.Result>()
    private var ongingAdapter: OngoingSingleEntryHisAdapter? = null
    private var adptr: SecondGatePassHistoryAdapter? = null
    private var list = ArrayList<GateKeeperListRes.Data.Result>()
    private var showphotoAdapter: ShowImgAdapter? = null
    private var photo_upload_list = ArrayList<String>()
    var drw: DrawerLayout? = null
    private var managerNumber: String = ""
    private var buildingId: String = ""
    private lateinit var dialog: Dialog
    private var showImg: String = ""

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGateKeeperHomeBinding.inflate(layoutInflater)
        activity = getActivity() as MainGateKepperActivity
        drw = activity.requireViewById(R.id.newGateKeepeerDrw)
        if (prefs.getString(
                SessionConstants.STOREID, GPSService.mLastLocation?.latitude.toString()
            ).isNotEmpty()
        ) {
            prefs.put(SessionConstants.STOREID, "")
            Toast.makeText(
                requireContext(),
                getString(R.string.this_link_is_not_valid_for_gateKeeper),
                Toast.LENGTH_SHORT
            ).show()
        }
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()

        ///old Data
        /*binding.commonBtn.tv.text = "Add new visitor"
        binding.commonBtn1.tv.text = "Create Gate Pass"
        val genderList = resources.getStringArray(R.array.LangOne)
        binding.homeToolbar.langSp.adapter =
            ArrayAdapter(requireContext(), R.layout.spinner_dropdown_item, genderList)
        binding.homeToolbar.langSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val itemSelected = parent.getItemAtPosition(position)
                binding.homeToolbar.tvLang.setText(itemSelected.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding.homeToolbar.tvLang.setOnClickListener {
            binding.homeToolbar.langSp.performClick()
        }*/
        ///old Data
    }

    private fun initialize() {
        val repo = GateKeeperHomeRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(
            this, GateKeeperFactory(repo)
        )[GateKeeperHomeViewModel::class.java]


    }

    private fun getManagerDetails() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.gateKeeperDetails(token)

    }

    private fun gateKeeperList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.gateKeeperList(token, buildingId, null, "Ongoing")

    }

    private fun singleEntryHistroyList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.singleEntryHistoryList(token, "Active", "")
    }

    private fun observer() {
        viewModel.gateKeeperDetailsLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            if (it.data.userDetails.fullName.isNullOrEmpty()) {
                                binding.homeToolbar.tvTittle.text = getString(R.string.hi_guest)
//                                binding.textView165.text = prefs.getString(
//                                    SessionConstants.KADDRESS,
//                                    GPSService.mLastLocation!!.latitude.toString()
//                                )

                            } else {
                                buildingId = it.data.userDetails.buildingId._id
                                println("----buildi$buildingId")
                                prefs.put(
                                    SessionConstants.NEWBUILDINGID,
                                    it.data.userDetails.buildingId._id
                                )
                                managerNumber = it.data.userDetails.buildingId.manager.phoneNumber
                                binding.imageView26.loadImagesWithGlideExt(it.data.userDetails.buildingId.manager.profilePic)
                                binding.tvContactName.text =
                                    it.data.userDetails.buildingId.manager.fullName
                                binding.homeToolbar.imageView59.loadImagesWithGlideExt(it.data.userDetails.profilePic)
                                binding.homeToolbar.tvTittle.text = it.data.userDetails.fullName

                                prefs.put(
                                    SessionConstants.FLATNAME,
                                    it.data.userDetails.buildingId.buildingName
                                )
                                gateKeeperList()
                                /*binding.textView165.text = prefs.getString(
                                    SessionConstants.KADDRESS,
                                    GPSService.mLastLocation?.latitude.toString()
                                )*/
                                //binding.textView165.text = it.data.userDetails.buildingId.address

                            }
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireContext().longToast(it.message)
                        } else {

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
                                singleEntrylist.clear()
                                singleEntrylist.addAll(it.data.result)
                                binding.rcyVisitor.layoutManager =
                                    LinearLayoutManager(
                                        requireContext(),
                                        RecyclerView.HORIZONTAL,
                                        false
                                    )
                                ongingAdapter =
                                    OngoingSingleEntryHisAdapter(
                                        requireContext(),
                                        this,
                                        singleEntrylist
                                    )
                                binding.rcyVisitor.adapter = ongingAdapter
                                ongingAdapter!!.notifyDataSetChanged()
                            } else if (it.status == AppConstants.STATUS_500) {
                                requireContext().longToast(it.message)
                            } else if (it.status == AppConstants.STATUS_404) {
                                requireContext().longToast(it.message)
                            } else {

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
        viewModel.visitorNotifyToUserLiveData.observe(
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
                                requireContext().longToast(it.message)
                            } else if (it.status == AppConstants.STATUS_500) {
                                requireContext().longToast(it.message)
                            } else if (it.status == AppConstants.STATUS_404) {
                                requireContext().longToast(it.message)
                            } else {

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

        viewModel.outSingleVisitorEntryGateKeeperLiveData.observe(
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
                                requireContext().longToast(getString(R.string.out_successfully))
                                singleEntryHistroyList()
                            } else if (it.status == AppConstants.STATUS_500) {
                                requireContext().longToast(it.message)
                            } else if (it.status == AppConstants.STATUS_404) {
                                requireContext().longToast(it.message)
                            } else {

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
                            binding.rcyHomeGatePass.layoutManager = LinearLayoutManager(
                                requireContext(),
                                RecyclerView.HORIZONTAL,
                                false
                            )
                            adptr = SecondGatePassHistoryAdapter(requireContext(), list, this, this,"ongoing_exit_gatePass")
                            binding.rcyHomeGatePass.adapter = adptr
                            adptr!!.notifyDataSetChanged()

                        } else if (it.status == AppConstants.STATUS_404) {
                            requireContext().longToast(it.message)
                        } else {

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
        binding.homeToolbar.ivMenu.setOnClickListener {
            drw!!.openDrawer(GravityCompat.START)

        }
        binding.imageView110.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${managerNumber}")
            startActivity(intent)
        }
        binding.textView242.setOnClickListener {
            startActivity(Intent(requireContext(), SingleEntryActivity::class.java))
        }
        binding.homeToolbar.ivNoti.setOnClickListener {
            startActivity(Intent(requireContext(), TenantNotificationActivity::class.java))
        }
        ///old Data
        /*  binding.homeToolbar.ivMenu.setOnClickListener {
             startActivity(
                 Intent(requireContext(), ProfileActivity::class.java).putExtra(
                     "from",
                     "gatekeeper"
                 )
             )
          }*/
        /* binding.commonBtn.tv.setOnClickListener {
             startActivity(
                 Intent(
                     requireContext(),
                     MainGateKepperActivity::class.java
                 ).putExtra("from", "from_gate_home").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

             )
         }
         binding.commonBtn1.tv.setOnClickListener {
             startActivity(
                 Intent(
                     requireContext(),
                     MainGateKepperActivity::class.java
                 ).putExtra("from", "from_gate_create_pass").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
             )
         }*/
        ///old Data
    }

    override fun onResume() {
        super.onResume()
        getManagerDetails()
        singleEntryHistroyList()

    }

    override fun onOutClick(position: Int, msg: SingleEntryHistoryList.Data.Result) {
        outPopup(msg)
    }

    override fun onNotify(visitorId: String, position: Int) {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.visitorNotifyToUser(token, visitorId)
    }

    override fun onOut(visitorId: String) {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.outSingleVisitorEntryGateKeeper(token, visitorId)
    }

    private fun outPopup(msg: SingleEntryHistoryList.Data.Result) {
        val dialog = this.let { Dialog(requireContext()) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.out_single_entry_his_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        val tvOut = dialog.findViewById<TextView>(R.id.tvOut)

        tvOut.visibility = View.GONE
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
        tvDelivery.text =
            "${msg.visitCategoryName} | ${msg.visitorType} ${getString(R.string.entry)}"

        tvCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${msg.mobileNumber}")
            startActivity(intent)
        }
//        tvOut.setOnClickListener {
//            dialog.dismiss()
//
//
//        }
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    override fun onViewPass(msg: GateKeeperListRes.Data.Result, position: Int) {

    }

    override fun onExitGatePass(id: String) {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
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
        showImg = ""
        dialog.show()

    }

}