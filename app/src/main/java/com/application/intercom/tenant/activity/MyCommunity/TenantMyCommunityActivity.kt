package com.application.intercom.tenant.activity.MyCommunity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerHome.OwnerHomeViewModel
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.remote.owner.community.OwnerCommunityListRes
import com.application.intercom.data.model.remote.owner.getComment.OwnerGetCommentList
import com.application.intercom.databinding.ActivityTenantMyCommunityBinding
import com.application.intercom.databinding.TenanatCommentBottomSheetBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.owner.activity.OwnerParking.OwnerParkingActivity
import com.application.intercom.owner.activity.chat.OwnerCommunityChatActivity
import com.application.intercom.owner.activity.favorate.OwnerTenantFavorateActivity
import com.application.intercom.owner.activity.helpSupport.OwnerHelpSupportActivity
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.owner.activity.ownerProperty.OwnerPropertyActivity
import com.application.intercom.owner.activity.ownerVisitor.OwnerVisitorActivity
import com.application.intercom.owner.activity.ownerbilling.BillingAccountOwnerActivity
import com.application.intercom.owner.activity.ownerbilling.OwnerBillingActivity
import com.application.intercom.owner.activity.properties.OwnerPropertiesActivity
import com.application.intercom.owner.activity.registerComplain.OwnerAddRegisterComplainActivity
import com.application.intercom.tenant.Model.ProfileModal
import com.application.intercom.tenant.activity.MyCommunity.communityFragement.MemberListFragment
import com.application.intercom.tenant.activity.MyCommunity.communityFragement.MyCommunityFragment
import com.application.intercom.tenant.activity.billing.TenantBillingsActivity
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.tenant.activity.noticBoard.TenantNoticBoardActivity
import com.application.intercom.tenant.activity.registerComplain.TenantRegisterComplainActivity
import com.application.intercom.tenant.activity.setting.TenantSettingActivity
import com.application.intercom.tenant.adapter.Profile.ProfileAdapter
import com.application.intercom.tenant.adapter.comment.TenantCommentAdapter
import com.application.intercom.tenant.adapter.myCommunity.TenantMyCommunityAdapter
import com.application.intercom.user.aboutapp.AboutUsActivity
import com.application.intercom.user.aboutapp.PrivacyPolicyActivity
import com.application.intercom.user.aboutapp.TermsOfServiceActivity
import com.application.intercom.user.newflow.UserUpgradActivity
import com.application.intercom.utils.SessionConstants
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator

class TenantMyCommunityActivity : BaseActivity<ActivityTenantMyCommunityBinding>(),
    ProfileAdapter.ProfileClick
