package com.application.intercom.manager.service

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.FragmentManagerServiceBinding


class ManagerServiceFragment : Fragment() {
    lateinit var binding: FragmentManagerServiceBinding
    private var recently_adapter: ManagerServiceRecentlyAdapter? = null
    private var list_adapter: ManagerServiceListAdapter? = null
    private var key: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManagerServiceBinding.inflate(layoutInflater)
        key = arguments?.getString("key").toString()
        Log.d("Himanshu", "onCreateView: $key")
        initView()
        lstnr()
        return (binding.root)
    }

    private fun initView() {
        binding.serviceToolbar.tvTittle.text = "Services"
        binding.rcyRecently.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recently_adapter = ManagerServiceRecentlyAdapter(requireContext(), key)
        binding.rcyRecently.adapter = recently_adapter
        recently_adapter!!.notifyDataSetChanged()

        binding.rcyList.layoutManager = GridLayoutManager(requireContext(), 4)
        list_adapter = ManagerServiceListAdapter(requireContext())
        binding.rcyList.adapter = list_adapter
        list_adapter!!.notifyDataSetChanged()


    }

    private fun lstnr() {
//        binding.tvViewAll.setOnClickListener {
//            startActivity(Intent(requireContext(), ListOfServicesActivity::class.java))
//        }

    }

}