package com.application.intercom.manager.service_charge

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.local.IdModel
import com.application.intercom.data.model.remote.manager.managerSide.serviceCharege.ManagerServiceChargeList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityServiceChargeListBinding
import com.application.intercom.utils.*

class ServiceChargeListActivity : AppCompatActivity(), ServiceChargeListAdapter.Delete {
    lateinit var binding: ActivityServiceChargeListBinding
    private var adptr: ServiceChargeListAdapter? = null
    private lateinit var viewModel: ManagerSideViewModel
    private var list = ArrayList<ManagerServiceChargeList.Data>()
    private var id_list = ArrayList<IdModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServiceChargeListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CommonUtil.themeSet(this, window)
        initView()
        lstnr()
    }

    private fun initView() {
        initialize()
        observer()


    }

    private fun initialize() {
        val siderepo = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(siderepo))[ManagerSideViewModel::class.java]

    }

    private fun getServiceChargeList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        viewModel.viewServiceChargeManager(token)

    }

    private fun observer() {
        viewModel.viewServiceChargeManagerLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            list.clear()
                            list.addAll(it.data)
                            binding.rcyServiceList.layoutManager = LinearLayoutManager(this)
                            adptr = ServiceChargeListAdapter(this, list, this)
                            binding.rcyServiceList.adapter = adptr
                            adptr!!.notifyDataSetChanged()
                            id_list.clear()
                            for (id in it.data) {
                                if (id.flatId != null) {
                                    id_list.add(
                                        IdModel(
                                            id.flatId!!._id ?: "",
                                            id.billMonth ?: "",
                                            id.billYear ?: ""
                                        )
                                    )
                                }

                            }
                            if (list.isEmpty()) {
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                binding.lottieEmpty.visibility = View.INVISIBLE
                            }
                            println("---id$id_list")
                            println("---list${it.data}")
                        } else if (it.status == AppConstants.STATUS_500) {
                            //this.longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_404) {
                            //this.longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else {
                            binding.lottieEmpty.visibility = View.VISIBLE
                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    binding.lottieEmpty.visibility = View.VISIBLE
                    ErrorUtil.handlerGeneralError(this, it.throwable!!)
                }
                else -> {}
            }
        })
        viewModel.deleteUnPaidBillManagerLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            this.longToast(it.message)
                            getServiceChargeList()
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
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
        binding.tvAddServiceCharge.setOnClickListener {
            startActivity(
                Intent(this, ServiceChargeActivity::class.java).putExtra(
                    "idLIst",
                    id_list
                )
            )
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        getServiceChargeList()
    }

    override fun onClick(_id: String) {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        //viewModel.deleteServiceChargeListManager(token, _id)
        viewModel.deleteUnPaidBillManager(token, _id?:"")
    }

    override fun onEdit(position: Int) {
        startActivity(
            Intent(this, ServiceChargeActivity::class.java).putExtra("from", "edit_from")
                .putExtra("editData", list[position])
        )
        finish()
    }
}