/*TenantMyCommunityAdapter.Click, CommunityImgAdapter.ClickImg, TenantCommentAdapter.ListClick*/ {

    private var adptr: TenantMyCommunityAdapter? = null
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var comment_by_bottom: TenanatCommentBottomSheetBinding
    private var commentAdapter: TenantCommentAdapter? = null
    private var from: String = ""
    private var projectId: String = ""
    private var userId: String = ""
    private var storeId: String = ""
    private lateinit var owner_viewModel: OwnerHomeViewModel
    private lateinit var side_owner_viewModel: OwnerSideViewModel
    private var communityList = ArrayList<OwnerCommunityListRes.Data>()
    private var getCommentList = ArrayList<OwnerGetCommentList.Data>()
    private var postid: String = ""
    private lateinit var dialog: Dialog
    private var showImg: String? = ""
    private var viewPostCountId: String = ""

    ///side menu
    private var profile_list = ArrayList<ProfileModal>()
    private var profileAdapter: ProfileAdapter? = null

    ///side menu
    override fun getLayout(): ActivityTenantMyCommunityBinding {
        return ActivityTenantMyCommunityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from") ?: ""
        println("----from$from")
        projectId = intent.getStringExtra("projectId") ?: ""
        storeId = intent.getStringExtra("storeId") ?: ""
        println("======$storeId")
        /* projectId = prefs.getString(
             SessionConstants.PROJECTID, GPSService.mLastLocation?.latitude.toString()
         )
         userId = prefs.getString(
             SessionConstants.USERID, GPSService.mLastLocation?.latitude.toString()
         )
         println("---commprojectId${projectId}")*/
        var lang =
            prefs.getString(SessionConstants.LANG, GPSService.mLastLocation?.latitude.toString())
        println("=====home$lang")
        if (lang.isEmpty()) {
            lang = Language.BANGLA.languageCode
            println("=====home$lang")
            setLocale(lang)
        }
        if (lang == "bn") {
            binding.nav.tvBl.visibility = View.VISIBLE
            binding.nav.tvEnglish.visibility = View.INVISIBLE
            binding.nav.imageView96.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.pro_bangla_img
                )
            )
        } else {
            binding.nav.tvBl.visibility = View.INVISIBLE
            binding.nav.tvEnglish.visibility = View.VISIBLE
            binding.nav.imageView96.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.pro_img
                )
            )
        }
        initView()
        lstnr()
        rcyItem()
    }

    private fun rcyItem() {
        if (from == "tenant") {
            profile_list.add(ProfileModal(R.drawable.home_icon, getString(R.string.home)))
            profile_list.add(ProfileModal(R.drawable.property_icon, getString(R.string.property)))
            profile_list.add(ProfileModal(R.drawable.parking_icon, getString(R.string.parking)))
            profile_list.add(ProfileModal(R.drawable.service_icon, getString(R.string.services)))
            profile_list.add(ProfileModal(R.drawable.fav_icon, getString(R.string.favorites)))
            profile_list.add(
                ProfileModal(
                    R.drawable.community_icon,
                    getString(R.string.my_community)
                )
            )
//        profile_list.add(ProfileModal(R.drawable.complaint_icon, "Complaint"))
            profile_list.add(ProfileModal(R.drawable.billing_icon, getString(R.string.my_billings)))
            profile_list.add(
                ProfileModal(
                    R.drawable.visitor_icon,
                    getString(R.string.visitors_gatepass)
                )
            )
            profile_list.add(ProfileModal(R.drawable.notics_icon, getString(R.string.notice_board)))
            profile_list.add(
                ProfileModal(
                    R.drawable.help_icon,
                    getString(R.string.help_and_support)
                )
            )
            profile_list.add(ProfileModal(R.drawable.setting_icon, getString(R.string.settings)))
            profile_list.add(ProfileModal(R.drawable.share_new_icon, getString(R.string.share)))
            profile_list.add(
                ProfileModal(
                    R.drawable.privacy_icon,
                    getString(R.string.privacy_policy)
                )
            )
            profile_list.add(
                ProfileModal(
                    R.drawable.term_icon,
                    getString(R.string.terms_and_conditions)
                )
            )
            profile_list.add(ProfileModal(R.drawable.term_icon, getString(R.string.about)))
            binding.nav.rcyProfile.layoutManager = LinearLayoutManager(this)
            profileAdapter = ProfileAdapter(this, profile_list, "tenant", this)
            binding.nav.rcyProfile.adapter = profileAdapter
            profileAdapter!!.notifyDataSetChanged()

        } else {
            profile_list.add(ProfileModal(R.drawable.home_icon, getString(R.string.home)))
            profile_list.add(
                ProfileModal(
                    R.drawable.property_icon,
                    getString(R.string.my_properties)
                )
            )
            profile_list.add(ProfileModal(R.drawable.parking_icon, getString(R.string.property)))
            profile_list.add(ProfileModal(R.drawable.service_icon, getString(R.string.parking)))
//        profile_list.add(ProfileModal(R.drawable.property_icon, "My Property"))
//        profile_list.add(ProfileModal(R.drawable.parking_icon, "My Parking"))
            profile_list.add(ProfileModal(R.drawable.fav_icon, getString(R.string.favorites)))
            profile_list.add(
                ProfileModal(
                    R.drawable.community_icon,
                    getString(R.string.my_community)
                )
            )
            profile_list.add(ProfileModal(R.drawable.billing_icon, getString(R.string.complain)))
          /*  profile_list.add(
                ProfileModal(
                    R.drawable.community_icon,
                    getString(R.string.my_billings)
                )
            )*/
            profile_list.add(
                ProfileModal(
                    R.drawable.visitor_icon,
                    getString(R.string.visitors_gatepass)
                )
            )
            profile_list.add(
                ProfileModal(
                    R.drawable.term_icon,
                    "Billing Account"
                )
            )
            profile_list.add(ProfileModal(R.drawable.notics_icon, getString(R.string.notice_board)))
            profile_list.add(
                ProfileModal(
                    R.drawable.help_icon,
                    getString(R.string.help_and_support)
                )
            )
            profile_list.add(ProfileModal(R.drawable.setting_icon, getString(R.string.settings)))
            profile_list.add(ProfileModal(R.drawable.share_new_icon, getString(R.string.share)))
            profile_list.add(
                ProfileModal(
                    R.drawable.privacy_icon,
                    getString(R.string.privacy_policy)
                )
            )
            profile_list.add(
                ProfileModal(
                    R.drawable.term_icon,
                    getString(R.string.terms_and_conditions)
                )
            )
            profile_list.add(
                ProfileModal(
                    R.drawable.term_icon,
                    getString(R.string.about)
                )
            )
            binding.nav.rcyProfile.layoutManager = LinearLayoutManager(this)
            profileAdapter = ProfileAdapter(this, profile_list, "owner", this)
            binding.nav.rcyProfile.adapter = profileAdapter
            profileAdapter!!.notifyDataSetChanged()
        }
    }
    private fun initView() {
//        binding.rcyMyCommunity.layoutManager = LinearLayoutManager(this)
//        initialize()
//        observer()
        binding.myCommunityToolbar.ivmneu.visibility = View.VISIBLE
        binding.myCommunityToolbar.ivBack.visibility = View.INVISIBLE
        if (from.equals("owner")) {
            binding.myCommunityToolbar.ivChatIcon.visibility = View.VISIBLE
        } else if (from.equals("tenant")) {
            binding.myCommunityToolbar.ivChatIcon.visibility = View.VISIBLE
        } else {
            binding.myCommunityToolbar.ivChatIcon.visibility = View.INVISIBLE
        }

        binding.myCommunityToolbar.tvTittle.text = getString(R.string.my_community)
        binding.viewPager.adapter = ScreenSlidePagerAdapter(this)
        TabLayoutMediator(binding.tabLay, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.my_community)
                }
                1 -> {
                    tab.text = "Community Member"
                }

            }
        }.attach()
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2
        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return MyCommunityFragment(from, projectId, storeId)
                }
                1 -> {
                    return MemberListFragment()
                }
            }
            return MyCommunityFragment(from, projectId, storeId)
        }
    }
    /* private fun initialize() {
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
     }*/

