package com.application.intercom.owner.activity.ownerParkingDetails

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.data.model.remote.owner.flat.OwnerFlatListRes
import com.application.intercom.data.model.remote.owner.parking.OwnerParkingListRes
import com.application.intercom.databinding.ActivityOwnerParkingDetailsBinding
import com.application.intercom.owner.activity.ownerPropertyToLetSale.OwnerPropertyToletSaleActivity
import com.application.intercom.utils.loadImagesWithGlideExt

class OwnerParkingDetailsActivity : BaseActivity<ActivityOwnerParkingDetailsBinding>() {

    override fun getLayout(): ActivityOwnerParkingDetailsBinding {
        return  ActivityOwnerParkingDetailsBinding.inflate(layoutInflater)
    }

    private var send_ParkingList: OwnerParkingListRes.Data? = null
    private var from: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from").toString()
        send_ParkingList =
            intent.getSerializableExtra("parkingList") as OwnerParkingListRes.Data
        println("----Parkinglist${send_ParkingList}")
        initView()
        listener()
    }

    private fun initView() {
        binding.parkingDetailsToolbar.tvTittle.text = "Parking Details"
        binding.commonBtn.tv.text = "To-Let / Sale"
///feching Data
        binding.textView132.text = send_ParkingList!!.buildingInfo[0].buildingName
        binding.textView133.text = send_ParkingList!!.buildingInfo[0].address
        binding.tvParkingNumber.text = send_ParkingList!!.parkingNumber
        val htmlAsString = send_ParkingList!!.buildingInfo[0].description
        val htmlAsSpanned = Html.fromHtml(htmlAsString)
        binding.textView135.text = htmlAsSpanned
        binding.imageView68.loadImagesWithGlideExt(send_ParkingList!!.buildingInfo[0].photos.get(0))


    }

    private fun listener() {
        binding.parkingDetailsToolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.commonBtn.tv.setOnClickListener {
            startActivity(
                Intent(this, OwnerPropertyToletSaleActivity::class.java).putExtra(
                    "from",
                    from
                ).putExtra("parkingList", send_ParkingList)
            )
        }

    }
}