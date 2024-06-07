package com.application.intercom.tenant.activity.noticBoard

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.databinding.ActivityNoticeBoardDetailsBinding
import com.application.intercom.helper.DynamicLinks.shareIntent
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.setFormatDate
import com.application.intercom.manager.gatekeeper.NoticeDetailsAdapter
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter
import com.application.intercom.tenant.adapter.noticeBoard.TenantNoticeBoardAdapter
import com.application.intercom.utils.*

class TenantNoticeBoardDetailsActivity : BaseActivity<ActivityNoticeBoardDetailsBinding>(), CommunityImgAdapter.ClickImg {

    private lateinit var viewModel: OwnerSideViewModel
    override fun getLayout(): ActivityNoticeBoardDetailsBinding {
        return ActivityNoticeBoardDetailsBinding.inflate(layoutInflater)
    }

    private var viewId: String = ""
    private lateinit var adptr: NoticeDetailsAdapter
    private var photo_list = ArrayList<String>()
    private lateinit var dialog: Dialog
    private var showImg: String? = ""
    private var from: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewId = intent.getStringExtra("viewId").toString()
        from = intent.getStringExtra("from") ?: ""
        println("---viewId${viewId}")
        println("---from${from}")
        initView()
        lstnr()
    }

    private fun initView() {
        initialize()
        observer()
        viewNoticesDetails()
        binding.noticeDetailsToolbar.ivForward.visibility = View.VISIBLE
        binding.noticeDetailsToolbar.tvTittle.text = getString(R.string.notice_board)


    }

    private fun initialize() {
        val repo = OwnerSideRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(
            this,
            OwnerSideFactory(repo)
        )[OwnerSideViewModel::class.java]
    }

    private fun viewNoticesDetails() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.viewNoticeOwner(token, viewId)

    }

    private fun observer() {
        viewModel.viewNoticeOwnerLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            val date = setFormatDate(it.data.createdAt)
                            binding.textView38.text = date
                            binding.textView371.text = it.data.title
                            binding.textView40.text = it.data.content
                            it.data.images.forEach {
                                photo_list.add(it)
                            }
                            binding.rcy.layoutManager =
                                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
                            adptr = NoticeDetailsAdapter(this, photo_list, this)
                            binding.rcy.adapter = adptr
                            adptr.notifyDataSetChanged()
                            println("====${it.data}")
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(this, it.throwable!!)
                }
                else -> {}
            }
        })


    }

    private fun lstnr() {
        binding.noticeDetailsToolbar.ivBack.setOnClickListener {
            if (from == "tenant") {
                finish()
                //startActivity(Intent(this, TenantMainActivity::class.java))
            } else {
                finish()
                // startActivity(Intent(this, OwnerMainActivity::class.java))
            }
        }
        binding.noticeDetailsToolbar.ivForward.setOnClickListener {
            val links = "https://intercomapp.page.link/Go1D/Notice/${viewId}"
            prefs.put(SessionConstants.STOREID, viewId)
            shareIntent(this, links)
        }

    }

    override fun onBackPressed() {
        finish()
    }

    private fun dialogProile() {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_profile_owner)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val imgGallery = dialog.findViewById<ImageView>(R.id.ivProImg)
        imgGallery.loadImagesWithGlideExt(showImg.toString())


        dialog.show()

    }

    override fun showImg(img: String) {
        showImg = img
        dialogProile()
    }
}