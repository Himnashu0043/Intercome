package com.application.intercom.user.newflow.steps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.getUserDetailsViewModel.GetUserDetailsViewModel
import com.application.intercom.data.model.factory.getUserDetailsFactory.GetUserDetailsFactory
import com.application.intercom.data.model.remote.newUser.MyList.ActiveNewPhaseList
import com.application.intercom.data.model.remote.newUser.NewUserAmenitiesList
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ServiceRepository
import com.application.intercom.data.repository.UserHomeRepository
import com.application.intercom.data.repository.getUserDetailsRepo.GetUserDetailsRepo
import com.application.intercom.databinding.ActivitySteps2Binding
import com.application.intercom.user.home.UserHomeFactory
import com.application.intercom.user.home.UserHomeViewModel
import com.application.intercom.user.home.UserTopAdvitAdapter
import com.application.intercom.user.login.LoginUsingOtpActivity
import com.application.intercom.user.newflow.adapter.FeatureAdapter
import com.application.intercom.user.newflow.modal.NameModel
import com.application.intercom.user.newflow.modal.UserTestAmenitiesModel
import com.application.intercom.user.newflow.modal.UserTestPostModel
import com.application.intercom.user.service.ServiceFactory
import com.application.intercom.user.service.ServiceViewModel
import com.application.intercom.utils.*
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList

class Steps2Activity : AppCompatActivity(), FeatureAdapter.AmenitiesCLick {
    lateinit var binding: ActivitySteps2Binding
    private var adptr: FeatureAdapter? = null
    private var list = ArrayList<UserTestPostModel>()
    private var sendlist = ArrayList<UserTestPostModel>()
    private lateinit var viewModel: UserHomeViewModel
    private var amenities = ArrayList<NewUserAmenitiesList.Data>()
    private var bedcount: Int = 1
    private var bathcount: Int = 1
    var aminet = ArrayList<UserTestPostModel.Amentity>()
    private var editlist: ActiveNewPhaseList.Data? = null
    private var editFrom: String = ""
    private var nameList = ArrayList<String>()
    private var editId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySteps2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        editFrom = intent.getStringExtra("editFrom").toString()
        editId = intent.getStringExtra("edit_id").toString()
        println("--id$editId")
        if (editFrom.equals("editData")) {
            list = intent.getSerializableExtra("testList") as ArrayList<UserTestPostModel>
            editlist = intent.getSerializableExtra("editList") as ActiveNewPhaseList.Data?
            println("-----ediitst$list")

            binding.edtLoc.setText(editlist!!.price.toString())
            binding.edtArea.setText(editlist!!.sqft.toString())
            binding.tvData.text = editlist!!.bedroom.toString()
            binding.tvData1.text = editlist!!.bathroom.toString()
            for (name1 in editlist!!.amentities) {
                nameList.add(name1.name)
            }
            println("----name$nameList")

        } else {
            list = intent.getSerializableExtra("testList") as ArrayList<UserTestPostModel>
            println("---test$list")
        }

