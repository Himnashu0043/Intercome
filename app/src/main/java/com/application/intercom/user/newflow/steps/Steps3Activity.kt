package com.application.intercom.user.newflow.steps

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.aws.AWSUtils
import com.application.intercom.data.model.remote.newUser.MyList.ActiveNewPhaseList
import com.application.intercom.databinding.ActivitySteps3Binding
import com.application.intercom.helper.TakeImageWithCrop
import com.application.intercom.owner.adapter.PhotoUploadAdapter
import com.application.intercom.user.aboutapp.PrivacyPolicyActivity
import com.application.intercom.user.newflow.PreviewListingActivity
import com.application.intercom.user.newflow.adapter.Step3ImgUploadAdapter
import com.application.intercom.user.newflow.modal.UserTestPostModel
import com.application.intercom.utils.CommonUtil
import com.application.intercom.utils.EmpCustomLoader
import com.catalyist.aws.AWSListner
import com.google.firebase.crashlytics.internal.common.CommonUtils

class Steps3Activity : AppCompatActivity(), AWSListner, Step3ImgUploadAdapter.ClickImg {
    lateinit var binding: ActivitySteps3Binding
    private var sendlist = ArrayList<UserTestPostModel>()
    private var send1list = ArrayList<UserTestPostModel>()
    private var photoAdapter: Step3ImgUploadAdapter? = null
    private var photo_upload_list = ArrayList<String>()
    private lateinit var dialog: Dialog
    private var imagePath: String = ""
    private var editlist: ActiveNewPhaseList.Data? = null
    private var editFrom: String = ""
    private var editId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySteps3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        editFrom = intent.getStringExtra("editFrom").toString()
        if (editFrom.equals("editData")) {
            sendlist = intent.getSerializableExtra("testList") as ArrayList<UserTestPostModel>
            editlist = intent.getSerializableExtra("editList") as ActiveNewPhaseList.Data?
            println("-----ediitst$editlist")
            editId = intent.getStringExtra("edit_id").toString()
            binding.edtPrice.setText(editlist!!.title)
            binding.edtPrice1.setText(editlist!!.description)
            binding.textView232.text = editlist!!.title.length.toString()
            binding.textView23215.text = editlist!!.description.length.toString()
            binding.recyclerView2.visibility = View.VISIBLE

            binding.imageView132.visibility = View.INVISIBLE
            photo_upload_list.clear()
            photo_upload_list.addAll(editlist!!.photos!!)
            binding.recyclerView2.layoutManager =
                GridLayoutManager(this, 3)
            photoAdapter = Step3ImgUploadAdapter(this, photo_upload_list, this)
            binding.recyclerView2.adapter = photoAdapter
            photoAdapter!!.notifyDataSetChanged()

        } else {
            sendlist = intent.getSerializableExtra("testList") as ArrayList<UserTestPostModel>
            println("---test$sendlist")
        }

