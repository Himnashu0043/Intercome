package com.application.intercom.tenant.activity.noticBoard

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerside.OwnerSideViewModel
import com.application.intercom.data.model.ViewModel.tenantViewModel.TenantSideViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.factory.tenantFactory.TenantSideFactory
import com.application.intercom.data.model.remote.owner.noticBoard.OwnerNoticBoardListRes
import com.application.intercom.data.model.remote.tenant.tenantSide.TenantNoticeListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.data.repository.tenantRepo.TenantSideRepo
import com.application.intercom.databinding.ActivityTenantNoticBoardBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.owner.activity.OwnerParking.OwnerParkingActivity
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
import com.application.intercom.tenant.activity.MyCommunity.TenantMyCommunityActivity
import com.application.intercom.tenant.activity.billing.TenantBillingsActivity
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.tenant.activity.setting.TenantSettingActivity
import com.application.intercom.tenant.adapter.Profile.ProfileAdapter
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter
import com.application.intercom.tenant.adapter.noticeBoard.SecondTenantNoticBoardAdapter
import com.application.intercom.tenant.adapter.noticeBoard.TenantNoticeBoardAdapter
import com.application.intercom.user.aboutapp.AboutUsActivity
import com.application.intercom.user.aboutapp.PrivacyPolicyActivity
import com.application.intercom.user.aboutapp.TermsOfServiceActivity
import com.application.intercom.user.newflow.UserUpgradActivity
import com.application.intercom.utils.*

