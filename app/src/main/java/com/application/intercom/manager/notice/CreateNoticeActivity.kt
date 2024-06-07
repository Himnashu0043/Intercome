package com.application.intercom.manager.notice

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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.allViews
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
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.managerRepo.managerSideRepo.ManagerSideRepo
import com.application.intercom.databinding.ActivityCreateNoticeBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.TakeImageWithCrop
import com.application.intercom.manager.dialog.NoticeDateAndTimeDialog
import com.application.intercom.owner.adapter.PhotoUploadAdapter
import com.application.intercom.utils.*
import com.catalyist.aws.AWSListner
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateNoticeActivity : AppCompatActivity(), AWSListner {

    private lateinit var binding: ActivityCreateNoticeBinding
    private var imagePath: String = ""
    private var photoAdapter: PhotoUploadAdapter? = null
    private var photo_upload_list = ArrayList<String>()
    private var user_type: String = "all"
    var selectDate: String = ""

    var date_time: String = ""
    val TIME_DIALOG_ID = 1111
    private var hour = 0
    private var minute = 0
    var giventime: String? = ""
    lateinit var time: TextView
    private lateinit var viewModel: ManagerSideViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        listener()
    }

    private fun initView() {
        initialize()
        observer()

        binding.toolbar.tvTittle.text = getString(R.string.create_notice)
    }

    private fun initialize() {
        val repo = ManagerSideRepo(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, ManagerSideFactory(repo))[ManagerSideViewModel::class.java]

    }

    private fun addNoticeBoard() {
        val token = prefs.getString(
            SessionConstants.TOKEN, ""
        )
        val model = ManagerAddNoticeBoardPostModel(
            binding.edtDesc.text.trim().toString(),
            null,
            photo_upload_list,
            false,
            binding.edtNoticeName.text.trim().toString(),
            user_type
        )
        viewModel.addnoticeBoard(token, model)
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
        time.setText(giventime)
        println("----time$giventime")
    }

    private fun observer() {
        viewModel.addNoticeBoardLiveData.observe(this, androidx.lifecycle.Observer {
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

    private fun schedulePopup() {
        val dialog = this.let { Dialog(this) }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.schedule_popup)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
        val tvDate = dialog.findViewById<TextView>(R.id.edt_date)
        time = dialog.findViewById<TextView>(R.id.edt_time)
        val tvBtn = dialog.findViewById<TextView>(R.id.tvschedule)
        tvBtn.setText(getString(R.string.schedule))
        tvDate.setOnClickListener {
            MaterialDatePicker.Builder.datePicker().setSelection(Date().time).build()
                .apply {
                    show(supportFragmentManager, this@CreateNoticeActivity.toString())
                    addOnPositiveButtonClickListener {
                        selectDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                            Date(it)
                        )
                        println("----date$selectDate")
                        tvDate.setText(
                            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(
                                Date(it)
                            )
                        )
                    }
                }
        }
        tvBtn.setOnClickListener {
            dialog.dismiss()
        }
        time.setOnClickListener {
            showDialog(TIME_DIALOG_ID)
        }
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun listener() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.btnPostLater.setOnClickListener {
            schedulePopup()
            /*NoticeDateAndTimeDialog.newInstance(
                getString(R.string.tv_register_member),
                getString(R.string.app_name)
            )
                .show(supportFragmentManager, NoticeDateAndTimeDialog.TAG)*/

        }
        binding.layoutUploadPhoto.setOnClickListener {
            showImagePickDialog()
        }
        binding.btnPostNow.setOnClickListener {
            if (!validationData()) {
                return@setOnClickListener
            }
            addNoticeBoard()
        }
        binding.all.setOnClickListener {
            user_type = "all"
        }
        binding.tenant.setOnClickListener {
            user_type = "tenant"
        }
        binding.owner.setOnClickListener {
            user_type = "owner"
        }
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
    private var currentImagePath: Uri = Uri.EMPTY
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val path = data?.getStringExtra("filePath")
        if (resultCode == RESULT_OK) {
            if (!path.isNullOrEmpty()) {
                currentImagePath = Uri.parse(path)
                EmpCustomLoader.showLoader(this)
                AWSUtils(
                    this, path, this
                )
            }
        } else if (requestCode == TakeImageWithCrop.GALLERY_REQUEST) {
            if (!path.isNullOrEmpty()) {
                currentImagePath = Uri.parse(path)
                EmpCustomLoader.showLoader(this)
                AWSUtils(
                    this, path, this
                )
            }
        }
    }

    override fun onAWSLoader(isLoader: Boolean) {
        EmpCustomLoader.showLoader(this)
    }

    override fun onAWSSuccess(url: String?) {
        if (url != null) {
            EmpCustomLoader.hideLoader()
            imagePath = url!!
            binding.rcyPhoto.visibility = View.VISIBLE
            binding.pro.visibility = View.GONE
            photo_upload_list.add(currentImagePath.toString())
            println("-----photo${photo_upload_list}")
            binding.rcyPhoto.layoutManager =
                LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            photoAdapter = PhotoUploadAdapter(this, photo_upload_list)
            binding.rcyPhoto.adapter = photoAdapter
            photoAdapter!!.notifyDataSetChanged()

        }
    }

    override fun onAWSError(error: String?) {
        EmpCustomLoader.hideLoader()
        binding.rcyPhoto.visibility = View.INVISIBLE
        Log.e("error", error ?: "")
    }

    override fun onAWSProgress(progress: Int?) {
        EmpCustomLoader.showLoader(this)
        binding.rcyPhoto.visibility = View.VISIBLE
        Log.e("progress", progress!!.toString())
    }

    private fun validationData(): Boolean {
        if (binding.edtNoticeName.text.trim().toString().length < 4) {
            Toast.makeText(
                applicationContext,
                getString(R.string.please_enter_name),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edtDesc.text?.trim().toString())
        ) {
            Toast.makeText(
                applicationContext,
                getString(R.string.please_enter_description),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } /*else if (binding.all.isChecked || binding.tenant.isChecked || binding.owner.isChecked) {
            Toast.makeText(
                applicationContext,
                "Please Select User!!",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }*/

        return true

    }
}