        initView()
        lstnr()
    }

    private fun initView() {
        binding.toolbar.tvTittle.text = getString(R.string.step_3_3)
        binding.commonBtn.tv.text = getString(R.string.next_step)
    }

    private fun openCameraGallery() {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.gallery_picker)
        dialog.setCanceledOnTouchOutside(true)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val bottomLayout = dialog.findViewById<CardView>(R.id.bottomLayout)
        val imgCamera = dialog.findViewById<ImageView>(R.id.ImgCamera)
        val imgGallery = dialog.findViewById<ImageView>(R.id.ImgGallery)
        imgCamera.setOnClickListener { openCamera() }
        imgGallery.setOnClickListener { openGallery() }
        bottomLayout.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

    private fun openGallery() {
        val intent = Intent(this, TakeImageWithCrop::class.java)
        intent.putExtra("from", "gallery")
        startActivityForResult(intent, TakeImageWithCrop.GALLERY_REQUEST)
        dialog.dismiss()
    }

    private fun openCamera() {
        val intent = Intent(this, TakeImageWithCrop::class.java)
        intent.putExtra("from", "camera")
        startActivityForResult(intent, TakeImageWithCrop.CAMERA_REQUEST)
        dialog.dismiss()
    }

    private fun lstnr() {
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }


        binding.edtPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString().isEmpty()) {
                    var ti = s.length
                    if (ti == 36) {
                        binding.edtPrice.isEnabled = false
                        binding.edtPrice.setTextColor(Color.BLACK)
                    } else {
                        binding.textView232.text = ti.toString()
                    }

                }

            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.edtPrice1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!s.toString().isEmpty()) {
                    var ti = s.length
                    if (ti == 1001) {
                        binding.edtPrice1.isEnabled = false
                        binding.edtPrice1.setTextColor(Color.BLACK)
                    } else {
                        binding.textView23215.text = ti.toString()
                    }

                }

            }

            override fun afterTextChanged(s: Editable) {}
        })
        binding.commonBtn.tv.setOnClickListener {
            if (validationData()) {
                if (photo_upload_list.isEmpty()) {
                    Toast.makeText(this, "Please Select Images!!", Toast.LENGTH_SHORT).show()
                } else if (editFrom.equals("editData")) {
                    send1list.add(
                        UserTestPostModel(
                            null,
                            sendlist.get(0).address,
                            sendlist.get(0).amentities,
                            sendlist.get(0).bathroom,
                            sendlist.get(0).bedroom,
                            binding.edtPrice1.text.trim().toString(),
                            sendlist.get(0).district,
                            sendlist.get(0).division,
                            sendlist.get(0).flatStatus,
                            sendlist.get(0).floorLevel,
                            sendlist.get(0).lat,
                            sendlist.get(0).long,
                            photo_upload_list,
                            "",
                            "",
                            sendlist.get(0).price,
                            sendlist.get(0).propertyType,
                            sendlist.get(0).sqft,
                            sendlist.get(0).subPropertyType,
                            binding.edtPrice.text.trim().toString(),
                            sendlist.get(0).totalFloor
                        )
                    )

                    startActivity(
                        Intent(this, PreviewListingActivity::class.java).putExtra(
                            "testList",
                            send1list
                        ).putExtra("edit_id", editId).putExtra("editFrom", editFrom)
                    )
                } else {
                    send1list.add(
                        UserTestPostModel(
                            null,
                            sendlist.get(0).address,
                            sendlist.get(0).amentities,
                            sendlist.get(0).bathroom,
                            sendlist.get(0).bedroom,
                            binding.edtPrice1.text.trim().toString(),
                            sendlist.get(0).district,
                            sendlist.get(0).division,
                            sendlist.get(0).flatStatus,
                            sendlist.get(0).floorLevel,
                            sendlist.get(0).lat,
                            sendlist.get(0).long,
                            photo_upload_list,
                            "",
                            "",
                            sendlist.get(0).price,
                            sendlist.get(0).propertyType,
                            sendlist.get(0).sqft,
                            sendlist.get(0).subPropertyType,
                            binding.edtPrice.text.trim().toString(),
                            sendlist.get(0).totalFloor
                        )
                    )

                    startActivity(
                        Intent(this, PreviewListingActivity::class.java).putExtra(
                            "testList",
                            send1list
                        )
                    )
                }

            }

        }
        /* binding.imageView132.setOnClickListener {
             showImagePickDialog()
         }*/
        binding.imageView132.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (CommonUtil.checkCameraPermission2(this)) {
                    openCameraGallery()
                } else {
                    CommonUtil.requestCamFilePermission2(this@Steps3Activity)
                }
            } else {
                if (CommonUtil.checkCameraPermission(this@Steps3Activity)) {
                    openCameraGallery()
                } else {
                    CommonUtil.requestCamFilePermission(this@Steps3Activity)
                }
            }


        }

    }

    /*  private fun showImagePickDialog() {
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
      }*/
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
        EmpCustomLoader.hideLoader()
        binding.recyclerView2.visibility = View.VISIBLE
    }

    override fun onAWSSuccess(url: String?) {
        if (url != null) {
            imagePath = url
            println("---url$url")
            binding.recyclerView2.visibility = View.VISIBLE
            binding.imageView132.visibility = View.INVISIBLE
            EmpCustomLoader.hideLoader()
            photo_upload_list.add(currentImagePath.toString())
            println("-----photo${photo_upload_list}")
            binding.recyclerView2.layoutManager =
                GridLayoutManager(this, 3)
            photoAdapter = Step3ImgUploadAdapter(this, photo_upload_list, this)
            binding.recyclerView2.adapter = photoAdapter
            photoAdapter!!.notifyDataSetChanged()

        }
    }

    override fun onAWSError(error: String?) {
        EmpCustomLoader.hideLoader()
        binding.recyclerView2.visibility = View.INVISIBLE

        Log.e("error", error ?: "")
    }

    override fun onAWSProgress(progress: Int?) {
        EmpCustomLoader.showLoader(this)
        binding.recyclerView2.visibility = View.VISIBLE

        Log.e("progress", progress!!.toString())
    }

    private fun validationData(): Boolean {
        if (TextUtils.isEmpty(binding.edtPrice.text!!.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Enter Title!!", Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (TextUtils.isEmpty(binding.edtPrice1.text!!.trim().toString())) {
            Toast.makeText(
                applicationContext, "Please Enter Description !!", Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true

    }

    override fun onClickPlus(position: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (CommonUtil.checkCameraPermission2(this)) {
                openCameraGallery()
            } else {
                CommonUtil.requestCamFilePermission2(this@Steps3Activity)
            }
        } else {
            if (CommonUtil.checkCameraPermission(this@Steps3Activity)) {
                openCameraGallery()
            } else {
                CommonUtil.requestCamFilePermission(this@Steps3Activity)
            }
        }
    }
}