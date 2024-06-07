package com.application.intercom.gatekepper.Fragment.VisitorDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.databinding.FragmentUpComingBinding
import com.application.intercom.gatekepper.gatekeeperAdapter.VisitorDetails.UpComingAdapter

class UpComingFragment : Fragment() {
    lateinit var binding: FragmentUpComingBinding
    private var adptr: UpComingAdapter? = null
    private var changeText = "upcoming"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpComingBinding.inflate(layoutInflater)
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        binding.rcyUpcoming.layoutManager = LinearLayoutManager(requireContext())
        adptr = UpComingAdapter(requireContext(),changeText)
        binding.rcyUpcoming.adapter = adptr
        adptr!!.notifyDataSetChanged()

    }

    private fun lstnr() {

    }

}