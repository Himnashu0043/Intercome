package com.application.intercom.manager.complaint

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.manager.managerSide.complain.ManagerPendingListRes
import com.application.intercom.databinding.ItemManagerRegisterComplaintsBinding
import com.application.intercom.helper.setFormatDate
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter


class ResolveComplaintsAdapter(
    private val context: Context,
    val list: ArrayList<ManagerPendingListRes.Data.Result>,
    val onPress: ResolveClick,
    val onPress1: CommunityImgAdapter.ClickImg
) : RecyclerView.Adapter<ResolveComplaintsAdapter.MyViewHolder>() {
    private lateinit var notificationsAdapterItem: ManagerComplainImgAdapter
    private val viewPool = RecyclerView.RecycledViewPool()

    class MyViewHolder(val mView: ItemManagerRegisterComplaintsBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemManagerRegisterComplaintsBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.tvConfirmationPending.text = list[position].status
        holder.itemView.setOnClickListener {
            onPress.onClick(position)
            /* context.startActivity(
                 Intent(
                     context,
                     RegisterComplainsOwnerTenantDetailsActivity::class.java
                 )
             )*/
        }
        holder.mView.tvProfileName.text = list[position].userInfo.get(0).fullName
        holder.mView.tvFlatNumber1.text = list[position].flatInfo.get(0).name
        holder.mView.tvTvComplainId1.text = list[position].compId
       // holder.mView.tvServiceCategory1.text = list[position].serviceCategory.get(0).category_name
        holder.mView.tvComplaintName1.text = list[position].complainName
        holder.mView.tvDescription.text = "Description : ${list[position].description}"
        val date = setFormatDate(list[position].createdAt)
        holder.mView.tvDate.text = date
        notificationsAdapterItem =
            ManagerComplainImgAdapter(
                context,
                list[position].photo,
                "resolve",
                onPress1
            )
        holder.mView.recyclerView.apply {
            adapter = notificationsAdapterItem
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            setRecycledViewPool(viewPool)
        }
    }

    override fun getItemCount(): Int {
        return list.size

    }

    interface ResolveClick {
        fun onClick(position: Int)
    }
}