package com.application.intercom.manager.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.intercom.R
import com.application.intercom.data.model.remote.manager.managerSide.complain.ManagerPendingListRes
import com.application.intercom.databinding.ItemRecentComplaintsBinding
import com.application.intercom.helper.setFormatDate
import com.application.intercom.manager.complaint.PendingComplaintsAdapter


class ManagerRecentComplaintAdapter(
    private val context: Context, val list: ArrayList<ManagerPendingListRes.Data.Result>,val onPress:PendingComplaintsAdapter.PendingClick
) : RecyclerView.Adapter<ManagerRecentComplaintAdapter.MyViewHolder>() {
    class MyViewHolder(val mView: ItemRecentComplaintsBinding) : RecyclerView.ViewHolder(mView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemRecentComplaintsBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mView.tvComplainId.text =
            "${context.getString(R.string.complain_id)} - ${list[position].compId}"
        holder.mView.tvFlatNumber.text =
            "${context.getString(R.string.flat_number_1)} - ${list[position].flatInfo.get(0).name}"
        // holder.mView.tvServiceCategory.text =
        //"${context.getString(R.string.service_charge)} - ${list[position].serviceCategory.get(0).category_name}"
        val date = setFormatDate(list[position].createdAt)
        holder.mView.tvDate.text = "${context.getString(R.string.date)} -${date}"
        if (list[position].status == "Resolve") {
            holder.mView.tvPending.text = "Waiting"
            holder.mView.tvResolve.setTextColor(context.getColor(R.color.orange))
            holder.mView.tvResolve.visibility = View.GONE
            /*  holder.mView.tvResolve.text = list[position].status*/
        } else {
            holder.mView.tvPending.text = "Pending"
            holder.mView.tvResolve.visibility = View.VISIBLE
        }
        holder.itemView.setOnClickListener {
            onPress.onCLick(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}