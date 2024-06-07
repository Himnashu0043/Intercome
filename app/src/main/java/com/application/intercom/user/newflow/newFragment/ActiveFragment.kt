package com.application.intercom.user.newflow.newFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.local.newFlow.StatusUpdatePostModel
import com.application.intercom.data.model.remote.newUser.MyList.ActiveNewPhaseList
import com.application.intercom.data.model.remote.newUser.MyList.PendingNewPhaseList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.databinding.FragmentActiveBinding
import com.application.intercom.user.home.UserHomeFactory
import com.application.intercom.user.home.UserHomeViewModel
import com.application.intercom.user.newflow.adapter.ActiveAdapter
import com.application.intercom.user.newflow.adapter.RejectAdapter
import com.application.intercom.utils.*

class ActiveFragment : Fragment(), ActiveAdapter.InActiveClick {
    lateinit var binding: FragmentActiveBinding
    private var adptr: ActiveAdapter? = null
    private lateinit var viewModel: UserHomeViewModel
    private var activeList = ArrayList<ActiveNewPhaseList.Data>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActiveBinding.inflate(layoutInflater)

        initView()
        lstnr()
        return binding.root
    }

    private fun lstnr() {

    }

    private fun initView() {
        initialize()
        observer()


    }

    private fun initialize() {
        val repo = UserHomeRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, UserHomeFactory(repo))[UserHomeViewModel::class.java]


    }

    private fun activeList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.activeList(token, "Approve")

    }

    private fun observer() {
        viewModel.activeListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            activeList.clear()
                            activeList.addAll(it.data)
                            binding.rcyActive.layoutManager = LinearLayoutManager(requireContext())
                            adptr = ActiveAdapter(requireContext(), activeList, this)
                            binding.rcyActive.adapter = adptr
                            adptr!!.notifyDataSetChanged()
                            if (activeList.isEmpty()) {
                                binding.rcyActive.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                binding.rcyActive.visibility = View.VISIBLE
                                binding.lottieEmpty.visibility = View.INVISIBLE
                            }
                        } else if (it.status == AppConstants.STATUS_401) {
                            binding.rcyActive.visibility = View.INVISIBLE
                           // requireActivity().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            binding.rcyActive.visibility = View.INVISIBLE
                          //  requireActivity().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_500) {
                            binding.rcyActive.visibility = View.INVISIBLE
                            //requireActivity().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
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
        viewModel.statusUpdateLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            activeList()
                        } else if (it.status == AppConstants.STATUS_401) {
                            requireActivity().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            requireActivity().longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_500) {
                            requireActivity().longToast(it.message)
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

    override fun onResume() {
        super.onResume()
        activeList()
    }

    override fun onClick(id: String) {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        val model = StatusUpdatePostModel(id, "Inactive")
        viewModel.statusUpdate(token, model)
    }


}