package com.application.intercom.owner.activity.rent

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome.OwnerHomeViewModel
import com.application.intercom.data.model.factory.ownerfactory.ownerHome.OwnerHomeFactory
import com.application.intercom.data.model.local.IdModel
import com.application.intercom.data.model.remote.manager.managerSide.rent.RentManagerListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.databinding.ActivityOwnerRentListBinding
import com.application.intercom.utils.*

class OwnerRentListActivity : BaseActivity<ActivityOwnerRentListBinding>(),OwnerRentListAdapter.OwnerRent {
    private var idlist = ArrayList<IdModel>()
    private lateinit var viewModel: OwnerHomeViewModel
    private var adptr: OwnerRentListAdapter? = null
    private var list = ArrayList<RentManagerListRes.Data.Result>()
    override fun getLayout(): ActivityOwnerRentListBinding {
        return ActivityOwnerRentListBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CommonUtil.themeSet(this, window)
        initView()
        listener()
    }

    private fun initView() {
        initialize()
        observer()
    }
    private fun initialize() {
        val ownerModel = OwnerHomeRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(
            this, OwnerHomeFactory(ownerModel)
        )[OwnerHomeViewModel::class.java]
    }
    private fun getRentList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        viewModel.getOwnerRent(token)

    }

    private fun observer() {
        viewModel.getOwnerRentLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            list.clear()
                            list.addAll(it.data?.result!!)
                            binding.rcyRentList.layoutManager = LinearLayoutManager(this)
                            adptr = OwnerRentListAdapter(this, list, this)
                            binding.rcyRentList.adapter = adptr
                            adptr!!.notifyDataSetChanged()
                            idlist.clear()
                            for (id in it.data?.result!!) {
                                if (id.flatInfo != null) {
                                    idlist.add(
                                        IdModel(
                                            id.flatInfo?.first()?._id ?: "",
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
                            println("---id$idlist")
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
        viewModel.deleteOwnerRentLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            /*this.longToast(it.message)*/
                            getRentList()
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
    private fun listener() {
        binding.imageView4.setOnClickListener {
            finish()
        }
        binding.tvAddRent.setOnClickListener {
            startActivity(
                Intent(this, AddOwnerRentActivity::class.java).putExtra(
                    "idLIst",
                    idlist
                )
            )
        }
    }

    override fun onDelete(_id: String) {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.deleteOwnerRent(token, _id)
    }

    override fun onResume() {
        super.onResume()
        getRentList()
    }
}