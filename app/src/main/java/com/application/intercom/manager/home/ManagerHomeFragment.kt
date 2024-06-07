package com.application.intercom.manager.home

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.BaseFragment
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerHomeViewModel.ManagerHomeViewModel
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerHome.ManagerHomeFactory
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.remote.manager.managerSide.bill.MangerBillPendingListRes
import com.application.intercom.data.model.remote.manager.managerSide.complain.ManagerPendingListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerHomeRepo.ManagerHomeRepo
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.FragmentManagerHomeBinding
import com.application.intercom.db.dao.ServicesCategoryDao
import com.application.intercom.db.entity.ServicesCategoryTable
import com.application.intercom.gatekepper.activity.newFlow.singleEntry.SingleEntryActivity
import com.application.intercom.helper.GPSService
import com.application.intercom.manager.bills.ApprovalBillingManagerActivity
import com.application.intercom.manager.bills.UnPaidBillingManagerActivity
import com.application.intercom.manager.bills.ViewReceiptManagerActivity
import com.application.intercom.manager.complaint.PendingComplaintsAdapter
import com.application.intercom.manager.complaint.RegisterComplainsOwnerTenantDetailsActivity
import com.application.intercom.manager.complaint.RegisterComplaintsActivity
import com.application.intercom.manager.gatekeeper.GateKeeperListingActivity
import com.application.intercom.manager.main.ManagerMainActivity
import com.application.intercom.manager.notice.NoticeBoardActivity
import com.application.intercom.manager.service.ManagerListOfServicesActivity
import com.application.intercom.tenant.Model.HomeExtraModal
import com.application.intercom.tenant.activity.notification.TenantNotificationActivity
import com.application.intercom.tenant.adapter.home.TenantHomeExtraAdapter
import com.application.intercom.user.service.ServiceViewModel
import com.application.intercom.utils.*

