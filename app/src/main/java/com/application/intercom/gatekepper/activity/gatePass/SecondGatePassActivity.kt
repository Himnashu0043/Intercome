package com.application.intercom.gatekepper.activity.gatePass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.manager.managerSide.gatepass.ManagerGatePassHistoryListRes
import com.application.intercom.databinding.ActivitySecondGatePassBinding
import com.application.intercom.manager.gatekeeper.NoticeDetailsAdapter


class SecondGatePassActivity : AppCompatActivity() {
    lateinit var binding: ActivitySecondGatePassBinding
   /* private var gatePassList: ManagerGatePassHistoryListRes.Data.Result? = null
    private var imgAdapter: NoticeDetailsAdapter? = null
    private var imgList = ArrayList<String>()*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondGatePassBinding.inflate(layoutInflater)
        setContentView(binding.root)
       /* gatePassList = intent.getSerializableExtra("gatePass_list") as ManagerGatePassHistoryListRes.Data.Result?*/
        initView()
        lstnr()
    }

    private fun initView() {
        binding.secondToolbar.tvTittle.text = "Gatepass"
      /*  gatePassList!!.photo.forEach {
            imgList.add(gatePassList!!.photo.get(0))
        }
        println("---img$imgList")
        binding.r.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        imgAdapter = NoticeDetailsAdapter(this, imgList)
        binding.rcynoticeDetials.adapter = imgAdapter
        imgAdapter!!.notifyDataSetChanged()*/

    }

    private fun lstnr() {
        binding.secondToolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.download.setOnClickListener {
            startActivity(Intent(this, GatePassActivity::class.java))
        }
        binding.printout.setOnClickListener {
            startActivity(Intent(this, GatePassActivity::class.java))
        }

    }
}