/*
    private fun observer() {
        communityList.clear()
        owner_viewModel.ownerCommunityLiveData.observe(this, Observer {
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
                            adptr = TenantMyCommunityAdapter(this, this, from, communityList, this)
                            binding.rcyMyCommunity.adapter = adptr
                            adptr!!.notifyDataSetChanged()
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
        owner_viewModel.viewPostCountOwnerLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            startActivity(
                                Intent(this, ViewPostDetailsActivity::class.java).putExtra(
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
                            getOwnerCommunityList()
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
                            getOwnerCommunityList()
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
                            getOwnerCommunityList()
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
////tenant
        side_owner_viewModel.deleteCommentTenantLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            getCommentList()
                            getOwnerCommunityList()
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
        side_owner_viewModel.editCommentTenantLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            getCommentList()
                            getOwnerCommunityList()
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

    }*/

    private fun lstnr() {
        /* binding.myCommunityToolbar.ivBack.setOnClickListener {
             finish()
             if (from == "owner") {
                 startActivity(Intent(this, OwnerMainActivity::class.java))
             } else if (from == "tenant") {
                 startActivity(Intent(this, TenantMainActivity::class.java))
             } else {
                 startActivity(Intent(this, OwnerMainActivity::class.java))
             }

         }*/
        if (from == "tenant") {
            binding.nav.tvEnglish.setOnClickListener {
                binding.nav.tvBl.visibility = View.VISIBLE
                binding.nav.tvEnglish.visibility = View.INVISIBLE
                prefs.put(SessionConstants.LANG, Language.BANGLA.languageCode)
                println("======test${Language.BANGLA.languageCode}")
                setLocale(Language.BANGLA.languageCode)
                finishAffinity()
                startActivity(Intent(this, TenantMainActivity::class.java))
            }
            binding.nav.tvBl.setOnClickListener {
                binding.nav.tvBl.visibility = View.INVISIBLE
                binding.nav.tvEnglish.visibility = View.VISIBLE
                prefs.put(SessionConstants.LANG, Language.ENGLISH.languageCode)
                println("======test${Language.ENGLISH.languageCode}")
                setLocale(Language.ENGLISH.languageCode)
                finishAffinity()
                startActivity(Intent(this, TenantMainActivity::class.java))
            }
        } else {
            binding.nav.tvEnglish.setOnClickListener {
                binding.nav.tvBl.visibility = View.VISIBLE
                binding.nav.tvEnglish.visibility = View.INVISIBLE
                prefs.put(SessionConstants.LANG, Language.BANGLA.languageCode)
                println("======test${Language.BANGLA.languageCode}")
                setLocale(Language.BANGLA.languageCode)
                finishAffinity()
                startActivity(Intent(this, OwnerMainActivity::class.java))
            }
            binding.nav.tvBl.setOnClickListener {
                binding.nav.tvBl.visibility = View.INVISIBLE
                binding.nav.tvEnglish.visibility = View.VISIBLE
                prefs.put(SessionConstants.LANG, Language.ENGLISH.languageCode)
                println("======test${Language.ENGLISH.languageCode}")
                setLocale(Language.ENGLISH.languageCode)
                finishAffinity()
                startActivity(Intent(this, OwnerMainActivity::class.java))
            }
        }
        binding.nav.constraintLayout10.setOnClickListener {
            startActivity(Intent(this, UserUpgradActivity::class.java))
        }
        binding.myCommunityToolbar.ivmneu.setOnClickListener {
            binding.communityDrw.openDrawer(GravityCompat.START)
        }
        binding.myCommunityToolbar.ivChatIcon.setOnClickListener {
            startActivity(
                Intent(this, OwnerCommunityChatActivity::class.java).putExtra("key", from)
            )
        }
//        binding.myCommunityToolbar.tvText.setOnClickListener {
//            startActivity(
//                Intent(this, TenantPostActivity::class.java).putExtra("from", "post")
//                    .putExtra("projectId", projectId)
//            )
//        }
        /*binding.tvMyPost.setOnClickListener {
            startActivity(
                Intent(this, TenantPostActivity::class.java).putExtra("from", "post")
                    .putExtra("projectId", projectId)
            )
        }
        binding.tvCreatPost.setOnClickListener {
            startActivity(Intent(this, TenantWritePostActivity::class.java))
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
        }*/


    }

    /* override fun onCLick(position: Int, id: String, postStatus: Boolean) {
         postid = id
         getCommentList()
         comment_BottomSheet(postStatus)

     }

     override fun isLike(
         likedBy: String, postBy: String, status: String, position: Int, myLikeStatus: Boolean
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
         binding.rcyMyCommunity.layoutManager = LinearLayoutManager(this@TenantMyCommunityActivity)
         adptr = TenantMyCommunityAdapter(this, this, "", communityList, this)
         binding.rcyMyCommunity.adapter = adptr
         adptr!!.notifyItemInserted(position)

         val token = prefs.getString(
             SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
         )
         val model = OwnerLikeCommunityPostModel(likedBy, postBy, status)
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
         *//*val title = "My Community"
        val description = "Test"
        val imageUrl = ""*//*
        val links = "https://intercomapp.page.link/Go1D/Community/${storeId}"
        prefs.put(SessionConstants.STOREID,storeId)
        shareIntent(this, links)


    }

    private fun comment_BottomSheet(postStatus: Boolean) {
        getCommentList()
        comment_by_bottom = TenanatCommentBottomSheetBinding.inflate(LayoutInflater.from(this))
        bottomSheetDialog = BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme).apply {
            setContentView(comment_by_bottom.root)
            comment_by_bottom.rcyComment.layoutManager =
                LinearLayoutManager(this@TenantMyCommunityActivity)
            commentAdapter = TenantCommentAdapter(
                this@TenantMyCommunityActivity,
                getCommentList,
                postStatus,
                this@TenantMyCommunityActivity
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
                        comment_by_bottom.edName.text.trim().toString(), userId, postid
                    )
                    side_owner_viewModel.commentPostOwnerList(token, model)
                    comment_by_bottom.edName.setText("")
                } else {
                    Toast.makeText(
                        this@TenantMyCommunityActivity, "Please Enter Comment!!", Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
        bottomSheetDialog.show()
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

    override fun onResume() {
        super.onResume()
        getOwnerCommunityList()
    }

    override fun commentEdit(position: Int, commentId: String, comment: String) {
        editPopup(commentId, comment)
    }

    override fun commentDelete(position: Int, commentId: String) {
        *//*if (from == "tenant") {
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            side_owner_viewModel.deleteCommentTenant(token, commentId, postid)
        } else {
            val token = prefs.getString(
                SessionConstants.TOKEN, ""
            )
            side_owner_viewModel.deleteCommentOwner(token, commentId, postid)
        }*//*
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
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
            *//*if (from == "tenant") {
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
            }*//*
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
*/
    override fun onBackPressed() {
        finish()
        if (from == "owner") {
            startActivity(Intent(this, OwnerMainActivity::class.java))
        } else if (from == "tenant") {
            startActivity(Intent(this, TenantMainActivity::class.java))
        } else {
            startActivity(Intent(this, OwnerMainActivity::class.java))
        }
    }

    override fun onClick(position: Int) {
        if (from == "tenant") {
            when (position) {
                0 -> {
                    startActivity(
                        Intent(this, TenantMainActivity::class.java).putExtra(
                            "from",
                            "from_side_home"
                        )
                    )

                }
                1 -> {
                    startActivity(
                        Intent(this, TenantMainActivity::class.java).putExtra(
                            "from", "from_side_property"
                        )
                    )

                }
                2 -> {
                    startActivity(
                        Intent(this, TenantMainActivity::class.java).putExtra(
                            "from", "from_side_parking"
                        )
                    )


                }
                3 -> {
                    startActivity(
                        Intent(this, TenantMainActivity::class.java).putExtra(
                            "from", "from_side_service"
                        )
                    )
                }
                4 -> {
                    startActivity(
                        Intent(this, OwnerTenantFavorateActivity::class.java).putExtra(
                            "from",
                            "tenant"
                        )
                    )
                }
                5 -> {
                    startActivity(
                        Intent(
                            this, TenantMyCommunityActivity::class.java
                        ).putExtra("from", "tenant")/*.putExtra("projectId", projectId)*/
                    )
                }
                6 -> {
                    startActivity(
                        Intent(this, TenantBillingsActivity::class.java)
                    )
                }
                7 -> {
                    startActivity(
                        Intent(this, OwnerVisitorActivity::class.java).putExtra(
                            "from", "tenant"
                        )
                    )
                }
                8 -> {
                    startActivity(Intent(this, TenantNoticBoardActivity::class.java))

                }
                9 -> {
                    startActivity(
                        Intent(this, OwnerHelpSupportActivity::class.java).putExtra(
                            "from",
                            "tenant"
                        )
                    )
                }
                10 -> {
                    startActivity(Intent(this, TenantSettingActivity::class.java))
                }
                11 -> {
                    shareIntent(this, "https://intercomapp.page.link/Go1D")
                }
                12 -> {
                    startActivity(Intent(this, PrivacyPolicyActivity::class.java))

                }
                13 -> {
                    startActivity(Intent(this, TermsOfServiceActivity::class.java))

                }
                14 -> {
                    startActivity(Intent(this, AboutUsActivity::class.java))
                }
            }
        } else {
            when (position) {
                0 -> {
                    startActivity(
                        Intent(this, OwnerMainActivity::class.java).putExtra(
                            "from", "from_side_home"
                        )
                    )
                }
                1 -> {
                    startActivity(
                        Intent(this, OwnerPropertiesActivity::class.java)
                    )
                }
                2 -> {
                    startActivity(
                        Intent(this, OwnerPropertyActivity::class.java)
                    )
                }
                3 -> {
                    startActivity(
                        Intent(this, OwnerParkingActivity::class.java)
                    )
                }
                4 -> {
                    startActivity(Intent(this, OwnerTenantFavorateActivity::class.java))
                }
                5 -> {
                    startActivity(
                        Intent(
                            this, TenantMyCommunityActivity::class.java
                        ).putExtra("from", "owner")/*.putExtra("projectId", projectId)*/
                    )
                }
                6 -> {
                    startActivity(
                        Intent(
                            this,
                            TenantRegisterComplainActivity::class.java
                        ).putExtra("from", "owner")
                    )
                }
                7 -> {
                    /*startActivity(
                        Intent(this, OwnerBillingActivity::class.java).putExtra(
                            "from", "owner"
                        )
                    )*/
                    startActivity(
                        Intent(this, OwnerVisitorActivity::class.java).putExtra(
                            "from",
                            "owner"
                        )
                    )

                }
                8 -> {
                    startActivity(
                        Intent(
                            this, BillingAccountOwnerActivity::class.java
                        )
                    )
                }
                9 -> {
//                startActivity(
//                    Intent(
//                        this, TenantNoticBoardActivity::class.java
//                    ).putExtra("from", "owner")
//                )
                    startActivity(
                        Intent(
                            this, TenantNoticBoardActivity::class.java
                        ).putExtra("from", "owner")
                    )
                }
                10 -> {
                    startActivity(Intent(this, OwnerHelpSupportActivity::class.java))
                }
                11 -> {
                    startActivity(Intent(this, TenantSettingActivity::class.java))

                }
                12 -> {
                    shareIntent(this, "https://intercomapp.page.link/Go1D")
                }
                13 -> {
                    startActivity(Intent(this, PrivacyPolicyActivity::class.java))


                }
                14 -> {
                    startActivity(Intent(this, TermsOfServiceActivity::class.java))
                }
                15 -> {
                    startActivity(Intent(this, AboutUsActivity::class.java))

                }

            }
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

    fun closeDrawer() {
        if (binding.communityDrw.isDrawerOpen(GravityCompat.START)) {
            binding.communityDrw.closeDrawer(GravityCompat.START)
        }
    }

    override fun onResume() {
        super.onResume()
        closeDrawer()
    }
}