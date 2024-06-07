package com.application.intercom.manager.gatekeeper

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.aws.AWSUtils
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ManagerViewModel.managerSideViewModel.ManagerSideViewModel
import com.application.intercom.data.model.factory.managerFactory.managerSideFactory.ManagerSideFactory
import com.application.intercom.data.model.local.manager.ManagerAddNoticeBoardPostModel
import com.application.intercom.data.model.local.manager.gatePass.ManagerAddGatePassPostModel
import com.application.intercom.data.model.local.owner.registerComplain.OwnerRegisterComplainPostModel
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityManagerCreateGatePassBinding
import com.application.intercom.gatekepper.activity.gatePass.GatePassActivity
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.TakeImageWithCrop
import com.application.intercom.owner.adapter.PhotoUploadAdapter
import com.application.intercom.owner.adapter.SpinnerAdapterBrands
import com.application.intercom.utils.*
import com.catalyist.aws.AWSListner

class ManagerCreateGatePassActivity : AppCompatActivity(), AWSListner {
    lateinit var binding: ActivityManagerCreateGatePassBinding
    private lateinit var viewModel: ManagerSideViewModel
    private var flatOfBuildingList = ArrayList<String>()
    private var flatOfBuildingHashMapID: HashMap<String, String> = HashMap()
    private var flatOfBuildingId: String = ""
    private var imagePath: String = ""
    private var photoAdapter: PhotoUploadAdapter? = null
    private var photo_upload_list = ArrayList<String>()
    val TIME_DIALOG_ID = 1111
    private var hour = 0
    private var minute = 0
    var giventime: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerCreateGatePassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        lstnr()
    }

    private fun initView() {
        flatOfBuildingList.add(0, "Choose Flat")
        initialize()
        observer()
        flatOfBuilding()
        binding.createToolbar.tvCreateGatePass.visibility = View.VISIBLE
        binding.createToolbar.tvTittle.text = "Create Gatepass"
        binding.commonBtn.tv.text = "Create Gatepass"

        binding.chooseSpiner.adapter =
            ArrayAdapter(this, R.layout.spinner_dropdown_item, flatOfBuildingList)
        binding.chooseSpiner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                flatOfBuildingId =
                    flatOfBuildingHashMapID.get(binding.chooseSpiner.selectedItem.toString())
                        .toString()
                println("---memberId$flatOfBuildingId")
                if (binding.chooseSpiner.selectedItemPosition > 0) {
                    binding.mainNew.visibility = View.VISIBLE
                } else {
                    binding.mainNew.visibility = View.GONE
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    /* private fun spinnerForSubCategoryList(memberList: List<String>) {
         val adapter =
             SpinnerAdapterBrands(
                 this,
                 R.layout.spinner_dropdown_item, memberList
             )
         binding.spinner.adapter = adapter
         binding.spinner.onItemSelectedListener = onItemSelectedStateListenerMemberList

     }

     private var onItemSelectedStateListenerMemberList: AdapterView.OnItemSelectedListener = object :
         AdapterView.OnItemSelectedListener {
         override fun onItemSelected(
             adapterView: AdapterView<*>?,
             view: View,
             postion: Int,
             l: Long
         ) {
             val itemSelected = adapterView!!.getItemAtPosition(postion) as String
             binding.edService.text = itemSelected

             serviceCategoryId =
                 serviceCategoryHashMapID.get(binding.spinner.selectedItem.toString()).toString()
             println("---memberId$serviceCategoryId")

         }

         override fun onNothingSelected(adapterView: AdapterView<*>?) {}
     }*/
    private fun initialize() {
        val repo = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo))[ManagerSideViewModel::class.java]

    }

    private fun flatOfBuilding() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.flatOfBuildingList(token)
    }

    private fun addGatePass() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            GPSService.mLastLocation!!.latitude.toString()
        )
        val model = ManagerAddGatePassPostModel(
            binding.edActivityName.text.trim().toString(),
            binding.edWriteDescription.text.trim().toString(),
            giventime.toString(),
            flatOfBuildingId,
            binding.edPhoneNumber.text.trim().toString(),
            photo_upload_list


        )
        viewModel.addGatePass(token, model)
    }

    private fun observer() {
        viewModel.flatOfBuildingListLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            it.data.result.forEach {
                                flatOfBuildingList.add(it.name)
                                flatOfBuildingHashMapID.put(it.name, it._id)
                            }
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
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
        viewModel.addGatePassLiveData.observe(this, androidx.lifecycle.Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            finish()
                        } else if (it.status == AppConstants.STATUS_500) {
                            this.longToast(it.message)
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

    private fun showImagePickDialog() {
        val dialog: AlertDialog.Builder = AlertDialog.Builder(this)
        dialog.setMessage("Choose image")
        dialog.setPositiveButton(
            "Gallery"
        ) { _, _ ->
            val intent = Intent(this, TakeImageWithCrop::class.java)
            intent.putExtra("from", "gallery")
            startActivityForResult(intent, TakeImageWithCrop.GALLERY_REQUEST)
        }
        dialog.setNegativeButton(
            "Camera"
        ) { _, _ ->


            val intent = Intent(this, TakeImageWithCrop::class.java)
            intent.putExtra("from", "camera")
            startActivityForResult(intent, TakeImageWithCrop.CAMERA_REQUEST)


        }
        dialog.setNeutralButton(
            "Cancel"
        ) { dialog, which -> dialog.dismiss() }
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val path = data?.getStringExtra("filePath")
        if (resultCode == RESULT_OK) {
            if (!path.isNullOrEmpty()) {
                AWSUtils(
                    this, path, this
                )
            }
        } else if (requestCode == TakeImageWithCrop.GALLERY_REQUEST) {
            if (!path.isNullOrEmpty()) {
                AWSUtils(
                    this, path, this
                )
            }
        }
    }

    override fun onCreateDialog(id: Int): Dialog? {
        when (id) {
            TIME_DIALOG_ID ->
                // set time picker as current time
                return TimePickerDialog(
                    this, timePickerListener, hour, minute, false
                )
        }
        return null
    }

    private val timePickerListener =
        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minutes -> // TODO Auto-generated method stub
            hour = hourOfDay
            minute = minutes
            updateTime(hour, minute)
        }

    private fun updateTime(hours: Int, mins: Int) {
        var hours = hours
        var timeSet = ""
        if (hours > 12) {
            hours -= 12
            timeSet = "PM"
        } else if (hours == 0) {
            hours += 12
            timeSet = "AM"
        } else if (hours == 12) timeSet = "PM" else timeSet = "AM"
        var minutes = ""
        minutes = if (mins < 10) "0$mins" else mins.toString()

        // Append in a StringBuilder
        val aTime =
            StringBuilder().append(hours).append(':').append(minutes).append(" ").append(timeSet)
                .toString()

        giventime = aTime
        binding.tvTime.setText(giventime)
        println("----time$giventime")

    }

    private fun lstnr() {
        binding.createToolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.commonBtn.tv.setOnClickListener {
            startActivity(Intent(this, GatePassActivity::class.java))
        }
        binding.createToolbar.tvCreateGatePass.setOnClickListener {
            startActivity(Intent(this, ManagerGatePassHistoryActivity::class.java))
        }
        binding.imageView16.setOnClickListener {
            showImagePickDialog()
        }
        binding.tvTime.setOnClickListener {
            showDialog(TIME_DIALOG_ID)
        }
        binding.commonBtn.tv.setOnClickListener {
            if (!validationData()) {
                return@setOnClickListener
            }
            addGatePass()
        }
    }

    private fun validationData(): Boolean {
        if (binding.edActivityName.text.trim().toString().length < 4) {
            Toast.makeText(
                applicationContext, getString(R.string.please_enter_name),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edWriteDescription.text?.trim().toString())
        ) {
            Toast.makeText(
                applicationContext,
                getString(R.string.please_enter_description),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.tvTime.text?.trim().toString())
        ) {
            Toast.makeText(
                applicationContext,
                getString(R.string.please_enter_time),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edPhoneNumber.text!!.trim().toString())) {
            Toast.makeText(
                applicationContext,
                getString(R.string.please_enter_phone_number),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.edPhoneNumber.text!!.trim()
                .toString().length < 10 || binding.edPhoneNumber.text!!.trim()
                .toString().length > 12
        ) {
            Toast.makeText(
                applicationContext,
                getString(R.string.please_enter_valid_phone_number),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true

    }

    override fun onAWSLoader(isLoader: Boolean) {
        binding.pro.visibility = View.VISIBLE
    }

    override fun onAWSSuccess(url: String?) {
        if (url != null) {
            imagePath = url!!
            binding.rcyPhoto.visibility = View.VISIBLE
            binding.pro.visibility = View.GONE
            photo_upload_list.add(imagePath)
            println("-----photo${photo_upload_list}")
            binding.rcyPhoto.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            photoAdapter = PhotoUploadAdapter(this, photo_upload_list)
            binding.rcyPhoto.adapter = photoAdapter
            photoAdapter!!.notifyDataSetChanged()

        }
    }

    override fun onAWSError(error: String?) {
        binding.pro.visibility = View.GONE
        binding.rcyPhoto.visibility = View.INVISIBLE
        Log.e("error", error ?: "")
    }

    override fun onAWSProgress(progress: Int?) {
        binding.pro.visibility = View.VISIBLE
        binding.rcyPhoto.visibility = View.VISIBLE
        Log.e("progress", progress!!.toString())
    }
}