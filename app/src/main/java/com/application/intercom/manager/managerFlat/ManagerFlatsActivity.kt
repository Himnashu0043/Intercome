package com.application.intercom.manager.managerFlat

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerHomeViewModel.ManagerHomeViewModel
import com.application.intercom.data.model.factory.managerFactory.managerHome.ManagerHomeFactory
import com.application.intercom.data.model.local.IdModel
import com.application.intercom.data.model.local.NameIdModel
import com.application.intercom.data.model.remote.manager.managerProperty.ManagerPropertyListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerHomeRepo.ManagerHomeRepo
import com.application.intercom.databinding.ActivityManagerFlatsBinding
import com.application.intercom.manager.service_charge.ServiceChargeActivity
import com.application.intercom.utils.*
import com.google.gson.Gson

class ManagerFlatsActivity : AppCompatActivity(), MangerFlatsAdapter.CLickOn {
    lateinit var binding: ActivityManagerFlatsBinding
    private var flat_adptr: MangerFlatsAdapter? = null
    private lateinit var viewModel: ManagerHomeViewModel
    private var flatList = ArrayList<ManagerPropertyListRes.Data>()
    private var id_list = ArrayList<IdModel>()
    private var check_list = ArrayList<IdModel>()
    private var serviceCharge: String = ""
    private var selectDate: String = ""
    private var nameId_list = ArrayList<NameIdModel>()
    private var from: String = ""
    private var editMonths: String = ""
    private var editYears: String = ""
    private var selectDateNew: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerFlatsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonUtil.themeSet(this, window)
        from = intent.getStringExtra("from").toString()
        if (from.equals("back_service")) {
            nameId_list = intent.getSerializableExtra("nameIdList") as ArrayList<NameIdModel>
            check_list = intent.getSerializableExtra("checkId") as ArrayList<IdModel>
            id_list = intent.getSerializableExtra("idLIst") as ArrayList<IdModel>
            println("----nameId_list$nameId_list")
            println("----check_list$check_list")
            println("----idloist$id_list")
        } else {
            id_list = intent.getSerializableExtra("idLIst") as ArrayList<IdModel>
            serviceCharge = intent.getStringExtra("billing_Type").toString()
            selectDate = intent.getStringExtra("date").toString()
            selectDateNew = intent.getStringExtra("selectDateNew").toString()
            editMonths = intent.getStringExtra("setMonths").toString()
            editYears = intent.getStringExtra("setYear").toString()
            println("----idloistmanna$id_list")
            println("----selectDateNew$selectDateNew")
            println("----selectDate$selectDate")
            println("----serviceCharge$serviceCharge")
        }

        binding.rcyFlatsManager.layoutManager = GridLayoutManager(this, 2)

        binding.checkBox2.setOnClickListener {
            nameId_list.clear()
           if (binding.checkBox2.isChecked) {
               for (idnn in check_list) {
                   for (get in flatList) {
                       if (idnn._id.equals(get._id)) {
                           get.isSelected = true
                       }

                   }
               }
               for (get in flatList) {
                   for (check in check_list) {
                       if (!check._id.equals(get._id)) {
                           nameId_list.add(NameIdModel(get._id, get.name, true))
                           get.isSelected1 = true
                       }
                   }
                   println("---test$nameId_list")
               }

               flat_adptr = MangerFlatsAdapter(this, flatList, check_list, this)

           } else {
               for (idnn in id_list) {
                   for (get in flatList) {
                       if (idnn._id.equals(get._id)) {
                           get.isSelected = true
                       }
                   }
               }
               for (get in flatList) {
                   get.isSelected1 = false
               }
               flat_adptr = MangerFlatsAdapter(this, flatList, check_list, this)

           }
            binding.rcyFlatsManager.adapter = flat_adptr
            flat_adptr!!.notifyDataSetChanged()
        }
        initView()
        lstnr()
    }

    private fun initView() {
        binding.btnLogin.tv.text = getString(R.string.select)
        binding.btnLogin.tv.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
        binding.btnLogin.tv.setTextColor(ContextCompat.getColor(this, R.color.orange))
        initialize()
        observer()


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
                            check_list.clear()
                            for (idnn in id_list) {
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
                                MangerFlatsAdapter(this, flatList, check_list, this)
                            binding.rcyFlatsManager.adapter = flat_adptr
                            flat_adptr!!.notifyDataSetChanged()

                            binding.checkBox2.isChecked =
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

    private fun lstnr() {
        binding.imageView4.setOnClickListener {
            finish()
        }
        binding.constraintLayout7.setOnClickListener {
            /*nameId_list.clear()
            for (id in flatList) {
                if (id.isSelected1)
                    nameId_list.add(NameIdModel(id._id, id.name, true))
            }*/
            if (nameId_list.isNotEmpty()) {
                startActivity(
                    Intent(this, ServiceChargeActivity::class.java)
                        .putExtra("from", "back_flat_screen")
                        .putExtra("nameIdList", nameId_list)
                        .putExtra("checkId", check_list)
                        .putExtra("idLIst", id_list)
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


    override fun onResume() {
        super.onResume()
        getFlatList()
    }

    override fun onCLick(isClick: Boolean, obj: NameIdModel) {
        if (isClick)
            nameId_list.add(obj)
        else nameId_list.remove(obj)

        binding.checkBox2.isChecked = (nameId_list.size + check_list.size) == flatList.size

        Log.d("TAG", "---laxman${Gson().toJson(nameId_list)}")

    }
}