package com.application.intercom.user.newflow.fav

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
import com.application.intercom.data.model.remote.newUser.favList.UserFavListRes
import com.application.intercom.data.model.remote.newUser.favList.UserFavPropertyListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.databinding.FragmentFavPropertyBinding
import com.application.intercom.user.home.UserHomeFactory
import com.application.intercom.user.home.UserHomeViewModel
import com.application.intercom.user.newflow.adapter.UserFavAdapter
import com.application.intercom.utils.*

class FavPropertyFragment : Fragment(),UserFavAdapter.FavCLick {
    lateinit var binding: FragmentFavPropertyBinding
    private var list = ArrayList<UserFavPropertyListRes.Data>()
    private lateinit var viewModel: UserHomeViewModel
    private var adpter: UserFavAdapter? = null
    private var flatId: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavPropertyBinding.inflate(layoutInflater)
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        initialize()
        observer()
    }

    private fun lstnr() {

    }

    private fun initialize() {
        val repo = UserHomeRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, UserHomeFactory(repo))[UserHomeViewModel::class.java]


    }


    private fun userFavList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.userfav(token, "Property")

    }

    private fun observer() {
        viewModel.userFavListLiveData.observe(requireActivity(), androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            list.clear()
                            list.addAll(it.data)
                            println("----listSIze${list.size}")
                            if (list.isEmpty()) {
                                binding.rcyRcy.visibility = View.INVISIBLE
                                binding.lottieEmpty.visibility = View.VISIBLE
                            } else {
                                binding.rcyRcy.visibility = View.VISIBLE
                                binding.lottieEmpty.visibility = View.INVISIBLE
                            }
                            binding.rcyRcy.layoutManager = LinearLayoutManager(requireContext())
                            adpter = UserFavAdapter(requireContext(), list, this)
                            binding.rcyRcy.adapter = adpter
                            adpter!!.notifyDataSetChanged()


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
        viewModel.userAddFavListLiveData.observe(requireActivity(), androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            userFavList()

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

    override fun onResume() {
        super.onResume()
        userFavList()
    }

    override fun selectFav(propertyId: String) {
        flatId = propertyId
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )

        viewModel.userAddfavProperty(token, flatId, null)
    }


}