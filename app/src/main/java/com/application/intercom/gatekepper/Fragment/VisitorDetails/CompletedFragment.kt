package com.application.intercom.gatekepper.Fragment.VisitorDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.databinding.FragmentCompletedBinding
import com.application.intercom.gatekepper.gatekeeperAdapter.VisitorDetails.UpComingAdapter

class CompletedFragment : Fragment() {
    lateinit var binding: FragmentCompletedBinding
    private var adptr: UpComingAdapter? = null
    private var changeText = "completed"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCompletedBinding.inflate(layoutInflater)
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        binding.rcyCompleted.layoutManager = LinearLayoutManager(requireContext())
        adptr = UpComingAdapter(requireContext(),changeText)
        binding.rcyCompleted.adapter = adptr
        adptr!!.notifyDataSetChanged()
    }

    private fun lstnr() {
    }

}