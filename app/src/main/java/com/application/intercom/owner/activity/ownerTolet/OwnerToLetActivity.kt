package com.application.intercom.owner.activity.ownerTolet

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.MainActivity
import com.application.intercom.R
import com.application.intercom.aws.AWSUtils
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.baseActivity.BaseApplication
import com.application.intercom.baseActivity.prefs
import com.application.intercom.data.model.ViewModel.ownerViewModel.ownerTolet.OwnerToletSaleViewModel
import com.application.intercom.data.model.factory.ownerfactory.OwnerSideFactory
import com.application.intercom.data.model.factory.ownerfactory.ownerTolet.OwnerToletFactory
import com.application.intercom.data.model.local.localModel.SendAmenitiesModel
import com.application.intercom.data.model.local.owner.propertyToletSale.OwnerPropertySalePostModel
import com.application.intercom.data.model.local.owner.propertyToletSale.OwnerPropertyToletPostModel
import com.application.intercom.data.model.remote.owner.flat.OwnerFlatListRes
import com.application.intercom.data.model.remote.owner.parking.OwnerParkingListRes
import com.application.intercom.data.repository.EmpResource
import com.application.intercom.data.repository.ServiceRepository
import com.application.intercom.data.repository.ownerRepo.ownerSide.OwnerSideRepo
import com.application.intercom.data.repository.ownerRepo.ownerTolet.OwnerToletSale
import com.application.intercom.databinding.ActivityOwnerToLetBinding
import com.application.intercom.helper.GPSService
import com.application.intercom.helper.TakeImageWithCrop
import com.application.intercom.owner.activity.main.OwnerMainActivity
import com.application.intercom.owner.activity.properties.OwnerPropertiesActivity
import com.application.intercom.owner.adapter.PhotoUploadAdapter
import com.application.intercom.owner.adapter.SpinnerAdapterBrands
import com.application.intercom.user.service.ServiceFactory
import com.application.intercom.user.service.ServiceViewModel
import com.application.intercom.utils.*
import com.catalyist.aws.AWSListner


class OwnerToLetActivity : BaseActivity<ActivityOwnerToLetBinding>(), AWSListner {

    private var from: String = ""
    private var key: String = ""
    private var imagePath: String = ""
    private lateinit var viewModel: OwnerToletSaleViewModel
    private var amenitiesList = ArrayList<String>()
    private var amenitiesHashMapID: HashMap<String, String> = HashMap()
    private var amenitiesImgHashMapID: HashMap<String, String> = HashMap()
    private var amenitiesId: String = ""
    var img1: String = ""
    override fun getLayout(): ActivityOwnerToLetBinding {
        return ActivityOwnerToLetBinding.inflate(layoutInflater)
    }

    private var photoAdapter: PhotoUploadAdapter? = null
    private var photo_upload_list = ArrayList<String>()
    private var testlist = ArrayList<String>()
    private var sendAmenitiesList = ArrayList<OwnerPropertyToletPostModel.Amentity>()
    private var send_PropertyList: OwnerFlatListRes.Data? = null
    private var send_ParkingList: OwnerParkingListRes.Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        key = intent.getStringExtra("key").toString()
        from = intent.getStringExtra("from").toString()


