package com.application.intercom.tenant.activity.post

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome.OwnerHomeViewModel
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.factory.ownerfactory.ownerHome.OwnerHomeFactory
import com.application.intercom.data.model.local.owner.OwnerCommunityPostModel
import com.application.intercom.data.model.local.owner.commentPost.EditCommentPostModel
import com.application.intercom.data.model.local.owner.commentPost.OwnerCommentPostModel
import com.application.intercom.data.model.local.owner.getCommentPostModel.OwnerGetCommentPostModel
import com.application.intercom.data.model.local.owner.likeCommunty.OwnerLikeCommunityPostModel
import com.application.intercom.data.model.remote.owner.community.OwnerCommunityListRes
import com.application.intercom.data.model.remote.owner.community.OwnerMyCommunityListRes
import com.application.intercom.data.model.remote.owner.getComment.OwnerGetCommentList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.databinding.ActivityTenantPostBinding
import com.application.intercom.databinding.TenanatCommentBottomSheetBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.owner.activity.viewPostDetails.ViewPostDetailsActivity
import com.application.intercom.tenant.adapter.comment.TenantCommentAdapter
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter
import com.application.intercom.tenant.adapter.myCommunity.OwnerMyPostAdapter
import com.application.intercom.tenant.adapter.myCommunity.TenantMyCommunityAdapter
import com.application.intercom.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog

