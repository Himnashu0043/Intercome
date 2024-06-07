package com.application.intercom.tenant.activity.MyCommunity.communityFragement

import android.app.Activity
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
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome.OwnerHomeViewModel
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.factory.ownerfactory.ownerHome.OwnerHomeFactory
import com.application.intercom.data.model.local.owner.commentPost.EditCommentPostModel
import com.application.intercom.data.model.local.owner.commentPost.OwnerCommentPostModel
import com.application.intercom.data.model.local.owner.getCommentPostModel.OwnerGetCommentPostModel
import com.application.intercom.data.model.local.owner.likeCommunty.OwnerLikeCommunityPostModel
import com.application.intercom.data.model.remote.owner.community.OwnerCommunityListRes
import com.application.intercom.data.model.remote.owner.getComment.OwnerGetCommentList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerHome.OwnerHomeRepo
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.databinding.FragmentMyCommunityBinding
import com.application.intercom.databinding.TenanatCommentBottomSheetBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.owner.activity.viewPostDetails.ViewPostDetailsActivity
import com.application.intercom.tenant.activity.post.TenantPostActivity
import com.application.intercom.tenant.activity.writePost.TenantWritePostActivity
import com.application.intercom.tenant.adapter.comment.TenantCommentAdapter
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter
import com.application.intercom.tenant.adapter.myCommunity.TenantMyCommunityAdapter
import com.application.intercom.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog

class MyCommunityFragment(var from: String, var projectId: String, var storeId: String) :
    Fragment(),
    TenantMyCommunityAdapter.Click,
    CommunityImgAdapter.ClickImg, TenantCommentAdapter.ListClick {
    lateinit var binding: FragmentMyCommunityBinding
    private var adptr: TenantMyCommunityAdapter? = null
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var comment_by_bottom: TenanatCommentBottomSheetBinding
    private var commentAdapter: TenantCommentAdapter? = null
   // private var from: String = ""
   // private var projectId: String = ""
    private var userId: String = ""
  //  private var storeId: String = ""
    private lateinit var owner_viewModel: OwnerHomeViewModel
    private lateinit var side_owner_viewModel: OwnerSideViewModel
    private var communityList = ArrayList<OwnerCommunityListRes.Data>()
    private var getCommentList = ArrayList<OwnerGetCommentList.Data>()
    private var postid: String = ""
    private var flatNameforComment: String = ""
    private lateinit var dialog: Dialog
    private var showImg: String? = ""
    private var viewPostCountId: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyCommunityBinding.inflate(layoutInflater)
        projectId = prefs.getString(
            SessionConstants.PROJECTID, GPSService.mLastLocation?.latitude.toString()
        )
        userId = prefs.getString(
            SessionConstants.USERID, GPSService.mLastLocation?.latitude.toString()
        )
        println("---commprojectId${projectId}")
        initView()
        lstnr()
        return binding.root
    }

    private fun initView() {
        binding.rcyMyCommunity.layoutManager = LinearLayoutManager(requireContext())
        initialize()
        observer()
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

    private fun getOwnerCommunityList() {
        if (storeId.isNotEmpty()) {
            val token = prefs.getString(
                SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
            )
            owner_viewModel.ownerCommunity(
                token,
                hashMapOf("projectId" to projectId, "_id" to storeId)
            )
        } else {
            val token = prefs.getString(
                SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
            )
            owner_viewModel.ownerCommunity(token, hashMapOf("projectId" to projectId))
        }

    }


    private fun getCommentList() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        val model = OwnerGetCommentPostModel(postid)
        side_owner_viewModel.getCommentOwnerList(token, model)
    }

    private fun viewPostCountOwner() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )

        owner_viewModel.viewPostCountOwner(token, viewPostCountId)
    }


    private fun observer() {
        communityList.clear()
        owner_viewModel.ownerCommunityLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            communityList.clear()
                            communityList.addAll(it.data)
                            adptr = TenantMyCommunityAdapter(
                                requireContext(),
                                this,
                                from,
                                communityList,
                                this
                            )
                            binding.rcyMyCommunity.adapter = adptr
                            adptr!!.notifyDataSetChanged()
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireActivity().longToast(it.message)
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                }
                else -> {}
            }
        })
        owner_viewModel.likeownerCommunityLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {

                        } else if (it.status == AppConstants.STATUS_404) {

                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                }
                else -> {}
            }
        })
        owner_viewModel.viewPostCountOwnerLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            startActivity(
                                Intent(
                                    requireContext(),
                                    ViewPostDetailsActivity::class.java
                                ).putExtra(
                                    "postId", postid
                                ).putExtra("from", from)
                            )
                        } else if (it.status == AppConstants.STATUS_404) {

                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                }
                else -> {}
            }
        })
        side_owner_viewModel.getCommentOwnerListLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            getCommentList.clear()
                            getCommentList.addAll(it.data)
                            commentAdapter!!.notifyDataSetChanged()
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireActivity().longToast(it.message)
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                }
                else -> {}
            }
        })
        side_owner_viewModel.commentPostOwnerListLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            getCommentList()
                            getOwnerCommunityList()
                            commentAdapter!!.notifyDataSetChanged()
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireActivity().longToast(it.message)
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                }
                else -> {}
            }
        })
        side_owner_viewModel.deleteCommentOwnerLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            getCommentList()
                            getOwnerCommunityList()
                            commentAdapter!!.notifyDataSetChanged()
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireActivity().longToast(it.message)
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                }
                else -> {}
            }
        })
        side_owner_viewModel.editCommentOwnerLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            getCommentList()
                            getOwnerCommunityList()
                            commentAdapter!!.notifyDataSetChanged()
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireActivity().longToast(it.message)
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                }
                else -> {}
            }
        })