        initView()
        lstnr()
    }

    private fun initView() {
        amenitiesList.clear()
        testlist.clear()
        sendAmenitiesList.clear()
        amenitiesList.add(0, "Select Amenities")
        init()
        observer()
        if (key.equals("parking")) {
            send_ParkingList =
                intent.getSerializableExtra("send_parkingList") as OwnerParkingListRes.Data
            println("----Parkinglist${send_ParkingList}")
            if (from.equals("sale")) {
                binding.toletToolbar.tvTittle.text = getString(R.string.sale)
                ///fetch data
                binding.textView116.text = send_ParkingList!!.buildingInfo[0].buildingName
                binding.textView117.text = send_ParkingList!!.buildingInfo[0].address
                binding.imageView63.loadImagesWithGlideExt(
                    send_ParkingList!!.buildingInfo[0].photos.get(
                        0
                    )
                )
                binding.textView1171.text = getString(R.string.parking_number)
                binding.tvFlat.text = send_ParkingList!!.parkingNumber
                binding.textView118.visibility = View.GONE
                binding.radioEmail.visibility = View.GONE
                binding.textView5.visibility = View.GONE
                binding.edActivityName.visibility = View.GONE
                binding.ivCrossTolet.visibility = View.GONE
                /* binding.commonBtn.tv.text = "Sale Parking"*/
                binding.commonBtn.tv.text = getString(R.string.add_parking)
            } else if (from.equals("to-let")) {
                getOwnerAmenitiesList()
                binding.toletToolbar.tvTittle.text = getString(R.string.to_let)
                ///fetch data
                binding.textView116.text = send_ParkingList!!.buildingInfo[0].buildingName
                binding.textView117.text = send_ParkingList!!.buildingInfo[0].address
                binding.imageView63.loadImagesWithGlideExt(
                    send_ParkingList!!.buildingInfo[0].photos.get(
                        0
                    )
                )
                binding.textView1171.text = getString(R.string.parking_number)
                binding.tvFlat.text = send_ParkingList!!.parkingNumber
                binding.commonBtn.tv.text = getString(R.string.add_parking)
                binding.textView118.visibility = View.GONE
                binding.radioEmail.visibility = View.GONE
                binding.textView5.visibility = View.GONE
                binding.edActivityName.visibility = View.GONE
                binding.ivCrossTolet.visibility = View.GONE
                /* binding.textView118.visibility = View.VISIBLE
                 binding.radioEmail.visibility = View.VISIBLE
                 binding.textView5.visibility = View.VISIBLE
                 binding.edActivityName.visibility = View.VISIBLE
                 binding.ivCrossTolet.visibility = View.VISIBLE*/
            }
        } else {
            if (from.equals("sale")) {
                binding.toletToolbar.tvTittle.text = getString(R.string.sale)
                send_PropertyList =
                    intent.getSerializableExtra("send_property_list") as OwnerFlatListRes.Data
                println("----list${send_PropertyList}")
                ///fetch data
                binding.textView116.text = send_PropertyList!!.buildingInfo[0].buildingName
                binding.textView117.text = send_PropertyList!!.buildingInfo[0].address
                binding.imageView63.loadImagesWithGlideExt(
                    send_PropertyList!!.buildingInfo[0].photos.get(
                        0
                    )
                )

                binding.tvFlat.text = send_PropertyList!!.name
                binding.textView118.visibility = View.GONE
                binding.radioEmail.visibility = View.GONE
                binding.textView5.visibility = View.GONE
                binding.edActivityName.visibility = View.GONE
                binding.ivCrossTolet.visibility = View.GONE
                binding.commonBtn.tv.text = getString(R.string.sale_property)
            } else if (from.equals("to-let")) {
                getOwnerAmenitiesList()
                send_PropertyList =
                    intent.getSerializableExtra("send_property_list") as OwnerFlatListRes.Data
                println("----list${send_PropertyList}")
                binding.toletToolbar.tvTittle.text = getString(R.string.to_let)
                ///fetch data
                binding.textView116.text = send_PropertyList!!.buildingInfo[0].buildingName
                binding.textView117.text = send_PropertyList!!.buildingInfo[0].address
                binding.imageView63.loadImagesWithGlideExt(
                    send_PropertyList!!.buildingInfo[0].photos.get(
                        0
                    )
                )
                binding.tvFlat.text = send_PropertyList!!.name
                binding.commonBtn.tv.text = getString(R.string.add_property)
                binding.textView118.visibility = View.VISIBLE
                binding.radioEmail.visibility = View.VISIBLE
                binding.textView5.visibility = View.VISIBLE
                binding.edActivityName.visibility = View.VISIBLE
                binding.ivCrossTolet.visibility = View.VISIBLE
            }
        }


    }

    private fun init() {
        val repo = OwnerToletSale(BaseApplication.apiService)
        viewModel =
            ViewModelProvider(this, OwnerToletFactory(repo))[OwnerToletSaleViewModel::class.java]

    }

    private fun spinnerForSubCategoryList(memberList: List<String>) {
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
            amenitiesId =
                amenitiesHashMapID[binding.spinner.selectedItem.toString()].toString()
            println("---memberId$amenitiesId")
            img1 = amenitiesImgHashMapID.get(binding.spinner.selectedItem.toString()) ?: ""
            println("---img$img1")
            if (!testlist.contains(itemSelected) && postion > 0) {
                testlist.add(itemSelected)
                binding.edActivityName.text = testlist.toString().replace("[", "").replace("]", "")
                sendAmenitiesList.add(OwnerPropertyToletPostModel.Amentity(img1, itemSelected))
                println("---send${sendAmenitiesList}")
            } else {
                binding.edActivityName.text = itemSelected
            }


        }

        override fun onNothingSelected(adapterView: AdapterView<*>?) {}
    }

    private fun getOwnerAmenitiesList() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        viewModel.getOwnerAmenitiesList(token)
    }

    private fun addFlatOwner() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        var price = binding.edPrice.text.trim().toString()
        val model = OwnerPropertyToletPostModel(
            sendAmenitiesList,
            send_PropertyList!!.buildingId,
            binding.edWriteDescription.text.trim().toString(),
            send_PropertyList!!._id,
            "To-Let",
            if (binding.radioEmail.isChecked) true else false,
            photo_upload_list,
            price.toInt()
        )
        viewModel.addFlatOwner(token, model)
    }

    private fun addParkingOwner() {
        val token = prefs.getString(
            SessionConstants.TOKEN,
            ""
        )
        var price = binding.edPrice.text.trim().toString()
        if (key.equals("parking")) {
            val model = OwnerPropertySalePostModel(
                send_ParkingList!!.buildingId,
                binding.edWriteDescription.text.trim().toString(),
                send_ParkingList!!._id,
                send_ParkingList!!.buildingInfo[0].packageId,
                price.toInt(),
                "Sale",
                photo_upload_list


            )
            viewModel.addParkingOwner(token, model)
        } else {
            val model = OwnerPropertySalePostModel(
                send_PropertyList!!.buildingId,
                binding.edWriteDescription.text.trim().toString(),
                send_PropertyList!!._id,
                send_PropertyList!!.buildingInfo[0].packageId,
                price.toInt(),
                "Sale",
                photo_upload_list


            )
            viewModel.addParkingOwner(token, model)
        }

    }

    private fun observer() {
        viewModel.getOwnerAmenitiesLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            it.data.forEach {
                                amenitiesList.add(it.name)
                                amenitiesHashMapID.put(it.name, it._id)
                                amenitiesImgHashMapID.put(it.name, it.image)
                            }
                            spinnerForSubCategoryList(amenitiesList)
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
        viewModel.addFlatOwnerLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            startActivity(
                                Intent(
                                    this,
                                    OwnerPropertiesActivity::class.java
                                )/*.putExtra("from", "from_side_property")*/.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )
                            finish()
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
        viewModel.addParkingOwnerLiveData.observe(this, Observer {
            when (it) {
                is EmpResource.Loading -> {
                    EmpCustomLoader.showLoader(this)
                }

                is EmpResource.Success -> {
                    EmpCustomLoader.hideLoader()
                    it.value.let {
                        if (it.status == AppConstants.STATUS_SUCCESS) {
                            if (key.equals("parking")) {
                                startActivity(
                                    Intent(
                                        this,
                                        OwnerPropertiesActivity::class.java
                                    )/*.putExtra("from", "from_side_property")*/.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                )
                                finish()
                            } else {
                                startActivity(
                                    Intent(
                                        this,
                                        OwnerPropertiesActivity::class.java
                                    )/*.putExtra("from", "from_side_property")*/.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                )
                                finish()
                            }

                        } else if (it.status == AppConstants.STATUS_404) {
                            this.longToast(it.message)
                        } else if (it.status == AppConstants.STATUS_401) {
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
        binding.toletToolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.radioEmail.setOnClickListener {
            binding.radioEmail.isChecked = true
            binding.radioMobile.isChecked = false
        }
        binding.radioMobile.setOnClickListener {
            binding.radioMobile.isChecked = true
            binding.radioEmail.isChecked = false

        }
        binding.edActivityName.setOnClickListener {
            binding.spinner.performClick()
        }
        binding.ivCrossTolet.setOnClickListener {
            testlist.clear()
            sendAmenitiesList.clear()
            println("----sendremove$sendAmenitiesList")
            binding.edActivityName.text = amenitiesList.get(0)
        }
        binding.imageView16.setOnClickListener {
            showImagePickDialog()
        }
        binding.commonBtn.tv.setOnClickListener {
            if (!validationData()) {
                return@setOnClickListener
            } else {
                if (key.equals("parking")) {
                    addParkingOwner()
                } else if (from.equals("sale")) {
                    addParkingOwner()
                } else {
                    addFlatOwner()
                }
            }

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

    override fun onAWSLoader(isLoader: Boolean) {
        EmpCustomLoader.showLoader(this)
    }

    override fun onAWSSuccess(url: String?) {
        if (url != null) {
            imagePath = url!!
            EmpCustomLoader.hideLoader()
            binding.rcyImg.visibility = View.VISIBLE
            binding.pro.visibility = View.GONE
            photo_upload_list.add(imagePath)
            println("-----photo${photo_upload_list}")
            binding.rcyImg.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            photoAdapter = PhotoUploadAdapter(this, photo_upload_list)
            binding.rcyImg.adapter = photoAdapter
            photoAdapter!!.notifyDataSetChanged()

        }
    }

    override fun onAWSError(error: String?) {
        EmpCustomLoader.hideLoader()
        binding.rcyImg.visibility = View.INVISIBLE
        Log.e("error", error ?: "")
    }

    override fun onAWSProgress(progress: Int?) {
        EmpCustomLoader.showLoader(this)
        binding.rcyImg.visibility = View.VISIBLE
        Log.e("progress", progress!!.toString())
    }

    private fun validationData(): Boolean {
        if (key.equals("parking")) {
            if (TextUtils.isEmpty(binding.edWriteDescription.text.trim().toString())) {
                Toast.makeText(
                    applicationContext, "Please Enter Description!!", Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(binding.edPrice.text.trim().toString())
            ) {
                Toast.makeText(this, "Please Enter Price!!", Toast.LENGTH_SHORT).show()
                return false

            }
        } else if (from.equals("sale")) {
            if (TextUtils.isEmpty(binding.edWriteDescription.text.trim().toString())) {
                Toast.makeText(
                    applicationContext, "Please Enter Description!!", Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(binding.edPrice.text.trim().toString())
            ) {
                Toast.makeText(this, "Please Enter Price!!", Toast.LENGTH_SHORT).show()
                return false

            }
        } else {
            if (TextUtils.isEmpty(binding.edWriteDescription.text.trim().toString())) {
                Toast.makeText(
                    applicationContext, "Please Enter Description!!", Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (binding.spinner.selectedItemPosition.equals(0)) {
                Toast.makeText(
                    applicationContext, "Please Select Amenities", Toast.LENGTH_SHORT
                ).show()
                return false
            } else if (TextUtils.isEmpty(binding.edPrice.text.trim().toString())
            ) {
                Toast.makeText(this, "Please Enter Price!!", Toast.LENGTH_SHORT).show()
                return false

            }
        }

        return true

    }
}