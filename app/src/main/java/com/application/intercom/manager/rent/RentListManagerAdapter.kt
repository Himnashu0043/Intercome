package com.application.intercom.manager.rent

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.manager.managerSide.rent.RentManagerListRes
import com.application.intercom.databinding.ServiceChargeListItemsBinding
import com.application.intercom.helper.getYearMonthOfDate
import com.application.intercom.helper.setNewFormatDate

class RentListManagerAdapter(
    val con: Context,
    val list: ArrayList<RentManagerListRes.Data.Result>,
    val onPress: ListenerRent
) :
    RecyclerView.Adapter<RentListManagerAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ServiceChargeListItemsBinding) :
        RecyclerView.ViewHolder(mView.root) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            ServiceChargeListItemsBinding.inflate(
                LayoutInflater.from(con),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.tvRent.text = getYearMonthOfDate(list[position].date ?: "")
        if (list[position].flatInfo == null) {
            holder.mView.tvFlatNo.text = "--"
        } else {
            holder.mView.tvFlatNo.text = list[position].flatInfo?.get(0)?.name ?: ""
        }

        holder.mView.tvDate.text = "Due Date:${setNewFormatDate(list[position].dueDate ?: "")}"
        holder.mView.tvPrice.text = "৳${list[position].amount ?: ""}"
        holder.mView.tvEditHis.setOnClickListener {
            con.startActivity(
                Intent(con, AddRentManagerActivity::class.java).putExtra(
                    "from",
                    "edit_from"
                ).putExtra("editData", list[position])
            )
        }
        holder.mView.tvDelete.setOnClickListener {
            onPress.onDelete(list[position]._id ?: "")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ListenerRent {
        fun onDelete(_id: String)
//        fun onEdit(position: String)
    }
}