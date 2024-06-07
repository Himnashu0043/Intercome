package com.application.intercom.owner.activity.rent

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.data.model.remote.manager.managerSide.rent.RentManagerListRes
import com.application.intercom.databinding.ServiceChargeListItemsBinding
import com.application.intercom.helper.getYearMonthOfDate
import com.application.intercom.helper.setNewFormatDate

class OwnerRentListAdapter(
    val con: Context, val list: ArrayList<RentManagerListRes.Data.Result>,
    val onPress: OwnerRent
) :
    RecyclerView.Adapter<OwnerRentListAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ServiceChargeListItemsBinding) :
        RecyclerView.ViewHolder(mView.root)

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
        if (!list[position].date.isNullOrEmpty()) {
            holder.mView.tvRent.text = getYearMonthOfDate(list[position].date ?: "")
        }
        if (list[position].flatInfo == null) {
            holder.mView.tvFlatNo.text = "--"
        } else {
            holder.mView.tvFlatNo.text = list[position].flatInfo?.get(0)?.name ?: ""
        }

        if (!list[position].dueDate.isNullOrEmpty()) {
            holder.mView.tvDate.text = "Due Date:${setNewFormatDate(list[position].dueDate ?: "")}"
        } else {
            if (!list[position].date.isNullOrEmpty()) {
                holder.mView.tvDate.text = "Due Date:${setNewFormatDate(list[position].date ?: "")}"
            }
        }

        holder.mView.tvPrice.text = "৳${list[position].amount ?: ""}"
        holder.mView.tvEditHis.setOnClickListener {
//            onPress.onEdit(list[position]._id?:"")
            con.startActivity(
                Intent(con, AddOwnerRentActivity::class.java).putExtra(
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

    interface OwnerRent {
        fun onDelete(_id: String)
//        fun onEdit(position: String)
    }
}