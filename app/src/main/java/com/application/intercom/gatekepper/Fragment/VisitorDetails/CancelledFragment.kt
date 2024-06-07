package com.application.intercom.gatekepper.Fragment.VisitorDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.databinding.FragmentCancelledBinding
import com.application.intercom.gatekepper.gatekeeperAdapter.VisitorDetails.UpComingAdapter

class CancelledFragment : Fragment() {
    lateinit var binding: FragmentCancelledBinding
    private var adptr: UpComingAdapter? = null
    private var changeText = "cancelled"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCancelledBinding.inflate(layoutInflater)
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        binding.rcyCancelled.layoutManager = LinearLayoutManager(requireContext())
        adptr = UpComingAdapter(requireContext(),changeText)
        binding.rcyCancelled.adapter = adptr
        adptr!!.notifyDataSetChanged()
    }

    private fun lstnr() {
    }

}