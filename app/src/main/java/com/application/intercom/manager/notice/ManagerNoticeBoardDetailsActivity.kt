package com.application.intercom.manager.notice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.remote.manager.managerSide.noticeBoard.ManagerNoticeBoardListRes
import com.application.intercom.databinding.ActivityManagerNoticeBoardDetailsBinding
import com.application.intercom.helper.DynamicLinks.shareIntent
import com.application.intercom.helper.setFormatDate
import com.application.intercom.manager.gatekeeper.NoticeDetailsAdapter
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter
import com.application.intercom.utils.SessionConstants

class ManagerNoticeBoardDetailsActivity : AppCompatActivity(), CommunityImgAdapter.ClickImg {
    lateinit var binding: ActivityManagerNoticeBoardDetailsBinding
    private var notice_list: ManagerNoticeBoardListRes.Data? = null
    private var imgAdapter: NoticeDetailsAdapter? = null
    private var imgList = ArrayList<String>()
    private var storeId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerNoticeBoardDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        notice_list = intent.getSerializableExtra("notice_list") as ManagerNoticeBoardListRes.Data
        println("---viewId${notice_list}")
        storeId = notice_list!!._id
        initView()
        lstnr()
    }

    private fun initView() {
        binding.noticeDetailsToolbar.tvTittle.text = getString(R.string.notice_board)
        binding.noticeDetailsToolbar.ivForward.visibility = View.VISIBLE
        notice_list!!.images.forEach {
            imgList.add(notice_list!!.images.get(0))
        }
        println("---img$imgList")
        binding.rcynoticeDetials.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        imgAdapter = NoticeDetailsAdapter(this, imgList, this)
        binding.rcynoticeDetials.adapter = imgAdapter
        imgAdapter!!.notifyDataSetChanged()
        ////fetch Data
        val date = setFormatDate(notice_list!!.createdAt)
        binding.textView38.text = date
        binding.textView371.text = notice_list!!.title
        binding.textView40.text = notice_list!!.content

    }

    private fun lstnr() {
        binding.noticeDetailsToolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.noticeDetailsToolbar.ivForward.setOnClickListener {
            val links = "https://intercomapp.page.link/Go1D/Notice/$storeId"
            prefs.put(SessionConstants.STOREID, storeId)
            shareIntent(this, links)
        }

    }

    override fun showImg(img: String) {

    }
}