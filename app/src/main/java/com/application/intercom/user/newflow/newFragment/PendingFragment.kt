package com.application.intercom.user.newflow.newFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.remote.newUser.MyList.ActiveNewPhaseList
import com.application.intercom.data.model.remote.newUser.MyList.PendingNewPhaseList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.databinding.FragmentPending2Binding
import com.application.intercom.databinding.FragmentPendingBinding
import com.application.intercom.user.home.UserHomeFactory
import com.application.intercom.user.home.UserHomeViewModel
import com.application.intercom.user.newflow.adapter.ActiveAdapter
import com.application.intercom.user.newflow.adapter.FeatureAdapter
import com.application.intercom.user.newflow.adapter.PendingAdapter
import com.application.intercom.utils.*


class PendingFragment : Fragment() {
    lateinit var binding: FragmentPending2Binding
    private var adptr: PendingAdapter? = null
    private lateinit var viewModel: UserHomeViewModel
    private var pendingList = ArrayList<ActiveNewPhaseList.Data>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPending2Binding.inflate(layoutInflater)
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

    private fun pendingList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.pendingList(token, "Pending")

    }

    private fun observer() {
        viewModel.pendingListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            binding.lottieEmpty.visibility = View.INVISIBLE
                            binding.rcyPending.visibility = View.VISIBLE
                            pendingList.clear()
                            pendingList.addAll(it.data)
                            binding.rcyPending.layoutManager = LinearLayoutManager(requireContext())
                            adptr = PendingAdapter(requireContext(), pendingList)
                            binding.rcyPending.adapter = adptr
                            adptr!!.notifyDataSetChanged()
                            println("-----list${pendingList}")
                            if (pendingList.isEmpty()) {
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                binding.lottieEmpty.visibility = View.INVISIBLE
                            }
                        } else if (it.status == AppConstants.STATUS_401) {
                            binding.rcyPending.visibility = View.INVISIBLE
                            //requireActivity().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            binding.rcyPending.visibility = View.INVISIBLE
                           // requireActivity().longToast(it.message)
                            binding.lottieEmpty.visibility = View.VISIBLE
                        } else if (it.status == AppConstants.STATUS_500) {
                            binding.rcyPending.visibility = View.INVISIBLE
                           // requireActivity().longToast(it.message)
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
        pendingList()
    }

}