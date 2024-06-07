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
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.databinding.FragmentInActiveBinding
import com.application.intercom.user.home.UserHomeFactory
import com.application.intercom.user.home.UserHomeViewModel
import com.application.intercom.user.newflow.adapter.ActiveAdapter
import com.application.intercom.user.newflow.adapter.InActiveAdapter
import com.application.intercom.utils.*

class InActiveFragment : Fragment() {
    lateinit var binding: FragmentInActiveBinding
    private lateinit var viewModel: UserHomeViewModel
    private var adptr: InActiveAdapter? = null
    private var inactiveList = ArrayList<ActiveNewPhaseList.Data>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInActiveBinding.inflate(layoutInflater)
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

    private fun activeList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.activeList(token, "Inactive")

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
                            binding.rcyInActive.visibility = View.VISIBLE
                            inactiveList.clear()
                            inactiveList.addAll(it.data)
                            binding.rcyInActive.layoutManager =
                                LinearLayoutManager(requireContext())
                            adptr = InActiveAdapter(requireContext(), inactiveList)
                            binding.rcyInActive.adapter = adptr
                            adptr!!.notifyDataSetChanged()
                            if (inactiveList.isEmpty()) {
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                binding.lottieEmpty.visibility = View.INVISIBLE
                            }
                        } else if (it.status == AppConstants.STATUS_401) {
                            binding.rcyInActive.visibility = View.INVISIBLE
                            //requireActivity().longToast(it.message)
                            binding.lottieEmpty.visibility = View.INVISIBLE
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            binding.rcyInActive.visibility = View.INVISIBLE
                           // requireActivity().longToast(it.message)
                            binding.lottieEmpty.visibility = View.INVISIBLE
                        } else if (it.status == AppConstants.STATUS_500) {
                            binding.rcyInActive.visibility = View.INVISIBLE
                            //requireActivity().longToast(it.message)
                            binding.lottieEmpty.visibility = View.INVISIBLE
                        }

                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    binding.lottieEmpty.visibility = View.INVISIBLE
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
        activeList()
    }

}