package com.application.intercom.tenant.activity.registerComplain

import android.app.Activity
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
import com.application.intercom.databinding.ActivityTenantRegisterComplainBinding
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
import com.application.intercom.owner.activity.properties.OwnerPropertiesActivity
import com.application.intercom.owner.activity.registerComplain.OwnerAddRegisterComplainActivity
import com.application.intercom.owner.activity.registerComplain.PendingComplainOwnerTenantFragment
import com.application.intercom.owner.activity.registerComplain.ResolvedComplainOwnerTenantFragment
import com.application.intercom.tenant.Model.ProfileModal
import com.application.intercom.tenant.activity.MyCommunity.TenantMyCommunityActivity
import com.application.intercom.tenant.activity.billing.TenantBillingsActivity
import com.application.intercom.tenant.activity.main.TenantMainActivity
import com.application.intercom.tenant.activity.noticBoard.TenantNoticBoardActivity
import com.application.intercom.tenant.activity.setting.TenantSettingActivity
import com.application.intercom.tenant.adapter.Profile.ProfileAdapter
import com.application.intercom.user.aboutapp.AboutUsActivity
import com.application.intercom.user.aboutapp.PrivacyPolicyActivity
import com.application.intercom.user.aboutapp.TermsOfServiceActivity
import com.application.intercom.utils.SessionConstants
import com.google.android.material.tabs.TabLayoutMediator

