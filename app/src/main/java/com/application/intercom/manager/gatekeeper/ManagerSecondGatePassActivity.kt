package com.application.intercom.manager.gatekeeper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.manager.managerSide.gatepass.ManagerGatePassHistoryListRes
import com.application.intercom.databinding.ActivityManagerSecondGatePassBinding
import com.application.intercom.helper.setFormatDate
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter


class ManagerSecondGatePassActivity : AppCompatActivity(),CommunityImgAdapter.ClickImg {
    lateinit var binding: ActivityManagerSecondGatePassBinding
    private var gatePassList: ManagerGatePassHistoryListRes.Data.Result? = null
    private var imgAdapter: NoticeDetailsAdapter? = null
    private var imgList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerSecondGatePassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        gatePassList =
            intent.getSerializableExtra("gatePass_list") as ManagerGatePassHistoryListRes.Data.Result
        initView()
        lstnr()
    }

    private fun initView() {
        binding.secondToolbar.tvTittle.text = getString(R.string.gate_pass)
        /// fetchData
        gatePassList!!.photo.forEach {
            imgList.add(gatePassList!!.photo.get(0))
        }
        println("---img$imgList")
        binding.rcyGatePassDetails.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        imgAdapter = NoticeDetailsAdapter(this, imgList,this)
        binding.rcyGatePassDetails.adapter = imgAdapter
        imgAdapter!!.notifyDataSetChanged()

        binding.textView3.text = gatePassList!!.activity
        binding.textView31.text = gatePassList!!.phoneNumber
        val date = setFormatDate(gatePassList!!.createdAt)
        binding.textView312.text = "${date} , ${gatePassList!!.entryTime}"
        binding.textView311.text = gatePassList!!.flatInfo.get(0).name
        binding.textView3111.text = "#${gatePassList!!.passNo}"

    }

    private fun lstnr() {
        binding.secondToolbar.ivBack.setOnClickListener {
            finish()
        }
       /* binding.download.setOnClickListener {
            startActivity(Intent(this, ManagerGatePassActivity::class.java))
        }
        binding.printout.setOnClickListener {
            startActivity(Intent(this, ManagerGatePassActivity::class.java))
        }*/

    }

    override fun showImg(img: String) {

    }
}