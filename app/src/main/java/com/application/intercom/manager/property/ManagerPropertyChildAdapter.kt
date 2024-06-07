package com.application.intercom.manager.property

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.manager.managerProperty.ManagerPropertyListRes
import com.application.intercom.databinding.ChildPropertyManagerItemsBinding
import com.application.intercom.helper.setFormatDate
import com.application.intercom.utils.loadImagesWithGlideExt

class ManagerPropertyChildAdapter(
    val con: Context,
    val list: ArrayList<ManagerPropertyListRes.Data.OwnerInfo>,
    var date:String
) :
    RecyclerView.Adapter<ManagerPropertyChildAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ChildPropertyManagerItemsBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            ChildPropertyManagerItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (!list.isEmpty()) {
            holder.mView.tvExpandProfileName.text = list[0].fullName
            holder.mView.tvExpandAddress.text =  list[0].address
            holder.mView.ivExpandProfile.loadImagesWithGlideExt(list[0].profilePic)
            val valid = setFormatDate(date)
            holder.mView.tvDate.text = valid
            holder.mView.tvStatus.text = "Till Now"
            holder.mView.ivCalling.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${list[0].phoneNumber}")
                con.startActivity(intent)
            }
            holder.mView.tvUserType.text = list[0].role

        }/* else if (tenantInfo!!.isNotEmpty()) {
            holder.mView.tvExpandProfileName.text = tenantInfo!![0].fullName
            holder.mView.tvExpandAddress.text = "--"
            holder.mView.ivExpandProfile.loadImagesWithGlideExt(tenantInfo!![0].profilePic)
//            val valid = setFormatDate(data.buildingInfo.get(0).validFrom)
//            holder.mView.tvDate.text = valid
            holder.mView.tvStatus.text = "Till Now"
            holder.mView.ivCalling.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${tenantInfo!![0].phoneNumber}")
                con.startActivity(intent)
            }
            holder.mView.tvUserType.text = tenantInfo!!.get(0).role

        }*/ else {
            holder.mView.tvExpandProfileName.text = ""
            holder.mView.tvExpandAddress.text = ""
            holder.mView.tvDate.text = ""
            holder.mView.tvStatus.text = ""
        }
    }

    override fun getItemCount(): Int {
        /*if (tenantInfo!!.isEmpty()) {
            return list.size
        } else if (tenantInfo.isNotEmpty()) {
            return list.size
            return tenantInfo.size
        } else {
            return list.size
        }*/
        return list.size
    }
}