////tenant
        side_owner_viewModel.deleteCommentTenantLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            getCommentList()
                            getOwnerCommunityList()
                            commentAdapter!!.notifyDataSetChanged()
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireActivity().longToast(it.message)
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                }
                else -> {}
            }
        })
        side_owner_viewModel.editCommentTenantLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(requireActivity())
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            getCommentList()
                            getOwnerCommunityList()
                            commentAdapter!!.notifyDataSetChanged()
                        } else if (it.status == AppConstants.STATUS_404) {
                            requireActivity().longToast(it.message)
                        } else {

                        }
                    }
                }

                is EmpResource.Failure -> {
                    EmpCustomLoader.hideLoader()
                    ErrorUtil.handlerGeneralError(requireContext(), it.throwable!!)
                }
                else -> {}
            }
        })

    }

    private fun lstnr() {
//        binding.myCommunityToolbar.ivBack.setOnClickListener {
//            finish()
//            if (from == "owner") {
//                startActivity(Intent(this, OwnerMainActivity::class.java))
//            } else if (from == "tenant") {
//                startActivity(Intent(this, TenantMainActivity::class.java))
//            } else {
//                startActivity(Intent(this, OwnerMainActivity::class.java))
//            }
//
//        }
//        binding.myCommunityToolbar.ivChatIcon.setOnClickListener {
//            startActivity(
//                Intent(this, OwnerCommunityChatActivity::class.java).putExtra("key", from)
//            )
//        }
//        binding.myCommunityToolbar.tvText.setOnClickListener {
//            startActivity(
//                Intent(this, TenantPostActivity::class.java).putExtra("from", "post")
//                    .putExtra("projectId", projectId)
//            )
//        }
        binding.tvMyPost.setOnClickListener {
            startActivity(
                Intent(requireContext(), TenantPostActivity::class.java).putExtra("from", "post")
                    .putExtra("projectId", projectId)
            )
        }
        binding.tvCreatPost.setOnClickListener {
            startActivity(Intent(requireContext(), TenantWritePostActivity::class.java))
        }
        binding.imageView79.setOnClickListener {
            if (binding.imageView79.visibility == View.VISIBLE) {
                binding.constraintLayout24.visibility = View.VISIBLE
                binding.ivSubFloting.visibility = View.VISIBLE
                binding.tvMyPost.visibility = View.VISIBLE
                binding.tvCreatPost.visibility = View.VISIBLE
                binding.imageView79.visibility = View.INVISIBLE
            } else {
                binding.constraintLayout24.visibility = View.INVISIBLE
                binding.ivSubFloting.visibility = View.INVISIBLE
                binding.tvMyPost.visibility = View.INVISIBLE
                binding.tvCreatPost.visibility = View.INVISIBLE
                binding.imageView79.visibility = View.VISIBLE
            }
        }
        binding.ivSubFloting.setOnClickListener {
            if (binding.ivSubFloting.visibility == View.VISIBLE) {
                binding.constraintLayout24.visibility = View.INVISIBLE
                binding.ivSubFloting.visibility = View.INVISIBLE
                binding.tvMyPost.visibility = View.INVISIBLE
                binding.tvCreatPost.visibility = View.INVISIBLE
                binding.imageView79.visibility = View.VISIBLE

            } else {
                binding.constraintLayout24.visibility = View.VISIBLE
                binding.ivSubFloting.visibility = View.VISIBLE
                binding.tvMyPost.visibility = View.VISIBLE
                binding.tvCreatPost.visibility = View.VISIBLE
                binding.imageView79.visibility = View.INVISIBLE
            }
//            startActivity(Intent(this, TenantWritePostActivity::class.java))
        }


    }

    override fun onCLick(position: Int, id: String, postStatus: Boolean, flatName: String) {
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
        flatName: String
    ) {
        println("---post$postBy")
        var count = communityList[position].likesCount ?: 0
        if (status == "dislike") {
            count--
            communityList[position].likesCount = count
        } else {
            count++
            communityList[position].likesCount = count
        }

        communityList[position].myLikeStatus = myLikeStatus
        binding.rcyMyCommunity.layoutManager = LinearLayoutManager(requireContext())
        adptr = TenantMyCommunityAdapter(requireContext(), this, "", communityList, this)
        binding.rcyMyCommunity.adapter = adptr
        adptr!!.notifyItemInserted(position)

        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        val model = OwnerLikeCommunityPostModel(likedBy, postBy, status, flatName)
        owner_viewModel.likeownerCommunity(token, model)
    }

    override fun viewDetailCommunityOwner(postId: String) {
        postid = postId
        viewPostCountId = postId
        viewPostCountOwner()


    }

    override fun shareCommunity(share_id: String) {
        println("=======$share_id")
//        storeId = share_id
        val storeId = share_id
        /*val title = "My Community"
        val description = "Test"
        val imageUrl = ""*/
        val links = "https://intercomapp.page.link/Go1D/Community/${storeId}"
        prefs.put(SessionConstants.STOREID, storeId)
        shareIntent(requireActivity(), links)


    }

    private fun comment_BottomSheet(postStatus: Boolean) {
        getCommentList()
        comment_by_bottom =
            TenanatCommentBottomSheetBinding.inflate(LayoutInflater.from(requireContext()))
        bottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.CustomBottomSheetDialogTheme).apply {
                setContentView(comment_by_bottom.root)
                comment_by_bottom.rcyComment.layoutManager =
                    LinearLayoutManager(requireContext())
                commentAdapter = TenantCommentAdapter(
                    requireContext(),
                    getCommentList,
                    postStatus,
                    this@MyCommunityFragment
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
                            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
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
                            requireContext(), "Please Enter Comment!!", Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        bottomSheetDialog.show()
    }

    private fun dialogProile() {
        dialog = Dialog(requireContext())
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

    override fun onResume() {
        super.onResume()
        getOwnerCommunityList()
    }

    override fun commentEdit(position: Int, commentId: String, comment: String) {
        editPopup(commentId, comment)
    }

    override fun commentDelete(position: Int, commentId: String) {
        /*if (from == "tenant") {
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            side_owner_viewModel.deleteCommentTenant(token, commentId, postid)
        } else {
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            side_owner_viewModel.deleteCommentOwner(token, commentId, postid)
        }*/
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        side_owner_viewModel.deleteCommentOwner(token, commentId, postid)

    }

    private fun editPopup(commentId: String, comment: String) {
        val dialog = this.let { Dialog(requireContext()) }

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
            /*if (from == "tenant") {
                val token = prefs.getString(
                    SessionConstants.TOKEN, ""
                )
                val model = EditCommentPostModel(
                    editData.text.trim().toString(), commentId, userId, postid
                )
                side_owner_viewModel.editCommentTenant(token, model)
            } else {
                val token = prefs.getString(
                    SessionConstants.TOKEN, ""
                )
                val model = EditCommentPostModel(
                    editData.text.trim().toString(), commentId, userId, postid
                )
                side_owner_viewModel.editCommentOwner(token, model)
            }*/
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            val model = EditCommentPostModel(
                editData.text.trim().toString(), commentId, userId, postid
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

    fun shareIntent(activity: Activity?, shearableLink: String?) {
        ShareCompat.IntentBuilder
            .from(activity!!)
            .setText(shearableLink)
            .setType("text/plain")
            .setChooserTitle("Share with the users")
            .startChooser()
    }

}