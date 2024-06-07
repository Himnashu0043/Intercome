package com.application.intercom.tenant.Fragment.ServiceFragmnet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.databinding.FragmentServiceBinding
import com.application.intercom.tenant.adapter.FragementServices.ListServiceAdapter
import com.application.intercom.tenant.adapter.RecentlyAdapter

class ServiceFragment : Fragment() {
    lateinit var binding: FragmentServiceBinding
    private var recently_adapter: RecentlyAdapter? = null
    private var list_adapter: ListServiceAdapter? = null
    private var key: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentServiceBinding.inflate(layoutInflater)
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
        recently_adapter = RecentlyAdapter(requireContext(), key)
        binding.rcyRecently.adapter = recently_adapter
        recently_adapter!!.notifyDataSetChanged()

        binding.rcyList.layoutManager = GridLayoutManager(requireContext(), 4)
        list_adapter = ListServiceAdapter(requireContext())
        binding.rcyList.adapter = list_adapter
        list_adapter!!.notifyDataSetChanged()


    }

    private fun lstnr() {
//        binding.tvViewAll.setOnClickListener {
//            startActivity(Intent(requireContext(), ListOfServicesActivity::class.java))
//        }

    }

}