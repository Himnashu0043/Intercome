package com.application.intercom.gatekepper.activity.createGatePass

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.aws.AWSUtils
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.gateKeeperViewModel.GateKeeperHomeViewModel
import com.application.intercom.data.model.factory.gateKeeperFactory.GateKeeperFactory
import com.application.intercom.data.model.local.gateKeeper.AddGatePassPostModel
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.gateKeeperRepo.GateKeeperHomeRepo
import com.application.intercom.databinding.ActivityCreateGatePassBinding
import com.application.intercom.gatekepper.activity.gatePass.GatePassActivity
import com.application.intercom.gatekepper.activity.gatepassHistory.GatePassHistoryActivity
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.TakeImageWithCrop
import com.application.intercom.helper.getCurrentDate
import com.application.intercom.owner.adapter.PhotoUploadAdapter
import com.application.intercom.owner.adapter.ShowImgAdapter
import com.application.intercom.utils.*
import com.catalyist.aws.AWSListner

class CreateGatePassActivity : AppCompatActivity(), AWSListner {
    lateinit var binding: ActivityCreateGatePassBinding
    private lateinit var viewModel: GateKeeperHomeViewModel
    private var flatOfBuildingList = ArrayList<String>()
    private var flatOfBuildingHashMapID: HashMap<String, String> = HashMap()
    private var flatOfBuildingBuildingHashMapID: HashMap<String, String> = HashMap()
    private var flatOfBuildingMobileNumberHashMapID: HashMap<String, String> = HashMap()
    private var flatOfBuildingId: String = ""
    private var showphotoAdapter: ShowImgAdapter? = null
    private var imagePath: String = ""
    private var photoAdapter: PhotoUploadAdapter? = null
    private var photo_upload_list = ArrayList<String>()
    val TIME_DIALOG_ID = 1111
    private var hour = 0
    private var minute = 0
    var giventime: String? = ""
    private var type: String = ""
    private var doc_img: String = ""
    private var mobileOwnerNumber: String = ""
    private var mobileNumber: String = ""
    private var flatName: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateGatePassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        lstnr()
    }

    private fun initView() {
        flatOfBuildingList.add(0, "Choose Flat")
        initialize()
        observer()
        flatOfBuilding()
        binding.createToolbar.tvTittle.text = "Create Gatepass"
        binding.commonBtn.tv.text = "Create Gatepass"
        // val genderList = resources.getStringArray(com.application.intercom.R.array.EditProfile)
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
                mobileOwnerNumber =
                    flatOfBuildingMobileNumberHashMapID.get(binding.chooseSpiner.selectedItem.toString())
                        .toString()
                println("---flateId$flatOfBuildingId")
                println("---mobileNumber$mobileOwnerNumber")
                if (binding.chooseSpiner.selectedItemPosition > 0) {
                    binding.mainNew.visibility = View.VISIBLE
                    flatName = binding.chooseSpiner.selectedItem.toString()
                } else {
                    binding.mainNew.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initialize() {
        val repo = GateKeeperHomeRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, GateKeeperFactory(repo))[GateKeeperHomeViewModel::class.java]

    }

    private fun flatOfBuilding() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        viewModel.flatOfBuildingList(token)
    }

    private fun addGatePass() {
        val token = prefs.getString(
            SessionConstants.TOKEN, GPSService.mLastLocation?.latitude.toString()
        )
        val model = AddGatePassPostModel(
            binding.edActivityName.text.trim().toString(),
            binding.edContact.text.trim().toString(),
            binding.edWriteDescription.text.trim().toString(),
            binding.edEntryTime.text.trim().toString(),
            flatOfBuildingId,
            imagePath,
            binding.edContactPhoneName.text.trim().toString(),
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
                                flatOfBuildingBuildingHashMapID.put(it.name, it.buildingId)
                                flatOfBuildingMobileNumberHashMapID.put(
                                    it.name, it.ownerInfo.get(0).phoneNumber
                                )
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
                            startActivity(Intent(this, GatePassHistoryActivity::class.java))
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

    private fun lstnr() {
        binding.createToolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.commonBtn.tv.setOnClickListener {
            if (!validationData()) {
                return@setOnClickListener
            }
            notifyPopup()
        }
        binding.edEntryTime.setOnClickListener {
            showDialog(TIME_DIALOG_ID)
        }
        binding.imageView16111.setOnClickListener {
            type = "doc_img"
            showImagePickDialog()
        }
        binding.profileImg.setOnClickListener {
            type = "img"
            showImagePickDialog()
        }
    }

    private fun notifyPopup() {
        val dialog = this.let { Dialog(this) }

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.gate_pass_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        val tvNotity = dialog.findViewById<TextView>(R.id.tvgatepssnotify)
        val tvName = dialog.findViewById<TextView>(R.id.textView166)
        val tvflat = dialog.findViewById<TextView>(R.id.textView167)
        val tvDelivery = dialog.findViewById<TextView>(R.id.textView168)
        val tvphone = dialog.findViewById<TextView>(R.id.textView170)
        val tvOwnerphone = dialog.findViewById<TextView>(R.id.textView1722)
        val tvDate = dialog.findViewById<TextView>(R.id.textView172)
        val tvTime = dialog.findViewById<TextView>(R.id.tvInTime)
        val tvNote = dialog.findViewById<TextView>(R.id.tvNote)
        val tvRecy = dialog.findViewById<RecyclerView>(R.id.rcyPhoto)
        val tvimg = dialog.findViewById<ImageView>(R.id.imageView91)
        val tvCalling = dialog.findViewById<ImageView>(R.id.imageView97)
        val tvOwnerCalling = dialog.findViewById<ImageView>(R.id.imageView98)
        val date = getCurrentDate()
        tvName.text = binding.edContact.text.trim().toString()
        tvphone.text = binding.edContactPhoneName.text.trim().toString()
        mobileNumber = binding.edContactPhoneName.text.trim().toString()
        tvTime.text = binding.edEntryTime.text.trim().toString()
        tvNote.text = binding.edWriteDescription.text.trim().toString()
        tvDelivery.text = binding.edActivityName.text.trim().toString()
        tvOwnerphone.text = mobileOwnerNumber
        tvDate.text = date
        tvflat.text = flatName
        tvCalling.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${mobileNumber}")
            startActivity(intent)
        }
        tvOwnerCalling.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${mobileOwnerNumber}")
            startActivity(intent)
        }
        tvimg.loadImagesWithGlideExt(imagePath)
        tvRecy.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        showphotoAdapter = ShowImgAdapter(this, photo_upload_list)
        tvRecy.adapter = showphotoAdapter
        showphotoAdapter!!.notifyDataSetChanged()
        tvNotity.setOnClickListener {
            dialog.dismiss()
            addGatePass()
//            startActivity(Intent(this, GatePassHistoryActivity::class.java))


        }
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT
            )
        }

    }

    private fun validationData(): Boolean {
        if (binding.edContact.text.trim().toString().length < 4) {
            Toast.makeText(
                applicationContext, "Please Enter Name!!", Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edContactPhoneName.text!!.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Enter Phone Number !!", Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.edContactPhoneName.text!!.trim()
                .toString().length < 10 || binding.edContactPhoneName.text!!.trim()
                .toString().length > 12
        ) {
            Toast.makeText(
                applicationContext, "Please Enter Valid Phone Number !!", Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edActivityName.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Enter Activity Name!!", Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edWriteDescription.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Enter Note!!", Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edEntryTime.text?.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Enter Entry Time!!", Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return true

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
        binding.edEntryTime.setText(giventime)
        println("----time$giventime")

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
        if (requestCode == TakeImageWithCrop.CAMERA_REQUEST) {
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

    override fun onAWSLoader(isLoader: Boolean) {
        binding.pro.visibility = View.VISIBLE

    }

    override fun onAWSSuccess(url: String?) {
        if (url != null) {
            imagePath = url!!
            if (type.equals("img")) {
                println("---imagePath${imagePath}")
                binding.pro.visibility = View.GONE
                binding.rcyPhoto.visibility = View.GONE
                binding.profileImg.loadImagesWithGlideExt(imagePath)
            } else if (type.equals("doc_img")) {
                doc_img = url
                binding.rcyPhoto.visibility = View.VISIBLE
                binding.pro.visibility = View.GONE
                photo_upload_list.add(doc_img)
                println("-----photo${photo_upload_list}")
                binding.rcyPhoto.layoutManager =
                    LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
                photoAdapter = PhotoUploadAdapter(this, photo_upload_list)
                binding.rcyPhoto.adapter = photoAdapter
                photoAdapter!!.notifyDataSetChanged()
            }


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