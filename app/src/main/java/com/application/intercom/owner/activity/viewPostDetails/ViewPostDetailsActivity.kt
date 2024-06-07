package com.application.intercom.owner.activity.viewPostDetails

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome.OwnerHomeViewModel
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.ViewModel.tenantViewModel.TenantSideViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.factory.ownerfactory.ownerHome.OwnerHomeFactory
import com.application.intercom.data.model.factory.tenantFactory.TenantSideFactory
import com.application.intercom.data.model.local.owner.getCommentPostModel.OwnerGetCommentPostModel
import com.application.intercom.data.model.local.owner.likeCommunty.OwnerLikeCommunityPostModel
import com.application.intercom.data.model.remote.owner.getComment.OwnerGetCommentList
import com.application.intercom.data.model.remote.tenant.tenantSide.TenantViewPostDetailsLIst
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.data.repository.tenantRepo.TenantSideRepo
import com.application.intercom.databinding.ActivityViewPostDetailsBinding
import com.application.intercom.databinding.TenanatCommentBottomSheetBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.setFormatDate
import com.application.intercom.owner.adapter.ownerHome.OwnerPostingAdapter
import com.application.intercom.tenant.Model.ImageModels
import com.application.intercom.tenant.activity.MyCommunity.TenantMyCommunityActivity
import com.application.intercom.tenant.adapter.comment.TenantCommentAdapter
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter
import com.application.intercom.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog

