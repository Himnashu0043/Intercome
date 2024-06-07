package com.application.intercom.manager.managerTenantHistory

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.manager.managerProperty.ManagerTenantHistoryList
import com.application.intercom.databinding.ChildPropertyManagerItemsBinding
import com.application.intercom.helper.setFormatDate
import com.application.intercom.utils.loadImagesWithGlideExt

class ManagerTenantHistoryAdapter(
    val con: Context,
    val list: ArrayList<ManagerTenantHistoryList.Data.Result>,
    val address: String
) :
    RecyclerView.Adapter<ManagerTenantHistoryAdapter.MyViewHolder>() {
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
        holder.mView.ivExpandProfile.loadImagesWithGlideExt(list[position].photo)
        holder.mView.tvExpandProfileName.text = list[position].fullName
        holder.mView.tvExpandAddress.text = address
        holder.mView.tvUserType.text = "Tenant"
        val date = setFormatDate(list[position].createdAt)
        holder.mView.tvDate.text = date
        holder.mView.tvStatus.text = "Till Now"
        holder.mView.ivCalling.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${list[position].mobileNumber}")
            con.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}