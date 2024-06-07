package com.application.intercom.owner.activity.ownerbilling

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.databinding.ActivityBillingAccountOwnerBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.Language
import com.application.intercom.helper.LocaleHelper.setLocale
import com.application.intercom.owner.activity.OwnerParking.OwnerParkingActivity
import com.application.intercom.owner.activity.favorate.OwnerTenantFavorateActivity
import com.application.intercom.owner.activity.helpSupport.OwnerHelpSupportActivity
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.owner.activity.ownerProperty.OwnerPropertyActivity
import com.application.intercom.owner.activity.ownerVisitor.OwnerVisitorActivity
import com.application.intercom.owner.activity.properties.OwnerPropertiesActivity
import com.application.intercom.owner.activity.rent.OwnerRentListActivity
import com.application.intercom.tenant.Model.ProfileModal
import com.application.intercom.tenant.activity.MyCommunity.TenantMyCommunityActivity
import com.application.intercom.tenant.activity.noticBoard.TenantNoticBoardActivity
import com.application.intercom.tenant.activity.registerComplain.TenantRegisterComplainActivity
import com.application.intercom.tenant.activity.setting.TenantSettingActivity
import com.application.intercom.tenant.adapter.Profile.ProfileAdapter
import com.application.intercom.user.aboutapp.AboutUsActivity
import com.application.intercom.user.aboutapp.PrivacyPolicyActivity
import com.application.intercom.user.aboutapp.TermsOfServiceActivity
import com.application.intercom.utils.*

class BillingAccountOwnerActivity : BaseActivity<ActivityBillingAccountOwnerBinding>(),
    ProfileAdapter.ProfileClick {
    private lateinit var viewModel: OwnerSideViewModel

    /////side menu
    private var profile_list = ArrayList<ProfileModal>()
    private var profileAdapter: ProfileAdapter? = null

    ///side mneu
    override fun getLayout(): ActivityBillingAccountOwnerBinding {
        return ActivityBillingAccountOwnerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //side mneu
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
                    this, R.drawable.pro_bangla_img
                )
            )
        } else {
            binding.nav.tvBl.visibility = View.INVISIBLE
            binding.nav.tvEnglish.visibility = View.VISIBLE
            binding.nav.imageView96.setImageDrawable(
                ContextCompat.getDrawable(
                    this, R.drawable.pro_img
                )
            )
        }
        //side menu
        initView()
        listener()
        ///side menu
        rcyItem()
        //sidemenu
    }

    private fun rcyItem() {
        profile_list.add(ProfileModal(R.drawable.home_icon, getString(R.string.home)))
        profile_list.add(ProfileModal(R.drawable.property_icon, getString(R.string.my_properties)))
        profile_list.add(ProfileModal(R.drawable.parking_icon, getString(R.string.property)))
        profile_list.add(ProfileModal(R.drawable.service_icon, getString(R.string.parking)))
//        profile_list.add(ProfileModal(R.drawable.property_icon, "My Property"))
//        profile_list.add(ProfileModal(R.drawable.parking_icon, "My Parking"))
        profile_list.add(ProfileModal(R.drawable.fav_icon, getString(R.string.favorites)))
        profile_list.add(ProfileModal(R.drawable.community_icon, getString(R.string.my_community)))
        profile_list.add(ProfileModal(R.drawable.billing_icon, getString(R.string.complain)))
       // profile_list.add(ProfileModal(R.drawable.community_icon, getString(R.string.my_billings)))
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
        profile_list.add(ProfileModal(R.drawable.help_icon, getString(R.string.help_and_support)))
        profile_list.add(ProfileModal(R.drawable.setting_icon, getString(R.string.settings)))
        profile_list.add(ProfileModal(R.drawable.share_new_icon, getString(R.string.share)))
        profile_list.add(ProfileModal(R.drawable.privacy_icon, getString(R.string.privacy_policy)))
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

    private fun initView() {

        CommonUtil.themeSet(this, window)
        CommonUtil.setLightStatusBar(this)
        initialize()
        observer()
        binding.toolbar.tvTittle.text = getString(R.string.billing_account)
        binding.toolbar.ivBack.visibility = View.INVISIBLE
        binding.toolbar.ivmneu.visibility = View.VISIBLE
    }

    private fun initialize() {
        val repo = OwnerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, OwnerSideFactory(repo))[OwnerSideViewModel::class.java]
    }

    private fun billCount() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.billCountOwner(token)
    }

    private fun observer() {
        viewModel.billCountOwnerLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            binding.textView2631.text = "${it.data?.resultPaidCount ?: 0}"
                            binding.textView2641.text = "৳ ${it.data?.resultPaidSum ?: 0}"
                            binding.textView263.text = "${it.data?.resultUnPaidCount ?: 0}"
                            binding.textView264.text = "৳ ${it.data?.resultUnPaidSum ?: 0}"
                            binding.textView26311.text = "${it.data?.resultUnApprovedCount ?: 0}"
                            binding.textView26411.text = "৳ ${it.data?.resultUnApprovedSum ?: 0}"
                            binding.textView263111.text = "${it.data?.resultRentCount ?: 0}"
                            binding.textView264111.text = "৳ ${it.data?.resultRentSum ?: 0}"

                            binding.textView2632.text = "${it.data?.resultPendingCount ?: 0}"
                            binding.textView2642.text = "৳ ${it.data?.resultPendingSum ?: 0}"

                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message ?: "")
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message ?: "")
                        } else if (it.status == AppConstants.STATUS_FAILURE) {
                            this.longToast(it.message ?: "")
                        } else {
                            this.longToast(it.message ?: "")
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
        /*binding.toolbar.ivBack.setOnClickListener {
            finish()
        }*/
        binding.toolbar.ivmneu.setOnClickListener {
            binding.ownerBillingAccountDrw.openDrawer(GravityCompat.START)
        }
        binding.paidBill.setOnClickListener {
            startActivity(
                Intent(this, OwnerBillingActivity::class.java).putExtra("key", "paid")
                    .putExtra("from", "owner")
            )
        }
        binding.pendingBill.setOnClickListener {
            startActivity(
                Intent(this, OwnerBillingActivity::class.java).putExtra("key", "approve")
                    .putExtra("from", "owner")
            )
        }
        binding.pending.setOnClickListener {
            startActivity(
                Intent(this, OwnerBillingActivity::class.java).putExtra("key", "pending")
                    .putExtra("from", "owner")
            )
        }
        binding.rentBill.setOnClickListener {
            /*startActivity(
                Intent(this, OwnerBillingActivity::class.java).putExtra("key", "rent")
                    .putExtra("from", "owner")
            )*/
            startActivity(Intent(this, OwnerRentListActivity::class.java))
        }
        binding.card.setOnClickListener {
            startActivity(
                Intent(this, OwnerBillingActivity::class.java)
                    .putExtra("from", "owner")
            )
        }
    }

    override fun onResume() {
        super.onResume()
        billCount()
        closeDrawer()
    }

    override fun onClick(position: Int) {
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

    fun shareIntent(activity: Activity?, shearableLink: String?) {
        ShareCompat.IntentBuilder
            .from(activity!!)
            .setText(shearableLink)
            .setType("text/plain")
            .setChooserTitle("Share with the users")
            .startChooser()
    }

    fun closeDrawer() {
        if (binding.ownerBillingAccountDrw.isDrawerOpen(GravityCompat.START)) {
            binding.ownerBillingAccountDrw.closeDrawer(GravityCompat.START)
        }
    }
}