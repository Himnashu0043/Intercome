package com.application.intercom.gatekepper.Fragment.newFragment.newVisitor

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.application.intercom.R
import com.application.intercom.baseActivity.prefs
import com.application.intercom.databinding.FragmentNewVisitorGateKeeperBinding
import com.application.intercom.gatekepper.activity.newFlow.regularEntry.RegularEntryActivity
import com.application.intercom.gatekepper.activity.newFlow.regularEntryHistory.RegularEntryHistoryActivity
import com.application.intercom.gatekepper.activity.newFlow.singleEntry.SingleEntryActivity
import com.application.intercom.gatekepper.activity.newFlow.singleEntryHistory.SingleEntryHistoryActivity
import com.application.intercom.helper.GPSService
import com.application.intercom.utils.SessionConstants


class NewVisitorGateKeeperFragment : Fragment() {
    lateinit var binding: FragmentNewVisitorGateKeeperBinding
    private var flatName: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewVisitorGateKeeperBinding.inflate(layoutInflater)
        flatName = prefs.getString(
            SessionConstants.FLATNAME,
            GPSService.mLastLocation?.latitude.toString()
        )
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        binding.textView245.text = flatName
        binding.textView2451.text = flatName
        binding.toolbar.ivBack.visibility = View.INVISIBLE
        binding.toolbar.tvTittle.text = getString(R.string.visitors)

    }

    private fun lstnr() {
        binding.cardView7.setOnClickListener {
            startActivity(Intent(requireContext(), SingleEntryHistoryActivity::class.java))
        }
        binding.cardView8.setOnClickListener {
            startActivity(Intent(requireContext(), RegularEntryHistoryActivity::class.java))
        }

    }


}