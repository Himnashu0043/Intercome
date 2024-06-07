package com.application.intercom.manager.rent

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerHomeViewModel.ManagerHomeViewModel
import com.application.intercom.data.model.factory.managerFactory.managerHome.ManagerHomeFactory
import com.application.intercom.data.model.local.IdModel
import com.application.intercom.data.model.local.NameIdModel
import com.application.intercom.data.model.remote.manager.managerProperty.ManagerPropertyListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerHomeRepo.ManagerHomeRepo
import com.application.intercom.databinding.ActivityRentFlatManagerBinding
import com.application.intercom.utils.*
import com.google.gson.Gson

class RentFlatManagerActivity : BaseActivity<ActivityRentFlatManagerBinding>(),
    RentFlatManagerAdapter.RentFlat {
    private lateinit var viewModel: ManagerHomeViewModel
    private var flatList = ArrayList<ManagerPropertyListRes.Data>()
    private var flat_adptr: RentFlatManagerAdapter? = null
    private var check_list = ArrayList<IdModel>()
    private var nameId_list = ArrayList<NameIdModel>()
    private var idlist = ArrayList<IdModel>()
    private var from: String = ""
    private var editMonths: String = ""
    private var editYears: String = ""
    private var selectDateNew: String = ""
    private var serviceCharge: String = ""
    private var selectDate: String = ""
    override fun getLayout(): ActivityRentFlatManagerBinding {
        return ActivityRentFlatManagerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRentFlatManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonUtil.themeSet(this, window)
        from = intent.getStringExtra("from").toString()
        if (from.equals("back_service")) {
            nameId_list = intent.getSerializableExtra("nameIdList") as ArrayList<NameIdModel>
            check_list = intent.getSerializableExtra("checkId") as ArrayList<IdModel>
            idlist = intent.getSerializableExtra("idLIst") as ArrayList<IdModel>
            println("----nameId_list$nameId_list")
            println("----check_list$check_list")
            println("----idloist$idlist")
        } else {
            idlist = intent.getSerializableExtra("idLIst") as ArrayList<IdModel>
            serviceCharge = intent.getStringExtra("billing_Type").toString()
            selectDate = intent.getStringExtra("date").toString()
            selectDateNew = intent.getStringExtra("selectDateNew").toString()
            editMonths = intent.getStringExtra("setMonths").toString()
            editYears = intent.getStringExtra("setYear").toString()
            println("----idloistmanna$idlist")
            println("----selectDateNew$selectDateNew")
            println("----selectDate$selectDate")
            println("----serviceCharge$serviceCharge")
        }
        binding.rcyRentFlatsManager.layoutManager = GridLayoutManager(this, 2)
        initView()
        listener()

    }

    private fun initialize() {
        val repo = ManagerHomeRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(
            this, ManagerHomeFactory(repo)
        )[ManagerHomeViewModel::class.java]


    }

    private fun getFlatList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        viewModel.managerFlatList(token)

    }

    private fun observer() {
        viewModel.managerFlatListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            flatList.clear()
                             for (idnn in idlist) {
                                 for (get in it.data) {
                                     if (idnn._id.equals(get._id)) {
                                         get.isSelected = true
                                         check_list.add(IdModel(get._id, "",""))
                                     }

                                 }
                             }
                             if (from.equals("back_service")) {
                                 for (tt in nameId_list) {
                                     for (get in it.data) {
                                         if (tt._id.equals(get._id)) {
                                             get.isSelected1 = true
                                         }

                                     }
                                 }

                             }
                            flatList.addAll(it.data)
                            flat_adptr =
                                RentFlatManagerAdapter(this, flatList, this, check_list)
                            binding.rcyRentFlatsManager.adapter = flat_adptr
                            flat_adptr!!.notifyDataSetChanged()

                            binding.checkBox.isChecked =
                                (nameId_list.size + check_list.size) == flatList.size


                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
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

    private fun listener() {
        binding.imageView4.setOnClickListener {
            finish()
        }
        /*binding.checkBox.setOnClickListener {
            nameId_list.clear()
             check_list.clear()
             if (idlist.isEmpty()) {
                 for (id in flatList) {
                     nameId_list.add(NameIdModel(id._id, id.name, true))
                     id.isSelected1 = binding.checkBox.isChecked
                 }
                 flat_adptr = RentFlatManagerAdapter(this, flatList,  this,check_list)
             }
            binding.rcyRentFlatsManager.adapter = flat_adptr
            flat_adptr!!.notifyDataSetChanged()
        }*/
        binding.checkBox.setOnClickListener {
            nameId_list.clear()
            if (binding.checkBox.isChecked) {
                for (idnn in check_list) {
                    for (get in flatList) {
                        if (idnn._id.equals(get._id)) {
                            get.isSelected = true
                        }

                    }
                }
                for (get in flatList) {
                    for (check in check_list) {
                        if (!get.tenantInfo.isNullOrEmpty()) {
                            if (!check._id.equals(get._id)) {
                                nameId_list.add(NameIdModel(get._id, get.name, true))
                                get.isSelected1 = true
                            }
                        }

                    }
                    println("---test$nameId_list")
                }
                for (get1 in flatList) {
                    if (!get1.tenantInfo.isNullOrEmpty()) {
                        nameId_list.add(NameIdModel(get1._id, get1.name, true))
                        get1.isSelected1 = true
                        println("=========ttttttt")
                    }
                }
                flat_adptr = RentFlatManagerAdapter(this, flatList, this, check_list)

            } else {
            for (idnn in idlist) {
                for (get in flatList) {
                    if (idnn._id.equals(get._id)) {
                        get.isSelected = true
                    }
                }
            }
            for (get in flatList) {
                get.isSelected1 = false
            }
            flat_adptr = RentFlatManagerAdapter(this, flatList, this, check_list)

        }
            binding.rcyRentFlatsManager.adapter = flat_adptr
            flat_adptr!!.notifyDataSetChanged()
        }
        binding.constraintLayout7.setOnClickListener {
           /* nameId_list.clear()
            for (id in flatList) {
                if (id.isSelected1)
                    nameId_list.add(NameIdModel(id._id, id.name, true))
            }*/
            if (nameId_list.isNotEmpty()) {
                startActivity(
                    Intent(this, AddRentManagerActivity::class.java)
                        .putExtra("from", "back_flat_screen")
                        .putExtra("nameIdList", nameId_list)
                        .putExtra("checkId", check_list)
                        .putExtra("idLIst", idlist)
                        .putExtra("billing_Type", serviceCharge)
                        .putExtra("date", selectDate)
                        .putExtra("setMonths", editMonths)
                        .putExtra("setYear", editYears)
                        .putExtra("selectDateNew", selectDateNew)
                    /*.putExtra("amount", amount)*/
                )
                finish()
            } else {
                Toast.makeText(this, "Please Select!!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun initView() {
        initialize()
        observer()
        binding.btnLogin.tv.text = getString(R.string.select)
        binding.btnLogin.tv.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        binding.btnLogin.tv.setTextColor(ContextCompat.getColor(this, R.color.orange))
    }

    override fun onCLick(isClick: Boolean, obj: NameIdModel) {
        if (isClick)
            nameId_list.add(obj)
        else nameId_list.remove(obj)

        binding.checkBox.isChecked = (nameId_list.size + check_list.size) == flatList.size

        Log.d("TAG", "---laxman${Gson().toJson(nameId_list)}")
    }

    override fun onResume() {
        super.onResume()
        getFlatList()
    }
}