class TenantRegisterComplainActivity : BaseActivity<ActivityTenantRegisterComplainBinding>(),
    ProfileAdapter.ProfileClick {

    /////side menu
    private var profile_list = java.util.ArrayList<ProfileModal>()
    private var profileAdapter: ProfileAdapter? = null
    /////side menu


    override fun getLayout(): ActivityTenantRegisterComplainBinding {
        return ActivityTenantRegisterComplainBinding.inflate(layoutInflater)
    }


    private var from: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        from = intent.getStringExtra("from") ?: ""
        println("----listComplain$from")
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
            profile_list.add(ProfileModal(R.drawable.fav_icon, getString(R.string.favorites)))
            profile_list.add(
                ProfileModal(
                    R.drawable.community_icon,
                    getString(R.string.my_community)
                )
            )
            profile_list.add(ProfileModal(R.drawable.billing_icon, getString(R.string.complain)))
            /*profile_list.add(
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
        binding.regiterToolbar.ivBack.visibility = View.INVISIBLE
        binding.regiterToolbar.ivmneu.visibility = View.VISIBLE
        binding.regiterToolbar.tvText.visibility = View.VISIBLE
        binding.regiterToolbar.tvText.text = "Register"
        binding.regiterToolbar.tvText.textSize = 12f
        binding.viewPager.adapter = ScreenSlidePagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.pending)

                }
                1 -> {
                    tab.text = getString(R.string.resolved)
                }
            }
        }.attach()
        /*initialize()
        observer()
        if (from == "tenant") {
            tenantComplainList()
        } else {
            registerComplainList()
        }*/

        binding.regiterToolbar.tvTittle.text = getString(R.string.complain_history)


    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return PendingComplainOwnerTenantFragment(from)

                }
                1 -> {
                    return ResolvedComplainOwnerTenantFragment(from)
                }
            }
            return PendingComplainOwnerTenantFragment(from)
        }
    }
    /*  private fun initialize() {
          val repo = OwnerSideRepo(BaseApplication.apiService)
          viewModel = ViewModelProvider(
              this,
              OwnerSideFactory(repo)
          )[OwnerSideViewModel::class.java]

          val tenantrepo = TenantSideRepo(BaseApplication.apiService)
          tenant_viewModel = ViewModelProvider(
              this,
              TenantSideFactory(tenantrepo)
          )[TenantSideViewModel::class.java]


      }

      private fun registerComplainList() {
          val token = prefs.getString(
              SessionConstants.TOKEN,
              GPSService.mLastLocation?.latitude.toString()
          )
          viewModel.listregisterComplainOwner(token)

      }

      private fun actionToComplain() {
          val token = prefs.getString(
              SessionConstants.TOKEN,
              GPSService.mLastLocation?.latitude.toString()
          )
          val model = OwnerActionToComplainPostModel(comId, "Confirm", "")
          viewModel.actionToComplainOwner(token, model)

      }

      private fun denyactionToComplain() {
          val token = prefs.getString(
              SessionConstants.TOKEN,
              GPSService.mLastLocation?.latitude.toString()
          )
          val model = OwnerActionToComplainPostModel(comId, "Deny", editData)
          viewModel.actionToComplainOwner(token, model)

      }

      private fun tenantComplainList() {
          val token = prefs.getString(
              SessionConstants.TOKEN,
              GPSService.mLastLocation?.latitude.toString()
          )
          tenant_viewModel.tenantComplainList(token)

      }

      private fun observer() {
          viewModel.listregisterComplainOwnerLiveData.observe(this, Observer {
              when (it) {
                  is EmpResource.Loading -> {
                      EmpCustomLoader.showLoader(this)
                  }

                  is EmpResource.Success -> {
                      EmpCustomLoader.hideLoader()
                      it.value.let {
                          if (it.status == AppConstants.STATUS_SUCCESS) {
                              list.clear()
                              list.addAll(it.data.result)
                              binding.rcyRegister.layoutManager = LinearLayoutManager(this)
                              adpter =
                                  TenantRegisterComplainAdapter(
                                      this,
                                      this,
                                      list,
                                      tenantlist,
                                      from,
                                      this
                                  )
                              binding.rcyRegister.adapter = adpter
                              adpter!!.notifyDataSetChanged()
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
          tenant_viewModel.tenantComplainListLiveData.observe(this, Observer {
              when (it) {
                  is EmpResource.Loading -> {
                      EmpCustomLoader.showLoader(this)
                  }

                  is EmpResource.Success -> {
                      EmpCustomLoader.hideLoader()
                      it.value.let {
                          if (it.status == AppConstants.STATUS_SUCCESS) {
                              tenantlist.clear()
                              tenantlist.addAll(it.data.result)
                              binding.rcyRegister.layoutManager = LinearLayoutManager(this)
                              adpter =
                                  TenantRegisterComplainAdapter(this, this, list, tenantlist, from,this)
                              binding.rcyRegister.adapter = adpter
                              adpter!!.notifyDataSetChanged()
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
          viewModel.actionToComplainOwnerLiveData.observe(this, Observer {
              when (it) {
                  is EmpResource.Loading -> {
                      EmpCustomLoader.showLoader(this)
                  }

                  is EmpResource.Success -> {
                      EmpCustomLoader.hideLoader()
                      it.value.let {
                          if (it.status == AppConstants.STATUS_SUCCESS) {
                              this.longToast(it.message)
                              finish()
                              if (prefs.getString(
                                      SessionConstants.NOTYTYPE,
                                      ""
                                  ) == "NEW_COMPLAIN_RESOLVE"
                              ) {
                                  prefs.put(SessionConstants.NOTYTYPE, "")
                                  if (from == "owner") {
                                      startActivity(Intent(this, OwnerMainActivity::class.java))
                                  } else {
                                      startActivity(Intent(this, TenantMainActivity::class.java))
                                  }
                              }


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
        binding.regiterToolbar.tvText.setOnClickListener {
            if (from == "owner") {
                startActivity(Intent(this, OwnerAddRegisterComplainActivity::class.java))
//                finish()
            } else {
                startActivity(Intent(this, TenantAddRegisterComplainActivity::class.java))
//                finish()
            }
        }
//        binding.regiterToolbar.ivBack.setOnClickListener {
//            finish()
//            if (prefs.getString(
//                    SessionConstants.NOTYTYPE,
//                    ""
//                ) == "NEW_COMPLAIN_RESOLVE"
//            ) {
//                prefs.put(SessionConstants.NOTYTYPE, "")
//                if (from == "owner") {
//                    startActivity(Intent(this, OwnerMainActivity::class.java))
//                } else {
//                    startActivity(Intent(this, TenantMainActivity::class.java))
//                }
//            }
//        }
        binding.regiterToolbar.ivmneu.setOnClickListener {
            binding.ownerComplainDrw.openDrawer(GravityCompat.START)
        }
    }

    /*override fun onClick(id: String, position: Int) {
        comId = id
        println("----id$comId")
        complainPopup()

    }*/

    /*  override fun onDenyClcik(id: String, position: Int) {
          comId = id
          reasonPopup()
      }*/

    /*private fun complainPopup() {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.tenant_complain_resolve_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val resolve = dialog.findViewById<TextView>(R.id.tvComplainresolve)
        val reject = dialog.findViewById<TextView>(R.id.tvReject)
        resolve.setOnClickListener {
            dialog.dismiss()
            actionToComplain()
        }
        reject.setOnClickListener {
            dialog.dismiss()
        }
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    private fun reasonPopup() {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.reason_deny_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val submit = dialog.findViewById<TextView>(R.id.tvsubmit)
        val edit = dialog.findViewById<EditText>(R.id.ed)
         edit.setOnClickListener {
             editData = edit.text.toString()
            println("----data$editData")
        }
        submit.setOnClickListener {
            dialog.dismiss()
            editData = edit.text.toString()
            println("----sndsj$editData")
            denyactionToComplain()
        }
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    private fun dialogshowImg() {
        val dialog = this.let { Dialog(this) }
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
        dialogshowImg()
    }*/

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
        if (binding.ownerComplainDrw.isDrawerOpen(GravityCompat.START)) {
            binding.ownerComplainDrw.closeDrawer(GravityCompat.START)
        }
    }

    override fun onResume() {
        super.onResume()
        closeDrawer()
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