class TenantPostActivity : BaseActivity<ActivityTenantPostBinding>(), OwnerMyPostAdapter.Click,
    CommunityImgAdapter.ClickImg, TenantCommentAdapter.ListClick {
    override fun getLayout(): ActivityTenantPostBinding {
        return ActivityTenantPostBinding.inflate(layoutInflater)
    }

    private var adptr: OwnerMyPostAdapter? = null
    private var from: String = ""
    private var communityList = ArrayList<OwnerMyCommunityListRes.Data>()
    private var projectId: String = ""
    private var showImg: String? = ""
    private lateinit var owner_viewModel: OwnerHomeViewModel
    private lateinit var dialog: Dialog
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var comment_by_bottom: TenanatCommentBottomSheetBinding
    private var commentAdapter: TenantCommentAdapter? = null
    private lateinit var side_owner_viewModel: OwnerSideViewModel
    private var getCommentList = ArrayList<OwnerGetCommentList.Data>()
    private var userId: String = ""
    private var postid: String = ""
    private var flatNameforComment: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from").toString()
        projectId = intent.getStringExtra("projectId").toString()
        userId = prefs.getString(
            SessionConstants.USERID,
            GPSService.mLastLocation?.latitude.toString()
        )
        initView()
        lstnr()
    }

    private fun initView() {
        initialize()
        observer()
        binding.postToolbar.tvTittle.text = getString(R.string.my_post)

    }

    private fun initialize() {
        val ownerModel = OwnerHomeRepo(BaseApplication.apiService)
        owner_viewModel = ViewModelProvider(
            this, OwnerHomeFactory(ownerModel)
        )[OwnerHomeViewModel::class.java]

        val sideownerModel = OwnerSideRepo(BaseApplication.apiService)
        side_owner_viewModel = ViewModelProvider(
            this, OwnerSideFactory(sideownerModel)
        )[OwnerSideViewModel::class.java]
    }

    private fun getOwnerMyCommunityList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        val model = OwnerCommunityPostModel(projectId,null)
        owner_viewModel.ownerMyCommunityList(token, model)
    }

    private fun getCommentList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        val model = OwnerGetCommentPostModel(postid)
        side_owner_viewModel.getCommentOwnerList(token, model)
    }

    private fun lstnr() {
        binding.postToolbar.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun observer() {
        owner_viewModel.ownerMyCommunityLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            communityList.clear()
                            communityList.addAll(it.data)
                            binding.rcyPost.layoutManager = LinearLayoutManager(this)
                            adptr = OwnerMyPostAdapter(this, this, from, communityList, this)
                            binding.rcyPost.adapter = adptr
                            adptr!!.notifyDataSetChanged()
                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            // this.longToast(it.message)
                            binding.rcyPost.visibility = View.INVISIBLE
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
                            this.longToast(it.message)
                            getOwnerMyCommunityList()
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
        side_owner_viewModel.commentPostOwnerListLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            getCommentList()
                            getOwnerMyCommunityList()
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
        side_owner_viewModel.deleteMyCommunityPostOwnerLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
//                            getCommentList()
                            getOwnerMyCommunityList()
//                            commentAdapter!!.notifyDataSetChanged()
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
////coment
        side_owner_viewModel.deleteCommentOwnerLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            getCommentList()
                            getOwnerMyCommunityList()
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
        side_owner_viewModel.editCommentOwnerLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            getCommentList()
                            getOwnerMyCommunityList()
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
    }

    override fun onCLick(position: Int, id: String, postStatus: Boolean,flatName: String) {
        postid = id
        flatNameforComment = flatName
        getCommentList()
        comment_BottomSheet(postStatus)
    }

    override fun isLike(
        likedBy: String,
        postBy: String,
        status: String,
        position: Int,
        myLikeStatus: Boolean,
        flatName:String
    ) {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation?.latitude.toString()
        )
        val model = OwnerLikeCommunityPostModel(likedBy, postBy, status,flatName)
        owner_viewModel.likeownerCommunity(token, model)
    }

    override fun viewDetailCommunityOwner(postId: String) {
        startActivity(
            Intent(this, ViewPostDetailsActivity::class.java).putExtra(
                "postId",
                postId
            ).putExtra("from", from)
        )
    }

    override fun deletePost(pos: Int, id: String) {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        side_owner_viewModel.deleteMyCommunityPostOwner(token, id)
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

    private fun comment_BottomSheet(postStatus: Boolean) {
        getCommentList()
        comment_by_bottom = TenanatCommentBottomSheetBinding.inflate(LayoutInflater.from(this))
        bottomSheetDialog =
            BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme).apply {
                setContentView(comment_by_bottom.root)
                comment_by_bottom.rcyComment.layoutManager =
                    LinearLayoutManager(this@TenantPostActivity)
                commentAdapter =
                    TenantCommentAdapter(
                        this@TenantPostActivity,
                        getCommentList,
                        postStatus,
                        this@TenantPostActivity
                    )
                comment_by_bottom.rcyComment.adapter = commentAdapter
                commentAdapter!!.notifyDataSetChanged()

                comment_by_bottom.imageView30.setOnClickListener {
                    bottomSheetDialog.dismiss()
                }
                comment_by_bottom.imageView8.setOnClickListener {
                    val data = comment_by_bottom.edName.text.trim().toString()
                    if (data.isNotEmpty()) {
                        val token = prefs.getString(
                            SessionConstants.TOKEN,
                            GPSService.mLastLocation?.latitude.toString()
                        )
                        val model = OwnerCommentPostModel(
                            comment_by_bottom.edName.text.trim().toString(),
                            userId,
                            postid,
                            flatNameforComment
                        )
                        side_owner_viewModel.commentPostOwnerList(token, model)
                        comment_by_bottom.edName.setText("")
                    } else {
                        Toast.makeText(
                            this@TenantPostActivity,
                            "Please Enter Comment!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        bottomSheetDialog.show()
    }

    override fun onResume() {
        super.onResume()
        getOwnerMyCommunityList()
    }

    override fun commentEdit(position: Int, commentId: String, comment: String) {
        editPopup(commentId, comment)
    }

    override fun commentDelete(position: Int, commentId: String) {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        side_owner_viewModel.deleteCommentOwner(token, commentId, postid)
    }

    private fun editPopup(commentId: String, comment: String) {
        val dialog = this.let { Dialog(this) }

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.edit_comment_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val editData = dialog.findViewById<EditText>(R.id.editText)
        val okBtn = dialog.findViewById<TextView>(R.id.tvOkComment)
        val cancelBtn = dialog.findViewById<TextView>(R.id.tvCancelComment)
        editData.setText(comment)
        okBtn.setOnClickListener {
            dialog.dismiss()
            val token = prefs.getString(
                SessionConstants.TOKEN,
                ""
            )
            val model = EditCommentPostModel(
                editData.text.trim().toString(),
                commentId,
                userId,
                postid
            )
            side_owner_viewModel.editCommentOwner(token, model)
        }
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }


        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
    }
}