class ManagerHomeFragment : BaseFragment<FragmentManagerHomeBinding>(),
    ManagerServicesAdapter.ServiceHome, TenantHomeExtraAdapter.ExtraClick,
    GPSService.OnLocationUpdate, ManagerUnpaidBillingsAdapter.ManagerUserNotify,
    ManagerBillsToApproveAdapter.MarkClick, PendingComplaintsAdapter.PendingClick {
    private var recentComplaintAdapter: ManagerRecentComplaintAdapter? = null
    private var unpaidBillingsAdapter: ManagerUnpaidBillingsAdapter? = null
    private var billsToApproveAdapter: ManagerBillsToApproveAdapter? = null
    private var servicesAdapter: ManagerServicesAdapter? = null
    private lateinit var viewModel: ManagerHomeViewModel
    private lateinit var side_viewModel: ManagerSideViewModel
    private var pendingComplainListList = ArrayList<ManagerPendingListRes.Data.Result>()
    private var billPendingList = ArrayList<MangerBillPendingListRes.Data.Result>()
    private var projectId: String = ""
    private lateinit var service_viewModel: ServiceViewModel
    private var servicesCategoryDao: ServicesCategoryDao? = null
    private var serviceList: ArrayList<ServicesCategoryTable> = ArrayList()
    private var resolvedComplainListList = ArrayList<MangerBillPendingListRes.Data.Result>()
    private var extraList = ArrayList<HomeExtraModal>()
    private var extra_adapter: TenantHomeExtraAdapter? = null
    private lateinit var activity: ManagerMainActivity
    var drw: DrawerLayout? = null
    val RESULT_PERMISSION_LOCATION = 1
    override fun lstnr() {
        binding.userHomeToolbar.ivMenu.setOnClickListener {
            /*startActivity(
                Intent(requireContext(), ProfileActivity::class.java).putExtra(
                    "from",
                    "manager"
                )
            )*/
            drw!!.openDrawer(GravityCompat.START)

        }
     /*   binding.userHomeToolbar.ivhomeChat.setOnClickListener {
            startActivity(Intent(requireContext(), TenantChatActivity::class.java))
        }
        binding.userHomeToolbar.tvLang.setOnClickListener {
            binding.userHomeToolbar.langSp.performClick()
        }*/
        binding.tvRecentComplaintViewAll.setOnClickListener {
            startActivity(
                Intent(requireContext(), RegisterComplaintsActivity::class.java)
            )
        }
        binding.tvServiceViewAll.setOnClickListener {
            startActivity(
                Intent(requireContext(), ManagerMainActivity::class.java).putExtra(
                    "from",
                    "from_side_service"
                )
            )
        }
        binding.tvUnpaidBillsViewAll.setOnClickListener {
            startActivity(
                Intent(requireContext(), UnPaidBillingManagerActivity::class.java))
        }
        binding.tvBillsToApproveViewAll.setOnClickListener {
            startActivity(
                Intent(requireContext(), ApprovalBillingManagerActivity::class.java))
        }
        binding.userHomeToolbar.ivNoti.setOnClickListener {
            startActivity(Intent(requireContext(), TenantNotificationActivity::class.java))
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentManagerHomeBinding {
        servicesCategoryDao = BaseApplication.appDatabase.servicesDao()
        /* lifecycleScope.launch {
             recentList =
                 servicesCategoryDao?.servicesCategoryList(true) as ArrayList<ServicesCategoryTable>
             recently_adapter?.notifiyData(recentList)
         }
         Log.d("ffgfqfdsdscd", "onCreateView: $recentList")*/
        activity = getActivity() as ManagerMainActivity
        drw = activity.requireViewById(R.id.managerDrw)
        return FragmentManagerHomeBinding.inflate(inflater, container, false)
    }
    override fun init() {
        binding.userHomeToolbar.ivhomeChat.visibility = View.INVISIBLE
        initialize()
        // getServicesList()
        binding.manL.setOnRefreshListener {
            billPendingList()
        }
        billPendingList()
        approvalList()
        recentCompainList()
        extraList.add(HomeExtraModal(getString(R.string.add_visitors), R.drawable.complain_img))
        extraList.add(HomeExtraModal(getString(R.string.add_billings), R.drawable.payment_img))
        extraList.add(HomeExtraModal(getString(R.string.add_gatekeeper), R.drawable.gatekeeper_img))
        extraList.add(HomeExtraModal(getString(R.string.add_notice), R.drawable.notice_img))
//        val genderList = resources.getStringArray(R.array.LangOne)
//        binding.userHomeToolbar.langSp.adapter =
//            ArrayAdapter(requireContext(), R.layout.spinner_dropdown_item, genderList)
//        binding.userHomeToolbar.langSp.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    parent: AdapterView<*>,
//                    view: View?,
//                    position: Int,
//                    id: Long,
//                ) {
//                    val itemSelected = parent.getItemAtPosition(position)
//                    binding.userHomeToolbar.tvLang.setText(itemSelected.toString())
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>?) {}
//            }
        setRecentComplaintAdapter()
        setServicesAdapter()


    }
    private fun initialize() {
        val repo = ManagerHomeRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(
            this, ManagerHomeFactory(repo)
        )[ManagerHomeViewModel::class.java]
        /*  val service_repo = ServiceRepository(BaseApplication.apiService)
          service_viewModel =
              ViewModelProvider(this, ServiceFactory(service_repo))[ServiceViewModel::class.java]*/
        val siderepo = ManagerSideRepo(BaseApplication.apiService)
        side_viewModel =
            ViewModelProvider(this, ManagerSideFactory(siderepo))[ManagerSideViewModel::class.java]

    }
    private fun getManagerDetails() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.managerDetails(token)

    }
    private fun getServicesList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation?.latitude.toString()
        )
        service_viewModel.getServicesListAndSearch(token, "")
    }
    private fun billPendingList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        side_viewModel.billpending(token, "Pending",null)
    }
    private fun recentCompainList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        side_viewModel.pendingComplain(token, "Pending")
    }
    private fun approvalList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        side_viewModel.billpending(token, "Unapproved",null)
    }
    override fun observer() {
        viewModel.managerDetailsLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            if (it.data.userDetails.fullName.isNullOrEmpty()) {
                                binding.userHomeToolbar.tvTittle.text = "Hi,Guest"
                                binding.userHomeToolbar.tvHomeDes.text = prefs.getString(
                                    SessionConstants.KADDRESS,
                                    GPSService.mLastLocation!!.latitude.toString()
                                )
//                                    "Amrapali, Sector 50, Noida"
                            } else {
                                binding.userHomeToolbar.imageView59.loadImagesWithGlideExt(it.data.userDetails.profilePic)
                                binding.userHomeToolbar.tvTittle.text = it.data.userDetails.fullName
                                binding.userHomeToolbar.tvHomeDes.text =
                                    it.data.userData.buildingName
                                prefs.put(
                                    SessionConstants.NEWBUILDINGID, it.data.userData._id
                                )
                                prefs.put(
                                    SessionConstants.MANAGERNAME, it.data.userDetails.fullName!!
                                )
                                prefs.put(
                                    SessionConstants.NAMEBUILDING, it.data.userData.buildingName
                                )
                                prefs.put(SessionConstants.PROJECTID, it.data.userData.projectId)
                                prefs.put(
                                    SessionConstants.MFSACCOUNT,
                                    it.data.userData.mfsAccnNumber
                                )
                                prefs.put(
                                    SessionConstants.MFSHOLDERNAME,
                                    it.data.userData.mfsAccnHolderName
                                )
                                prefs.put(SessionConstants.MFS, it.data.userData.mfs)
                                prefs.put(SessionConstants.ACCOUNT, it.data.userData.accNumber)
                                prefs.put(
                                    SessionConstants.HOLDERNAME,
                                    it.data.userData.accHolderNAme
                                )
                                prefs.put(SessionConstants.PAYTYPE, it.data.userData.payMethod)
                                if (!it.data.userData.associationType.isNullOrEmpty()) {
                                    if (it.data.userData.associationType == "Owner") {
                                        prefs.put(
                                            SessionConstants.ASSOCIATION_TYPE,
                                            it.data.userData.associationType ?: ""
                                        )
                                    }
                                }
                                //binding.userHomeToolbar.tvHomeDes.text = "${it.data.userData.get(0).buildingId.division} , ${it.data.userData[0].buildingId.district}"
                            }

                            println("====daaa${it.data.userData}")
                            println("====buildinGManagerdaaa${it.data.userData._id}")
                            println("====${it.data.userDetails}")
//                            prefs.put(
//                                SessionConstants.BUILDINGID, it.data.userData[0].buildingId._id
//                            )
//                            projectId = it.data.userData.get(0).buildingId.projectId._id
//                            println("-----pppject${projectId}")
                            /* prefs.setBoolean(
                                 SessionConstants.SUBSCRIPTION,
                                 it.data.userDetails.subscription_active
                             )
                             prefs.put(
                                 SessionConstants.BUILDINGID,
                                 it.data.userData[0].buildingId._id
                             )*/
                            //  getOwnerCommunityList()
                            redicrtionByDeepLinking()
                        } else if (it.status == AppConstants.STATUS_404) {
                           // requireActivity().longToast(it.message)
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(requireActivity(), it.throwable!!)
                }
                else -> {}
            }
        })
        /*service_viewModel.serviceLiveData.observe(requireActivity(), Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            serviceList.clear()
                            serviceList.addAll(it.data.docs)
                            setServicesAdapter(serviceList)
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
        })*/
        side_viewModel.billpendingLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    //EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    //EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            billPendingList.clear()
                            billPendingList.addAll(it.data.result!!.take(4))
                            setUnpaidBillsAdapter(billPendingList)
                        } else if (it.status == AppConstants.STATUS_500) {
                            requireContext().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                            //requireContext().longToast(it.message)
                        } else {

                        }
                    }
                    binding.manL.isRefreshing = false
                }

                is EmpResource.Failure -> {
                    binding.manL.isRefreshing = false
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                }
                else -> {}
            }
        })
        side_viewModel.pendingComplainLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            pendingComplainListList.clear()
                            pendingComplainListList.addAll(it.data.result.take(2))
                            setRecentComplaintAdapter(pendingComplainListList)
                            if (pendingComplainListList.isEmpty()) {
                                binding.tvRecentComplaint.visibility = View.GONE
                                binding.tvRecentComplaintViewAll.visibility = View.GONE
                                binding.rvRecentComplaint.visibility = View.GONE
                            } else {
                                binding.tvRecentComplaint.visibility = View.VISIBLE
                                binding.tvRecentComplaintViewAll.visibility = View.VISIBLE
                                binding.rvRecentComplaint.visibility = View.VISIBLE
                            }

                        } else if (it.status == AppConstants.STATUS_500) {
                            requireContext().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                           // requireContext().longToast(it.message)
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
        side_viewModel.managerNotifyUserLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            billPendingList()
                            requireContext().longToast(getString(R.string.notify_successfully))
                        } else if (it.status == AppConstants.STATUS_500) {
                            requireContext().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                           // requireContext().longToast(it.message)
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
        side_viewModel.billApproveLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            resolvedComplainListList.clear()
                            resolvedComplainListList.addAll(it.data.result!!)
                            setBillsToApproveAdapter(resolvedComplainListList)
                        } else if (it.status == AppConstants.STATUS_500) {
                            requireContext().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireContext().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            //requireContext().longToast(it.message)
                            binding.rvBillsToApprove.visibility = View.GONE
                            binding.tvBillsToApprove.visibility = View.GONE
                            binding.tvBillsToApproveViewAll.visibility = View.GONE
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
        side_viewModel.markAsPaidManagerLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            approvalList()
                        } else if (it.status == AppConstants.STATUS_500) {
                            requireContext().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_404) {
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
        side_viewModel.rejectBillManagerLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            approvalList()
                        } else if (it.status == AppConstants.STATUS_500) {
                            requireActivity().longToast(it.message ?: "")
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireActivity().longToast(it.message ?: "")
                        } else {
                            requireActivity().longToast(it.message ?: "")
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
    private fun redicrtionByDeepLinking() {
        if (prefs.getString(
                SessionConstants.STORENAME, GPSService.mLastLocation?.latitude.toString()
            ).isNotEmpty()
        ) {
            if (prefs.getString(
                    SessionConstants.STORENAME, GPSService.mLastLocation?.latitude.toString()
                ) == "Community"
            ) {
                Toast.makeText(
                    requireContext(),
                    "This link is not Valid for Manager!!",
                    Toast.LENGTH_SHORT
                ).show()
                /*startActivity(
                    Intent(requireContext(), TenantMyCommunityActivity::class.java).putExtra(
                        "from", prefs.getString(
                            SessionConstants.ROLE, ""
                        )
                    ).putExtra(
                        "storeId", prefs.getString(
                            SessionConstants.STOREID, GPSService.mLastLocation?.latitude.toString()
                        )
                    )
                )*/
                prefs.put(SessionConstants.STORENAME, "")
                prefs.put(SessionConstants.STOREID, "")
            } else /*if (prefs.getString(
                    SessionConstants.STORENAME, GPSService.mLastLocation?.latitude.toString()
                ) == "Notice"
            )*/ {
                startActivity(
                    Intent(requireContext(), NoticeBoardActivity::class.java).putExtra(
                        "storeId", prefs.getString(
                            SessionConstants.STOREID, GPSService.mLastLocation?.latitude.toString()
                        )
                    )
                )
                /*startActivity(
                    Intent(requireContext(), TenantNoticeBoardDetailsActivity::class.java).putExtra(
                        "from", prefs.getString(
                            SessionConstants.ROLE, ""
                        )
                    ).putExtra(
                        "viewId", prefs.getString(
                            SessionConstants.STOREID, GPSService.mLastLocation?.latitude.toString()
                        )
                    )
                )*/
                prefs.put(SessionConstants.STORENAME, "")
                prefs.put(SessionConstants.STOREID, "")
            }
        }
    }
    private fun setRecentComplaintAdapter(list: ArrayList<ManagerPendingListRes.Data.Result> = ArrayList()) {
        binding.rvRecentComplaint.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recentComplaintAdapter = ManagerRecentComplaintAdapter(requireContext(), list,this)
        binding.rvRecentComplaint.adapter = recentComplaintAdapter

    }
    private fun setUnpaidBillsAdapter(list: ArrayList<MangerBillPendingListRes.Data.Result> = ArrayList()) {
        binding.rvUnpaidBilling.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        unpaidBillingsAdapter = ManagerUnpaidBillingsAdapter(requireContext(), list, this)
        binding.rvUnpaidBilling.adapter = unpaidBillingsAdapter
        unpaidBillingsAdapter!!.notifyDataSetChanged()
    }
    private fun setBillsToApproveAdapter(list1: ArrayList<MangerBillPendingListRes.Data.Result> = ArrayList()) {
        binding.rvBillsToApprove.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        billsToApproveAdapter = ManagerBillsToApproveAdapter(requireContext(), list1, this)
        binding.rvBillsToApprove.adapter = billsToApproveAdapter
        billsToApproveAdapter!!.notifyDataSetChanged()
    }
    private fun setServicesAdapter(list: ArrayList<ServicesCategoryTable> = ArrayList()) {
        /* binding.rvHomeService.layoutManager =
             LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
         servicesAdapter = ManagerServicesAdapter(requireContext(), list, this)
         binding.rvHomeService.adapter = servicesAdapter
         servicesAdapter!!.notifyDataSetChanged()*/
        binding.rvHomeService.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        extra_adapter = TenantHomeExtraAdapter(requireContext(), this, extraList)
        binding.rvHomeService.adapter = extra_adapter
        extra_adapter!!.notifyDataSetChanged()

    }
    override fun onServiceClick(position: Int) {
        startActivity(
            Intent(
                requireContext(),
                ManagerListOfServicesActivity::class.java
            ).putExtra("from", "manager_home")
                .putExtra("serviceList", serviceList[position])
        )
    }
    override fun onClick(position: Int) {
        when (position) {
            0 -> {
                /*startActivity(
                    Intent(
                        requireContext(), RegisterComplaintsActivity::class.java
                    )
                )*/
                startActivity(
                    Intent(requireContext(), SingleEntryActivity::class.java).putExtra(
                        "from",
                        "manager"
                    )
                )
//                requireActivity().finish()
            }
            1 -> {
                startActivity(Intent(requireContext(), UnPaidBillingManagerActivity::class.java))
                //flat_BottomSheet()
            }
            2 -> {
                startActivity(Intent(requireContext(), GateKeeperListingActivity::class.java))
            }
            3 -> {
                startActivity(Intent(requireContext(), NoticeBoardActivity::class.java))
            }
        }
    }
    override fun onLocationUpdate(latitude: Double, longitude: Double) {

    }
    fun hasAccessFineLocationPermissions(context: Context?): Boolean {
        return (ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }
    private fun buildAlertMessageNoGps(message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton(
                "Yes"
            ) { dialog, id ->
                val i = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts(
                        "package",
                        context!!.packageName, null
                    )
                )

                startActivityForResult(i, 1001)
            }
        val alert = builder.create()
        alert.getWindow()?.setBackgroundDrawable(resources?.let {
            ColorDrawable(
                it.getColor(
                    R.color.white
                )
            )
        })
        alert.show()
    }
    private fun alertMessageNoGps() {
        val dialog = this.let { Dialog(requireContext()) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.alert_message_no_gps_popup)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val tvYes = dialog.findViewById<TextView>(R.id.tvYes)
        tvYes.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))

        }


        dialog.show()


    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RESULT_PERMISSION_LOCATION) {
            Log.w("TAG", "onRequestPermissionsResult: ")
            if (hasAccessFineLocationPermissions(requireContext())) {
                if (CommonUtil.checkGPS(requireContext())) {
                    GPSService(requireContext(), this)
                }
            } else {
                buildAlertMessageNoGps(getString(R.string.location))

            }
        }
    }

    private fun checkLocationPermissions() {
        if (!hasAccessFineLocationPermissions(requireContext())) {
            buildAlertMessageNoGps(getString(R.string.location))
        } else if (!CommonUtil.checkGPS(requireContext())) {
            alertMessageNoGps()
        }
    }

    override fun onResume() {
        super.onResume()
        /*if (hasAccessFineLocationPermissions(requireContext())) {
            if (CommonUtil.checkGPS(requireContext())) {
                GPSService(requireContext(), this)
                getManagerDetails()
            }
        }
        checkLocationPermissions()*/
        getManagerDetails()
    }

    override fun onClickNotify(position: Int, billingId: String?) {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        side_viewModel.managerNotifyUser(token, billingId?:"")
    }

    override fun onMaskClick(position: Int, id: String?) {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        side_viewModel.markAsPaidManager(token, id?:"")
    }

    override fun onViewReceipt(refno: String, uploadDocument: String) {
        startActivity(
            Intent(requireContext(), ViewReceiptManagerActivity::class.java).putExtra(
                "img",
                uploadDocument
            ).putExtra("ref", refno)
        )
        //viewReceiptPop(refno, uploadDocument)
    }

    override fun onReject(billID: String) {
        rejectPopup(billID)
    }

    private fun rejectPopup(billID: String) {
        val dialog = this.let { Dialog(requireContext()) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.reject_manager_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        val tvCancel = dialog.findViewById<TextView>(R.id.tvcancelReject)
        val tvReject = dialog.findViewById<TextView>(R.id.tvReject)
        val tvEdit = dialog.findViewById<EditText>(R.id.edtReject)
        tvCancel.setOnClickListener {
            dialog.dismiss()
        }
        tvReject.setOnClickListener {
            dialog.dismiss()
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            side_viewModel.rejectBillManager(token, billID, tvEdit.text.toString())
        }


        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    private fun viewReceiptPop(refno: String, uploadDocument: String) {
        val dialog = this.let { Dialog(requireContext()) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.view_receipt_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        val tvDone = dialog.findViewById<TextView>(R.id.tvRefferral)
        val img = dialog.findViewById<ImageView>(R.id.imageView20)
        tvDone.text = refno
        if (!uploadDocument.isNullOrEmpty()) {
            img.visibility = View.VISIBLE
            img.loadImagesWithGlideExt(uploadDocument)

        } else {
            img.visibility = View.GONE
        }


        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }
    override fun onCLick(position: Int) {
        startActivity(
            Intent(
                requireContext(),
                RegisterComplainsOwnerTenantDetailsActivity::class.java
            ).putExtra("pendingList", pendingComplainListList[position]).putExtra("from", "pending")
        )
    }
}