        initView()
        lstnr()
    }

    fun setOldData() {
        val sizzz = list.size
        for (j in 0..sizzz) {
            for (i in nameList) {
                if (i.equals(amenities[j].name)) {
                    amenities[j].isSelect = true
                    aminet.add(
                        UserTestPostModel.Amentity(
                            amenities.get(j).image,
                            amenities.get(j).name
                        )
                    )
                }
            }

        }
        adptr!!.notifyDataSetChanged()
    }

    private fun initView() {
        initialize()
        observer()
        userNewAmenities()
        binding.commonBtn.tv.text = getString(R.string.next_step)
        binding.toolbar.tvTittle.text = getString(R.string.step_2_3)

    }

    private fun initialize() {
        val repo = UserHomeRepository(BaseApplication.apiService)
        viewModel = ViewModelProvider(this, UserHomeFactory(repo))[UserHomeViewModel::class.java]


    }

    private fun userNewAmenities() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        viewModel.userNewAmenities(token)

    }

    private fun observer() {
        viewModel.userNewAmenitiesLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            amenities.clear()
                            amenities.addAll(it.data)
                            binding.rcyFeature.layoutManager = GridLayoutManager(this, 3)
                            adptr = FeatureAdapter(
                                this, amenities, this, nameList
                            )
                            binding.rcyFeature.adapter = adptr
                            setOldData()

                            adptr!!.notifyDataSetChanged()
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
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.commonBtn.tv.setOnClickListener {
            if (validationData()) {
                if (editFrom.equals("editData")) {
                    var sqr = binding.edtArea.text.trim().toString()
                    var price = binding.edtLoc.text.trim().toString()
                    sendlist.add(
                        UserTestPostModel(
                            null,
                            list.get(0).address,
                            aminet,
                            bathcount,
                            bedcount,
                            null,
                            list.get(0).district,
                            list.get(0).division,
                            list.get(0).flatStatus,
                            list.get(0).floorLevel,
                            list.get(0).lat,
                            list.get(0).long,
                            null,
                            "",
                            "",
                            price.toInt(),
                            list.get(0).propertyType,
                            sqr.toInt(),
                            list.get(0).subPropertyType,
                            "",
                            list.get(0).totalFloor
                        )
                    )

                    startActivity(
                        Intent(this, Steps3Activity::class.java).putExtra(
                            "testList",
                            sendlist
                        ).putExtra("editFrom", "editData").putExtra("editList", editlist).putExtra("edit_id",editId)
                    )
                    println("-----ssss$sendlist")
                } else {

                    var sqr = binding.edtArea.text.trim().toString()
                    var price = binding.edtLoc.text.trim().toString()
                    sendlist.add(
                        UserTestPostModel(
                            null,
                            list.get(0).address,
                            aminet,
                            bathcount,
                            bedcount,
                            null,
                            list.get(0).district,
                            list.get(0).division,
                            list.get(0).flatStatus,
                            list.get(0).floorLevel,
                            list.get(0).lat,
                            list.get(0).long,
                            null,
                            "",
                            "",
                            price.toInt(),
                            list.get(0).propertyType,
                            sqr.toInt(),
                            list.get(0).subPropertyType,
                            "",
                            list.get(0).totalFloor
                        )
                    )

                    startActivity(
                        Intent(this, Steps3Activity::class.java).putExtra(
                            "testList",
                            sendlist
                        )
                    )
                }
            }

        }
        binding.tvPlus.setOnClickListener {
            ++bedcount
            binding.tvData.text = "$bedcount"
            binding.tvSub.visibility = View.VISIBLE
        }
        binding.tvSub.setOnClickListener {
            if (bedcount > 1) {
                --bedcount
                binding.tvSub.visibility = View.VISIBLE
            } else {
                binding.tvSub.visibility = View.INVISIBLE
            }
            binding.tvData.text = "${bedcount}"
        }
        binding.tvPlus1.setOnClickListener {
            ++bathcount
            binding.tvData1.text = "$bathcount"
            binding.tvSub1.visibility = View.VISIBLE
        }
        binding.tvSub1.setOnClickListener {
            if (bathcount > 1) {
                --bathcount
                binding.tvSub1.visibility = View.VISIBLE
            } else {
                binding.tvSub1.visibility = View.INVISIBLE
            }
            binding.tvData1.text = "${bathcount}"

        }

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onClick(
        position: Int,
        selectedStatus: Boolean,
        object1: UserTestPostModel.Amentity
    ) {

        amenities.get(position).isSelect = selectedStatus
        adptr!!.notifyDataSetChanged()
        if (selectedStatus) {
            aminet.add(
                UserTestPostModel.Amentity(
                    amenities.get(position).image,
                    amenities.get(position).name
                )
            )
        } else {

            aminet.remove(object1)

        }


    }

    private fun validationData(): Boolean {
        if (TextUtils.isEmpty(binding.edtLoc.text!!.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Enter Price!!", Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edtArea.text!!.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Enter Property Area !!", Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true

    }
}