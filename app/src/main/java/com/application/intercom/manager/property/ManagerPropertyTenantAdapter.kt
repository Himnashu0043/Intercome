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

class ManagerPropertyTenantAdapter(
    val con: Context,
    val list: ArrayList<ManagerPropertyListRes.Data.TenantInfo>,
    var date:String
) :
    RecyclerView.Adapter<ManagerPropertyTenantAdapter.MyViewHolder>() {
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

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
            holder.mView.tvExpandProfileName.text = list[0].fullName
            holder.mView.tvExpandAddress.text = "--"
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


    }

    override fun getItemCount(): Int {
        return list.size
    }
}