class TenantNoticBoardActivity : BaseActivity<ActivityTenantNoticBoardBinding>(),
    TenantNoticeBoardAdapter.OwnerNoticeClick,
    SecondTenantNoticBoardAdapter.TenantNoticeClick, CommunityImgAdapter.ClickImg,
    ProfileAdapter.ProfileClick {
    private var profile_list = ArrayList<ProfileModal>()
    private var profileAdapter: ProfileAdapter? = null
    private var adptr: TenantNoticeBoardAdapter? = null
    private var from: String = ""
    private lateinit var viewModel: OwnerSideViewModel
    override fun getLayout(): ActivityTenantNoticBoardBinding {
        return ActivityTenantNoticBoardBinding.inflate(layoutInflater)
    }

    private lateinit var tenant_viewModel: TenantSideViewModel
    private var noticeList = ArrayList<OwnerNoticBoardListRes.Data>()
    private var tenant_noticeList = ArrayList<TenantNoticeListRes.Data>()
    private var tenant_adptr: SecondTenantNoticBoardAdapter? = null
    private lateinit var dialog: Dialog
    private var showImg: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from")?:""
        println("---main$from")
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
        if (from == "owner") {
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
            profile_list.add(
                ProfileModal(
                    R.drawable.community_icon,
                    getString(R.string.my_billings)
                )
            )
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

        } else {
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
        }
    }

    private fun initView() {
        initialize()
        observer()
        binding.noticBoard.ivBack.visibility = View.INVISIBLE
        binding.noticBoard.ivmneu.visibility = View.VISIBLE
        binding.noticBoard.tvTittle.text = getString(R.string.notice_board)


    }

    private fun initialize() {
        binding.rcyNotice.layoutManager = LinearLayoutManager(this)
        val repo = OwnerSideRepo(BaseApplication.apiService)
        viewModel = ViewModelProvider(
            this,
            OwnerSideFactory(repo)
        )[OwnerSideViewModel::class.java]
        val tenant_repo = TenantSideRepo(BaseApplication.apiService)
        tenant_viewModel = ViewModelProvider(
            this,
            TenantSideFactory(tenant_repo)
        )[TenantSideViewModel::class.java]


    }

    private fun getOwnerNotices() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        /* if (from.equals("owner")) {*/
        viewModel.ownerNoticeList(token)
        /* } else {
             tenant_viewModel.tenantNoticeList(token)
         }*/


    }

    private fun getTenantNotices() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation?.latitude.toString()
        )
        tenant_viewModel.tenantNoticeList(token)


    }

    private fun observer() {
        viewModel.ownerNoticeLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            noticeList.clear()
                            noticeList.addAll(it.data)
                            binding.rcyNotice.layoutManager = LinearLayoutManager(this)
                            adptr = TenantNoticeBoardAdapter(
                                this,
                                noticeList,
                                this,
                                from,
                                this

                            )
                            binding.rcyNotice.adapter = adptr
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

        tenant_viewModel.tenantNoticeLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            tenant_noticeList.clear()
                            tenant_noticeList.addAll(it.data)
                            tenant_adptr = SecondTenantNoticBoardAdapter(
                                this,
                                tenant_noticeList,

                                this,
                                this
                            )
                            binding.rcyNotice.adapter = tenant_adptr
                            tenant_adptr!!.notifyDataSetChanged()


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
        /* binding.noticBoard.ivBack.setOnClickListener {
             finish()
             if (from == "owner") {
                 startActivity(Intent(this, OwnerMainActivity::class.java))
             } else {
                 startActivity(Intent(this, TenantMainActivity::class.java))
             }

         }*/
        binding.noticBoard.ivmneu.setOnClickListener {
            binding.noticeBoardDrw.openDrawer(GravityCompat.START)
        }
        if (from == "owner") {
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
        } else {
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
        }
        binding.nav.constraintLayout10.setOnClickListener {
            startActivity(Intent(this, UserUpgradActivity::class.java))
        }
    }

    override fun onClick(position: Int, id: String) {
        /*if (from.equals("owner")) {*/
        startActivity(
            Intent(this, TenantNoticeBoardDetailsActivity::class.java).putExtra(
                "viewId",
                id
            )
        )
        /* } else {
             startActivity(
                 Intent(this, TenantNoticeBoardDetailsActivity::class.java).putExtra(
                     "viewId",
                     id
                 )
             )
         }*/

    }

    override fun onResume() {
        super.onResume()
        if (from.equals("owner")) {
            getOwnerNotices()
        } else {
            getTenantNotices()
        }
        closeDrawer()
    }

    override fun onTenantClick(position: Int, id: String) {
        startActivity(
            Intent(this, TenantNoticeBoardDetailsActivity::class.java).putExtra(
                "viewId",
                id
            )
        )
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

    override fun onClick(position: Int) {
        if (from == "owner") {
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
                    startActivity(Intent(this, OwnerAddRegisterComplainActivity::class.java))
                }
                7 -> {
                    startActivity(
                        Intent(this, OwnerBillingActivity::class.java).putExtra(
                            "from", "owner"
                        )
                    )

                }
                8 -> {
                    startActivity(
                        Intent(this, OwnerVisitorActivity::class.java).putExtra(
                            "from",
                            "owner"
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
                            this, BillingAccountOwnerActivity::class.java
                        )
                    )
                }
                10 -> {
                    startActivity(
                        Intent(
                            this, TenantNoticBoardActivity::class.java
                        ).putExtra("from", "owner")
                    )
                }
                11 -> {
                    startActivity(Intent(this, OwnerHelpSupportActivity::class.java))

                }
                12 -> {
                    startActivity(Intent(this, TenantSettingActivity::class.java))
                }
                13 -> {
                    shareIntent(this, "https://intercomapp.page.link/Go1D")

                }
                14 -> {
                    startActivity(Intent(this, PrivacyPolicyActivity::class.java))

                }
                15 -> {
                    startActivity(Intent(this, TermsOfServiceActivity::class.java))

                }
                16 -> {
                    startActivity(Intent(this, AboutUsActivity::class.java))
                }
            }
        } else {
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
        }
    }

    fun closeDrawer() {
        if (binding.noticeBoardDrw.isDrawerOpen(GravityCompat.START)) {
            binding.noticeBoardDrw.closeDrawer(GravityCompat.START)
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
//    override fun showImg(img: String) {
//        showImg = img
//        dialogProile()
//    }
}