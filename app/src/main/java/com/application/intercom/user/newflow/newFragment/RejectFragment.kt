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
import com.application.intercom.data.model.remote.newUser.MyList.ActiveNewPhaseList
import com.application.intercom.data.model.remote.newUser.MyList.PendingNewPhaseList
import com.application.intercom.data.model.remote.newUser.MyList.RejectNewPhaseList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.databinding.FragmentRejectBinding
import com.application.intercom.user.home.UserHomeFactory
import com.application.intercom.user.home.UserHomeViewModel
import com.application.intercom.user.newflow.adapter.ActiveAdapter
import com.application.intercom.user.newflow.adapter.PendingAdapter
import com.application.intercom.user.newflow.adapter.RejectAdapter
import com.application.intercom.utils.*


class RejectFragment : Fragment() {
    lateinit var binding: FragmentRejectBinding
    private var adptr: RejectAdapter? = null
    private lateinit var viewModel: UserHomeViewModel
    private var rejectList = ArrayList<ActiveNewPhaseList.Data>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRejectBinding.inflate(layoutInflater)
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()
    }

    private fun initialize() {
        val repo = UserHomeRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, UserHomeFactory(repo))[UserHomeViewModel::class.java]


    }

    private fun rejectList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.rejectList(token, "Reject")

    }
    private fun observer() {
        viewModel.rejectListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            rejectList.clear()
                            rejectList.addAll(it.data)


                            binding.rcyReject.visibility = View.VISIBLE
                            binding.rcyReject.layoutManager = LinearLayoutManager(requireContext())
                            adptr = RejectAdapter(requireContext(), rejectList)
                            binding.rcyReject.adapter = adptr
                            adptr!!.notifyDataSetChanged()
                            if (rejectList.isEmpty()) {
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                binding.lottieEmpty.visibility = View.INVISIBLE
                            }
                        } else if (it.status == AppConstants.STATUS_401) {
                            binding.rcyReject.visibility = View.INVISIBLE
                           // requireActivity().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            binding.rcyReject.visibility = View.INVISIBLE
                           // requireActivity().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_500) {
                            binding.rcyReject.visibility = View.INVISIBLE
                            //requireActivity().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
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

    }
    private fun lstnr() {

    }

    override fun onResume() {
        super.onResume()
        rejectList()
    }


}