class ViewPostDetailsActivity : BaseActivity<ActivityViewPostDetailsBinding>(),
    CommunityImgAdapter.ClickImg {

    override fun getLayout(): ActivityViewPostDetailsBinding {
        return ActivityViewPostDetailsBinding.inflate(layoutInflater)
    }

    private lateinit var tenant_viewModel: TenantSideViewModel
    private lateinit var side_owner_viewModel: OwnerSideViewModel
    private lateinit var owner_viewModel: OwnerHomeViewModel
    private var list = ArrayList<TenantViewPostDetailsLIst.Data>()
    private var postId: String = ""
    private lateinit var adapterItem: CommunityImgAdapter
    private var photo_list = ArrayList<ImageModels>()
    private lateinit var dialog: Dialog
    private var showImg: String? = ""
    private var from: String = ""
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var comment_by_bottom: TenanatCommentBottomSheetBinding
    private var commentAdapter: TenantCommentAdapter? = null
    private var getCommentList = ArrayList<OwnerGetCommentList.Data>()
    private var userId: String = ""
    private var likedBy: String = ""
    private var status: String = ""
    private var flatName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = prefs.getString(
            SessionConstants.USERID, GPSService.mLastLocation?.latitude.toString()
        )
        from = intent.getStringExtra("from").toString()
        println("----from$from")
        postId = intent.getStringExtra("postId").toString()
        initView()
        listener()
    }

    private fun initView() {
        initialize()
        observer()
        if (from.equals("owner")) {
            viewPostDetailsOwner()
        } else if (from.equals("ownerHome")) {
            viewPostDetailsOwner()
        } else if (from.equals("tenantHome")) {
            viewPostDetailsTenant()
        } else {
            viewPostDetailsTenant()
        }


        binding.toolbar.tvTittle.text = "View Details"

    }

    private fun initialize() {
        val tenant_repo = TenantSideRepo(BaseApplication.apiService)
        tenant_viewModel = ViewModelProvider(
            this, TenantSideFactory(tenant_repo)
        )[TenantSideViewModel::class.java]

        val sideownerModel = OwnerSideRepo(BaseApplication.apiService)
        side_owner_viewModel = ViewModelProvider(
            this, OwnerSideFactory(sideownerModel)
        )[OwnerSideViewModel::class.java]
        val ownerModel = OwnerHomeRepo(BaseApplication.apiService)
        owner_viewModel = ViewModelProvider(
            this, OwnerHomeFactory(ownerModel)
        )[OwnerHomeViewModel::class.java]


    }

    private fun viewPostDetailsTenant() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        tenant_viewModel.viewPostDetailsTenant(token, postId)


    }

    private fun getCommentList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        val model = OwnerGetCommentPostModel(postId)
        side_owner_viewModel.getCommentOwnerList(token, model)
    }

    private fun viewPostDetailsOwner() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        tenant_viewModel.viewPostDetailsOwner(token, postId)


    }

    private fun observer() {
        tenant_viewModel.viewPostDetailsOwnerLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            val date = setFormatDate(it.data[0].createdAt)
                            binding.textView63.text = it.data[0].userId.fullName
                            binding.textView64.text = date
                            binding.textView65.text = it.data[0].userId.role
                            binding.textView66.text = it.data[0].description
                            binding.tvLike.text = it.data[0].likesCount.toString()
                            binding.tvComment.text = it.data[0].commentsCount.toString()
                            binding.textView67.text = it.data[0].totalViews.toString()
                            binding.imageView31.loadImagesWithGlideExt(it.data[0].userId.profilePic)
                            photo_list.clear()

                            for (img in it.data) {
                                if (!img.file.isNullOrEmpty()) {
                                    photo_list.add(
                                        ImageModels(
                                            img.file.toString(),
                                            0
                                        )
                                    )
                                }

                            }
                            binding.rcyCommunity.layoutManager =
                                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
                            adapterItem = CommunityImgAdapter(this, photo_list, this)
                            binding.rcyCommunity.adapter = adapterItem
                            adapterItem.notifyDataSetChanged()
                            likedBy = it.data.get(0).userId._id
                            flatName=it.data.get(0).flatId.name
                            val myLikeStatus = it.data.get(0).myLikeStatus
                            if (myLikeStatus) {
                                binding.imageView34.setImageResource(R.drawable.select_heart_icon)
                                status = "dislike"

                            } else {
                                binding.imageView34.setImageResource(R.drawable.like_icon)
                                status = "like"

                            }
                            println("----likk${myLikeStatus}")


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
        tenant_viewModel.viewPostDetailsTenantLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            val date = setFormatDate(it.data[0].createdAt)
                            binding.textView63.text = it.data[0].userId.fullName
                            binding.textView64.text = date
                            binding.textView65.text = it.data[0].userId.role
                            binding.textView66.text = it.data[0].description
                            binding.tvLike.text = it.data[0].likesCount.toString()
                            binding.tvComment.text = it.data[0].commentsCount.toString()
                            binding.textView67.text = it.data[0].totalViews.toString()
                            binding.imageView31.loadImagesWithGlideExt(it.data[0].userId.profilePic)
                            photo_list.clear()
                            for (img in it.data) {
                                if (!img.file.isNullOrEmpty()) {
                                    photo_list.add(
                                        ImageModels(
                                            img.file.toString(),
                                            0
                                        )
                                    )
                                }
                            }
                            binding.rcyCommunity.layoutManager =
                                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
                            adapterItem = CommunityImgAdapter(this, photo_list, this)
                            binding.rcyCommunity.adapter = adapterItem
                            adapterItem.notifyDataSetChanged()
                            likedBy = it.data.get(0).userId._id
                            flatName=it.data.get(0).flatId.name
                            val myLikeStatus = it.data.get(0).myLikeStatus
                            if (myLikeStatus) {
                                binding.imageView34.setImageResource(R.drawable.select_heart_icon)
                                status = "dislike"

                            } else {
                                binding.imageView34.setImageResource(R.drawable.like_icon)
                                status = "like"

                            }
                            /*if (it.data.get(0)._id.equals(it.data.get(0).flatId.owner._id)) {
                                binding.imageView32.visibility = View.INVISIBLE
                            } else {
                                binding.imageView32.visibility = View.VISIBLE
                            }*/
                            println("----likk${myLikeStatus}")

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
        side_owner_viewModel.getCommentOwnerListLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            getCommentList.clear()
                            getCommentList.addAll(it.data)

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
        side_owner_viewModel.commentPostOwnerListLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            viewPostDetailsOwner()
                            getCommentList()
                            commentAdapter!!.notifyDataSetChanged()
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
        owner_viewModel.likeownerCommunityLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            viewPostDetailsOwner()
                        } else if (it.status == AppConstants.STATUS_404) {

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

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            if (from.equals("owner")) {
                startActivity(
                    Intent(this, TenantMyCommunityActivity::class.java).putExtra(
                        "from", from
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()
            } else if (from.equals("ownerHome")) {
                finish()
            } else if (from.equals("tenantHome")) {
                finish()
            } else {
                startActivity(
                    Intent(this, TenantMyCommunityActivity::class.java).putExtra(
                        "from", from
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()
            }
        }
        binding.ivcmmt.setOnClickListener {
            //comment_BottomSheet()
        }
        binding.imageView34.setOnClickListener {
            val token = prefs.getString(
                SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
            )
            val model = OwnerLikeCommunityPostModel(likedBy, postId, status,flatName)
            owner_viewModel.likeownerCommunity(token, model)
        }

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

    /* private fun comment_BottomSheet() {
         getCommentList()
         comment_by_bottom = TenanatCommentBottomSheetBinding.inflate(LayoutInflater.from(this))
         bottomSheetDialog = BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme).apply {
             setContentView(comment_by_bottom.root)
             comment_by_bottom.rcyComment.layoutManager =
                 LinearLayoutManager(this@ViewPostDetailsActivity)
             commentAdapter = TenantCommentAdapter(this@ViewPostDetailsActivity, getCommentList)
             comment_by_bottom.rcyComment.adapter = commentAdapter
             commentAdapter!!.notifyDataSetChanged()

             comment_by_bottom.imageView30.setOnClickListener {
                 bottomSheetDialog.dismiss()
             }
             comment_by_bottom.imageView8.setOnClickListener {
                 val data = comment_by_bottom.edName.text.trim().toString()
                 if (data.isNotEmpty()) {
                     val token = prefs.getString(
                         SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
                     )
                     val model = OwnerCommentPostModel(
                         comment_by_bottom.edName.text.trim().toString(), userId, postId
                     )
                     side_owner_viewModel.commentPostOwnerList(token, model)
                     comment_by_bottom.edName.setText("")
                 } else {
                     Toast.makeText(
                         this@ViewPostDetailsActivity, "Please Enter Comment!!", Toast.LENGTH_SHORT
                     ).show()
                 }

             }
         }
         bottomSheetDialog.show()
     }*/

    override fun onBackPressed() {
        if (from.equals("owner")) {
            startActivity(
                Intent(this, TenantMyCommunityActivity::class.java).putExtra(
                    "from", from
                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
            finish()
        } else if (from.equals("ownerHome")) {
            finish()
        } else if (from.equals("tenantHome")) {
            finish()
        } else {
            startActivity(
                Intent(this, TenantMyCommunityActivity::class.java).putExtra(
                    "from", from
                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
            finish()
        }
    }
}