package com.application.intercom.owner.activity.ownerPropertyToLetSale

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.data.model.remote.owner.flat.OwnerFlatListRes
import com.application.intercom.data.model.remote.owner.parking.OwnerParkingListRes
import com.application.intercom.databinding.ActivityOwnerPropertyBinding
import com.application.intercom.owner.activity.ownerTolet.OwnerToLetActivity

class OwnerPropertyToletSaleActivity : BaseActivity<ActivityOwnerPropertyBinding>() {

    override fun getLayout(): ActivityOwnerPropertyBinding {
        return ActivityOwnerPropertyBinding.inflate(layoutInflater)
    }

    private var send_PropertyList: OwnerFlatListRes.Data? = null
    private var send_ParkingList: OwnerParkingListRes.Data? = null
    private var from: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        from = intent.getStringExtra("from").toString()
        if (from.equals("parking")) {
            send_ParkingList =
                intent.getSerializableExtra("parkingList") as OwnerParkingListRes.Data
            println("----Parkinglist${send_ParkingList}")
            binding.textView114.text = send_ParkingList!!.buildingInfo.get(0).buildingName
            binding.textView115.text = send_ParkingList!!.buildingInfo[0].address
            binding.textView1141.text = send_ParkingList!!.buildingInfo.get(0).buildingName
            binding.textView1151.text = send_ParkingList!!.buildingInfo.get(0).address
            binding.tvflatno.text =
                "${getString(R.string.parking_no)}${send_ParkingList!!.parkingNumber}"
            binding.tvFlatParkingNo.text =
                "${getString(R.string.parking_no)}${send_ParkingList!!.parkingNumber}"
        } else if (from.equals("property")) {
            send_PropertyList =
                intent.getSerializableExtra("send_property_list") as OwnerFlatListRes.Data
            println("----list${send_PropertyList}")
            binding.textView114.text = send_PropertyList!!.buildingInfo.get(0).buildingName
            binding.textView115.text = send_PropertyList!!.buildingInfo[0].address
            binding.textView1141.text = send_PropertyList!!.buildingInfo.get(0).buildingName
            binding.textView1151.text = send_PropertyList!!.buildingInfo.get(0).address
            binding.tvflatno.text = "${getString(R.string.flat_no)}${send_PropertyList!!.name}"
            binding.tvFlatParkingNo.text =
                "${getString(R.string.flat_no)}${send_PropertyList!!.name}"
        }

        initView()
        lstnr()
    }

    private fun initView() {
        binding.propertyToolbar.tvTittle.text = getString(R.string.property_to_let_sale)


    }

    private fun lstnr() {
        binding.propertyToolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.toLetLay.setOnClickListener {
            if (from.equals("parking")) {
                startActivity(
                    Intent(
                        this,
                        OwnerToLetActivity::class.java
                    ).putExtra("send_parkingList", send_ParkingList).putExtra("from", "to-let")
                        .putExtra("key", "parking")
                )
            } else if (from.equals("property")) {
                startActivity(
                    Intent(
                        this,
                        OwnerToLetActivity::class.java
                    ).putExtra("send_property_list", send_PropertyList).putExtra("from", "to-let")
                )
            }


        }
        binding.saleLay.setOnClickListener {
            if (from.equals("parking")) {
                startActivity(
                    Intent(
                        this,
                        OwnerToLetActivity::class.java
                    ).putExtra("send_parkingList", send_ParkingList).putExtra("from", "sale")
                        .putExtra("key", "parking")
                )
            } else if (from.equals("property")) {
                startActivity(
                    Intent(this, OwnerToLetActivity::class.java).putExtra("from", "sale")
                        .putExtra("send_property_list", send_PropertyList)
                )
            }

        }

    }
}