package com.application.intercom.user.service

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.MainActivity
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.remote.UserServiceProviderResponse
import com.application.intercom.data.model.remote.gateKeeper.gateKeeperHome.GateKeeperListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ServiceRepository
import com.application.intercom.databinding.FragmentUserServiceBinding
import com.application.intercom.db.dao.ServicesCategoryDao
import com.application.intercom.db.entity.ServicesCategoryTable
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.getCurrentDate
import com.application.intercom.manager.main.ManagerMainActivity
import com.application.intercom.manager.service.ManagerListOfServicesActivity
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.owner.adapter.ShowImgAdapter
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.tenant.activity.profile.ProfileActivity
import com.application.intercom.utils.*

import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class UserServiceFragment : Fragment(), ServicesCategoryListener {
    lateinit var binding: FragmentUserServiceBinding
    private var recently_adapter: UserServiceRecentlyAdapter? = null
    private var list_adapter: UserServiceListAdapter? = null
    private var serviceList: ArrayList<ServicesCategoryTable> = ArrayList()
    private var key: String = ""
    private var isSearchHide: Boolean = false

    private lateinit var viewModel: ServiceViewModel
    private var servicesCategoryDao: ServicesCategoryDao? = null
    var slist: ArrayList<ServicesCategoryTable> = ArrayList()
    var recentList: ArrayList<ServicesCategoryTable> = ArrayList()
    private lateinit var activity: MainActivity
    private lateinit var tenantactivity: TenantMainActivity
    private lateinit var ownertactivity: OwnerMainActivity
    private lateinit var manageractivity: ManagerMainActivity
    var drw: DrawerLayout? = null

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserServiceBinding.inflate(layoutInflater)
        key = arguments?.getString("key").toString()
        Log.d("Himanshu", "onCreateView: $key")
        if (key.equals("tenant_service")) {
            tenantactivity = getActivity() as TenantMainActivity
            drw = tenantactivity.requireViewById(R.id.tenantDrw)
        } else if (key.equals("home_service")) {
            activity = getActivity() as MainActivity
            drw = activity.requireViewById(R.id.content)
        } else if (key.equals("manager_service")) {
            manageractivity = getActivity() as ManagerMainActivity
            drw = manageractivity.requireViewById(R.id.managerDrw)
        } else {
            ownertactivity = getActivity() as OwnerMainActivity
            drw = ownertactivity.requireViewById(R.id.ownerDrw)
        }
        CommonUtil.setLightStatusBar(requireActivity())
        initView()
        lstnr()
        servicesCategoryDao = BaseApplication.appDatabase.servicesDao()
        lifecycleScope.launch {
            recentList =
                servicesCategoryDao?.servicesCategoryList(true) as ArrayList<ServicesCategoryTable>
            recently_adapter?.notifiyData(recentList)
            if (recentList.isNotEmpty()) {
                binding.rcyRecently.visibility = View.VISIBLE
                binding.textView12.visibility = View.VISIBLE
            }
        }
        Log.d("ffgfqfdsdscd", "onCreateView: $recentList")

        return (binding.root)
    }

    private fun initView() {
        binding.tvTittle.text = getString(R.string.services)
        binding.rcyRecently.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recently_adapter = UserServiceRecentlyAdapter(requireContext(), key, ArrayList())
        binding.rcyRecently.adapter = recently_adapter
        recently_adapter!!.notifyDataSetChanged()

        binding.rcyList.layoutManager = GridLayoutManager(requireContext(), 4)
        list_adapter = UserServiceListAdapter(requireContext(), ArrayList(), this /*false*/)
        binding.rcyList.adapter = list_adapter
        list_adapter!!.notifyDataSetChanged()

        init()
        observer()
        getServicesList()
        binding.ivSearch.setOnClickListener {
            if (isSearchHide) {
                binding.ivSearch.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.search_bg_icon
                    )
                )
                isSearchHide = false
                binding.edtSearch.visibility = View.GONE
                binding.edtSearch.text.clear()
                binding.tvTittle.visibility = View.VISIBLE
                binding.imageView25.visibility = View.GONE
                binding.ivInfoIcon.visibility = View.VISIBLE
                list_adapter?.notifiyData(serviceList)
            } else {
                binding.ivSearch.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.cross_bg_icon
                    )
                )
                binding.edtSearch.visibility = View.VISIBLE
                binding.imageView25.visibility = View.GONE
                binding.tvTittle.visibility = View.GONE
                binding.ivInfoIcon.visibility = View.GONE

                isSearchHide = true
            }

        }
