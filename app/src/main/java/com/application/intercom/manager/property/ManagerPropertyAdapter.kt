package com.application.intercom.manager.property

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.manager.managerProperty.ManagerPropertyListRes
import com.application.intercom.databinding.ItemManagerPropertyToLetFlatBinding
import com.application.intercom.manager.managerTenantHistory.ManagerTenantHistoryActivity
import com.application.intercom.utils.loadImagesWithGlideExt


class ManagerPropertyAdapter(
    private val context: Context,
    val list: ArrayList<ManagerPropertyListRes.Data>,
    val onPress: Click
) : RecyclerView.Adapter<ManagerPropertyAdapter.MyViewHolder>() {
    private lateinit var notificationsAdapterItem: ManagerPropertyChildAdapter
    private val viewPool = RecyclerView.RecycledViewPool()
    private lateinit var notificationsAdapterItem1: ManagerPropertyTenantAdapter
    private val viewPool1 = RecyclerView.RecycledViewPool()

    class MyViewHolder(val mView: ItemManagerPropertyToLetFlatBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemManagerPropertyToLetFlatBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = list[position]
        holder.itemView.setOnClickListener {
            onPress.onflatClick(position)
            // context.startActivity(Intent(context, ManagerPropertyToLetFlatActivity::class.java))
        }
        holder.mView.ivForwardArrow.setOnClickListener {
            holder.mView.ivForwardArrow.visibility = View.GONE
            holder.mView.ivDownwardArrow.visibility = View.VISIBLE
            val date = list[position].buildingInfo.get(0).validFrom
            if (!list[position].tenantInfo.isNullOrEmpty()) {
                holder.mView.layoutExpandTenant.visibility = View.VISIBLE
                notificationsAdapterItem1 = ManagerPropertyTenantAdapter(
                    context,
                    data.tenantInfo!!,
                    date
                )
                holder.mView.rcy1.apply {
                    adapter = notificationsAdapterItem1
                    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    setRecycledViewPool(viewPool1)
                }
                notificationsAdapterItem1.notifyDataSetChanged()
            }

            if (!list[position].ownerInfo.isNullOrEmpty()) {
                holder.mView.layoutExpand.visibility = View.VISIBLE
                notificationsAdapterItem = ManagerPropertyChildAdapter(
                    context,
                    data.ownerInfo,
                    date
                )
                holder.mView.rcy.apply {
                    adapter = notificationsAdapterItem
                    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    setRecycledViewPool(viewPool)
                }
                notificationsAdapterItem.notifyDataSetChanged()
            }


        }
        holder.mView.ivDownwardArrow.setOnClickListener {
            holder.mView.ivDownwardArrow.visibility = View.GONE
            holder.mView.ivForwardArrow.visibility = View.VISIBLE
            holder.mView.layoutExpand.visibility = View.GONE
            holder.mView.layoutExpandTenant.visibility = View.GONE

        }
        holder.mView.tvProfileName.text = data.name
        holder.mView.tvAddress.text = data.buildingInfo[0].address
        holder.mView.ivProfile.loadImagesWithGlideExt(data.buildingInfo.get(0).photos.get(0))
        holder.mView.tvLoginUsingPassword.setOnClickListener {
            context.startActivity(
                Intent(
                    context,
                    ManagerTenantHistoryActivity::class.java
                ).putExtra("from", "manager_property").putExtra("list", list[position])
            )
        }
        ////owner Data and tenant
        /*if (!data.tenantInfo.isNullOrEmpty()) {
            holder.mView.tvExpandProfileName.text = data.tenantInfo!![0].fullName
            holder.mView.tvExpandAddress.text = "--"
            holder.mView.ivExpandProfile.loadImagesWithGlideExt(data.tenantInfo!![0].profilePic)
            val valid = setFormatDate(data.buildingInfo.get(0).validFrom)
            holder.mView.tvDate.text = valid
            holder.mView.tvStatus.text = "Till Now"
            holder.mView.ivCalling.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${data.tenantInfo!![0].phoneNumber}")
                context.startActivity(intent)
            }
            holder.mView.tvUserType.text = data.tenantInfo!!.get(0).role

        }
        if (!data.ownerInfo.isNullOrEmpty()) {
            holder.mView.tvExpandProfileName.text = data.ownerInfo[0].fullName
            holder.mView.tvExpandAddress.text = data.ownerInfo[0].address
            holder.mView.ivExpandProfile.loadImagesWithGlideExt(data.ownerInfo[0].profilePic)
            val valid = setFormatDate(data.buildingInfo.get(0).validFrom)
            holder.mView.tvDate.text = valid
            holder.mView.tvStatus.text = "Till Now"
            holder.mView.ivCalling.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${data.ownerInfo[0].phoneNumber}")
                context.startActivity(intent)
            }
            holder.mView.tvUserType.text = data.ownerInfo!!.get(0).role
        } else {
            holder.mView.tvExpandProfileName.text = ""
            holder.mView.tvExpandAddress.text = ""
            holder.mView.tvDate.text = ""
            holder.mView.tvStatus.text = ""
        }*/


    }

    override fun getItemCount(): Int {
        return list.size

    }

    interface Click {
        fun onflatClick(pos: Int)
    }
}