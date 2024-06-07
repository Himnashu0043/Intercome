package com.application.intercom.manager.complaint

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.manager.managerSide.complain.ManagerPendingListRes
import com.application.intercom.databinding.ItemManagerRegisterComplaintsBinding
import com.application.intercom.helper.setFormatDate
import com.application.intercom.tenant.adapter.myCommunity.CommunityImgAdapter

class ResolvedComplaintsAdapter(
    private val context: Context, val list: ArrayList<ManagerPendingListRes.Data.Result>,
    val onPress: ReslovedClick,
    val onPress1: CommunityImgAdapter.ClickImg
) : RecyclerView.Adapter<ResolvedComplaintsAdapter.MyViewHolder>() {
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
        holder.mView.tvConfirmationPending.setTextColor(context.getColor(R.color.green))
        holder.mView.tvResolve.visibility = View.INVISIBLE
        holder.itemView.setOnClickListener {
            onPress.onCLick(position)

        }
        holder.mView.tvProfileName.text = list[position].userInfo.get(0).fullName
        holder.mView.tvFlatNumber1.text = list[position].flatInfo.get(0).name
        holder.mView.tvTvComplainId1.text = ": ${list[position].compId}"
       // holder.mView.tvServiceCategory1.text = list[position].serviceCategory.get(0).category_name
        holder.mView.tvComplaintName1.text = list[position].complainName
        holder.mView.tvDescription.text =
            "${context.getString(R.string.description)} : ${list[position].description}"
        val date = setFormatDate(list[position].createdAt)
        holder.mView.tvDate.text = date
        notificationsAdapterItem =
            ManagerComplainImgAdapter(
                context,
                list[position].photo,
                "resolved",
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

    interface ReslovedClick {
        fun onCLick(position: Int)
    }
}