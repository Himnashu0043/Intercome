package com.application.intercom.user.service

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.local.PropertyListPostModel.PropertyListUserPostModel
import com.application.intercom.data.model.remote.UserServiceProviderResponse
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ServiceRepository
import com.application.intercom.databinding.ActivityUserListOfServicesBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.user.property.PropertySortByBottomSheet
import com.application.intercom.utils.*
import java.util.*
import kotlin.collections.ArrayList

class UserListOfServicesActivity : AppCompatActivity(), UserListOfServiceAdapter.CallService,
    PropertySortByBottomSheet.Click {
    lateinit var binding: ActivityUserListOfServicesBinding
    private var listOf_Adapter: UserListOfServiceAdapter? = null
    private var from: String = ""
    private var totalServiceProviderCount: String = ""
    private var categoryId: String = ""
    private var category_name: String = ""
    private var serviceProviderList: ArrayList<UserServiceProviderResponse.Data.Result> =
        ArrayList()
    private var searchList: ArrayList<UserServiceProviderResponse.Data.Result> = ArrayList()

    private lateinit var viewModel: ServiceViewModel
    private var isSearchHide: Boolean = false
    private var sortValue: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserListOfServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        from = intent.getStringExtra("from").toString()
        categoryId = intent.getStringExtra(AppConstants.CATEGORY_ID).toString()
        category_name = intent.getStringExtra("category_name").toString()
        initView()
        lstnr()
        init()
        observer()
        getServiceProviderList()
    }

    private fun initView() {
        if (from.equals("user")) {
            binding.listOfToolbar.tvTittle.text = category_name
            binding.listOfToolbar.tvCountText.text = "($totalServiceProviderCount)"
        } else if (from.equals("tenant_service")) {
            binding.listOfToolbar.tvTittle.text = category_name
            binding.listOfToolbar.tvCountText.text = "($totalServiceProviderCount)"
        } else {
            binding.listOfToolbar.tvTittle.text = category_name
            binding.listOfToolbar.tvCountText.text = "($totalServiceProviderCount)"
        }

        binding.listOfToolbar.tvCountText.visibility = View.VISIBLE
        binding.rcyListOdService.layoutManager = LinearLayoutManager(this)
        listOf_Adapter = UserListOfServiceAdapter(this, ArrayList(), false, this)
        binding.rcyListOdService.adapter = listOf_Adapter
        listOf_Adapter!!.notifyDataSetChanged()

    }

    private fun lstnr() {
        binding.textView15.setOnClickListener {
            PropertySortByBottomSheet(this).apply {
                show(supportFragmentManager, PropertySortByBottomSheet.TAG)
            }
        }
        binding.ivCross.setOnClickListener {
            binding.listOfToolbar.ivSearch.visibility = View.VISIBLE
            binding.edtSearch.visibility = View.GONE
            binding.ivCross.visibility = View.GONE
            binding.edtSearch.text.clear()
            listOf_Adapter?.notifiyData(serviceProviderList, false)

        }
        binding.listOfToolbar.ivSearch.setOnClickListener {
            binding.listOfToolbar.ivSearch.visibility = View.GONE
            binding.edtSearch.visibility = View.VISIBLE
            binding.ivCross.visibility = View.VISIBLE

        }
//        binding.edtSearch.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                searchList.clear()
//                searchList = serviceProviderList.filter { s ->
//                    s.serviceProviderName.contains(binding.edtSearch.text.toString(), true)
//
//                } as ArrayList<UserServiceProviderResponse.Data.Result>
//                if (searchList.isNotEmpty()) {
//                    listOf_Adapter?.notifiyData(searchList, true)
//
////                   shortToast(searchList[0]._id)
//                } else {
//                    shortToast("data not found")
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
                    if (text.length > 0) {
                        var tempFilterList = serviceProviderList.filter {

                            it.serviceProviderName.lowercase(Locale.ROOT)
                                .contains(text.lowercase(Locale.ROOT))

                        }
                        listOf_Adapter?.notifiyData(
                            tempFilterList as ArrayList<UserServiceProviderResponse.Data.Result>,
                            false
                        )
                        if (tempFilterList.isEmpty()) {
                            Toast.makeText(
                                this@UserListOfServicesActivity,
                                "Data Not Found",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    listOf_Adapter?.notifiyData(serviceProviderList, false)

                }
            }
        })
    }


    private fun init() {
        val repo = ServiceRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, ServiceFactory(repo))[ServiceViewModel::class.java]
    }

    private fun getServiceProviderList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, /*GPSService.mLastLocation!!.latitude.toString()*/""
        )
        viewModel.getServiceProviderList(token, categoryId)
    }

    private fun observer() {
        viewModel.serviceProviderListLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            totalServiceProviderCount = it.data.total.toString()
                            binding.listOfToolbar.tvCountText.text = "($totalServiceProviderCount)"
                            serviceProviderList.clear()
                            serviceProviderList.addAll(it.data.result)
                            listOf_Adapter?.notifiyData(serviceProviderList, false)
                        } else if (it.status == AppConstants.STATUS_404) {
                            longToast(it.message)
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

    override fun onClickCall(number: String) {
        callServicePopup(number)
    }

    private fun callServicePopup(number: String) {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.call_service_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        val tvYes = dialog.findViewById<TextView>(R.id.tvYesService)
        val tvNo = dialog.findViewById<TextView>(R.id.tvNo)
        tvYes.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${number}")
            startActivity(intent)
        }
        tvNo.setOnClickListener {
            dialog.dismiss()
        }


        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    override fun onCLickSortProperty(name: String) {
        sortValue = name
        binding.textView15.background =
            ContextCompat.getDrawable(this, R.drawable.oragne_strock_with_white_bg)
        binding.textView15.setTextColor(ContextCompat.getColor(this, R.color.black))
        if (sortValue == "lowToHigh") {
            binding.textView15.text = "Sort by Price: Low to High"
            listOf_Adapter?.notifiyData(serviceProviderList.sortedBy { it.charges }, false)
        } else {
            binding.textView15.text = "Sort by Price: High to Low"
            listOf_Adapter?.notifiyData(
                serviceProviderList.sortedBy { it.charges }.reversed(),
                false
            )
        }
    }
}