//        binding.edtSearch.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                slist.clear()
//                slist = serviceList.filter { s ->
//                    s.category_name!!.contains(binding.edtSearch.text.toString(), true)
//
//                } as ArrayList<ServicesCategoryTable>
//                if (slist.isNotEmpty()) {
//                    list_adapter?.notifiyData(slist /*true*/)
//                    // requireContext().shortToast(slist[0]._id)
//                } else {
//                    requireContext().shortToast("data not found")
//                }
//            }
//            true
//        }
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                var text = p0.toString().trim()
                if (text.isNotEmpty()) {
                    if (text.isNotEmpty()) {
                        var tempFilterList = serviceList.filter {

                            it.category_name!!.lowercase(Locale.ROOT)
                                .contains(text.lowercase(Locale.ROOT))

                        }
                        list_adapter?.notifiyData(
                            tempFilterList as ArrayList<ServicesCategoryTable>,

                            )
                        if (tempFilterList.isEmpty()) {
                            Toast.makeText(
                                context, getString(R.string.data_not_found), Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    list_adapter?.notifiyData(serviceList)

                }
            }
        })


    }

    private fun init() {
        val repo = ServiceRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, ServiceFactory(repo))[ServiceViewModel::class.java]
    }

    private fun getServicesList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            /* GPSService.mLastLocation!!.latitude.toString()*/""
        )
        viewModel.getServicesListAndSearch(token, "")
    }

    private fun observer() {
        viewModel.serviceLiveData.observe(requireActivity(), Observer {
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
                            list_adapter?.notifiyData(serviceList /*false*/)
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

    }

    private fun lstnr() {
        /* binding.imageView25.setOnClickListener {
             if (key.equals("tenant_service")) {
                 *//*startActivity(
                    Intent(requireContext(), ProfileActivity::class.java).putExtra(
                        "from",
                        "tenant"
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )*//*
                drw!!.openDrawer(GravityCompat.START)
            } else if (key.equals("home_service")) {
                *//*startActivity(
                    Intent(requireContext(), ManagerMainActivity::class.java).putExtra(
                        "from",
                        "from_side_home"
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )*//*
                drw!!.openDrawer(GravityCompat.START)
            } else if (key.equals("user")) {
                *//* startActivity(
                     Intent(requireContext(), ProfileActivity::class.java).putExtra(
                         "from",
                         "user"
                     ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                 )*//*
                drw!!.openDrawer(GravityCompat.START)
            } else if (key.equals("manager_service")) {
                *//* startActivity(
                     Intent(requireContext(), ProfileActivity::class.java).putExtra(
                         "from",
                         "manager"
                     ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                 )*//*
                drw!!.openDrawer(GravityCompat.START)
            }
        }*/

//        binding.tvViewAll.setOnClickListener {
//            startActivity(Intent(requireContext(), ListOfServicesActivity::class.java))
//        }
        binding.ivInfoIcon.setOnClickListener {
           infoServicePopup()
        }

    }

    override fun servicesCategoryClick(isRecentItem: ServicesCategoryTable, position: Int) {
        if (isSearchHide) {
            isSearchHide = false
            binding.rcyRecently.visibility = View.VISIBLE
            binding.tvViewAll.visibility = View.GONE
            binding.textView12.visibility = View.VISIBLE
            startActivity(
                Intent(
                    requireContext(), ManagerListOfServicesActivity::class.java
                ).putExtra("from", key).putExtra("serviceList", isRecentItem)
            )
            lifecycleScope.launch {
                Log.d("INSERT_DATA", " ${isRecentItem.isRecent}")
                servicesCategoryDao?.insertServiceCategory(isRecentItem)
                recentList.clear()
                recentList =
                    servicesCategoryDao?.servicesCategoryList(true) as ArrayList<ServicesCategoryTable>
                recently_adapter?.notifiyData(recentList)
            }
        } else {
            isSearchHide = true
            /*binding.rcyRecently.visibility = View.VISIBLE
            binding.tvViewAll.visibility = View.VISIBLE
            binding.textView12.visibility = View.VISIBLE*/
            /* lifecycleScope.launch {
                 servicesCategoryDao?.insertServiceCategory(isRecentItem)
                 recentList.clear()
                 recentList =
                     servicesCategoryDao?.servicesCategoryList(true) as ArrayList<ServicesCategoryTable>
                 recently_adapter?.notifiyData(recentList)
                 startActivity(
                     Intent(
                         requireContext(),
                         UserListOfServicesActivity::class.java
                     ).putExtra("from", key)
                         .putExtra(AppConstants.CATEGORY_ID, serviceList[0]._id)
                         .putExtra("category_name", serviceList[0].category_name)
                 )

             }*/
            startActivity(
                Intent(
                    requireContext(),
                    ManagerListOfServicesActivity::class.java
                ).putExtra("from", key)
                    .putExtra("serviceList", isRecentItem)
            )
        }

        Log.d("dfff", "servicesCategoryClick: ${recentList}")
    }

    /*override fun onMainListClick(category_name: String, id: String) {
        startActivity(
            Intent(
                requireContext(),
                UserListOfServicesActivity::class.java
            ).putExtra("from", key)
                .putExtra(AppConstants.CATEGORY_ID, id)
                .putExtra("category_name", category_name)
        )
    }*/
    private fun infoServicePopup() {
        val dialog = this.let { Dialog(requireContext()) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.info_service_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()


        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

}