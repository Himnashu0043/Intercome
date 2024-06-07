package com.application.intercom.owner.activity.propertyDetails

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.baseActivity.BaseActivity
import com.application.intercom.data.model.local.owner.OwnerPropertyModel
import com.application.intercom.data.model.remote.owner.flat.OwnerFlatListRes
import com.application.intercom.databinding.ActivityOwnerPropertyDetailsBinding
import com.application.intercom.owner.activity.ownerCreateTenant.OwnerCreateTenantActivity
import com.application.intercom.owner.activity.ownerPropertyToLetSale.OwnerPropertyToletSaleActivity
import com.application.intercom.owner.adapter.ShowImgAdapter
import com.application.intercom.utils.loadImagesWithGlideExt

class OwnerPropertyDetailsActivity : BaseActivity<ActivityOwnerPropertyDetailsBinding>() {
    override fun getLayout(): ActivityOwnerPropertyDetailsBinding {
        return ActivityOwnerPropertyDetailsBinding.inflate(layoutInflater)
    }

    private var send_PropertyList: OwnerFlatListRes.Data? = null
    private var showphotoAdapter: ShowImgAdapter? = null
    private var photo_upload_list = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        send_PropertyList =
            intent.getSerializableExtra("send") as OwnerFlatListRes.Data
        println("----list${send_PropertyList}")
        initView()
        listener()
    }

    private fun initView() {
        binding.ownerPDetails.tvTittle.text = getString(R.string.property_details)
        binding.ownerPDetails.tvText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)

        ////fetchData
        binding.textView132.text = send_PropertyList!!.buildingInfo[0].buildingName
        binding.tvFlatName.text = send_PropertyList!!.name
        binding.textView133.text = send_PropertyList!!.buildingInfo[0].address
        binding.tvFit.text = "${send_PropertyList!!.sqft} ${getString(R.string.ft)}"
        binding.tvBedroom.text = "${send_PropertyList!!.bedroom} ${getString(R.string.bhk)}"
        binding.tvBathroom.text = "${send_PropertyList!!.bathroom} ${getString(R.string.bath)}"
        val htmlAsString = send_PropertyList!!.buildingInfo[0].description
        val htmlAsSpanned = Html.fromHtml(htmlAsString)
        binding.textView135.text = htmlAsSpanned
        binding.imageView68.loadImagesWithGlideExt(send_PropertyList!!.buildingInfo[0].photos.get(0))
        for (img in send_PropertyList!!.buildingInfo) {
            photo_upload_list.add(img.photos.get(0))
        }
        binding.rcyPhoto.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        showphotoAdapter = ShowImgAdapter(this, photo_upload_list)
        binding.rcyPhoto.adapter = showphotoAdapter
        showphotoAdapter!!.notifyDataSetChanged()
        if (send_PropertyList!!.flatStatus != null) {
            if (send_PropertyList!!.tenant.isNotEmpty()) {
                binding.ownerPDetails.tvText.visibility = View.VISIBLE
                binding.ownerPDetails.tvText.text = getString(R.string.view_tenant)
            } else {
                if (send_PropertyList!!.is_assign) {
                    binding.ownerPDetails.tvText.visibility = View.INVISIBLE
                    binding.ownerPDetails.tvText.text = getString(R.string.view_tenant)
                } else {
                    binding.tvCreateTenantBtn.visibility = View.VISIBLE
                }

//                if (send_PropertyList!!.flatStatus.equals("To-Let")) {
//                    binding.tvCreateTenantBtn.visibility = View.VISIBLE
//
//                } else {
//                    binding.tvCreateTenantBtn.visibility = View.INVISIBLE
//
//                }
            }

        } else {
            binding.ownerPDetails.tvText.visibility = View.INVISIBLE
            binding.ownerPDetails.tvText.text = getString(R.string.view_tenant)
            binding.tvCreateTenantBtn.visibility = View.VISIBLE
        }
        binding.commonBtn.tv.text = getString(R.string.to_let_sale)

    }

    private fun listener() {
        binding.ownerPDetails.ivBack.setOnClickListener {
            finish()
        }
        binding.ownerPDetails.tvText.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    OwnerSecondPropertyDetailsActivity::class.java
                ).putExtra("send_property_list", send_PropertyList)
            )
        }
        binding.commonBtn.tv.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    OwnerPropertyToletSaleActivity::class.java
                ).putExtra("send_property_list", send_PropertyList).putExtra("from", "property")
            )
        }
        binding.tvCreateTenantBtn.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    OwnerCreateTenantActivity::class.java
                ).putExtra("send_property_list", send_PropertyList)
            )
        }

    }
}