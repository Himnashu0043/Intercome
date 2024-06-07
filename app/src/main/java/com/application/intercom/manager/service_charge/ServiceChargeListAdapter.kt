package com.application.intercom.manager.service_charge

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.manager.managerSide.serviceCharege.ManagerServiceChargeList
import com.application.intercom.databinding.ServiceChargeListItemsBinding
import com.application.intercom.helper.getYearMonthOfDate
import com.application.intercom.helper.setNewFormatDate

class ServiceChargeListAdapter(
    val con: Context,
    val list: ArrayList<ManagerServiceChargeList.Data>,
    val onPress: Delete
) :
    RecyclerView.Adapter<ServiceChargeListAdapter.MyVIewHolder>() {
    class MyVIewHolder(val mView: ServiceChargeListItemsBinding) :
        RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyVIewHolder {
        return MyVIewHolder(
            ServiceChargeListItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyVIewHolder, position: Int) {
       holder.mView.tvRent.text = getYearMonthOfDate(list[position].date?:"")
        if (list[position].flatId == null) {
            holder.mView.tvFlatNo.text = "--"
        } else {
            holder.mView.tvFlatNo.text = list[position].flatId!!.name?:""
        }

        holder.mView.tvDate.text = "Due Date:${setNewFormatDate(list[position].dueDate ?: "")}"
        holder.mView.tvPrice.text = "৳${list[position].amount ?: ""}"
        holder.mView.tvDelete.setOnClickListener {
            onPress.onClick(list[position]._id?:"")
        }
        holder.mView.tvEditHis.setOnClickListener {
            onPress.onEdit(position)
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface Delete {
        fun onClick(_id: String)
        fun onEdit(position: Int)
    }
}